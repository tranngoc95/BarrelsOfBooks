package learn.barrel_of_books.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private int transactionId;

    @NotNull(message = "List of books cannot be null.")
    private List<CartItem> books;

    @NotNull(message = "Date cannot be null.")
    private LocalDate date;

    @NotBlank(message = "UserId is required and cannot be blank")
    private String userId;

    private BigDecimal total = BigDecimal.ZERO;

    private boolean employeeDiscount = false;

    public void updateTotal(){
        for(CartItem each : books){
            total = total.add(each.getBook().getPrice().multiply(new BigDecimal(each.getQuantity())));
        }
        if(employeeDiscount){
            total = total.multiply(new BigDecimal("0.7"));
        }
        total = total.setScale(2, RoundingMode.CEILING);
    }
}
