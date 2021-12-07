package exceptions;

public class UserAlreadyExistException extends Throwable {
    @Override
    public String getMessage() {
        return "Пользователь с таким username уже существует";
    }
}
