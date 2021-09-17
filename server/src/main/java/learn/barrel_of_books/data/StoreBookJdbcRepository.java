package learn.barrel_of_books.data;


import learn.barrel_of_books.data.mappers.StoreBookMapper;

import learn.barrel_of_books.models.StoreBook;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class StoreBookJdbcRepository implements StoreBookRepository {

    private JdbcTemplate template;

    public StoreBookJdbcRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<StoreBook> findByBookId(int bookId) {
        final String sql = "select sb.store_id, sb.book_id, sb.quantity, " +
                            "s.address, s.city, s.state, s.postal_code, s.phone_number "
                            + "from store_book sb "
                            + "left outer join store s on s.store_id = sb.store_id "
                            + "where sb.book_id = ?;";
        StoreBook sb =  template.query(sql,new StoreBookMapper(), bookId).stream()
                        .findFirst().orElse(null);
       if(sb == null) {
           return null;
       }
       return template.query(sql,new StoreBookMapper(),bookId);
    }

    @Override
    public boolean add(StoreBook storeBook) {
        final String sql = "insert into store_book(store_id, book_id, quantity) "
                            + "values(?,?,?);";

     return template.update(sql,storeBook.getStore().getStoreId(),storeBook.getBookId(),storeBook.getQuantity()) > 0;


//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        int rowsAffected = template.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            ps.setInt(1,storeBook.getStore().getStoreId());
//            ps.setInt(2,storeBook.getBookId());
//            ps.setInt(3,storeBook.getQuantity());
//            return ps;
//        },keyHolder);
//
//        if(rowsAffected <= 0) {
//            return null;
//        }
    }


    @Override
    public boolean update(StoreBook storeBook) {
        final String sql = "update store_book set "
                            + "quantity = ? "
                            + "where book_id = ? "
                            + "and store_id = ?;";
        return template.update(sql,storeBook.getQuantity(),storeBook.getBookId(),storeBook.getStore().getStoreId()) > 0;
    }

    @Override
    public boolean delete(int store_id, int book_id) {
        final String sql = "delete from store_book "
                            + "where store_id = ? "
                            + "and book_id = ?;";
        return template.update(sql,store_id,book_id) > 0;
    }


}
