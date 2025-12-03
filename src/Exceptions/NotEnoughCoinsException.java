package Exceptions;

public class NotEnoughCoinsException extends Exception {
    //Default besked til at fortælle at der ikke er nok coins
    private static final String defaultMessage = "You do not have enough coins";

    public NotEnoughCoinsException() {
        super(defaultMessage);
    }

    //For at kunne bruge throw new og skrive egen besked
    public NotEnoughCoinsException(String message) {
        super(message);
    }
}
