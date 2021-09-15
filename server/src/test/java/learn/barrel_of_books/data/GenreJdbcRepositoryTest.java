package learn.barrel_of_books.data;

import learn.barrel_of_books.models.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class GenreJdbcRepositoryTest {

    @Autowired
    GenreRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindALl() {
        List<Genre> genreList = repository.findAll();
        assertNotNull(genreList);
        assertTrue(genreList.size()>=3);
    }

    @Test
    void shouldFindById() {
        Genre actual = repository.findById(1);
        assertNotNull(actual);
        assertEquals("Fantasy", actual.getName());
    }

    @Test
    void shouldFindByName() {
        Genre actual = repository.findByName("Fantasy");
        assertNotNull(actual);
        assertEquals(1, actual.getGenreId());
    }

    @Test
    void shouldAdd() {
        Genre input = new Genre(0, "History", "History description");
        Genre actual = repository.add(input);
        assertNotNull(actual);
        assertEquals(5, actual.getGenreId());
    }

    @Test
    void shouldUpdate() {
        Genre input = new Genre(2, "Thriller", "Thriller description");
        assertTrue(repository.update(input));
    }

    @Test
    void shouldNotUpdateMissing() {
        Genre input = new Genre(21, "Cooking", "Cooking description");
        assertFalse(repository.update(input));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(3));
    }

    @Test
    void shouldNotDeleteMissing() {
        assertFalse(repository.deleteById(10));
    }

}