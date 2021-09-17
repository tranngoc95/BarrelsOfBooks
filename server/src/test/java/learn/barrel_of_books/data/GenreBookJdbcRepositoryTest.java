package learn.barrel_of_books.data;

import learn.barrel_of_books.models.Genre;
import learn.barrel_of_books.models.GenreBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class GenreBookJdbcRepositoryTest {

    @Autowired
    GenreBookJdbcRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }


    @Test
    void shouldFindByBookId() {
        List<GenreBook> list = repository.findByBookId(1);
        assertTrue(list.size() >= 1);
    }

    @Test
    void shouldNotFindByMissingBookId() {
        List<GenreBook> list = repository.findByBookId(11);
        assertNull(list);
    }

    @Test
    void shouldAdd() {
        Genre genre = new Genre(4, "Romance", "Romance description");
        GenreBook gb = new GenreBook(genre,3);
        boolean success = repository.add(gb);
        assertTrue(success);
    }

    @Test
    void shouldDelete() {
        boolean success = repository.delete(3,1);
        assertTrue(success);
    }

    @Test
    void shouldNotDelete() {
        boolean success = repository.delete(4,4);
        assertFalse(success);
    }
}
