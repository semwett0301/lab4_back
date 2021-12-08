package exceptions;

public class DataNotUpdateException extends Exception {
    @Override
    public String getMessage() {
        return "Данные не были обновлены";
    }
}
