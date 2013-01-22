package com.github.hatixon.mutenizer;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@SuppressWarnings({"rawtypes", "unused"})
public class CommandListener implements Listener
{
	public static Mutenizer plugin;
	
	public CommandListener(Mutenizer instance)
	{
		plugin = instance;
	}
	
	@EventHandler
	public void cmdPreProcess(PlayerCommandPreprocessEvent e)
	{
		Map blackList = plugin.getBlackMap();
		String type = plugin.getConfig().getString("PunishmentType");
		if(plugin.getCommandCheck())
		{
			Player player = e.getPlayer();
			String uName = player.getName();
			String splits[] = e.getMessage().split(" ");
			String cmd = splits[0].replace("/", "");
			if(player.hasPermission("mutenizer.bypass.commandcheck") || player.hasPermission("mutenizer.*"))
			{
				return;
			}
			if(plugin.commandSwear(cmd))
			{
				StringBuilder messagebuild = new StringBuilder();
		    	int x;
		    	for(x = 1; x < splits.length; x++)
		    	{
					String hello = splits[x].replace(".", "").replace("-", "")
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
						plugin.potionEffect(player);
						player.performCommand("me Camouflage: activated!");
						e.setCancelled(true);
					}
				}
				if(plugin.instaBanCheck(message))
				{
		    		if(player.hasPermission("muten.bypass.swear") || player.hasPermission("muten.bypass.*"))
		    		{
		    			return;
		    		}
		    		else
		    		{
		    			e.setCancelled(true);
			            plugin.instaBanPlayer(player);
			            return;
			            
		    		}
				}else
				{
			    	if(plugin.didTheySwear(message))
			    	{
			    		if(player.hasPermission("muten.bypass.swear") || player.hasPermission("muten.bypass.*"))
			    		{
			    			return;
			    		}
		    			message = plugin.censorCheck(message);
		    			if(message.length() > 0)
		    			{
		    				e.setMessage(message);
		    			} else
		    			{
		    				e.setCancelled(true);
		    			}
			    		
			            if(plugin.getNotifyOp())
			            {
		                    Player arr[] = plugin.getServer().getOnlinePlayers();
		                    int len = arr.length;
		                    for(int i = 0; i < len; i++)
		                    {
		                        Player player2 = arr[i];
		                        if(player2.hasPermission("mutenizer.notify"))
		                        {
		                            player2.sendMessage((new StringBuilder(pre)).append(" ").append(player.getName().toUpperCase()).append(" just swore and lost 1 warning.").toString());
		                        }
		                    }
			            }
			            
			    		if(plugin.getMoneyEnabled())
			    		{
			    			if(player.hasPermission("muten.bypass.money") || player.hasPermission("muten.bypass.*"))
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
				            			if(player.hasPermission("muten.bypass.ban") || player.hasPermission("muten.bypass.*"))
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
				        			if(player.hasPermission("muten.bypass.ban") || player.hasPermission("muten.bypass.*"))
				        			{
				        				player.sendMessage(new StringBuilder(pre).append(" Please stop swearing. If this continues, punishment may occur").toString());
			        					plugin.resetBanned(uName);
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
				    	                        if(player2.hasPermission("mutenizer.notify"))
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
				    	                    		if(player2.hasPermission("mutenizer.notify"))
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
		}
	}
}
