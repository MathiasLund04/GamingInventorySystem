package Enums;

public enum WeaponType {
    //Konstanter med beskrivelser
    SWORD("Sword"),
    LONGSWORD("Long Sword"),
    AXE("Axe"),
    HAMMER("Hammer"),
    BOW("Bow");

    private final String description;
    //Give konstanterne deres beskrivelse
    WeaponType(String description) {
        this.description = description;
    }

    //Returnere konstanternes beskrivelse
    public String getDescription() {
        return description;
    }
}
