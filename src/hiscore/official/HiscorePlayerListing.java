package hiscore.official;

import hiscore.model.Minigame;
import hiscore.model.MinigameListing;
import hiscore.model.PlayerListing;
import hiscore.model.Skill;
import hiscore.model.SkillListing;

import java.time.Instant;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

public final class HiscorePlayerListing implements PlayerListing {

    private final Instant timestamp = Instant.now();
    private final int rank;
    private final int totalLevel;
    private final long totalExperience;
    private final Map<Skill, SkillListing> skills = new EnumMap<>(Skill.class);
    private final Map<Minigame, MinigameListing> minigames = new EnumMap<>(Minigame.class);

    HiscorePlayerListing(String rawListing) {
        String[][] listingParts = Arrays.stream(rawListing.split("\n")).map(line -> line.split(",")).toArray(String[][]::new);
        rank = Integer.parseInt(listingParts[0][0]);
        totalLevel = Integer.parseInt(listingParts[0][1]);
        totalExperience = Long.parseLong(listingParts[0][2]);
        int index = 1;
        while (index <= 23) {
            addSkill(index, listingParts[index++]);
        }
        int lineCount = listingParts.length;
        while (index < lineCount) {
            addMinigame(index, listingParts[index++]);
        }
    }

    @Override
    public Instant timestamp() {
        return timestamp;
    }

    @Override
    public int rank() {
        return rank;
    }

    @Override
    public int totalLevel() {
        return totalLevel;
    }

    @Override
    public long totalExperience() {
        return totalExperience;
    }

    @Override
    public SkillListing skill(Skill skill) {
        return skills.get(skill);
    }

    @Override
    public MinigameListing minigame(Minigame minigame) {
        return minigames.get(minigame);
    }

    private void addSkill(int index, String[] skillParts) {
        Skill skill;
        switch (index) {
            case 1:
                skill = Skill.ATTACK;
                break;
            case 2:
                skill = Skill.DEFENCE;
                break;
            case 3:
                skill = Skill.STRENGTH;
                break;
            case 4:
                skill = Skill.HITPOINTS;
                break;
            case 5:
                skill = Skill.RANGED;
                break;
            case 6:
                skill = Skill.PRAYER;
                break;
            case 7:
                skill = Skill.MAGIC;
                break;
            case 8:
                skill = Skill.COOKING;
                break;
            case 9:
                skill = Skill.WOODCUTTING;
                break;
            case 10:
                skill = Skill.FLETCHING;
                break;
            case 11:
                skill = Skill.FISHING;
                break;
            case 12:
                skill = Skill.FIREMAKING;
                break;
            case 13:
                skill = Skill.CRAFTING;
                break;
            case 14:
                skill = Skill.SMITHING;
                break;
            case 15:
                skill = Skill.MINING;
                break;
            case 16:
                skill = Skill.HERBLORE;
                break;
            case 17:
                skill = Skill.AGILITY;
                break;
            case 18:
                skill = Skill.THIEVING;
                break;
            case 19:
                skill = Skill.SLAYER;
                break;
            case 20:
                skill = Skill.FARMING;
                break;
            case 21:
                skill = Skill.RUNECRAFT;
                break;
            case 22:
                skill = Skill.HUNTER;
                break;
            case 23:
                skill = Skill.CONSTRUCTION;
                break;
            default:
                throw new HiscoreException("Unexpected skill on line " + (index + 1) + '.');
        }
        SkillListing skillListing = new SkillListing(
                skill,
                Integer.parseInt(skillParts[0]),
                Integer.parseInt(skillParts[1]),
                Integer.parseInt(skillParts[2]));
        skills.put(skill, skillListing);
    }

    private void addMinigame(int index, String[] minigameParts) {
        Minigame minigame;
        switch (index) {
            case 24:
                minigame = Minigame.BOUNTY_HUNTER_HUNTER;
                break;
            case 25:
                minigame = Minigame.BOUNTY_HUNTER_ROGUE;
                break;
            case 26:
                minigame = Minigame.CLUE_SCROLLS_ALL;
                break;
            case 27:
                minigame = Minigame.CLUE_SCROLLS_EASY;
                break;
            case 28:
                minigame = Minigame.CLUE_SCROLLS_MEDIUM;
                break;
            case 29:
                minigame = Minigame.CLUE_SCROLLS_HARD;
                break;
            case 30:
                minigame = Minigame.CLUE_SCROLLS_ELITE;
                break;
            case 31:
                minigame = Minigame.CLUE_SCROLLS_MASTER;
                break;
            case 32:
                minigame = Minigame.LAST_MAN_STANDING;
                break;
            default:
                throw new HiscoreException("Unexpected minigame on line " + (index + 1) + '.');
        }
        MinigameListing minigameListing = new MinigameListing(
                minigame,
                Integer.parseInt(minigameParts[0]),
                Integer.parseInt(minigameParts[1]));
        minigames.put(minigame, minigameListing);
    }
}