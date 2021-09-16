package learn.barrel_of_books.models;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private int bookId;
    private int quantity;
    private String title;
    private String description;
    private String author;
    private BigDecimal price;
    private List<GenreBook> genres;

    public Book(int bookId, int quantity, String title, String description, String author, BigDecimal price) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.title = title;
        this.description = description;
        this.author = author;
        this.price = price;
    }

}