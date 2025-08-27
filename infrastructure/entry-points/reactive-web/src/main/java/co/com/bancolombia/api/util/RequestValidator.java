package co.com.bancolombia.api.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import co.com.bancolombia.exception.MissingFieldException;

@Component
public class RequestValidator {
    private final Validator validator;

    public RequestValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T dto, Class<T> clazz) {
        BindingResult errors = new BeanPropertyBindingResult(dto, clazz.getName());
        validator.validate(dto, errors);
        if (errors.hasErrors()) {
            String firstError = errors.getAllErrors().get(0).getDefaultMessage();
            throw new MissingFieldException(firstError);
        }
    }
}
