package hiscore.model;

public final class SkillListing implements Listing {

    public final Skill skill;
    public final int rank;
    public final int level;
    public final int experience;

    public SkillListing(Skill skill, int rank, int level, int experience) {
        this.skill = skill;
        this.rank = rank;
        this.level = level;
        this.experience = experience;
    }

    @Override
    public int rank() {
        return rank;
    }
}