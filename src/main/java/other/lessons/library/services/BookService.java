package other.lessons.library.services;

import org.springframework.stereotype.Service;
import other.lessons.library.models.Book;
import other.lessons.library.repositories.BookRepository;

import java.util.Collection;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Создаем книжку
    public Book createBook(Book book) {
        if (book.getBookId() != null && book.getBookId() == 0) {
            book.setBookId(null);
        }
        return bookRepository.save(book);
    }

    // Ищем книжку по bookId
    public Book findBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
    }

    // Редактируем книжку
    public Book editBook(Book book) {
        return bookRepository.save(book);
    }

    // Удаляем книжку по bookId
    public void deleteBook(long bookId) {
        bookRepository.deleteById(bookId);
    }

    // Получаем список всех книг из базы
    public Collection<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Ищем книжки по названию, делая поиск регистронезависимым
    public Book findByName(String name) {
        return bookRepository.findByNameIgnoreCase(name);
    }

    // Ищем книжки по автору, допуская ввод части запроса, а так же делая запрос регистронезависимым
    public Collection<Book> findByAuthor(String author) {
        return bookRepository.findBooksByAuthorContainsIgnoreCase(author);
    }

    // Ищем книжки по части названия, делая запрос регистронезависимым
    public Collection<Book> findByNamePart(String part) {
        return bookRepository.findAllByNameContainsIgnoreCase(part);
    }
}
