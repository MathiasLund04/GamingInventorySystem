package Enums;

public enum ArmorPlacement {
    //Konstanter med deres egne beskrivelser
    HELMET("Helmet"),
    BREASTPLATE("Breastplate"),
    LEGGINGS("Leggings"),
    BOOTS("Boots"),
    GLOVES("Gloves");

    public final String description;
    //Give en beskrivelse til hver konstant
    ArmorPlacement(String description) {
        this.description = description;
    }
    //Sende beskrivelses tilbage for at man kan se hvilken placering det har
    public  String getDescription() {
        return description;
    }
}
