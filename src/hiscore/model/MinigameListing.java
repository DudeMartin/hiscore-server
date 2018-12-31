package hiscore.model;

public final class MinigameListing implements Listing {

    public final Minigame minigame;
    public final int rank;
    public final int score;

    public MinigameListing(Minigame minigame, int rank, int score) {
        this.minigame = minigame;
        this.rank = rank;
        this.score = score;
    }

    @Override
    public int rank() {
        return rank;
    }
}