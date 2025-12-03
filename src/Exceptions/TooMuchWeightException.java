package Exceptions;

public class TooMuchWeightException extends Exception {
    //En default besked til at fortælle at spilleren har for meget i inventory
    private static final String defaultMessage = "You are carrying too much.";

    public TooMuchWeightException() {
        super(defaultMessage);
    }
    //For at kunne bruge throw new og skrive egen besked
    public TooMuchWeightException(String message) {
        super(message);
    }
}
