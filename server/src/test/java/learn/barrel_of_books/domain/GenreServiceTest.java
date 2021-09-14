package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.GenreRepository;
import learn.barrel_of_books.models.Genre;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class GenreServiceTest {

    @MockBean
    GenreRepository repository;

    @Autowired
    GenreService service;

    // READ
    @Test
    void shouldFindAll() {
        List<Genre> expected = new ArrayList<>();
        expected.add(makeExistingGenre());
        expected.add(new Genre(2, "Adventure", "Adventure description"));
        expected.add(new Genre(3, "Romance", "Romance description"));
        expected.add(new Genre(4, "Horror", "Horror description"));

        Mockito.when(repository.findAll()).thenReturn(expected);

        assertEquals(expected, service.findAll());
    }

    @Test
    void shouldFindById() {
        Genre expected = makeExistingGenre();
        Mockito.when(repository.findById(1)).thenReturn(expected);
        assertEquals(expected, service.findById(1));
    }

    @Test
    void shouldNotFindMissingId() {
        Mockito.when(repository.findById(10)).thenReturn(null);
        assertNull(service.findById(10));
    }

    // CREATE
    @Test
    void shouldAdd() {
        Genre input = makeNewGenre();
        Genre expected = makeNewGenre();
        expected.setGenreId(5);

        Mockito.when(repository.add(input)).thenReturn(expected);

        Result<Genre> actual = service.add(input);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldNotAddPresetID() {
        Genre input = makeNewGenre();
        input.setGenreId(7);

        Result<Genre> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldNotAddNull() {
        Result<Genre> actual = service.add(null);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotAddEmptyName() {
        Genre input = makeNewGenre();
        input.setName("");

        Result<Genre> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));

        input.setName(null);

        actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));
    }

    @Test
    void shouldNotAddDuplicateName() {
        Genre input = makeNewGenre();
        Genre found = makeNewGenre();
        found.setGenreId(2);

        Mockito.when(repository.findByName(input.getName())).thenReturn(found);

        Result<Genre> actual = service.add(input);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("duplicate"));
    }

    // UPDATE
    @Test
    void shouldUpdate() {
        Genre genre = makeExistingGenre();
        genre.setDescription("New exciting description.");

        Mockito.when(repository.update(genre)).thenReturn(true);

        Result<Genre> actual = service.update(genre);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateNoId() {
        Genre genre = makeExistingGenre();
        genre.setGenreId(0);

        Result<Genre> actual = service.update(genre);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldNotUpdateNull() {
        Result<Genre> actual = service.update(null);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("null"));
    }

    @Test
    void shouldNotUpdateEmptyName() {
        Genre genre = makeExistingGenre();
        genre.setName(" ");

        Result<Genre> actual = service.update(genre);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));

        genre.setName(null);

        actual = service.update(genre);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));
    }

    @Test
    void shouldNotUpdateDuplicateName() {
        Genre genre = makeExistingGenre();
        Genre found = makeExistingGenre();
        found.setGenreId(2);

        Mockito.when(repository.findByName(genre.getName())).thenReturn(found);

        Result<Genre> actual = service.update(genre);
        assertEquals(ResultType.INVALID, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("name"));
    }

    @Test
    void shouldNotUpdateMissing() {
        Genre genre = makeExistingGenre();
        genre.setGenreId(10);

        Mockito.when(repository.update(genre)).thenReturn(false);

        Result<Genre> actual = service.update(genre);
        assertEquals(ResultType.NOT_FOUND, actual.getType());
        assertTrue(actual.getMessages().get(0).toLowerCase().contains("not found"));
    }

    // DELETE
    @Test
    void shouldDelete() {
        Mockito.when(repository.deleteById(4)).thenReturn(true);
        assertTrue(service.deleteById(4));
    }

    @Test
    void shouldNotDeleteMissing() {
        Mockito.when(repository.deleteById(10)).thenReturn(false);
        assertFalse(service.deleteById(10));
    }

    // Helper methods
    private Genre makeExistingGenre(){
        return new Genre(1, "Fantasy", "Fantasy description");
    }

    private Genre makeNewGenre(){
        return new Genre(0, "Thriller", "Thriller description");
    }

}