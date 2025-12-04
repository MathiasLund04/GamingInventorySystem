package Enums;

public enum ConsumableType {

    HEALTHPOTION ("Health potion"),
    STRENGTHPOTION ("Strength potion"),
    STEALTHPOTION ("Stealth potion"),
    ARROW("Arrow");


    private final String description;
    ConsumableType(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
