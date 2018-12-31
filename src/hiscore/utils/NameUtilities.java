package hiscore.utils;

import hiscore.official.HiscoreType;

public final class NameUtilities {

    private NameUtilities() {

    }

    public static String toConsistentFormat(String name) {
        return name.trim().toLowerCase().replaceAll("[^a-zA-Z\\d]", "_");
    }

    public static String toConsistentFormat(String name, HiscoreType type) {
        return toConsistentFormat(name) + '_' + type;
    }
}