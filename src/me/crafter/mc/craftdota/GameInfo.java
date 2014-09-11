package me.crafter.mc.craftdota;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class GameInfo {

	public static List<String> aplayers = new ArrayList<String>();
	public static List<String> bplayers = new ArrayList<String>();
	
	public static int ascore = 0;
	public static int bscore = 0;
	
	public static String aname = ChatColor.BLUE + "蓝队";
	public static String bname = ChatColor.LIGHT_PURPLE + "紫队";
	
	public static ItemStack ahat;
	public static ItemStack bhat;
	
	//TOWERS START
	public static Tower abase;
	public static Tower bbase;
	
	public static Location anexus = new Location(Bukkit.getWorld("world"), 95, 13, 95);
	public static Location bnexus = new Location(Bukkit.getWorld("world"), -95, 13, -95);;
	
	
	
	//TOWERS END
	

	public static List<Tower> bases = new ArrayList<Tower>();
	public static List<Tower> towers = new ArrayList<Tower>();
	
	public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
	
	public GameInfo(){
		ahat = new ItemStack(Material.LEATHER_HELMET);
		bhat = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta ahatmeta = (LeatherArmorMeta)ahat.getItemMeta();
		LeatherArmorMeta bhatmeta = (LeatherArmorMeta)bhat.getItemMeta();
		ahatmeta.setColor(Color.fromRGB(95, 242, 250));
		bhatmeta.setColor(Color.fromRGB(237, 95, 250));
		ahat.setItemMeta(ahatmeta);
		bhat.setItemMeta(bhatmeta);

		if (scoreboard.getTeam("a") != null){
			scoreboard.getTeam("a").unregister();
		}
		Team a = scoreboard.registerNewTeam("a");
		a.setDisplayName("蓝队");
		a.setPrefix(ChatColor.BLUE + "");
		a.setSuffix(""+ChatColor.RESET);
		if (scoreboard.getTeam("b") != null){
			scoreboard.getTeam("b").unregister();
		}
		Team b = scoreboard.registerNewTeam("b");
		b.setDisplayName("紫队");
		b.setPrefix(ChatColor.LIGHT_PURPLE + "");
		b.setSuffix(""+ChatColor.RESET);
	}
	
	public static void towerInit(){
		abase = new Tower(ChatColor.BLUE + "蓝队基地", 0, 250, 250, 0.01D, new Location(Bukkit.getWorld("world"), 75, 11, 75));
		bbase = new Tower(ChatColor.LIGHT_PURPLE + "紫队基地", 1, 250, 250, 0.01D, new Location(Bukkit.getWorld("world"), -75, 11, -75));
		bases.add(abase);
		bases.add(bbase);
		//the following can use config
		towers.add(new Tower(ChatColor.BLUE + "蓝队基地防御塔", 0, 100, 100, 0.01D, new Location(Bukkit.getWorld("world"), 78, 11, 60)));
		towers.add(new Tower(ChatColor.BLUE + "蓝队基地防御塔", 0, 100, 100, 0.01D, new Location(Bukkit.getWorld("world"), 60, 11, 78)));
		towers.add(new Tower(ChatColor.BLUE + "蓝队高地防御塔", 0, 100, 100, 0.01D, new Location(Bukkit.getWorld("world"), 96, 11, 32)));
		towers.add(new Tower(ChatColor.BLUE + "蓝队高地防御塔", 0, 100, 100, 0.01D, new Location(Bukkit.getWorld("world"), 46, 11, 46)));
		towers.add(new Tower(ChatColor.BLUE + "蓝队高地防御塔", 0, 100, 100, 0.01D, new Location(Bukkit.getWorld("world"), 32, 11, 96)));
		towers.add(new Tower(ChatColor.BLUE + "蓝队外围防御塔", 0, 75, 75, 0.01D, new Location(Bukkit.getWorld("world"), 91, 8, -32)));
		towers.add(new Tower(ChatColor.BLUE + "蓝队外围防御塔", 0, 75, 75, 0.01D, new Location(Bukkit.getWorld("world"), 32, 8, 12)));
		towers.add(new Tower(ChatColor.BLUE + "蓝队外围防御塔", 0, 75, 75, 0.01D, new Location(Bukkit.getWorld("world"), -17, 8, 82)));
		
		towers.add(new Tower(ChatColor.LIGHT_PURPLE + "紫队基地防御塔", 1, 100, 100, 0.01D, new Location(Bukkit.getWorld("world"), -78, 11, -60)));
		towers.add(new Tower(ChatColor.LIGHT_PURPLE + "紫队基地防御塔", 1, 100, 100, 0.01D, new Location(Bukkit.getWorld("world"), -60, 11, -78)));
		towers.add(new Tower(ChatColor.LIGHT_PURPLE + "紫队高地防御塔", 1, 100, 100, 0.01D, new Location(Bukkit.getWorld("world"), -96, 11, -32)));
		towers.add(new Tower(ChatColor.LIGHT_PURPLE + "紫队高地防御塔", 1, 100, 100, 0.01D, new Location(Bukkit.getWorld("world"), -46, 11, -46)));
		towers.add(new Tower(ChatColor.LIGHT_PURPLE + "紫队高地防御塔", 1, 100, 100, 0.01D, new Location(Bukkit.getWorld("world"), -32, 11, -96)));
		towers.add(new Tower(ChatColor.LIGHT_PURPLE + "紫队外围防御塔", 1, 75, 75, 0.01D, new Location(Bukkit.getWorld("world"), -91, 8, 32)));
		towers.add(new Tower(ChatColor.LIGHT_PURPLE + "紫队外围防御塔", 1, 75, 75, 0.01D, new Location(Bukkit.getWorld("world"), -32, 8, -12)));
		towers.add(new Tower(ChatColor.LIGHT_PURPLE + "紫队外围防御塔", 1, 75, 75, 0.01D, new Location(Bukkit.getWorld("world"), 17, 8, -82)));
		
	}
	
	public static String getas(){
		return String.valueOf(ascore);
	}
	
	public static String getbs(){
		return String.valueOf(bscore);
	}
	
	
	public static boolean isina(Player p){
		return aplayers.contains(p.getName());
	}
	
	public static boolean isinb(Player p){
		return bplayers.contains(p.getName());
	}
	
	public static int getSide(Player p){
		if (isina(p)) return 0;
		if (isinb(p)) return 1;
		return 2;
	}
	
	
	public static boolean joina(Player p){
		if (isinb(p) || isina(p)) return false;
		if (joinablea()) {
			aplayers.add(p.getName());
			scoreboard.getTeam("a").addPlayer(p);
			//TODO after team join do?
			return true;
		} else {
			return false;
		}
	}
	
	public static void scorea(){
		ascore += 1;
	}
	
	public static void scoreb(){
		bscore += 1;
	}
	
	
	
	public static boolean joinb(Player p){
		if (isina(p) || isinb(p)) return false;
		if (joinableb()) {
			bplayers.add(p.getName());
			scoreboard.getTeam("b").addPlayer(p);
			//TODO after team join do?
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean joinablea(){
		int a = 0;
		int c = 0;
		for (String s : aplayers){
			if (Bukkit.getPlayerExact(s) != null){
				a ++;
				c ++;
			}
		}
		for (String s : aplayers){
			if (Bukkit.getPlayerExact(s) != null){
				c ++;
			}
		}
		return (((a+a)-c)<4);
	}
	
	public static boolean joinableb(){
		int b = 0;
		int c = 0;
		for (String s : aplayers){
			if (Bukkit.getPlayerExact(s) != null){
				c ++;
			}
		}
		for (String s : aplayers){
			if (Bukkit.getPlayerExact(s) != null){
				b ++;
				c ++;
			}
		}
		return (((b+b)-c)<4);
	}
	
	public static void playerReady(Player p){
		//reminder: see also PlayerListener - Player Rejoin
		PlayerInventory pinv = p.getInventory();
		pinv.clear();
		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setHelmet(null);
		if (isina(p)){
			pinv.setHelmet(ahat.clone());
			p.teleport(anexus);
			p.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "[" + ChatColor.RESET + "" + ChatColor.BLUE + "蓝队" + ChatColor.GREEN + ChatColor.BOLD + "]" + ChatColor.BLUE + p.getName() + ChatColor.RESET);
			p.setBedSpawnLocation(anexus, true);
		}
		if (isinb(p)){
			pinv.setHelmet(bhat.clone());
			p.teleport(bnexus);
			p.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "[" + ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "紫队" + ChatColor.GREEN + ChatColor.BOLD + "]" + ChatColor.LIGHT_PURPLE + p.getName() + ChatColor.RESET);
			p.setBedSpawnLocation(bnexus, true);
		}
		p.getWorld().spigot().playEffect(p.getLocation(), Effect.FLAME, 0, 0, 0.1F, 0.1F, 0.1F, 0.3F, 200, 32);
		p.getWorld().playSound(p.getLocation(), Sound.GHAST_FIREBALL, 0.5F, 1);
		pinv.setItem(0, new ItemStack(Material.STONE_SWORD));
		pinv.setItem(1, new ItemStack(Material.STONE_PICKAXE));
		pinv.setItem(7, new ItemStack(Material.BREAD, 64));
		pinv.setItem(8, new ItemStack(Material.EMERALD, Math.min(10+Math.abs(GameJob.tick)/30, 50)));		
	}
	
}
