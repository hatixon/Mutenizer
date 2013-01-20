package com.github.hatixon.mutenizer;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.ChatColor;


public class CapsCheck implements Listener
{

	public static Mutenizer plugin;
	
	public CapsCheck(Mutenizer instance)
	{
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		String type = plugin.getConfig().getString("PunishmentType");
		if(!plugin.getCapsOn())
		{
			return;
		}
		String message = e.getMessage();
		Player p = e.getPlayer();
		int capsCount = 0;
		ChatColor RED = ChatColor.RED;
		ChatColor YEL = ChatColor.YELLOW;
		String pre = (new StringBuilder().append(RED).append("[Mutenizer]").append(YEL)).toString();
		for(int c = 0; c < message.length(); c++)
		{
			int value = message.charAt(c);
			
            if(value >= 65 && value <= 90)
            {
                capsCount++;
            }
		}
		
		double percentCaps = (double)capsCount / (double)message.length();
		if(percentCaps > plugin.getCapsPercent())
		{
			if(p.hasPermission("muten.bypass.caps") || p.hasPermission("muten.bypass.*"))
			{
				return;
			}
			else
			{
				if(plugin.getCapsPunishment())
				{	
					String action;
					String uName = p.getName();

	                if(plugin.getNotifyPlayer())
	                {
	                	p.sendMessage(new StringBuilder(pre).append(" ").append(plugin.getMessageCaps()).toString());
	                }
	                if(plugin.getNotifyOp())
	                {
	                    Player arr[] = plugin.getServer().getOnlinePlayers();
	                    int len = arr.length;
	                    for(int i = 0; i < len; i++)
	                    {
	                        Player player2 = arr[i];
	                        if(player2.isOp())
	                        {
	                            player2.sendMessage((new StringBuilder(pre)).append(p.getName()).append(" just swore and lost 1 warning.").toString());
	                        }
	                    }
	                }
					e.setCancelled(true);
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
			            			if(p.hasPermission("muten.bypass.caps") || p.hasPermission("muten.bypass.*"))
			            			{
			            				return;
			            			}
			            			else
			            			{
			            				action = plugin.getMessageCapsKick();
			            				p.kickPlayer(action);
			            				return;
			            			}
			        			}
			        		}
			        		else
			        		if(warnRemainings.intValue() == 0)
			        		{
			        			if(p.hasPermission("muten.bypass.caps") || p.hasPermission("muten.bypass.*"))
			        			{
			        				p.sendMessage(new StringBuilder(pre).append(" Please stop using too many capitals.").toString());
			        			}else
			        			{
			        				if(plugin.getResetOnBan())
			        				{
			        					plugin.resetBanned(uName);
			        					plugin.capsBunnyRabbit(p);
			    	                    Player arr[] = plugin.getServer().getOnlinePlayers();
			    	                    int len = arr.length;
			    	                    if(plugin.getNotifyOp())
			    	                    {
			    	                    	for(int i = 0; i < len; i++)
			    	                    	{
			    	                    		Player player2 = arr[i];
			    	                    		if(player2.isOp())
			    	                    		{
			    	                    			player2.sendMessage((new StringBuilder(pre)).append(" ").append(uName.toUpperCase()).append(" was banned for repeated swearing.").toString());
			    	                    		}
			    	                    	}
			    	                    }
			        					return;
			        				}else
			        				{
			        					plugin.capsBunnyRabbit(p);
			    	                    Player arr[] = plugin.getServer().getOnlinePlayers();
			    	                    int len = arr.length;
			    	                    if(plugin.getNotifyOp())
			    	                    {
			    	                    	for(int i = 0; i < len; i++)
			    	                    	{
			    	                    		Player player2 = arr[i];
			    	                    		if(player2.isOp())
			    	                    		{
			    	                    			player2.sendMessage((new StringBuilder(pre)).append(" ").append(uName.toUpperCase()).append(" was banned for spamming capitals.").toString());
			    	                    		}
			    	                    	}
			    	                    }
			        				}
			        			}
			        		}
		                }
		    		}
					if(type.contains(("lightning").toLowerCase()))
					{
						p.getWorld().strikeLightning(p.getLocation());
					}
					if(type.contains(("damage").toLowerCase()))
					{
						p.damage(plugin.getConfig().getInt("Damage"));
					}
				}else
				{
					e.setCancelled(true);
				}
			}
		}
	}
}
