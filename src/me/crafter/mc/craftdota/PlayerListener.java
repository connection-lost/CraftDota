package me.crafter.mc.craftdota;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {
	
	@EventHandler //player score a kill
	public void onPlayerDeath(PlayerDeathEvent event){
		event.getEntity().getWorld().spigot().playEffect(event.getEntity().getLocation(), Effect.SMOKE, 0, 0, 0.1F, 0.1F, 0.1F, 0.1F, 200, 32);
		if (event.getEntity().getKiller() == null) return;
		if (!GameJob.ison) return;
		Player killer = event.getEntity().getKiller();
		Player death = event.getEntity();
		if (GameInfo.isina(killer) && GameInfo.isinb(death)){
			GameInfo.scorea();
		} else if (GameInfo.isinb(killer) && GameInfo.isina(death)){
			GameInfo.scoreb();
		}
		death.getWorld().dropItemNaturally(death.getEyeLocation(), new ItemStack(Material.EMERALD, 5));
	}
	
	@EventHandler
	public void onSneakKick(PlayerToggleSneakEvent event){
		if (GameJob.isdone){
			event.getPlayer().kickPlayer(ChatColor.GREEN + "[MCLOL] ���˳���MCߣ��ߣ��Ϸ��\n���ڿ���ѡ����һ��������룡");
		}
	}
	
	@EventHandler //No Helmet Move
	public void onPlayerMoveHelmet(InventoryClickEvent event){
		if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.LEATHER_HELMET && !(event.getWhoClicked().hasPermission("dotd.admin"))){
			event.setCancelled(true);
		}
		//TODO verify needed
	}
	
	@EventHandler //No Toss Item
	public void onPlayerTossItem(PlayerDropItemEvent event){
		if (!(event.getPlayer().hasPermission("dotd.admin"))){
			event.setCancelled(true);
		}
	}
	
	@EventHandler //Respawn Protection
	public void onPlayerRespawn(PlayerRespawnEvent event){
		//TODO unfinished
		Player p = event.getPlayer();
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 5));
		p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 2));
		if (GameInfo.isina(p)){
			//a respawns
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.FLAME, 0, 0, 0.1F, 0.1F, 0.1F, 0.3F, 200, 32);
			p.getWorld().playSound(p.getLocation(), Sound.GHAST_FIREBALL, 0.5F, 1);
		} else if (GameInfo.isinb(p)){
			//b respawns
			p.getWorld().spigot().playEffect(p.getLocation(), Effect.FLAME, 0, 0, 0.1F, 0.1F, 0.1F, 0.3F, 200, 32);
			p.getWorld().playSound(p.getLocation(), Sound.GHAST_FIREBALL, 0.5F, 1);
		} else {
			//spectator respawn, clear
			p.teleport(p.getWorld().getSpawnLocation());
			p.getInventory().clear();
			//see also player join
			p.sendMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.YELLOW + "��ӭ������Ϸ MCߣ��ߣ by Socket_Closed��");
			p.sendMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.YELLOW + "��������ʹ��̤��ѡ����飡 0w0~");
		}
	}
	
	@EventHandler
	public void onDurabilityChange(PlayerItemDamageEvent event){
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onFriendlyFire(EntityDamageByEntityEvent event){
		//if not ready cancel
		if (GameJob.tick < 0) {
			event.setCancelled(true);
		}
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player){
			Player attacker = (Player)event.getDamager();
			Player damager = (Player)event.getEntity();
			if ((GameInfo.isina(attacker) && GameInfo.isina(damager)) || (GameInfo.isinb(attacker) && GameInfo.isinb(damager))){
				event.setCancelled(true);
			}
		} else if (event.getDamager() instanceof LargeFireball){
			event.setDamage(event.getDamage()*(0.25F + (Bukkit.getOnlinePlayers().length/150F)));
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerRejoin(PlayerJoinEvent event){
		final Player p = event.getPlayer();
		p.getWorld().playSound(p.getLocation(), Sound.LEVEL_UP, 0.3F, 1F);
		//reminder: see also GameInfo - Player Ready
		if (GameInfo.isina(p)){
			p.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "[" + ChatColor.RESET + "" + ChatColor.BLUE + "����" + ChatColor.GREEN + ChatColor.BOLD + "]" + ChatColor.BLUE + p.getName() + ChatColor.RESET);
			p.setBedSpawnLocation(GameInfo.anexus, true);
		} else if (GameInfo.isinb(p)){
			p.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "[" + ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "�϶�" + ChatColor.GREEN + ChatColor.BOLD + "]" + ChatColor.LIGHT_PURPLE + p.getName() + ChatColor.RESET);
			p.setBedSpawnLocation(GameInfo.bnexus, true);
		} else {
			p.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "[" + ChatColor.RESET + "" + ChatColor.GRAY + "��ս" + ChatColor.GREEN + ChatColor.BOLD + "]" + ChatColor.RESET + p.getName());
			p.setBedSpawnLocation(p.getWorld().getSpawnLocation(), true);
			Location loc = p.getWorld().getSpawnLocation();
			loc.setYaw(-90F);
			p.teleport(loc.add(0.5D, 1D, 0.5D));
			p.setFoodLevel(20);
			p.getInventory().clear();
			p.getInventory().setBoots(null);
			p.getInventory().setChestplate(null);
			p.getInventory().setLeggings(null);
			p.getInventory().setHelmet(null);
			//see also on player respawn
			p.sendMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.YELLOW + "��ӭ������Ϸ MCߣ��ߣ by Socket_Closed��");
			p.sendMessage(ChatColor.GREEN + "[MCLOL] " + ChatColor.YELLOW + "��ʹ��̤��ѡ����飡 0w0~");
		}
		
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent event){
		event.setJoinMessage(null);
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerLeave(PlayerQuitEvent event){
		event.setQuitMessage(null);
		if (Bukkit.getOnlinePlayers().length < 2){
			GameJob.tick = 99999;
		}
	}
	
	@EventHandler
	public void onFrameDestroy(EntityDamageByEntityEvent event){
		if (event.getEntity() instanceof ItemFrame){
			if (event.getDamager() instanceof Player && ((Player)event.getDamager()).getGameMode() == GameMode.CREATIVE){
				//do it
			} else {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onKillCommand(PlayerCommandPreprocessEvent event){
		if (event.getMessage().startsWith("/kill")){
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "�������������ѣ�");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onServerListPing(ServerListPingEvent event){
		if (GameJob.isdone){
			event.setMotd(ChatColor.LIGHT_PURPLE + ">>> " + ChatColor.RED + "[��ѡ������������]\n" + ChatColor.LIGHT_PURPLE + ">>> " + ChatColor.RED + "�����ѽ���������������������");
		} else if (GameJob.ison){
			if (Bukkit.getServer().getOnlinePlayers().length + 5 > Bukkit.getServer().getMaxPlayers()){
				event.setMotd(ChatColor.LIGHT_PURPLE + ">>> " + ChatColor.YELLOW + "[�������(����)]\n" + ChatColor.LIGHT_PURPLE + ">>> " + ChatColor.AQUA + "�����Ѿ������� " + ChatColor.GOLD + (GameJob.tick/60-1) + ChatColor.AQUA + " ���ӣ�");
			} else if (GameJob.tick > 2520){
				event.setMotd(ChatColor.LIGHT_PURPLE + ">>> " + ChatColor.GREEN + "[�������(����)]\n" + ChatColor.LIGHT_PURPLE + ">>> " + ChatColor.AQUA + "�����Ѿ������� " + ChatColor.GOLD + (GameJob.tick/60-1) + ChatColor.AQUA + " ���ӣ�");
			} else {
				event.setMotd(ChatColor.LIGHT_PURPLE + ">>> " + ChatColor.GREEN + "[�������(�Ƽ�)]\n" + ChatColor.LIGHT_PURPLE + ">>> " + ChatColor.AQUA + "�����Ѿ������� " + ChatColor.GOLD + (GameJob.tick/60-1) + ChatColor.AQUA + " ���ӣ�");
			}			
		} else {
			event.setMotd(ChatColor.LIGHT_PURPLE + ">>> " + ChatColor.GREEN + "[�������(�Ƽ�)]\n" + ChatColor.LIGHT_PURPLE + ">>> " + ChatColor.AQUA + "����������ʼ��");
		}
	}
	
}
