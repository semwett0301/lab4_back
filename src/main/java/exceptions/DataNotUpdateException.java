package exceptions;

public class DataNotUpdateException extends Throwable {
    @Override
    public String getMessage() {
        return "Данные не были обновлены";
    }
}
