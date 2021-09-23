package learn.barrel_of_books.controllers;

import learn.barrel_of_books.models.AppUser;
import learn.barrel_of_books.utility.JwtConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetToken {

    @Autowired
    JwtConverter converter;

    static String TOKEN = "";
    static boolean hasRun = false;

    void set() {
        if (!hasRun) {
            hasRun = true;
            TOKEN = "Bearer " + converter.getTokenFromUser(getUser());
        }
    }

    private AppUser getUser() {
        return new AppUser("123e4567-e89b-12d3-a456-426614174000", "tester", "USER", "ADMIN", "MANAGER");
    }
}
