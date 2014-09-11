package me.crafter.mc.craftdota;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.api.Hologram;

public class TowerWorker implements Runnable {

	int count = 0;
	
	@Override
	public void run() {
		
		try {
			
			if (GameJob.isdone) return;
			for (String p1 : GameInfo.aplayers){
				Player p = Bukkit.getPlayer(p1);
				if (p != null && p.getWorld().getName().equals("world")){
					if (p.getLocation().distanceSquared(GameInfo.anexus) < 40F){
						if (GameInfo.isina(p)){
							p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 3));
						} else {
							p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 5));
						}
					} else if (p.getLocation().distanceSquared(GameInfo.bnexus) < 40F){
						if (GameInfo.isinb(p)){
							p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 3));
						} else {
							p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 5));
						}
					}
					for (Tower t : GameInfo.towers){
						if (t.side == 0) continue;
						if (p.getLocation().distanceSquared(t.activator) < 400F){
							p.getWorld().playSound(t.activator, Sound.GHAST_FIREBALL, 1F, 1F);
							p.getWorld().spigot().playEffect(p.getLocation().add(0D, 5D, 0D), Effect.FLAME, 0, 0, 0, 0, 0, 0.2F, 200, 32);
							LargeFireball fireball = p.getWorld().spawn(p.getLocation().add(0D, 5D, 0D), LargeFireball.class);
							fireball.setIsIncendiary(false);
							fireball.setYield(0);
							fireball.setVelocity(new Vector(0, -0.5D, 0));
							fireball.setDirection(new Vector(0, -0.5D, 0));
							//fireball.setMomentum(new Vector(0, -0.5D, 0));
							//Bukkit.broadcastMessage(ChatColor.RED + p.getLocation().toString());
						}
					}
				}
			}
			
			for (String p1 : GameInfo.bplayers){
				Player p = Bukkit.getPlayer(p1);
				if (p != null && p.getWorld().getName().equals("world")){
					if (p.getLocation().distanceSquared(GameInfo.anexus) < 40F){
						if (GameInfo.isina(p)){
							p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 3));
						} else {
							p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 5));
						}
					} else if (p.getLocation().distanceSquared(GameInfo.bnexus) < 40F){
						if (GameInfo.isinb(p)){
							p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 3));
						} else {
							p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 5));
						}
					}
					for (Tower t : GameInfo.towers){
						if (t.side == 1) continue;
						if (p.getLocation().distanceSquared(t.activator) < 400F){
							p.getWorld().playSound(t.activator, Sound.GHAST_FIREBALL, 1F, 1F);
							p.getWorld().spigot().playEffect(p.getLocation().add(0D, 5D, 0D), Effect.FLAME, 0, 0, 0, 0, 0, 0.2F, 200, 32);
							LargeFireball fireball = p.getWorld().spawn(p.getLocation().add(0D, 5D, 0D), LargeFireball.class);
							fireball.setIsIncendiary(false);
							fireball.setYield(0);
							fireball.setVelocity(new Vector(0, -0.5D, 0));
							fireball.setDirection(new Vector(0, -0.5D, 0));
							//fireball.setMomentum(new Vector(0, -0.5D, 0));
							//Bukkit.broadcastMessage(ChatColor.RED + p.getLocation().toString());
						}
					}
				}
			}
			
		} catch (Exception ex){
			Bukkit.getLogger().info("tower error");
		}
		
		if (count % 3 == 0){
			for (Hologram holo : HoloAPI.getManager().getHologramsFor(Bukkit.getPluginManager().getPlugin("CraftDota"))){
				holo.refreshDisplay();
			}
		}
		
		count ++;
		
	}

	
	
}
