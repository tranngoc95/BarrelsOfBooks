package learn.barrel_of_books.domain;


import learn.barrel_of_books.data.GenreBookRepository;
import learn.barrel_of_books.models.Genre;
import learn.barrel_of_books.models.GenreBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class GenreBookServiceTest {

    @Autowired
    GenreBookService service;

    @MockBean
    GenreBookRepository repository;


    @Test
    void shouldAdd() {
        Genre genre = new Genre(1,"fiction", "not real");
        GenreBook gb = new GenreBook(genre,1);

        when(repository.add(gb)).thenReturn(true);
        Result<GenreBook> result = service.add(gb);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddIfBookIdIs0() {
        Genre genre = new Genre(1,"fiction", "not real");
        GenreBook gb = new GenreBook(genre,0);

        when(repository.add(gb)).thenReturn(true);
        Result<GenreBook> result = service.add(gb);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfGenreIsNull() {
        GenreBook gb = new GenreBook(null,1);

        when(repository.add(gb)).thenReturn(true);
        Result<GenreBook> result = service.add(gb);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfDuplicate() {
        Genre genre = new Genre(1,"fiction", "not real");
        GenreBook gb = new GenreBook(genre,1);
        List<GenreBook> list = new ArrayList<>();
        list.add(gb);

        when(repository.add(gb)).thenReturn(true);
        when(repository.findByBookId(1)).thenReturn(list);

        Result<GenreBook> result = service.add(gb);
        assertFalse(result.isSuccess());

    }




}