package learn.barrel_of_books.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class AppUser {
    private String id;
    private String username;
    private List<String> roles = new ArrayList<>();

    public AppUser(String id, String username, String... roles) {
        this.id = id;
        this.username = username;
        this.roles = Arrays.asList(roles);
    }

    public boolean hasRole(String role) {
        if (roles == null) {
            return false;
        }
        return roles.contains(role);
    }
}
