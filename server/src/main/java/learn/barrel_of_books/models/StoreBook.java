package learn.barrel_of_books.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StoreBook {
    private int bookId;
    private Store store;
    private int quantity;
}
