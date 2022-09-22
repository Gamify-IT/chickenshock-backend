package de.unistuttgart.chickenshockbackend.errorhandling;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiError {

    private HttpStatus status;
    private List<String> errors;

    public ApiError(final HttpStatus status, final List<String> errors) {
        this.status = status;
        this.errors = errors;
    }
}
