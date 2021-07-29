package lazy.baubles.api.bauble;

import java.util.Arrays;

public enum BaubleType {
    AMULET(0),
    RING(1, 2),
    BELT(3),
    TRINKET(0, 1, 2, 3, 4, 5, 6),
    HEAD(4),
    BODY(5),
    CHARM(6);

    int[] validSlots;

    BaubleType(int... validSlots) {
        this.validSlots = validSlots;
    }

    public boolean hasSlot(int slot) {
        return Arrays.stream(validSlots).anyMatch(s -> s == slot);
    }

    public int[] getValidSlots() {
        return validSlots;
    }

    public static BaubleType getFromString(String type){
        return switch (type) {
            case "ring" -> BaubleType.RING;
            case "amulet" -> BaubleType.AMULET;
            case "belt" -> BaubleType.BELT;
            case "head" -> BaubleType.HEAD;
            case "body" -> BaubleType.BODY;
            case "charm" -> BaubleType.CHARM;
            default -> TRINKET;
        };
    }
}