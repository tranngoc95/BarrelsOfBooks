package learn.barrel_of_books.domain;


import learn.barrel_of_books.data.GenreBookRepository;
import learn.barrel_of_books.models.GenreBook;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreBookService {
    private GenreBookRepository repository;

    public GenreBookService(GenreBookRepository repository) {
        this.repository = repository;
    }


    public List<GenreBook> findByBookId(int bookId) {
        return repository.findByBookId(bookId);
    }

    public Result<GenreBook> add(GenreBook genreBook) {
        Result<GenreBook> result = new Result<>();

        if(genreBook.getBookId() <= 0) {
            result.addMessage("Book ID is required", ResultType.INVALID);
        }

        if(genreBook.getGenre() == null) {
            result.addMessage("Genre is required", ResultType.INVALID);
        }
        if(findByBookId(genreBook.getBookId()) != null) {
            for (GenreBook gb : findByBookId(genreBook.getBookId())) {
                if (genreBook.getGenre().getGenreId() == gb.getGenre().getGenreId()) {
                    result.addMessage("The entry already exists in the system", ResultType.INVALID);
                }
            }
        }
        if(result.isSuccess()) {
            repository.add(genreBook);
            result.setPayload(genreBook);
        }

        return result;
    }


    public Result<GenreBook> delete(int bookId) {
        Result<GenreBook> result = new Result<>();

        if(!repository.delete(bookId)) {
            result.addMessage("This entry does not exist",ResultType.NOT_FOUND);
        }
        return result;
    }


}
