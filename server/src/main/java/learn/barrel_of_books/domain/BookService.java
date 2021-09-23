package learn.barrel_of_books.domain;


import learn.barrel_of_books.data.*;
import learn.barrel_of_books.models.*;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BookService {

    private final BookRepository repository;
    private final GenreBookRepository gbRepository;
    private final GenreRepository genreRepository;
    private final StoreBookRepository sbRepository;
    private final StoreRepository storeRepository;

    public BookService(BookRepository repository, GenreBookRepository gbRepository, GenreRepository genreRepository, StoreBookRepository sbRepository, StoreRepository storeRepository) {
        this.gbRepository = gbRepository;
        this.repository = repository;
        this.genreRepository = genreRepository;
        this.sbRepository = sbRepository;
        this.storeRepository = storeRepository;
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public List<Book> findByTitleAuthorOrKeyword(String phrase) {
        return repository.findByTitleAuthorOrKeyword(phrase);
    }

    public List<Book> findByGenreName(String genreName) {
        return repository.findByGenreName(genreName);
    }

    public Book findById(int bookId) {
        return repository.findById(bookId);
    }

    public Book findByTitle(String title) {
        return repository.findByTitle(title);
    }

    public Result<Book> add(Book book) {
        Result<Book> result = validate(book);

        if(!result.isSuccess()) {
            return result;
        }

        if(book.getBookId() != 0) {
            result.addMessage("ID must not be set for the `add` operation",ResultType.INVALID);
        }

        for(Book listBook : repository.findAll()) {
            if(listBook.getAuthor().equalsIgnoreCase(book.getAuthor()) && listBook.getTitle().equalsIgnoreCase(book.getTitle())) {
                result.addMessage("This title/author combination already exists in the system", ResultType.INVALID);
            }
        }

        if(result.isSuccess()) {
            book = repository.add(book);
            result.setPayload(book);
        }
            return result;
    }



    public Result<Book> update(Book book) {
        Result<Book> result = validate(book);

        if(!result.isSuccess()) {
            return result;
        }

        if(book.getBookId() <= 0) {
            result.addMessage("Book ID must be set for `update` operation", ResultType.INVALID);
            return result;
        }


        for(Book listBook : repository.findAll()) {
            if(listBook.getBookId() == book.getBookId()) {
                continue;
            }
            if(listBook.getAuthor().equalsIgnoreCase(book.getAuthor()) && listBook.getTitle().equalsIgnoreCase(book.getTitle())) {
                result.addMessage("This title/author combination already exists in the system", ResultType.INVALID);
                return result;
            }
        }

        if(!repository.update(book)) {
            String message = String.format("Book with ID number %s could not be found",book.getBookId());
            result.addMessage(message,ResultType.NOT_FOUND);
        }

        return result;
    }



    public Result<Book> delete(int bookId) {
        Result<Book> result = new Result<>();

        if(!repository.delete(bookId)) {
            String message = String.format("Book with ID number %s could not be found",bookId);
            result.addMessage(message,ResultType.NOT_FOUND);
        }
        return result;
    }




    private Result<Book> validate(Book book) {
        Result<Book> result = new Result<>();

        if(book == null) {
            result.addMessage("Book cannot be null", ResultType.INVALID);
            return result;
        }

        if(book.getTitle() == null || book.getTitle().isBlank()) {
            result.addMessage("Title is required",ResultType.INVALID);
        }

        if(book.getAuthor() == null || book.getAuthor().isBlank()) {
            result.addMessage("Author is required",ResultType.INVALID);
        }

        if(book.getGenres() == null || book.getGenres().size() <= 0 || book.getGenres().isEmpty()) {
            result.addMessage("Book must belong to at least 1 genre",ResultType.INVALID);
        }

        if(book.getStores() == null || book.getStores().size() <= 0 || book.getStores().isEmpty()) {
            result.addMessage("Book must belong to at least 1 store",ResultType.INVALID);
        }


        if(book.getQuantity() <= 0) {
            result.addMessage("Quantity must be greater than 0", ResultType.INVALID);
        }

        if(book.getPrice().doubleValue() <= 0) {
            result.addMessage("Price must be a positive number", ResultType.INVALID);
        }

        return result;

    }
}
