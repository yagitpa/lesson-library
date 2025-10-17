package other.lessons.library.models;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "book_covers")
public class BookCover {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cover_seq")
    @SequenceGenerator(name = "cover_seq", sequenceName = "cover_id_seq", allocationSize = 1)
    private Long coverId;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "media_type")
    private String mediaType;

    @Lob
    private byte[] preview;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public long getCoverId() {
        return coverId;
    }

    public void setCoverId(long coverId) {
        this.coverId = coverId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getPreview() {
        return preview;
    }

    public void setPreview(byte[] preview) {
        this.preview = preview;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        BookCover bookCover = (BookCover) o;
        return coverId == bookCover.coverId &&
                fileSize == bookCover.fileSize &&
                Objects.equals(filePath, bookCover.filePath) &&
                Objects.equals(mediaType, bookCover.mediaType) &&
                Arrays.equals(preview, bookCover.preview) &&
                Objects.equals(book, bookCover.book);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(coverId);
        result = 31 * result + Objects.hashCode(filePath);
        result = 31 * result + Long.hashCode(fileSize);
        result = 31 * result + Objects.hashCode(mediaType);
        result = 31 * result + Arrays.hashCode(preview);
        result = 31 * result + Objects.hashCode(book);
        return result;
    }
}
