package learn.barrel_of_books.data;

import learn.barrel_of_books.models.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();

    List<Book> findByTitleAuthorOrKeyword(String phrase);

    Book findById(int id);

    Book findByTitle(String title);

    Book add(Book book);

    boolean update(Book book);

    boolean delete(int bookId);
}
