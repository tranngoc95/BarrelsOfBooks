package learn.barrel_of_books.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    private int storeId;
    private String name;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String phone;







}
