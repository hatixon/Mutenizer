package com.github.hatixon.mutenizer;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.*;
import org.bukkit.entity.Player;
@SuppressWarnings("unused")
public class SignPlaceListener implements Listener
{

	public static Mutenizer plugin;
	
	public SignPlaceListener(Mutenizer instance)
	{
		plugin = instance;
	}
	@EventHandler
	public void onSignPlace(SignChangeEvent e)
	{
		String type = plugin.getConfig().getString("PunishmentType");
		if(plugin.getConfig().getBoolean("Check.Signs"))
		{
			Player player = e.getPlayer();
			String uName = player.getName();
			int c;
			if(player.hasPermission("mutenizer.bypass.signcheck") || player.hasPermission("mutenizer.*"))
			{
				return;
			}
			for(c = 0; c < 4; c++)
			{
		    	StringBuilder messagebuild = new StringBuilder();
		    	String premessage[] = ((String) e.getLine(c)).split(" ");
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
	

				if(plugin.instaBanCheck(message))
				{
		    		if(player.hasPermission("muten.bypass.swear") || player.hasPermission("muten.bypass.*"))
		    		{
		    			return;
		    		}
		    		else
		    		{
			    		if(plugin.getConfig().getBoolean("BreakSignsOnSwear"))
			    		{
			    			e.getBlock().breakNaturally();
				            plugin.instaBanPlayer(player);
			    		}else
			    		{
				            message = plugin.censorCheck(message);
				            if(message.length() > 0)
				            {
				                e.setLine(c, message);
				            } else
				            {
				                e.setLine(c, "");
				            }
				            
				            plugin.instaBanPlayer(player);
				            return;
			    		}
		    		}
				}else
				{
			    	if(plugin.didTheySwear(message))
			    	{

		    		
			    		if(player.hasPermission("muten.bypass.swear") || player.hasPermission("muten.bypass.*"))
			    		{
			    			return;
			    		}
			    		
			    		if(plugin.getConfig().getBoolean("BreakSignsOnSwear"))
			    		{
			    			e.getBlock().breakNaturally();
			    		}else
			    		{
				            message = plugin.censorCheck(message);
				            if(message.length() > 0)
				            {
				                e.setLine(c, message);
				            } else
				            {
				                e.setCancelled(true);
				            }
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
				            			}
				        			}
				        		}
				        		else
				        		if(warnRemainings.intValue() == 0)
				        		{
				        			if(player.hasPermission("muten.bypass.ban") || player.hasPermission("muten.bypass.*"))
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
				    	                    if(plugin.getNotifyOp())
				    	                    {
				    	                    	for(int i = 0; i < len; i++)
				    	                    	{
				    	                    		Player player2 = arr[i];
				    	                    		if(player2.hasPermission("mutenizer.notify"))
				    	                    		{
				    	                    			player2.sendMessage((new StringBuilder(pre)).append(" ").append(player.getName().toUpperCase()).append(" was banned for repeated swearing.").toString());
				    	                    		}
				    	                    	}
				    	                    }
				        				}else
				        				{
				        					plugin.bunnyRabbit(player);
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
