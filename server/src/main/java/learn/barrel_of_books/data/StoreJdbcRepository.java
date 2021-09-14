package learn.barrel_of_books.data;

import org.springframework.jdbc.core.JdbcTemplate;

public class StoreJdbcRepository {

    private final JdbcTemplate template;

    public StoreJdbcRepository(JdbcTemplate template) {
        this.template = template;
    }



}
