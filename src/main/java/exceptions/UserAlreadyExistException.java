package exceptions;

public class UserAlreadyExistException extends Exception {
    @Override
    public String getMessage() {
        return "Пользователь с таким username уже существует";
    }
}
