package Enums;

public enum Rarity {

    COMMON("Common"),
    UNCOMMON("Uncommon"),
    RARE("Rare"),
    EPIC("Epic");


    private final String description;
    Rarity(String description){
        this.description = description;
    }
    public String getDescription(){
        return description;
    }
}
