package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.BookRepository;
import learn.barrel_of_books.models.Book;
import learn.barrel_of_books.models.Genre;
import learn.barrel_of_books.models.GenreBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BookServiceTest {
    @Autowired
    BookService service;

    @MockBean
    BookRepository repository;




    @Test
    void shouldAddBook() {
        Book book = makeBook();
        when(repository.findAll()).thenReturn(getBookList());
        when(repository.add(book)).thenReturn(book);
        Result<Book> result = service.add(book);
        assertTrue(result.isSuccess());
        assertEquals("harry plotter",result.getPayload().getTitle());
    }

    @Test
    void shouldNotAddIfBookIsNull() {
        Book book = null;
        Result<Book> result = service.add(book);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfTitleIsBlank() {
        Book book = makeBook();
        book.setTitle("    ");
        Result<Book> result = service.add(book);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfAuthorIsBlank() {
        Book book = makeBook();
        book.setAuthor("    ");
        Result<Book> result = service.add(book);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfGenresAreEmpty() {
        Book book = makeBook();
        List<GenreBook> cats = new ArrayList<>();
        book.setGenres(cats);
        Result<Book> result = service.add(book);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfQuantityIs0() {
        Book book = makeBook();
        book.setQuantity(0);
        Result<Book> result = service.add(book);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfPriceIs0() {
        Book book = makeBook();
        book.setPrice(BigDecimal.ZERO);
        Result<Book> result = service.add(book);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfIdIsSet() {
        Book book = makeBook();
        book.setBookId(5);
        when(repository.findAll()).thenReturn(getBookList());
        when(repository.add(book)).thenReturn(book);
        Result<Book> result = service.add(book);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfAuthorAndTitleAreDuplicated() {
        when(repository.findAll()).thenReturn(getBookList());
        Book book = makeBook();
        book.setTitle("harry potter");
        Result<Book> result = service.add(book);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldUpdate() {
        Book book = makeBook();
        book.setTitle("hermione");
        book.setBookId(1);
        when(repository.findAll()).thenReturn(getBookList());
        when(repository.update(book)).thenReturn(true);
        Result<Book> result = service.update(book);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateIfBookIdIs0() {
        Book book = makeBook();
        when(repository.findAll()).thenReturn(getBookList());
        when(repository.update(book)).thenReturn(true);
        Result<Book> result = service.update(book);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateIfAuthorAndTitleIsDuplicated() {
        when(repository.findAll()).thenReturn(getBookList());
        Book book = makeBook();
        book.setBookId(2);
        book.setTitle("harry potter");
        when(repository.update(book)).thenReturn(true);
        Result<Book> result = service.update(book);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldUpdateIfAuthorAndTitleIsNotChanged() {
        when(repository.findAll()).thenReturn(getBookList());
        Book book = makeBook();
        book.setBookId(1);
        book.setTitle("harry potter");
        book.setQuantity(100);
        when(repository.update(book)).thenReturn(true);
        Result<Book> result = service.update(book);
        assertTrue(result.isSuccess());
    }



    private Book makeBook() {
        Book book = new Book();
        book.setTitle("harry plotter");
        book.setDescription("magical man");
        List<GenreBook> cats = new ArrayList<>();
        GenreBook cb = new GenreBook();
        Genre c = new Genre();
        c.setGenreId(1);
        cb.setBookId(1);
        cb.setGenre(c);
        cats.add(cb);
        book.setGenres(cats);
        book.setAuthor("jk rowling");
        book.setPrice(new BigDecimal("13.45"));
        book.setQuantity(12);
        return book;
    }

    private List<Book> getBookList() {
        List<Book> books = new ArrayList<>();
        Book book1 = new Book();
        Book book2 = new Book();
        book1.setBookId(1);
        book1.setTitle("harry potter");
        book1.setDescription("magical man");
        book1.setAuthor("jk rowling");
        book1.setPrice(new BigDecimal("13.45"));
        book1.setQuantity(12);

        book2.setBookId(2);
        book2.setTitle("red robin");
        book2.setDescription("yum");
        book2.setAuthor("bear");
        book2.setPrice(new BigDecimal("14.55"));
        book2.setQuantity(9);

        books.add(book1);
        books.add(book2);
        return books;

    }
}