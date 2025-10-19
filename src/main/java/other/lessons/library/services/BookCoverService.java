package other.lessons.library.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import other.lessons.library.models.Book;
import other.lessons.library.models.BookCover;
import other.lessons.library.repositories.BookCoverRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class BookCoverService {

    @Value("${books.cover.dir.path}")
    private String coversDir;

    private final BookService bookService;
    private final BookCoverRepository bookCoverRepository;

    // Объявляем константы для работы с обложками
    private static final int PREVIEW_WIDTH = 100;
    private static final int BUFFER_SIZE = 1024;

    public BookCoverService(BookService bookService, BookCoverRepository bookCoverRepository) {
        this.bookService = bookService;
        this.bookCoverRepository = bookCoverRepository;
    }

    public void uploadBookCover(Long bookId, MultipartFile file) throws IOException {
        Book book = bookService.findBook(bookId);

        Path coverPath = Path.of(coversDir, bookId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(coverPath.getParent());
        Files.deleteIfExists(coverPath);

        // Работа с потоками данных при загрузке файла (try-with-resources)
        try (
                InputStream is = file.getInputStream(); // Входной поток, начинаем вычитывать данные
                OutputStream os = Files.newOutputStream(coverPath, CREATE_NEW); // Выходной поток, создаем новый
                // пустой файл, куда будем записывать данные
                BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE); // Создаем буферизированный
                // входной поток для оптимизации скорости чтения
                BufferedOutputStream bos = new BufferedOutputStream(os, BUFFER_SIZE) // Создаем буферизированный
                // выходной потом для оптимизации скорости записи
        ) {
            bis.transferTo(bos); // Происходит сама передача данных из входного потока в выходной
        }

        // Заполняем информацию об обложке
        BookCover bookCover = findBookCover(bookId); // Ищем обложку по ID книжки
        bookCover.setBook(book); // Указываем книгу, которой принадлежит обложка
        bookCover.setFilePath(coverPath.toString());
        bookCover.setFileSize(file.getSize());
        bookCover.setMediaType(file.getContentType());
        bookCover.setPreview(generateCoverPreview(coverPath)); // Создаем preview шириной в (КОНСТ) = 100 px

        bookCoverRepository.save(bookCover); // Сохраняем всю информацию в базу данных
    }

    // Ищем обложку по ID книжки
    public BookCover findBookCover(Long bookId) {
        // Если обложка существует - возвращаем ее
        Optional<BookCover> optionalBookCover = bookCoverRepository.findByBook_BookId(bookId);
        if (optionalBookCover.isPresent()) {
            return optionalBookCover.get();
        } else {
            // Если обложки нет - создаем ее
            BookCover newBookCover = new BookCover();
            Book book = bookService.findBook(bookId);
            newBookCover.setBook(book);
            return bookCoverRepository.save(newBookCover);
        }
    }

    // Создаем preview обложки
    private byte[] generateCoverPreview(Path coverPath) throws IOException {
        try (
                InputStream is = Files.newInputStream(coverPath);
                BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / PREVIEW_WIDTH);
            BufferedImage previewCover = new BufferedImage(PREVIEW_WIDTH, height, image.getType());
            Graphics2D graphics = previewCover.createGraphics();
            graphics.drawImage(image, 0,0,PREVIEW_WIDTH,height, null);
            graphics.dispose();

            ImageIO.write(previewCover, getExtension(coverPath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
