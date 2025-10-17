package other.lessons.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import other.lessons.library.models.BookCover;

import java.util.Optional;

public interface BookCoverRepository extends JpaRepository<BookCover, Long> {

    Optional<BookCover> findByBook_BookId(Long bookId);
}
