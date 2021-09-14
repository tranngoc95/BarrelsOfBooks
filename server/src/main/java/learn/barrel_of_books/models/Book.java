package learn.barrel_of_books.models;

import java.math.BigDecimal;
import java.util.List;

public class Book {
    private int bookId;
    private String title;
    private String description;
    private String author;
    private BigDecimal price;
    private List<CategoryBook> categories;

    



    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<CategoryBook> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryBook> categories) {
        this.categories = categories;
    }
}
