package by.teachmeskills.shop.exceptions;

public class UserAlreadyExistsException extends RegistrationException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
