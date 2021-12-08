package exceptions;

public class NoDataWasReceivedException extends Exception{
    @Override
    public String getMessage() {
        return "Данные не были получены";
    }
}
