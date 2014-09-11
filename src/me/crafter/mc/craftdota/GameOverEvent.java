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
 
    //getWinner returns "����ʤ��" "�϶�ʤ��" "ƽ��"
    public String getWinner() {
        return winner;
    }
    
    //returns �Ƿ�Ϊʱ�䵽��ǿ�ƽ���
    public boolean isTimeUp(){
    	return istimeup;
    }
    
    //returns ����ʱ�����ӵ÷�
    public int getTeamaScore(){
    	return teamascore;
    }

    //returns ����ʱ���϶ӵ÷�
    public int getTeambScore(){
    	return teambscore;
    }
    
    //returns ��Ϸʱ�䣬������1ǰ��׼��ʱ���2����׼��ʱ��
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