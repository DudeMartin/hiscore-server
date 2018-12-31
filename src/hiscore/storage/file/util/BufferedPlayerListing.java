package hiscore.storage.file.util;

import hiscore.model.Minigame;
import hiscore.model.MinigameListing;
import hiscore.model.PlayerListing;
import hiscore.model.Skill;
import hiscore.model.SkillListing;

import java.nio.ByteBuffer;
import java.time.Instant;

final class BufferedPlayerListing implements PlayerListing {

    private static final int TIMESTAMP_POSITION = 0;
    private static final int RANK_POSITION = TIMESTAMP_POSITION + Long.BYTES;
    private static final int TOTAL_LEVEL_POSITION = RANK_POSITION + Integer.BYTES;
    private static final int TOTAL_EXPERIENCE_POSITION = TOTAL_LEVEL_POSITION + Integer.BYTES;
    private static final int SKILL_START_POSITION = TOTAL_EXPERIENCE_POSITION + Long.BYTES;
    private static final int SKILL_BYTE_COUNT = Integer.BYTES * 3;
    private static final int MINIGAME_START_POSITION = SKILL_START_POSITION + Skill.VALUES.size() * SKILL_BYTE_COUNT;
    private static final int MINIGAME_BYTE_COUNT = Integer.BYTES * 2;
    static final int LISTING_BYTE_COUNT = MINIGAME_START_POSITION + Minigame.VALUES.size() * MINIGAME_BYTE_COUNT;
    private final ByteBuffer listingBytes;

    BufferedPlayerListing(ByteBuffer listingBytes) {
        this.listingBytes = listingBytes;
    }

    BufferedPlayerListing(PlayerListing listing) {
        listingBytes = ByteBuffer.allocate(LISTING_BYTE_COUNT);
        listingBytes.putLong(listing.timestamp().toEpochMilli());
        listingBytes.putInt(listing.rank());
        listingBytes.putInt(listing.totalLevel());
        listingBytes.putLong(listing.totalExperience());
        for (Skill skill : Skill.VALUES) {
            SkillListing skillListing = listing.skill(skill);
            listingBytes.putInt(skillListing.rank);
            listingBytes.putInt(skillListing.level);
            listingBytes.putInt(skillListing.experience);
        }
        for (Minigame minigame : Minigame.VALUES) {
            MinigameListing minigameListing = listing.minigame(minigame);
            listingBytes.putInt(minigameListing.rank);
            listingBytes.putInt(minigameListing.score);
        }
        listingBytes.flip();
    }

    @Override
    public Instant timestamp() {
        return Instant.ofEpochMilli(listingBytes.getLong(listingBytes.position() + TIMESTAMP_POSITION));
    }

    @Override
    public int rank() {
        return listingBytes.getInt(listingBytes.position() + RANK_POSITION);
    }

    @Override
    public int totalLevel() {
        return listingBytes.getInt(listingBytes.position() + TOTAL_LEVEL_POSITION);
    }

    @Override
    public long totalExperience() {
        return listingBytes.getInt(listingBytes.position() + TOTAL_EXPERIENCE_POSITION);
    }

    @Override
    public SkillListing skill(Skill skill) {
        int startPosition = listingBytes.position() + SKILL_START_POSITION + skill.ordinal() * SKILL_BYTE_COUNT;
        return new SkillListing(
                skill,
                listingBytes.getInt(startPosition),
                listingBytes.getInt(startPosition + Integer.BYTES),
                listingBytes.getInt(startPosition + Integer.BYTES * 2));
    }

    @Override
    public MinigameListing minigame(Minigame minigame) {
        int startPosition = listingBytes.position() + MINIGAME_START_POSITION + minigame.ordinal() * MINIGAME_BYTE_COUNT;
        return new MinigameListing(
                minigame,
                listingBytes.getInt(startPosition),
                listingBytes.getInt(startPosition + Integer.BYTES));
    }

    ByteBuffer getBytes() {
        return listingBytes.duplicate();
    }
}