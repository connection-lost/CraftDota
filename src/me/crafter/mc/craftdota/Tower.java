package me.crafter.mc.craftdota;

import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.dsh105.holoapi.api.Hologram;
import com.dsh105.holoapi.api.HologramFactory;

public class Tower {
	
	public int durability;
	public int maxdurability;
	public String name;//ex 青队中路二塔，紫队上路兵营二
	public int side;//青=0 紫=1
	public double recoil;
	public Location activator;//activator while destroyed
	public Location axis1;//min values
	public Location axis2;//max values
	public Hologram holo;
	
	
	public Tower(String towername, int towerside, int towerdurability, int towermaxdurability, double towerrecoil, Location toweraxis1, Location toweraxis2, Location toweractivator){
		name = towername;
		side = towerside;
		durability = towerdurability;
		maxdurability = towermaxdurability;
		recoil = towerrecoil;
		axis1 = toweraxis1;
		axis2 = toweraxis2;
		activator = toweractivator;
		holo = new HologramFactory(Bukkit.getPluginManager().getPlugin("CraftDota")).withLocation(activator.clone().add(0.5, -2D, 0.5)).withText(name, getReadableHealth(), getColoredHealth()).withSimplicity(true).build();
	}
	
	public Tower(String towername, int towerside, int towerdurability, int towermaxdurability, double towerrecoil, Location center){
		name = towername;
		side = towerside;
		durability = towerdurability;
		maxdurability = towermaxdurability;
		recoil = towerrecoil;
		axis1 = Tower.loctomin(center);
		axis2 = Tower.loctomax(center);
		activator = Tower.loctoact(center);
		
		Location hololoc = activator.clone().add(activator.clone().subtract(new Location(activator.getWorld(), 0, 0, 0)).toVector().normalize().setY(0D).multiply(-4D)).add(0.5D, 0, 0.5D);
		
		holo = new HologramFactory(Bukkit.getPluginManager().getPlugin("CraftDota")).withLocation(hololoc).withText(name, getReadableHealth(), getColoredHealth()).withSimplicity(true).build();
	}
	
	
	
	//helper function to determine whether hit in range
	public boolean inRange(Location loc){
		if (loc.getWorld() != axis1.getWorld()) return false;
		return (loc.getBlockX() >= axis1.getBlockX() && loc.getBlockX() <= axis2.getBlockX() && loc.getBlockY() >= axis1.getBlockY() && loc.getBlockY() <= axis2.getBlockY() && loc.getBlockZ() >= axis1.getBlockZ() && loc.getBlockZ() <= axis2.getBlockZ());
	}
	
	//check if hp=0
	public boolean isDead(){
		return (durability <= 0);
	}
	
	//subtract hp returns dead
	public boolean hit(){
		durability --;
		if (isDead()){
			//HoloAPI.getManager().stopTracking(holo);
			
			updateHolo();
			
			GameInfo.towers.remove(this);
			GameInfo.bases.remove(this);
			
			if (name.endsWith("基地")){ //nexus destroy
				GameJob.tick = 99999;
				final Location boomer = activator.clone().add(0, 1D, 0);
				//TODO tower destroyed afterworks
				Bukkit.getScheduler().runTaskTimer(Bukkit.getPluginManager().getPlugin("CraftDota"), new Runnable(){

					Random random = new Random();
					
					@Override
					public void run() {
						if (random.nextInt(8) == 1){
							boomer.getWorld().spigot().playEffect(boomer, Effect.EXPLOSION_HUGE, 0, 0, 5F, 5F, 5F, 0, 2, 64);
							boomer.getWorld().playSound(boomer, Sound.EXPLODE, 1.5F, 1F);
						}
						boomer.getWorld().spigot().playEffect(boomer, Effect.FLAME, 0, 0, 5F, 5F, 5F, 0, 12, 64);
					}

				}, 2L, 2L);
			} else { //normal tower destroy
				Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "[MCLOL] " + name + ChatColor.RESET + ChatColor.YELLOW + " 被摧毁了。。。");
				
				if (side == 0){
					for (String sp : GameInfo.bplayers){
						if (Bukkit.getPlayerExact(sp) != null){
							Bukkit.getPlayerExact(sp).getInventory().addItem(new ItemStack(Material.EMERALD, 4));
						}
					}
				} else if (side == 1){
					for (String sp : GameInfo.aplayers){
						if (Bukkit.getPlayerExact(sp) != null){
							Bukkit.getPlayerExact(sp).getInventory().addItem(new ItemStack(Material.EMERALD, 4));
						}
					}
				}
				
				final Location boomer = activator.clone().add(0, 1D, 0);
				//tower destroyed afterworks
				final BukkitTask thetask = Bukkit.getScheduler().runTaskTimer(Bukkit.getPluginManager().getPlugin("CraftDota"), new Runnable(){

					Random random = new Random();
					
					@Override
					public void run() {
						if (random.nextInt(8) == 1){
							boomer.getWorld().spigot().playEffect(boomer, Effect.EXPLOSION_HUGE, 0, 0, 3F, 3F, 3F, 0, 2, 64);
							boomer.getWorld().playSound(boomer, Sound.EXPLODE, 1.5F, 1F);
						}
						boomer.getWorld().spigot().playEffect(boomer, Effect.FLAME, 0, 0, 4F, 4F, 4F, 0, 12, 64);
					}

				}, 2L, 2L);
			
				Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("CraftDota"), new Runnable(){
					
					@Override
					public void run() {
						thetask.cancel();
					}

				}, 400L);
			//end afterworks
			
			}

			return true;
		}
		return false;
	}
	
	//damage tower check returns if success
	public boolean damage(Player p, Location loc){
		if ((GameInfo.isina(p) && side == 0) || (GameInfo.isinb(p) && side == 1)){
			return false;
		}
		
		if (inRange(loc)){
			hit();
			updateHolo();
			//=======STAR ANIMATION START========
			loc.getWorld().spigot().playEffect(loc, Effect.MAGIC_CRIT, 0, 0, 0.4F, 0.4F, 0.4F, 1F, 24, 16);
			loc.getWorld().spigot().playEffect(loc, Effect.CRIT, 0, 0, 0.4F, 0.4F, 0.4F, 1F, 16, 16);
			loc.getWorld().spigot().playEffect(loc, Effect.TILE_DUST, 0, 0, 0.8F, 0.8F, 0.8F, 0.65F, 18, 16);
			loc.getWorld().playSound(loc, Sound.EXPLODE, 0.8F, 1.45F);
			//=======STAR ANIMATION STOP========
			return true;
		} else {
			return false;
		}
	}
	
	public static Location loctomin(Location loc){
		return loc.clone().subtract(4D, 2D, 4D);
	}
	
	public static Location loctomax(Location loc){
		return loc.clone().add(4D, 20D, 4D);
	}
	
	public static Location loctoact(Location loc){
		return loc.clone().add(0, 4D, 0);
	}
	
	public String getName(){
		return name;
	}
	
	public String getReadableHealth(){
		return Math.max(durability, 0) + "/" + maxdurability;
	}
	
	public void updateHolo(){
		holo.updateLine(1, getReadableHealth());
		holo.updateLine(2, getColoredHealth());
	}
	
	public String getColoredHealth(){
		String ret = "";
		float percent = ((float)durability)/((float)maxdurability);
		if (percent > 0.6){
			ret += ChatColor.GREEN;
		} else if (percent > 0.3){
			ret += ChatColor.YELLOW;
		} else {
			ret += ChatColor.RED;
		}
		ret += StringUtils.repeat("▇", (int)(percent*20));
		ret += ChatColor.GRAY + StringUtils.repeat("▇", 20-((int)(percent*20)));
		
		return ret;
	}
	
}
