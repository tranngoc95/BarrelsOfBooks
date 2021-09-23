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
    @EqualsAndHashCode.Exclude
    private int quantity;
    private String title;
    private String description;
    private String author;
    private BigDecimal price;
    private String publisher;
    private String language;
    private int pages;
    private String ageRange;
    private String dimensions;
    private String isbn13;
    private List<GenreBook> genres;
    private List<StoreBook> stores;

    public Book(int bookId, int quantity, String title, String description, String author, BigDecimal price,
                List<GenreBook> genres, List<StoreBook> stores) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.title = title;
        this.description = description;
        this.author = author;
        this.price = price;
        this.genres = genres;
        this.stores = stores;
    }

    public Book(int bookId, int quantity, String title, String description, String author, BigDecimal price) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.title = title;
        this.description = description;
        this.author = author;
        this.price = price;
    }

    public void addQuantity(int amount){
            quantity += amount;
    }

    public void subtractQuantity(int amount){
        if(amount<=quantity) {
            quantity -= amount;
        }
    }
}
