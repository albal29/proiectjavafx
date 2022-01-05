package domain.validation;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}