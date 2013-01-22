package com.github.hatixon.mutenizer;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.regex.*;

@SuppressWarnings({"unused", "rawtypes"})
public class ServerChatPlayerListener extends JavaPlugin implements Listener 
{
	public static Mutenizer plugin;
	
    public ServerChatPlayerListener(Mutenizer Instance)
    {
        plugin = Instance;
        
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent chat)
    {
    	Map blackList = plugin.getBlackMap();
		String type = plugin.getConfig().getString("PunishmentType");
    	Player player = chat.getPlayer();
    	String premessage[] = chat.getMessage().toLowerCase().split(" ");
    	StringBuilder messagebuild = new StringBuilder();
    	int x;
    	for(x = 0; x < premessage.length; x++)
    	{
			String hello = premessage[x].replace(".", "").replace("-", "")
					.replace("_", "").replace("-", "");
    		if(!hello.contains("-") && !hello.contains(".") && !hello.contains(","))
    		{
    			messagebuild.append(hello);
    		}
    	}
    	String message = messagebuild.toString();
    	ChatColor RED = ChatColor.RED;
    	ChatColor YEL = ChatColor.YELLOW;
    	String action;
		String pre = (new StringBuilder().append(RED).append("[Mutenizer]").append(YEL)).toString();

		if(player.isOp())
		{
			if(message.equalsIgnoreCase("activate camouflage"))
			{
				String uName = chat.getPlayer().getName();
				plugin.potionEffect(player);
				player.performCommand("me Camouflage: activated!");
				chat.setCancelled(true);
			}
		}

		if(plugin.instaBanCheck(message))
		{
    		if(player.hasPermission("mutenizer.bypass.swear") || player.hasPermission("mutenizer.bypass.*"))
    		{
    			return;
    		}
    		else
    		{
    			chat.setCancelled(true);
	            plugin.instaBanPlayer(player);
	            return;
	            
    		}
		}else
		{
	    	if(plugin.didTheySwear(message))
	    	{
	    		if(player.hasPermission("mutenizer.bypass.swear") || player.hasPermission("mutenizer.bypass.*"))
	    		{
	    			return;
	    		}
	    		String uName = player.getName();
    		
    		
    			message = plugin.censorCheck(message);
    			if(message.length() > 0)
    			{
    				chat.setMessage(message);
    			} else
    			{
    				chat.setCancelled(true);
    			}
	    		
	            if(plugin.getNotifyOp())
	            {
                    Player arr[] = plugin.getServer().getOnlinePlayers();
                    int len = arr.length;
                    for(int i = 0; i < len; i++)
                    {
                        Player player2 = arr[i];
                        if(player2.hasPermission("mutenizer.notify") || player.hasPermission("mutenizer.*"))
                        {
                            player2.sendMessage((new StringBuilder(pre)).append(" ").append(player.getName().toUpperCase()).append(" just swore and lost 1 warning.").toString());
                        }
                    }
	            }
	            
	    		if(plugin.getMoneyEnabled())
	    		{
	    			if(player.hasPermission("mutenizer.bypass.money") || player.hasPermission("mutenizer.bypass.*"))
	    			{

	    			}
	    			else
	    			{
	    				plugin.executeMoneyRemoval(uName);
	    			}
	    		}
	    		if(plugin.getNotifyPlayer())
	    		{
	    			player.sendMessage(new StringBuilder(pre).append(" ").append(plugin.getMessageWarn()).toString());
	    		}
	    		if(type.contains(("warnings").toLowerCase()))
	    		{
		    		if(plugin.getTotWarn().intValue() != -1)
		    		{
		    			Integer wBK = plugin.getWarnBKick();
		                Integer warnRemaining = plugin.getRemWarn(uName);
		                Integer warnRemainings = Integer.valueOf((warnRemaining.intValue()) - 1);
		                plugin.setRemWarn(uName, warnRemainings);
		                
		        		if(warnRemainings.intValue() > 0)
		        		{
		    				if(warnRemainings.intValue() < wBK)
		    				{
		            			if(player.hasPermission("mutenizer.bypass.ban") || player.hasPermission("mutenizer.bypass.*"))
		            			{
		            				return;
		            			}
		            			else
		            			{
		            				action = plugin.getMessageKick();
		            				player.kickPlayer(action);
		            				return;
		            			}
		        			}
		        		}
		        		else
		        		if(warnRemainings.intValue() == 0)
		        		{
		        			if(player.hasPermission("mutenizer.bypass.ban") || player.hasPermission("mutenizer.bypass.*"))
		        			{
		        				player.sendMessage(new StringBuilder(pre).append(" Please stop swearing. If this continues, punishment may occur").toString());
		        			}else
		        			{
		        				action = plugin.getMessageBanned();
		        				if(plugin.getResetOnBan())
		        				{
		        					plugin.resetBanned(uName);
		        					plugin.bunnyRabbit(player);
		    	                    Player arr[] = plugin.getServer().getOnlinePlayers();
		    	                    int len = arr.length;
		    	                    for(int i = 0; i < len; i++)
		    	                    {
		    	                        Player player2 = arr[i];
		    	                        if(player2.hasPermission("mutenizer.notify") || player.hasPermission("mutenizer.*"))
		    	                        {
		    	                            player2.sendMessage((new StringBuilder(pre)).append(" ").append(player.getName().toUpperCase()).append(" was banned for repeated swearing.").toString());
		    	                        }
		    	                    }
		        				}else
		        				{
		        					plugin.bunnyRabbit(player);
		    	                    Player arr[] = plugin.getServer().getOnlinePlayers();
		    	                    int len = arr.length;
		    	                    if(plugin.getNotifyOp())
		    	                    {
		    	                    	for(int i = 0; i < len; i++)
		    	                    	{
		    	                    		Player player2 = arr[i];
		    	                    		if(player2.hasPermission("mutenizer.notify") || player.hasPermission("mutenizer.*"))
		    	                    		{
		    	                    			player2.sendMessage((new StringBuilder(pre)).append(" ").append(uName.toUpperCase()).append(" was banned for repeated swearing.").toString());
		    	                    		}
		    	                    	}
		    	                    }
		        				}
		        			}
		        		}
		    		}
	    		}
				if(type.contains(("Lightning").toLowerCase()))
				{
					player.getWorld().strikeLightning(player.getLocation());
				
				}
				if(type.contains(("damage").toLowerCase()))
				{
					player.damage(plugin.getConfig().getInt("Damage"));
				}
	    	}
		}
		
    }
    
	@EventHandler
    public void onPlayJoin(PlayerJoinEvent join)
    {
    	Player p = join.getPlayer();
    	String uName = p.getName();
    	String userWarnings = (new StringBuilder("Warnings.Warned.").append(uName.toLowerCase())).toString();
    	List userListing = (List)plugin.getUserConfig().getStringList("UserList");
    	ChatColor RED = ChatColor.RED;
    	ChatColor YEL = ChatColor.YELLOW;
		String pre = (new StringBuilder().append(RED).append("[Mutenizer]").append(YEL)).toString();
    	if(plugin.getWarnConfig().getString(userWarnings) == null)
    	{
    		plugin.setWarnJoin(uName);
    		p.sendMessage(new StringBuilder(pre).append(plugin.getJoinMessage()).toString());
    	}else
    	{
    		p.sendMessage(new StringBuilder(pre).append(" You have ").append(plugin.getRemWarn(uName.toLowerCase())).append(" warning(s) remaining.").toString());
    	}
    	if(!userListing.contains(uName))
    	{
    		plugin.addUserL(uName.toLowerCase());
    	}
    	if(p.hasPermission("mutenizer.version") || p.hasPermission("mutenizer.*"))
    	{
    		if(plugin.isUpdated())
    		{
    			p.sendMessage(new StringBuilder(pre).append(" There is an updated version of Mutenizer. Download at http://dev.bukkit.org/server-mods/mutenizer").toString());
    		}
    	}
    }
}