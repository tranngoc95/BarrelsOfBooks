package learn.barrel_of_books.data;

import learn.barrel_of_books.models.Book;
import learn.barrel_of_books.models.CartItem;
import learn.barrel_of_books.models.Transaction;
import learn.barrel_of_books.models.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TestData {
    public static Book makeBook() {
        return new Book(1, 45, "hp", "magic",
                "jk rowling", new BigDecimal("13.45"), null, null);
    }

    public static CartItem makeUpdateCartItem() {return new CartItem(3, 0, "1", makeBook(), 1);}

    public static CartItem makeExistingCartItem(){
        return new CartItem(2, 1, "1", makeBook(), 1);
    }

    public static CartItem makeNewCartItem(){
        return new CartItem(0, 0, "5", makeBook(), 1);
    }

    public static Transaction makeExistingTransaction() {
        return new Transaction(1,List.of(makeExistingCartItem()),
                LocalDate.parse("2020-09-09"),"1",new BigDecimal("9.42"), true, TransactionStatus.ORDERED);
    }

    public static Transaction makeNewTransaction() {
        CartItem cartItem = new CartItem(1, 0, "1", makeBook(), 2);
        return new Transaction(0, List.of(cartItem),
                null,"1", BigDecimal.ZERO, true, TransactionStatus.ORDERED);
    }

    public static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZXYxMC11c2" +
                "Vycy1hcGkiLCJzdWIiOiJqb2huc21pdGgiLCJpZCI6Ijk4M2YxMjI0LWFmNGYtMTFlYi04MzY4LTAyNDJ" +
                "hYzExMDAwMiIsImZpcnN0X25hbWUiOiJKb2huIiwibGFzdF9uYW1lIjoiU21pdGgiLCJlbWFpbF9hZGRy" +
                "ZXNzIjoiam9obkBzbWl0aC5jb20iLCJtb2JpbGVfcGhvbmUiOiI1NTUtNTU1LTU1NTUiLCJyb2xlcyI6I" +
                "kFETUlOLE1BTkFHRVIsVVNFUiIsImV4cCI6MTYzMjM0MzI1Nn0.IrZkesm5Uc5Ei4Tmpdrbk9kaaIt6mlEydX7z9yKm3QY";
}

