package Exceptions;

public class NotEnoughInventorySpaceException extends Exception {
    //Default besked til at vise at der ikke er nok plads i inventory
    private static final String defaultMessage = "Not enough inventory space.";

    public NotEnoughInventorySpaceException() {
        super(defaultMessage);
    }

    //For at kunne bruge throw new og skrive egen besked
    public NotEnoughInventorySpaceException(String message) {
        super(message);
    }
}
