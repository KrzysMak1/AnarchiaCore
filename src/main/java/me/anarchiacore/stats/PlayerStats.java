package me.anarchiacore.stats;

public class PlayerStats {
    private String name;
    private int kills;
    private int deaths;
    private int killstreak;
    private int bestKillstreak;

    public PlayerStats(String name, int kills, int deaths, int killstreak, int bestKillstreak) {
        this.name = name;
        this.kills = kills;
        this.deaths = deaths;
        this.killstreak = killstreak;
        this.bestKillstreak = bestKillstreak;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getKillstreak() {
        return killstreak;
    }

    public void setKillstreak(int killstreak) {
        this.killstreak = killstreak;
    }

    public int getBestKillstreak() {
        return bestKillstreak;
    }

    public void setBestKillstreak(int bestKillstreak) {
        this.bestKillstreak = bestKillstreak;
    }
}
