package learn.barrel_of_books.data.mappers;

import learn.barrel_of_books.models.Book;
import learn.barrel_of_books.models.Store;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoreMapper implements RowMapper<Store> {


    @Override
    public Store mapRow(ResultSet resultSet, int i) throws SQLException {
        Store store = new Store();
        store.setStoreId(resultSet.getInt("store_id"));
        store.setAddress(resultSet.getString("address"));
        store.setCity(resultSet.getString("city"));
        store.setState(resultSet.getString("state"));
        store.setPostalCode(resultSet.getString("postal_code"));
        store.setPhone(resultSet.getString("phone_number"));
        return store;
    }
}
