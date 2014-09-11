package me.crafter.mc.craftdota;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.api.Hologram;

public class TowerWorkerOld implements Runnable {

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
						if (p.getLocation().distanceSquared(t.activator) < 100F){
							if (t.side == GameInfo.getSide(p)){
								p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
								p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 160, 0));
								break;
							} else {
							}
						}
					}
				}
			}
			
			
		} catch (Exception ex){
			//nothing
		}
		
		if (count % 3 == 0){
			for (Hologram holo : HoloAPI.getManager().getHologramsFor(Bukkit.getPluginManager().getPlugin("CraftDota"))){
				holo.refreshDisplay();
			}
		}
		
		count ++;
		
	}

	
	
}
