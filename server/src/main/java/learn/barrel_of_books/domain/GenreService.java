package learn.barrel_of_books.domain;

import learn.barrel_of_books.data.GenreRepository;
import learn.barrel_of_books.models.Genre;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class GenreService {

    private final GenreRepository repository;

    public GenreService(GenreRepository repository) {
        this.repository = repository;
    }

    public List<Genre> findAll() {
        return repository.findAll();
    }

    public Genre findById(int genreId) {
        return repository.findById(genreId);
    }

    public Result<Genre> add(Genre genre) {
        Result<Genre> result = validate(genre);
        if(result.isSuccess()){

            if (genre.getGenreId() != 0) {
                result.addMessage("genreId cannot be set for `add` operation", ResultType.INVALID);
                return result;
            }

            genre = repository.add(genre);
            result.setPayload(genre);
        }

        return result;
    }

    public Result<Genre> update(Genre genre) {
        Result<Genre> result = validate(genre);

        if(result.isSuccess()){
            if (genre.getGenreId() <= 0) {
                result.addMessage("genreId must be set for `update` operation", ResultType.INVALID);
                return result;
            }
            if(!repository.update(genre)) {
                String msg = String.format("genreId: %s, not found", genre.getGenreId());
                result.addMessage(msg, ResultType.NOT_FOUND);
            }
        }

        return result;
    }

    public boolean deleteById(int genreId) {
        return repository.deleteById(genreId);
    }

    private Result<Genre> validate(Genre genre){
        Result<Genre> result = new Result<>();

        if(genre==null){
            result.addMessage("Genre cannot be null.", ResultType.INVALID);
            return result;
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Genre>> violations = validator.validate(genre);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Genre> violation : violations) {
                result.addMessage(violation.getMessage(), ResultType.INVALID);
            }
        }

        if(result.isSuccess()){
            Genre otherGenre = repository.findByName(genre.getName());
            if(otherGenre != null && otherGenre.getGenreId() != genre.getGenreId()){
                result.addMessage("Name should not be duplicated.", ResultType.INVALID);
            }
        }

        return result;
    }
}
