package me.crafter.mc.craftdota;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class DotaListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event){
		//creative
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
		//world check
		if (!event.getBlock().getWorld().getName().equals("world")) return;
		//cancel first
		event.setCancelled(true);
		//loop thru towers
		if (!GameJob.ison) return;
		//update inv
		event.getPlayer().updateInventory();
		
		//now using async!
		Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("CraftDota"), new Runnable(){

			@Override
			public void run() {
				
				if (event.getBlock().getType().equals(Material.TORCH)) return;

				for (Tower t : GameInfo.bases){
				//Bukkit.broadcastMessage("Checking Tower " + t.getName());
				//does this hit land in... ?
				if (t.damage(event.getPlayer(), event.getBlock().getLocation())){
					//Bukkit.broadcastMessage("[MCLOL] hit tower at");
					//Bukkit.broadcastMessage(event.getBlock().toString());
					return;
				}
				}
				for (Tower t : GameInfo.towers){
					if (t.damage(event.getPlayer(), event.getBlock().getLocation())){
						//Bukkit.broadcastMessage("[MCLOL] hit tower at");
						//Bukkit.broadcastMessage(event.getBlock().toString());
						return;
					}
				}
				
			}
		});
		
//		for (Tower t : GameInfo.bases){
//			//Bukkit.broadcastMessage("Checking Tower " + t.getName());
//			//does this hit land in... ?
//			if (t.damage(event.getPlayer(), event.getBlock().getLocation())){
//				//Bukkit.broadcastMessage("[MCLOL] hit tower at");
//				//Bukkit.broadcastMessage(event.getBlock().toString());
//				return;
//			}
//		}
//		for (Tower t : GameInfo.towers){
//			if (t.damage(event.getPlayer(), event.getBlock().getLocation())){
//				//Bukkit.broadcastMessage("[MCLOL] hit tower at");
//				//Bukkit.broadcastMessage(event.getBlock().toString());
//				return;
//			}
//		}
		
		
	}
	

}


