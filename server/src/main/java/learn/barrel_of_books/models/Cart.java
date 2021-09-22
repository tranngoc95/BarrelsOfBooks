package learn.barrel_of_books.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Cart {
    private List<CartItem> books = new ArrayList<>();
    private BigDecimal subtotal = BigDecimal.ZERO;
    private int itemNum = 0;

    public void updateSubtotal() {
        BigDecimal theTotal = BigDecimal.ZERO;
        for (CartItem each : books) {
            theTotal = theTotal.add(each.getBook().getPrice().multiply(new BigDecimal(each.getQuantity())));
        }
        subtotal = theTotal.setScale(2, RoundingMode.CEILING);
    }

    public void updateItemNum() {
        itemNum = 0;
        for (CartItem each : books) {
            itemNum += each.getQuantity();
        }
    }
}
