package hiscore.official;

public enum HiscoreType {

    NORMAL("https://services.runescape.com/m=hiscore_oldschool/index_lite.ws"),
    IRONMAN("https://services.runescape.com/m=hiscore_oldschool_ironman/index_lite.ws"),
    ULTIMATE_IRONMAN("https://services.runescape.com/m=hiscore_oldschool_ultimate/index_lite.ws"),
    HARDCORE_IRONMAN("https://services.runescape.com/m=hiscore_oldschool_hardcore_ironman/index_lite.ws"),
    DEADMAN_MODE("https://services.runescape.com/m=hiscore_oldschool_deadman/index_lite.ws"),
    SEASONAL("https://services.runescape.com/m=hiscore_oldschool_seasonal/index_lite.ws"),
    TOURNAMENT("https://services.runescape.com/m=hiscore_oldschool_tournament/index_lite.ws");

    public final String endpoint;

    HiscoreType(String endpoint) {
        this.endpoint = endpoint;
    }
}