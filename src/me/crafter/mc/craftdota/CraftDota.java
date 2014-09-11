package me.crafter.mc.craftdota;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class CraftDota extends JavaPlugin {

	public final Logger logger = Logger.getLogger("Mincraft");
	public final DotaListener pl = new DotaListener();
	public final PlayerListener pll = new PlayerListener();
	private BukkitTask mainjob = null;
	

    public void onEnable(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this.pl, this);
        pm.registerEvents(this.pll, this);
        new GameInfo();
        
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().info("[MCLOL] Metrics failed.");
        }

        Bukkit.getWorld("world").setGameRuleValue("mobGreifing", "false");
        Bukkit.getWorld("world").setGameRuleValue("doDaylightCycle", "false");
        Bukkit.getWorld("world").setGameRuleValue("doMobSpawning", "false");
        Bukkit.getWorld("world").setTime(6000L);
        
        Bukkit.getScheduler().runTaskLater(this, new Runnable(){

			@Override
			public void run() {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "dotd start");
			}
        	
        }, 200L);
        
    }
 

    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, final String[] args){
    	
    	if (cmd.getName().equals("credits")){
    		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.GREEN + "=============== Credits ===============");
    		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.YELLOW + "A practice of STR game in " + ChatColor.LIGHT_PURPLE + "Minecraft" + ChatColor.YELLOW + ".");
    		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.YELLOW + "Version: Beta " + ChatColor.LIGHT_PURPLE + "0.9.0729" + ChatColor.YELLOW + ".");
    		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.YELLOW + "Plugin made by " + ChatColor.LIGHT_PURPLE + "Socket_Closed" + ChatColor.YELLOW + ".");
    		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.YELLOW + "Idea from " + ChatColor.LIGHT_PURPLE + "KuaiKuai" + ChatColor.YELLOW + ".");
    		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.YELLOW + "Inspired by " + ChatColor.LIGHT_PURPLE + "Dota" + ChatColor.YELLOW + ", " + ChatColor.LIGHT_PURPLE + "League of Legends" + ChatColor.YELLOW + " and " + ChatColor.LIGHT_PURPLE + "CIV Craft" + ChatColor.YELLOW + ".");
    		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.GREEN + "=============== Credits ===============");
    		return true;
    	}
    	
    	
    	if (cmd.getName().equals("dotd")){
    		
    		if (args.length == 0){
    			sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.GREEN + "================ Usage ================");
        		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.YELLOW + "/dotd start 开始游戏，将来可以自动开始");
        		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.YELLOW + "/dotd tick 强制设定游戏当前时间");
        		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.YELLOW + "指令方块写/dotd join a或b @a[r=5]");
        		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.YELLOW + "管理员强制加入使用/dotd join a或b 自己的名字 force");
        		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.YELLOW + "有疑问，bug，请联系 Socket_Closed。");
        		sender.sendMessage(ChatColor.AQUA + "[MCLOL] " + ChatColor.GREEN + "================ Usage ================");
    		}
    		
    		if (args.length >= 3 && args[0].equals("join")){
    			Player theplayer = Bukkit.getPlayerExact(args[2]);
    			if (theplayer == null) return false;
    			boolean force = false;
    			if (args.length == 4 && args[3].equals("force")){
    				force = true;	
    			}
    			if (args[1].equals("a")){
    				if (GameInfo.joina(theplayer)){
    					theplayer.sendMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "你加入了队伍： " + ChatColor.BLUE + "蓝队");
    					Bukkit.getScoreboardManager().getMainScoreboard().getTeam("a").addPlayer(theplayer);
    					//Player Ready, Launch
    					GameInfo.playerReady(theplayer);
    				} else if (!force) {
    					theplayer.sendMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.RED + "该队伍已满或你已经入队。");
    				} else {
    					theplayer.sendMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "你强制加入了队伍： " + ChatColor.BLUE + "蓝队");
    					GameInfo.aplayers.remove(theplayer.getName());
    					GameInfo.bplayers.remove(theplayer.getName());
    					GameInfo.aplayers.add(theplayer.getName());
    					Bukkit.getScoreboardManager().getMainScoreboard().getTeam("a").addPlayer(theplayer);
    					//Player Ready, Launch
    					GameInfo.playerReady(theplayer);
    				}
    			} else if (args[1].equals("b")){
    				if (GameInfo.joinb(theplayer)){
    					theplayer.sendMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "你加入了队伍： " + ChatColor.LIGHT_PURPLE + "紫队");
    					Bukkit.getScoreboardManager().getMainScoreboard().getTeam("b").addPlayer(theplayer);
    					//Player Ready, Launch
    					GameInfo.playerReady(theplayer);
    				} else if (!force) {
    					theplayer.sendMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.RED + "该队伍已满或你已经入队。");
    				} else {
    					theplayer.sendMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "你强制加入了队伍： " + ChatColor.LIGHT_PURPLE + "紫队");
    					GameInfo.aplayers.remove(theplayer.getName());
    					GameInfo.bplayers.remove(theplayer.getName());
    					GameInfo.bplayers.add(theplayer.getName());
    					Bukkit.getScoreboardManager().getMainScoreboard().getTeam("b").addPlayer(theplayer);
    					//Player Ready, Launch
    					GameInfo.playerReady(theplayer);
    				}
    			}
    			return true;
    		}
    		
    		if (args.length == 1){
    			if (args[0].equals("start")){
    				if (mainjob == null){
        				mainjob = Bukkit.getScheduler().runTaskTimer(this, new GameJob(), 20L, 20L);
        				Bukkit.getScheduler().runTaskTimer(this, new TowerWorker(), 99L, 99L);
    				} else {
    					sender.sendMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "Already started!");
    				}
    				return true;
    			}
    		} else if (args.length == 2){
    			if (args[0].equals("tick")){
    				if (mainjob != null){
    					GameJob.tick = Integer.parseInt(args[1]);
    					sender.sendMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.AQUA + "Set current tick to " + Integer.parseInt(args[1]));
    					return true;
    				}
    			}
    		}
    	}
    	
    	return true;
    }
    
    
    
	
}
