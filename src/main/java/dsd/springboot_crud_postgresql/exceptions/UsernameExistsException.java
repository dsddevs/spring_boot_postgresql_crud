package dsd.springboot_crud_postgresql.exceptions;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException(String message) {
        super(message);
    }
}
