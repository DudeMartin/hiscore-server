package hiscore.model;

import java.time.Instant;

public interface PlayerListing extends Listing, Comparable<PlayerListing> {

    Instant timestamp();

    int totalLevel();

    long totalExperience();

    SkillListing skill(Skill skill);

    MinigameListing minigame(Minigame minigame);

    @Override
    default int compareTo(PlayerListing other) {
        return timestamp().compareTo(other.timestamp());
    }
}