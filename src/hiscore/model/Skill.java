package hiscore.model;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum Skill {

    ATTACK,
    DEFENCE,
    STRENGTH,
    HITPOINTS,
    RANGED,
    PRAYER,
    MAGIC,
    COOKING,
    WOODCUTTING,
    FLETCHING,
    FISHING,
    FIREMAKING,
    CRAFTING,
    SMITHING,
    MINING,
    HERBLORE,
    AGILITY,
    THIEVING,
    SLAYER,
    FARMING,
    RUNECRAFT,
    HUNTER,
    CONSTRUCTION;

    public static final Set<Skill> VALUES = Collections.unmodifiableSet(EnumSet.allOf(Skill.class));
}