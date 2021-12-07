package exceptions;

public class NoDataWasReceivedException extends Throwable{
    @Override
    public String getMessage() {
        return "Данные не были получены";
    }
}
