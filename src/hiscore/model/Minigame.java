package hiscore.model;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum Minigame {

    BOUNTY_HUNTER_HUNTER,
    BOUNTY_HUNTER_ROGUE,
    CLUE_SCROLLS_ALL,
    CLUE_SCROLLS_EASY,
    CLUE_SCROLLS_MEDIUM,
    CLUE_SCROLLS_HARD,
    CLUE_SCROLLS_ELITE,
    CLUE_SCROLLS_MASTER,
    LAST_MAN_STANDING;

    public static final Set<Minigame> VALUES = Collections.unmodifiableSet(EnumSet.allOf(Minigame.class));
}