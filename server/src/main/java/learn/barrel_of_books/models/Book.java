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
    private List<CategoryBook> categories;
}
