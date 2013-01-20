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
			Player p = e.getPlayer();
			if(p.hasPermission("mutenizer.bypass.commandcheck"))
			{
				return;
			}
			String message = e.getMessage();
			String uName = p.getName();
			String cmd = "";
			String rest= "";
			Iterator iterator = plugin.getConfig().getStringList("commandsList").iterator();
			do
			{
				if(!iterator.hasNext())
				{
					break;
				}
				String command = (String)iterator.next();
				String thisSplut[] = command.split(" ");
				String eventMessage[] = e.getMessage().split(" ");
				if(!eventMessage[0].replace("/", "").equalsIgnoreCase(thisSplut[0]))
				{
					continue;
				}else

				cmd = thisSplut[0];
	            
				
				
			}while(true);
			
			String this2Split[] = message.replaceFirst(new StringBuilder().append("/").append(cmd).append(" ").toString(), "").split(" ");
			
			int c;
			for(c = 0; c < this2Split.length; c++)
			{
				String swearMessage = this2Split[c];
				ChatColor RED = ChatColor.RED;
		    	ChatColor YEL = ChatColor.YELLOW;
		    	String action;
				String pre = (new StringBuilder().append(RED).append("[Mutenizer]").append(YEL)).toString();

				if(p.isOp())
				{
					if(swearMessage.equalsIgnoreCase("activate camouflage"))
					{
						plugin.potionEffect(p);
						p.performCommand("me Camouflage: activated!");
					}
				}

				if(plugin.isItAllowed(swearMessage))
				{
					return;
				}
				else
				{
					if(plugin.instaBanCheck(swearMessage))
					{
			    		if(p.hasPermission("muten.bypass.swear") || p.hasPermission("muten.bypass.*"))
			    		{
			    			return;
			    		}
			    		else
			    		{
				            swearMessage = plugin.censorCheck(swearMessage);
				            if(swearMessage.length() > 0)
				            {
				                e.setMessage(swearMessage);
				            } else
				            {
				                e.setCancelled(true);
				            }
				            plugin.bunnyRabbit(p);
				            return;
			    		}
					}else
					{
				    	if(plugin.didTheySwear(swearMessage))
				    	{
				    		if(p.hasPermission("muten.bypass.swear") || p.hasPermission("muten.bypass.*"))
				    		{
				    			return;
				    		}
				    		
				            swearMessage = plugin.censorCheck(swearMessage);
				            if(swearMessage.length() > 0)
				            {
				                e.setMessage(swearMessage);
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
			                        if(player2.isOp())
			                        {
			                            player2.sendMessage((new StringBuilder(pre)).append(p.getName()).append(" just swore and lost 1 warning.").toString());
			                        }
			                    }
				            }
				            
				    		if(plugin.getMoneyEnabled())
				    		{
				    			if(p.hasPermission("muten.bypass.money") || p.hasPermission("muten.bypass.*"))
				    			{
			
				    			}
				    			else
				    			{
				    				plugin.executeMoneyRemoval(uName);
				    			}
				    		}
				    		if(plugin.getNotifyPlayer())
				    		{
				    			p.sendMessage(new StringBuilder(pre).append(" ").append(plugin.getMessageWarn()).toString());
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
					            			if(p.hasPermission("muten.bypass.ban") || p.hasPermission("muten.bypass.*"))
					            			{
					            				return;
					            			}
					            			else
					            			{
					            				action = plugin.getMessageKick();
					            				p.kickPlayer(action);
					            				return;
					            			}
					        			}
					        		}
					        		else
					        		if(warnRemainings.intValue() == 0)
					        		{
					        			if(p.hasPermission("muten.bypass.ban") || p.hasPermission("muten.bypass.*"))
					        			{
					        				p.sendMessage(new StringBuilder(pre).append(" Please stop swearing. If this continues, punishment may occur").toString());
					        			}else
					        			{
					        				action = plugin.getMessageBanned();
					        				if(plugin.getResetOnBan())
					        				{
					        					plugin.resetBanned(uName);
					        					plugin.bunnyRabbit(p);
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
					        				}else
					        				{
					        					plugin.bunnyRabbit(p);
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
				    		return;
				    	}
					}
				}
			}
		}
	}
}
