package learn.barrel_of_books.data;

import learn.barrel_of_books.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BookJdbcRepositoryTest {

    @Autowired
    BookJdbcRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Book> books = repository.findAll();
        assertTrue(books.size() >= 1);
    }

    @Test
    void shouldFindById() {
        Book book = repository.findById(1);
        assertEquals("magic",book.getDescription());
    }

    @Test
    void shouldNotFindNonExistentId() {
        Book book = repository.findById(500);
        assertNull(book);
    }

    @Test
    void shouldFindByTitle() {
        Book book = repository.findByTitle("hp");
        assertEquals(1,book.getBookId());
    }

    @Test
    void shouldNotFindByMissingTitle() {
        Book book = repository.findByTitle("gsgeggerger");
        assertNull(book);
    }

    @Test
    void shouldFindByTitleAuthorOrKeyWord() {
        List<Book> books = repository.findByTitleAuthorOrKeyword("hp");
        assertEquals(1, books.size());
    }

    @Test
    void shouldFindByGenreName() {
        List<Book> books = repository.findByGenreName("fantasy");
        assertEquals(2, books.size());
    }

    @Test
    void shouldAddBook() {
        List<Book> books = repository.findAll();
        Book book = makeBook();
        repository.add(book);
        List<Book> newBooks = repository.findAll();
        assertEquals(books.size() + 1, newBooks.size());
    }

    @Test
    void shouldUpdateBook() {
        Book book = new Book();
        book.setBookId(1);
        book.setTitle("hp");
        book.setDescription("magic");
        book.setAuthor("jk rowling");
        book.setPrice(new BigDecimal(4.35));
        book.setQuantity(45);
        boolean success = repository.update(book);
        assertTrue(success);
        assertEquals("jk rowling", repository.findById(1).getAuthor());

    }


    @Test
    void shouldDeleteById() {
        List<Book> books = repository.findAll();
        boolean success = repository.delete(2);
        assertEquals(books.size() - 1, repository.findAll().size());

    }




    private Book makeBook() {
        Book book = new Book();
        book.setTitle("harry potter");
        book.setDescription("magical man");
        book.setAuthor("jk rowling");
        book.setPrice(new BigDecimal("13.45"));
        book.setQuantity(12);
        return book;
    }




}