package de.unistuttgart.chickenshockbackend.errorhandling;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public final class ApiError {

    private final HttpStatus status;
    private final List<String> errors;

    public ApiError() {
        super();
    }

    public ApiError(final HttpStatus status, final List<String> errors) {
        super();
        this.status = status;
        this.errors = errors;
    }

    public ApiError(final HttpStatus status, final String error) {
        super();
        this.status = status;
        errors = Collections.singletonList(error);
    }
}
