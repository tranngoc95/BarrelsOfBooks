package learn.barrel_of_books.domain;

import learn.barrel_of_books.models.Genre;

import java.lang.reflect.Field;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class Validate {

    public static <T> Result<T> validate(T object){
        Result<T> result = new Result<>();

        if(object==null){
            result.addMessage( "Input cannot be null.", ResultType.INVALID);
            return result;
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<T> violation : violations) {
                result.addMessage(violation.getMessage(), ResultType.INVALID);
            }
        }
        return result;
    }
}
