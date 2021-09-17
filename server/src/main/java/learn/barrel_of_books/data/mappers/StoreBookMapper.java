package learn.barrel_of_books.data.mappers;


import learn.barrel_of_books.models.StoreBook;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoreBookMapper implements RowMapper<StoreBook> {

    @Override
    public StoreBook mapRow(ResultSet resultSet, int i) throws SQLException {
        StoreBook storeBook = new StoreBook();
        storeBook.setBookId(resultSet.getInt("book_id"));
        storeBook.setQuantity(resultSet.getInt("quantity"));

        StoreMapper storeMapper = new StoreMapper();
        storeBook.setStore(storeMapper.mapRow(resultSet, i));

        return storeBook;
    }

}
