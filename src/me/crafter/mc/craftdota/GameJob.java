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
		Bukkit.broadcastMessage(ChatColor.AQUA + "[MCLOL] 游戏即将开始！");
	}
	
	@Override
	//Core clock
	public void run() {
		
		if (tick <= 0){ //initialize period do nothing
			if ((tick+120) % 20 == 1){
				Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "欢迎来到MC撸啊撸~！");
			} else {
				setall(ChatColor.GOLD + "---> 准备阶段 <---", 100F*(tick+60F)/60F);
			}
			if (tick == 0){
				if (Bukkit.getOnlinePlayers().length <= 1){
					Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "玩家人数不足 1 人，游戏回到准备阶段。");
					tick = -30;
				}
			}
		} else if (tick < 120){ //pre-game
			String message = ChatColor.RED + "倒计时 " + ChatColor.RESET + "- " + ChatColor.YELLOW + "[" + ChatColor.AQUA + itos(1-tick/60) + ":" + itos(59-tick%60) + ChatColor.YELLOW + "]";	
			setall(message, 100F-100F*tick/120F);
			if (tick == 60){
				Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.GOLD + "距离MC撸啊撸比赛开始还有1分钟！");
				Bukkit.getLogger().info("[CraftDota] Current game tick is " + tick + " .");
				Bukkit.getLogger().info("[CraftDota] There are currently " + Bukkit.getOnlinePlayers().length + " players online.");
			} else if (tick == 90){
				Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.GOLD + "距离MC撸啊撸比赛开始还有30秒！");
			}
			
		} else if (tick < 6120){ //in-game
			if (tick == 120){
				Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.GOLD + "MC撸啊撸比赛开始！摧毁对方的基地！");
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
					winner = ChatColor.LIGHT_PURPLE + "紫队获胜";
				} else if (GameInfo.bbase.isDead()){
					winner = ChatColor.BLUE + "蓝队获胜";
				} else if (GameInfo.ascore > GameInfo.bscore) {
					winner = ChatColor.BLUE + "蓝队获胜";
					istimeup = true;
				} else if (GameInfo.bscore > GameInfo.ascore) {
					winner = ChatColor.LIGHT_PURPLE + "紫队获胜";
					istimeup = true;
				} else {
					winner = ChatColor.WHITE + "平局";
					istimeup = true;
				}
				GameOverEvent goevent = new GameOverEvent(winner, istimeup, GameInfo.ascore, GameInfo.bscore, tick);
				Bukkit.getPluginManager().callEvent(goevent);
				
				Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "MC撸啊撸游戏结束！本次比赛的结果是：" + winner + ChatColor.AQUA + "!");
				Bukkit.broadcastMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "按[潜行]键退出游戏，服务器将在1分钟后关闭。");
				
				Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("CraftDota"), new Runnable(){

					@Override
					public void run() {
						for (Player p : Bukkit.getOnlinePlayers()){
							p.kickPlayer(ChatColor.GREEN + "[MCLOL] " + "MC撸啊撸游戏结束，服务器重设中，请选择另一个服务器加入！");
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
