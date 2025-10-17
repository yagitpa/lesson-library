package other.lessons.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import other.lessons.library.models.Book;

import java.util.Collection;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByNameIgnoreCase(String name);

    Collection<Book> findBooksByAuthorContainsIgnoreCase(String author);

    Collection<Book> findAllByNameContainsIgnoreCase(String part);

    Collection<Book> findBooksByNameIgnoreCaseAndAuthorIgnoreCase(String name, String author);

    Optional<Book> findByBookId(Long bookId);
}
