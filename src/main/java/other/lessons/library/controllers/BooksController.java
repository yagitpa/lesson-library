package other.lessons.library.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import other.lessons.library.models.Book;
import other.lessons.library.services.BookService;

import java.util.Collection;

@RestController
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("{id}") // GET http://localhost:9091/books/23
    public ResponseEntity getBookInfo(@PathVariable Long id) {
        Book book = bookService.findBook(id);
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

    @DeleteMapping("{id}") // DELETE http://localhost:9091/books/23
    public Book deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    @GetMapping // GET http://localhost:9091/books
    public ResponseEntity<Collection<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
}
