package learn.barrel_of_books.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    private int genreId;

    @NotBlank(message = "Name is required and cannot be blank.")
    private String name;

    @NotBlank(message = "Description is required and cannot be blank.")
    private String description;
}
