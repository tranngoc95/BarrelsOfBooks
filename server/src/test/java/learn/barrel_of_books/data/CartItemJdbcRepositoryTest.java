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
    void shouldFindActiveByUserId() {
        List<CartItem> actual = repository.findActiveByUserId("1");
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("hp", actual.get(0).getBook().getTitle());
    }

    @Test
    void shouldFindByCartItemId() {
        CartItem actual = repository.findByCartItemId(1);
        assertNotNull(actual);
        assertEquals(0, actual.getTransactionId());
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
        assertTrue(actual.getCartItemId()>=4);
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