package me.playbosswar.fkgui.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ConfigManager {
    int pvpCapDay;
    int netherCapDay;
    int tntCapDay;
    int endCapDay;
    boolean friendlyFire;
    boolean enderpearlAssault;
    boolean healthBelowName;
    boolean respawnAtHome;
    boolean tntJump;
    boolean disablePotions;
    Team teamBlue;
    Team teamRed;
    Team teamGreen;
    Team teamYellow;

    public ConfigManager() {
        this.pvpCapDay = getFkConfig().getInt("FkPI.RulesManager.Rules.PvpCap.value");
        this.netherCapDay = getFkConfig().getInt("FkPI.RulesManager.Rules.NetherCap.value");
        this.tntCapDay = getFkConfig().getInt("FkPI.RulesManager.Rules.TntCap.value");
        this.endCapDay = getFkConfig().getInt("FkPI.RulesManager.Rules.EndCap.value");

        this.friendlyFire = getFkConfig().getBoolean("FkPI.RulesManager.Rules.FriendlyFire.value");
        this.enderpearlAssault = getFkConfig().getBoolean("FkPI.RulesManager.Rules.EnderpearlAssault.value");
        this.healthBelowName = getFkConfig().getBoolean("FkPI.RulesManager.Rules.HealthBelowName.value");
        this.respawnAtHome = getFkConfig().getBoolean("FkPI.RulesManager.Rules.RespawnAtHome.value");
        this.tntJump = getFkConfig().getBoolean("FkPI.RulesManager.Rules.TntJump.value");

        this.teamBlue = new Team("Bleu");
        this.teamRed = new Team("Rouge");
        this.teamYellow = new Team("Jaune");
        this.teamGreen = new Team("Vert");
    }

    private YamlConfiguration getFkConfig() {
        File file = new File("plugins/FallenKingdom/save.yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    public void removePlayer(Player p) {
        this.teamBlue.removePlayer(p);
        this.teamRed.removePlayer(p);
        this.teamYellow.removePlayer(p);
        this.teamGreen.removePlayer(p);
    }

    public int getPvpCapDay() {
        return this.pvpCapDay;
    }

    public void setPvpCapDay(int pvpCapDay) {
        this.pvpCapDay = pvpCapDay;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fk rules pvpCap " + pvpCapDay);
    }

    public int getNetherCapDay() {
        return this.netherCapDay;
    }

    public void setNetherCapDay(int netherCapDay) {
        this.netherCapDay = netherCapDay;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fk rules netherCap " + netherCapDay);
    }

    public int getTntCapDay() {
        return this.tntCapDay;
    }

    public void setTntCapDay(int tntCapDay) {
        this.tntCapDay = tntCapDay;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fk rules tntCap " + tntCapDay);
    }

    public int getEndCapDay() {
        return this.endCapDay;
    }

    public void setEndCapDay(int endCapDay) {
        this.endCapDay = endCapDay;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fk rules endCap " + endCapDay);
    }

    public boolean isFriendlyFire() {
        return this.friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fk rules friendlyFire " + friendlyFire);
    }

    public boolean isEnderpearlAssault() {
        return this.enderpearlAssault;
    }

    public void setEnderpearlAssault(boolean enderpearlAssault) {
        this.enderpearlAssault = enderpearlAssault;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fk rules enderpearlAssault " + enderpearlAssault);
    }

    public boolean isHealthBelowName() {
        return this.healthBelowName;
    }

    public void setHealthBelowName(boolean healthBelowName) {
        this.healthBelowName = healthBelowName;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fk rules healthBelowName " + healthBelowName);
    }

    public boolean isRespawnAtHome() {
        return this.respawnAtHome;
    }

    public void setRespawnAtHome(boolean respawnAtHome) {
        this.respawnAtHome = respawnAtHome;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fk rules respawnAtHome " + respawnAtHome);
    }

    public boolean isTntJump() {
        return this.tntJump;
    }

    public void setTntJump(boolean tntJump) {
        this.tntJump = tntJump;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fk rules tntJump " + respawnAtHome);
    }

    public Team getTeamBlue() {
        return teamBlue;
    }

    public Team getTeamRed() {
        return teamRed;
    }

    public Team getTeamGreen() {
        return teamGreen;
    }

    public Team getTeamYellow() {
        return teamYellow;
    }
}
