package other.lessons.library.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "readers", indexes = {
        @Index(name = "idx_reader_personalNumber", columnList = "personalNumber"),
        @Index(name = "idx_reader_secondName", columnList = "secondName")
})
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reader_seq")
    @SequenceGenerator(name = "reader_seq", sequenceName = "reader_id_seq", allocationSize = 1)
    private long readerId;

    @Column(nullable = false, length = 20)
    private int personalNumber;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(length = 50)
    private String middleName;

    @Column(nullable = false, length = 50)
    private String secondName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "reader", fetch = FetchType.LAZY)
    private Collection<Book> books;

    public long getReaderId() {
        return readerId;
    }

    public void setReaderId(long readerId) {
        this.readerId = readerId;
    }

    public int getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(int personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Collection<Book> getBooks() {
        return books;
    }

    public void setBooks(Collection<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Reader reader = (Reader) o;
        return readerId == reader.readerId &&
                personalNumber == reader.personalNumber &&
                Objects.equals(firstName, reader.firstName) &&
                Objects.equals(middleName, reader.middleName) &&
                Objects.equals(secondName, reader.secondName);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(readerId);
        result = 31 * result + personalNumber;
        result = 31 * result + Objects.hashCode(firstName);
        result = 31 * result + Objects.hashCode(middleName);
        result = 31 * result + Objects.hashCode(secondName);
        return result;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "readerId=" + readerId +
                ", personalNumber=" + personalNumber +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }
}
