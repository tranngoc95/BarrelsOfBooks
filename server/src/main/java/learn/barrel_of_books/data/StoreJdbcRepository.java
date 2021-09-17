package learn.barrel_of_books.data;

import learn.barrel_of_books.data.mappers.BookMapper;
import learn.barrel_of_books.data.mappers.StoreMapper;
import learn.barrel_of_books.models.Book;
import learn.barrel_of_books.models.Store;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class StoreJdbcRepository implements StoreRepository {

    private JdbcTemplate template;

    public StoreJdbcRepository(JdbcTemplate template) {
        this.template = template;
    }


    @Override
    public List<Store> findAll() {
        final String sql = "select store_id, address, city, state, postal_code, phone_number "
                + "from store";

        return template.query(sql,new StoreMapper());
    }


    @Override
    public Store findById(int id) {
        final String sql = "select store_id, address, city, state, postal_code, phone_number "
                + "from store "
                + "where store_id = ?;";
        Store store = template.query(sql,new StoreMapper(), id).stream()
                .findFirst().orElse(null);

        return store;
    }


    @Override
    public Store findByPostCode(String postCode) {
        final String sql = "select store_id, address, city, state, postal_code, phone_number "
                + "from store "
                + "where postal_code = ?;";

       Store store = template.query(sql,new StoreMapper(), postCode).stream()
                .findFirst().orElse(null);


        return store;
    }


    @Override
    public Store add(Store store) {
        final String sql = "insert into store (address, city, state, postal_code, phone_number) "
                +"values (?,?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,store.getAddress());
            ps.setString(2,store.getCity());
            ps.setString(3,store.getState());
            ps.setString(4,store.getPostalCode());
            ps.setString(5,store.getPhone());
            return ps;
        }, keyHolder);

        if(rowsAffected <= 0) {
            return null;
        }
        store.setStoreId(keyHolder.getKey().intValue());
        return store;
    }


    @Override
    public boolean update(Store store) {
        final String sql = "update store set "
                + "address = ?, "
                + "city = ?, "
                + "state = ?, "
                + "postal_code = ?, "
                + "phone_number = ? "
                + "where store_id = ?;";
        return template.update(sql, store.getAddress(),store.getCity(), store.getState(),
                                store.getPostalCode(),store.getPhone(),store.getStoreId()) > 0;
    }

    @Override
    public boolean delete(int storeId) {
        template.update("delete from store_book where store_id = ?;", storeId);
        return template.update("delete from store where store_id = ?;",storeId) > 0;
    }





}
