package learn.barrel_of_books.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private int transactionId;

    @NotNull(message = "List of books cannot be null.")
    List<CartItem> books;

    @NotNull(message = "Date cannot be null.")
    LocalDate date;

    @Min(value = 1, message = "userId is required.")
    int userId;
}
