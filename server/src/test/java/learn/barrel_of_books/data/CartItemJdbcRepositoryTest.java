package learn.barrel_of_books.data;

import learn.barrel_of_books.models.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static learn.barrel_of_books.data.TestData.makeExistingCartItem;
import static learn.barrel_of_books.data.TestData.makeNewCartItem;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CartItemJdbcRepositoryTest {

    @Autowired
    CartItemRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindByTransactionId() {
        List<CartItem> actual = repository.findByTransactionId(1);
        assertNotNull(actual);
        assertTrue(actual.size()>=1);
    }

    @Test
    void shouldFindActiveByUserId() {
        List<CartItem> actual = repository.findActiveByUserId("1");
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("hp", actual.get(0).getBook().getTitle());
    }

    @Test
    void shouldFindActiveByUserIdAndBookId() {
        CartItem actual = repository.findActiveByUserIdAndBookId("1",1);
        assertNotNull(actual);
        assertEquals(1, actual.getCartItemId());
    }

    @Test
    void shouldAdd() {
        CartItem input = makeNewCartItem();
        CartItem actual = repository.add(input);
        assertNotNull(actual);
        assertEquals(4, actual.getCartItemId());
    }

    @Test
    void shouldUpdate() {
        CartItem input = makeExistingCartItem();
        input.setQuantity(2);
        assertTrue(repository.update(input));
    }

    @Test
    void shouldNotUpdateMissing() {
        CartItem input = makeExistingCartItem();
        input.setCartItemId(10);
        assertFalse(repository.update(input));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(3));
    }

    @Test
    void shouldNotDeleteMissing() {
        assertFalse(repository.deleteById(10));
    }
}