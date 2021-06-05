package lazy.baubles.api.bauble;

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
        for (int s : validSlots) {
            if (s == slot) return true;
        }
        return false;
    }

    public int[] getValidSlots() {
        return validSlots;
    }

    public static BaubleType getFromString(String type){
        switch (type) {
            case "ring": return BaubleType.RING;
            case "amulet": return BaubleType.AMULET;
            case "belt": return BaubleType.BELT;
            case "head": return BaubleType.HEAD;
            case "body": return BaubleType.BODY;
            case "charm": return BaubleType.CHARM;
        }
        return TRINKET;
    }
}