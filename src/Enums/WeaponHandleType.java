package Enums;

public enum WeaponHandleType {
    //Konstanter med deres beskrivelser
    ONEHANDED("One Handed"),
    TWOHANDED("two Handed"),
    OFFHAND("Off-hand"),;

    private final String description;
    //Give hver konstant deres beskrivelse
    WeaponHandleType(String description) {
        this.description = description;
    }
    //returnere beskrivelsen af konstanten
    public String getDescription() {
        return description;
    }
}
