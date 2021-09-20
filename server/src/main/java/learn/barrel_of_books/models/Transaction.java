package learn.barrel_of_books.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "List of books cannot be null.")
    private List<CartItem> books;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    @NotBlank(message = "UserId is required and cannot be blank")
    private String userId;

    private BigDecimal total = BigDecimal.ZERO;

    private boolean employeeDiscount = false;

    private TransactionStatus status = TransactionStatus.ORDERED;

    public void updateTotal(){
        BigDecimal theTotal = BigDecimal.ZERO;
        for(CartItem each : books){
            theTotal = theTotal.add(each.getBook().getPrice().multiply(new BigDecimal(each.getQuantity())));
        }
        if(employeeDiscount){
            theTotal = theTotal.multiply(new BigDecimal("0.7"));
        }
        total = theTotal.setScale(2, RoundingMode.CEILING);
    }
}
