package other.lessons.library.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "books", indexes = {
        @Index(name = "idx_book_author", columnList = "author"),
        @Index(name = "idx_book_name", columnList = "name")
})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    @SequenceGenerator(name = "book_seq", sequenceName = "book_id_seq", allocationSize = 1)
    private Long bookId;

    @Column(nullable = false, length = 500)
    private String name;

    @Column(nullable = false, length = 300)
    private String author;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
    private BookCover bookCover;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId) &&
                Objects.equals(name, book.name) &&
                Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(bookId);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(author);
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + bookId +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
