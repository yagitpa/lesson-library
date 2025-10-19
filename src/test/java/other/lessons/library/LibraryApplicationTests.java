package other.lessons.library;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import other.lessons.library.controllers.BooksController;
import other.lessons.library.models.Book;
import other.lessons.library.services.BookCoverService;
import other.lessons.library.services.BookService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BooksController.class)
public class LibraryApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookCoverService bookCoverService;

    @Test
    public void saveBookTest() throws Exception {
        final String name = "Test Book name";
        final String author = "Test Author Name";
        final long id = 1;

        JSONObject bookObject = new JSONObject();
        bookObject.put("name", name);
        bookObject.put("author", author);

        Book book = new Book();
        book.setBookId(id);
        book.setName(name);
        book.setAuthor(author);

        when(bookService.createBook(any(Book.class))).thenReturn(book);
        when(bookService.findBook(any(Long.class))).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders
                       .post("/books")
                       .content(bookObject.toString())
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.bookId").value(id))
               .andExpect(jsonPath("$.name").value(name))
               .andExpect(jsonPath("$.author").value(author));

        mockMvc.perform(MockMvcRequestBuilders
                       .get("/books/" + id)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.bookId").value(id))
               .andExpect(jsonPath("$.name").value(name))
               .andExpect(jsonPath("$.author").value(author));
    }
}