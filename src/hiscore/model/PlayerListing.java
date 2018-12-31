package hiscore.model;

public interface PlayerListing extends Listing {

    int totalLevel();

    long totalExperience();

    SkillListing skill(Skill skill);

    MinigameListing minigame(Minigame minigame);
}