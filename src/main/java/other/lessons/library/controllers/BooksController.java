package other.lessons.library.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import other.lessons.library.models.Book;
import other.lessons.library.models.BookCover;
import other.lessons.library.services.BookCoverService;
import other.lessons.library.services.BookService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


@RestController
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;
    private final BookCoverService bookCoverService;

    private static final int SIZE_BOOK_COVER = 1024 * 500;
    private static final int BUFFER_SIZE = 1024;

    public BooksController(BookService bookService, BookCoverService bookCoverService) {
        this.bookService = bookService;
        this.bookCoverService = bookCoverService;
    }

    @GetMapping("{bookId}") // GET http://localhost:9091/books/23
    public ResponseEntity getBookInfo(@PathVariable Long bookId) {
        Book book = bookService.findBook(bookId);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @PostMapping // POST http://localhost:9091/books
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @PutMapping // PUT http://localhost:9091/books
    public ResponseEntity<Book> editBook(@RequestBody Book book) {
        Book existedBook = bookService.editBook(book);
        if (existedBook == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existedBook);
    }

    @DeleteMapping("{bookId}") // DELETE http://localhost:9091/books/23
    public ResponseEntity deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{bookId}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBookCover(@PathVariable Long bookId, @RequestParam MultipartFile cover) throws IOException {
        if (cover.getSize() >= SIZE_BOOK_COVER) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        bookCoverService.uploadBookCover(bookId, cover);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{bookId}/cover/preview")
    public ResponseEntity<byte[]> downloadBookCoverPreview(@PathVariable Long bookId) {
        BookCover bookCover = bookCoverService.findBookCover(bookId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(bookCover.getMediaType())); // Заголовок говорит о том что за
        // тип данных мы возвращаем через HTTP-запрос и как его обрабатывать
        headers.setContentLength(bookCover.getPreview().length); // Сообщаем размер файла (Мб, Кб) для контроля
        // успешной загрузки файла - сколько всего, сколько осталось до полного скачивания, все ли загружено

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(bookCover.getPreview());
    }

    @GetMapping(value = "/{bookId}/cover")
    public void downloadBookCover(@PathVariable Long bookId, HttpServletResponse response) throws IOException {
        BookCover bookCover = bookCoverService.findBookCover(bookId);

        Path path = Path.of(bookCover.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();
             BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
             BufferedOutputStream bos = new BufferedOutputStream(os, BUFFER_SIZE)
        ) {
           response.setStatus(200);
           response.setContentType(bookCover.getMediaType());
           response.setContentLength((int) bookCover.getFileSize());
           bis.transferTo(bos);
        }
    }

    @GetMapping // GET http://localhost:9091/books
    public ResponseEntity findBooks(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String author,
                                    @RequestParam(required = false) String namePart) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(bookService.findByName(name));
        }
        if (author != null && !author.isBlank()) {
            return ResponseEntity.ok(bookService.findByAuthor(author));
        }
        if (namePart != null && !namePart.isBlank()) {
            return ResponseEntity.ok(bookService.findByNamePart(namePart));
        }
        return ResponseEntity.ok(bookService.getAllBooks());
    }
}
