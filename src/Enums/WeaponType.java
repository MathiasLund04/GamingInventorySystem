package Enums;

public enum WeaponType {
    //Konstanter med beskrivelser
    SWORD("Sword"),
    LONGSWORD("Long Sword"),
    AXE("Axe"),
    WARHAMMER("Warhammer"),
    BOW("Bow"),
    SHIELD("Shield");
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
