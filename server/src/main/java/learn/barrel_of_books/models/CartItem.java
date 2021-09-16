package learn.barrel_of_books.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private int cartItemId;
    private int transactionId=0;

    @NotBlank(message = "UserId is required and cannot be blank")
    private String userId;

    @NotNull(message = "Book cannot be null.")
    private Book book;

    @Min(value = 1, message = "Quantity must be above 0.")
    private int quantity;
}
