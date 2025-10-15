package other.lessons.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import other.lessons.library.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
