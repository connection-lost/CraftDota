package me.crafter.mc.craftdota;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GameOverEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String winner;
    private boolean istimeup;
    private int teamascore;
    private int teambscore;
    private int time;
 
    public GameOverEvent(String winnerz, boolean istimeupz, int teamascorez, int teambscorez, int timez) {
        winner = winnerz;
    	istimeup = istimeupz;
    	teamascore = teamascorez;
    	teambscore = teambscorez;
    	time = timez;
    }
 
    //getWinner returns "蓝队胜利" "紫队胜利" "平局"
    public String getWinner() {
        return winner;
    }
    
    //returns 是否为时间到而强制结束
    public boolean isTimeUp(){
    	return istimeup;
    }
    
    //returns 结束时的蓝队得分
    public int getTeamaScore(){
    	return teamascore;
    }

    //returns 结束时的紫队得分
    public int getTeambScore(){
    	return teambscore;
    }
    
    //returns 游戏时间，不包含1前期准备时间和2分钟准备时间
    public int getFinishTime(){
    	return time;
    }
    
    
 
    public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
}