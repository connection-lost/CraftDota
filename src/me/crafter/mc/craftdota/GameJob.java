package me.crafter.mc.craftdota;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GameJob implements Runnable {
	
	public static int tick = -60;
	public static boolean ison = false;
	public static String winner = null;
	public static boolean isdone = false;

	
	public GameJob(){
		//init tower list
		GameInfo.towerInit();
		Bukkit.broadcastMessage(ChatColor.AQUA + "[MCLOL] ��Ϸ������ʼ��");
	}
	
	@Override
	//Core clock
	public void run() {
		
		if (tick <= 0){ //initialize period do nothing
			if ((tick+120) % 20 == 1){
				Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "��ӭ����MCߣ��ߣ~��");
			} else {
				setall(ChatColor.GOLD + "---> ׼���׶� <---", 100F*(tick+60F)/60F);
			}
			if (tick == 0){
				if (Bukkit.getOnlinePlayers().length <= 1){
					Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "����������� 1 �ˣ���Ϸ�ص�׼���׶Ρ�");
					tick = -30;
				}
			}
		} else if (tick < 120){ //pre-game
			String message = ChatColor.RED + "����ʱ " + ChatColor.RESET + "- " + ChatColor.YELLOW + "[" + ChatColor.AQUA + itos(1-tick/60) + ":" + itos(59-tick%60) + ChatColor.YELLOW + "]";	
			setall(message, 100F-100F*tick/120F);
			if (tick == 60){
				Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.GOLD + "����MCߣ��ߣ������ʼ����1���ӣ�");
				Bukkit.getLogger().info("[CraftDota] Current game tick is " + tick + " .");
				Bukkit.getLogger().info("[CraftDota] There are currently " + Bukkit.getOnlinePlayers().length + " players online.");
			} else if (tick == 90){
				Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.GOLD + "����MCߣ��ߣ������ʼ����30�룡");
			}
			
		} else if (tick < 6120){ //in-game
			if (tick == 120){
				Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.GOLD + "MCߣ��ߣ������ʼ���ݻٶԷ��Ļ��أ�");
				for (Player p : Bukkit.getOnlinePlayers()){
					p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 0.5F, 1F);
				}
			}
			ison = true;
			String message = ChatColor.BLUE + GameInfo.getas() + " " + GameInfo.aname + ChatColor.YELLOW + " --[" + itos((tick-120)/60) + ":" + itos((tick-120)%60) + "]-- " + ChatColor.LIGHT_PURPLE + GameInfo.bname + " " + GameInfo.getbs();
			setall(message, 100F);
			
			if (tick % 30 == 0){
				for (Player p : Bukkit.getOnlinePlayers()){
					if (GameInfo.isina(p) || GameInfo.isinb(p)){
						p.getInventory().addItem(new ItemStack(Material.EMERALD));
					}
				}
				if (tick % 120 == 0){
					Bukkit.getLogger().info("[MCLOL] Current game tick is " + tick + " .");
					Bukkit.getLogger().info("[MCLOL] There are currently " + Bukkit.getOnlinePlayers().length + " players online.");
				}
			}
			
		} else { //sudden death
			ison = false;
			if (winner == null){
				isdone = true;
				boolean istimeup = false;
				if (GameInfo.abase.isDead()){
					winner = ChatColor.LIGHT_PURPLE + "�϶ӻ�ʤ";
				} else if (GameInfo.bbase.isDead()){
					winner = ChatColor.BLUE + "���ӻ�ʤ";
				} else if (GameInfo.ascore > GameInfo.bscore) {
					winner = ChatColor.BLUE + "���ӻ�ʤ";
					istimeup = true;
				} else if (GameInfo.bscore > GameInfo.ascore) {
					winner = ChatColor.LIGHT_PURPLE + "�϶ӻ�ʤ";
					istimeup = true;
				} else {
					winner = ChatColor.WHITE + "ƽ��";
					istimeup = true;
				}
				GameOverEvent goevent = new GameOverEvent(winner, istimeup, GameInfo.ascore, GameInfo.bscore, tick);
				Bukkit.getPluginManager().callEvent(goevent);
				
				Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "MCߣ��ߣ��Ϸ���������α����Ľ���ǣ�" + winner + ChatColor.AQUA + "!");
				Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "��[Ǳ��]���˳���Ϸ������������1���Ӻ�رա�");
				
				Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("CraftDota"), new Runnable(){

					@Override
					public void run() {
						for (Player p : Bukkit.getOnlinePlayers()){
							p.kickPlayer(ChatColor.GREEN + "[MCLOL] " + "MCߣ��ߣ��Ϸ�����������������У���ѡ����һ�����������룡");
						}
						Bukkit.getServer().shutdown();
					}

				}, 1199L);
				
			}
			String message = ChatColor.BLUE + GameInfo.getas() + " " + GameInfo.aname + ChatColor.DARK_RED + " --[" + winner + ChatColor.DARK_RED + "]-- " + ChatColor.LIGHT_PURPLE + GameInfo.bname + " " + GameInfo.getbs();
			setall(message, 100F);

		}
		
		tick ++;
	}
	
	public void settick(int newtick){
		tick = newtick;
	}
	
	public void setall(String message, float percent){
		for (Player p : Bukkit.getOnlinePlayers()){
			BarAPI.setMessage(p, message, percent);
		}
	}
	
	public String itos(int i){
		String ret = "";
		if (i < 10){
			ret += "0";
		}
		ret += String.valueOf(i);
		return ret;
	}
	
	public void updatescore(){
		
		
		
		
	}
	
}
