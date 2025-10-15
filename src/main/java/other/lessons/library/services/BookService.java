package other.lessons.library.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import other.lessons.library.models.Book;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class BookService {

    private final Map<Long, Book> books = new HashMap<>();
    // Начальный id = 0, в дальнейшем будем его увеличивать
    private long lastId = 0;

    // Создаем книжку
    public Book createBook(Book book) {
        // Увеличиваем id на 1
        book.setId(++lastId);
        // Кладем книжку в мапу с новым id
        books.put(lastId, book);
        return book;
    }

    // Ищем книжку по id
    public Book findBook(long id) {
        return books.get(id);
    }

    // Редактируем книжку
    public Book editBook(Book book) {
        if (books.containsKey(book.getId())) {
            books.put(book.getId(), book);
            return book;
        }
        return null;
    }

    // Удаляем книжку по id
    public Book deleteBook(long id) {
        return books.remove(id);
    }

    public Collection<Book> getAllBooks() {
        return books.values();
    }
}
