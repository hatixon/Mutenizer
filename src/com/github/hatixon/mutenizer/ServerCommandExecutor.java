package com.github.hatixon.mutenizer;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

@SuppressWarnings("rawtypes")
public class ServerCommandExecutor implements CommandExecutor
{
	
	public static Mutenizer plugin;
	
	public ServerCommandExecutor(Mutenizer Instance)
	{
		plugin = Instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[])
	{

		ChatColor RED = ChatColor.RED;
		ChatColor YEL = ChatColor.YELLOW;
		Map blackList = plugin.getBlackMap();
		Map whiteList = plugin.getWhiteMap();
		Map userList = plugin.getUserMap();
		Map instaBanList = plugin.getBanList();
		String pre = (new StringBuilder().append(RED).append("[Mutenizer]").append(YEL)).toString();
		String pre2 = (new StringBuilder().append(RED).append("[Mutenizer]").append(ChatColor.WHITE)).toString();
		String cantUse = new StringBuilder(pre).append(" You can not use this command!").toString();
		if(sender instanceof ConsoleCommandSender)
		{
			ConsoleCommandSender ccs = (ConsoleCommandSender)sender;
			if(cmd.getName().equalsIgnoreCase("mutenizer"))
	        {
				if(args.length < 1)
				{
					ccs.sendMessage(new StringBuilder(pre).append("mutenizer help").toString());
					return true;
				}
				if(args.length > 0)
				{
					String param = args[0];
					
	            	if(param.equalsIgnoreCase("reload"))
	            	{
	            		plugin.getServer().getPluginManager().disablePlugin(plugin);
	            		plugin.getServer().getPluginManager().enablePlugin(plugin);
	            		ccs.sendMessage(new StringBuilder(pre).append(" Successfully reloaded!").toString());
	            	}
	            	
					if(param.equalsIgnoreCase("resetall"))
					{
						String thisPlayer;
						for(Iterator iterator = userList.entrySet().iterator(); iterator.hasNext();)
        	            {
        	                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
        	                thisPlayer = (String)entry.getKey();
        	                plugin.resetWarn(thisPlayer);
        	            }
						ccs.sendMessage(new StringBuilder(pre).append(" Players warnings have been reset to default!").toString());
						return true;
					}
					if(param.equalsIgnoreCase("warnings"))
					{
						if(args.length > 2)
						{
							ccs.sendMessage(new StringBuilder(pre).append(" Too many players!").toString());
							return true;
						}
						if(args.length < 2)
						{
							ccs.sendMessage(new StringBuilder(pre).append(" Please specify a player!").toString());
							return true;
						}
						String uName = args[1];
            			int remWarn = plugin.getRemWarn(uName.toLowerCase());
            			if(remWarn != 0)
            			{
            				ccs.sendMessage(new StringBuilder(pre).append(" ").append(uName.toUpperCase()).append(" has ").append(remWarn).append(" warning(s) left.").toString());
            			}
            			else
            			{
            				ccs.sendMessage(new StringBuilder(pre).append(" ").append(uName.toUpperCase()).append(" does not exist. Make sure you spelt the name right.").toString());
            			}
            			return true;
					}
					if(!param.equalsIgnoreCase("whitelist") && !param.equalsIgnoreCase("blacklist") && !param.equalsIgnoreCase("reset") && !param.equalsIgnoreCase("warnings") && !param.equalsIgnoreCase("help") && !param.equalsIgnoreCase("instaban") && !param.equalsIgnoreCase("resetall") && !param.equalsIgnoreCase("info")  && !param.equalsIgnoreCase("reload"))
	                {
	                	ccs.sendMessage(new StringBuilder(pre).append("No such command. Use /mutenizer help").toString());
	                	return true;
	                }

	            	if(param.equalsIgnoreCase("whitelist"))
	            	{
	            		if(args.length < 2)
	            		{
	            			ccs.sendMessage(new StringBuilder(pre).append(" /mutenizer whitelist help").toString());
	            			return true;
	            		}
	            		
	            		if(!args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("delete")&& !args[1].equalsIgnoreCase("help"))
	            		{
	            			ccs.sendMessage(new StringBuilder().append(RED).append(" ").append(args[1]).append(" is not a valid parameter /mutenizer whitelist help").toString());
	            			return true;
	            		}
	            		
	            		if(args[1].equalsIgnoreCase("list"))
		            	{

		        	            String thisWord;
		        	            String replaceAppend;
		        	            ccs.sendMessage((new StringBuilder(pre).append(" Whitelisted words:")).toString());
		            			for(Iterator iterator = whiteList.entrySet().iterator(); iterator.hasNext(); sender.sendMessage((new StringBuilder(String.valueOf(thisWord))).append(replaceAppend).toString()))
		        	            {
		        	                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
		        	                thisWord = (String)entry.getKey();
		        	                String thisReplace = (String)entry.getValue();
		        	                replaceAppend = thisReplace.length() <= 0 ? "" : (new StringBuilder(":")).append(thisReplace).toString();
		        	            }
		            			return true;

	            		}
	            		

		            	if(args[1].equalsIgnoreCase("add"))
		            	{

			            		if(args.length > 3)
			            		{
			            			ccs.sendMessage(new StringBuilder(pre).append(" Too many arguments!").toString());
			            			return true;
			            		}
		            			if(args.length > 2)
		                        {
		                            String whiteWord = args[2].toLowerCase();
		                            if(plugin.addWhiteWord(whiteWord))
		                            {
		                                ccs.sendMessage(new StringBuilder(pre).append(" ").append(whiteWord).append(" was added to the whitelist.").toString());
		                            } else
		                            {
		                                ccs.sendMessage(new StringBuilder(pre).append(" ").append(whiteWord).append(" is already in the whitelist.").toString());
		                            }
		                            return true;
		                        }else
		                        if(args.length < 3)
			            		{
			            			ccs.sendMessage(new StringBuilder(pre).append(" Not enough arguments /mutenizer whitelist add <word>").toString());
			            			return true;
			            		}

		            	}
		            	if(args[1].equalsIgnoreCase("delete"))
		            	{
	
			            		if(args.length > 3)
			            		{
			            			ccs.sendMessage(new StringBuilder(pre).append(" Too many arguments!").toString());
			            			return true;
			            		}else
		                        if(args.length > 2)
		                        {
		                            String whiteWord = args[2].toLowerCase();
		                            if(plugin.delWhiteWord(whiteWord))
		                            {
		                                ccs.sendMessage(new StringBuilder(pre).append(" ").append(whiteWord).append(" was deleted from the whitelist.").toString());
		                            } else
		                            {
		                                ccs.sendMessage(new StringBuilder(pre).append(" ").append(whiteWord).append(" is not in the whitelist.").toString());
		                            }
		                            return true;
		                            
		                        }else
		                        	
		                        if(args.length < 3)
			            		{
			            			ccs.sendMessage(new StringBuilder(pre).append(" Not enough arguments /mutenizer whitelist delete <words>").toString());
			            			return true;
			            		}
		            		
	            		}
		            	if(args[1].equalsIgnoreCase("help"))
		            	{
		            		ccs.sendMessage(new StringBuilder(pre).append(" /mutenizer whitelist parameters are:").toString());
		            		ccs.sendMessage(new StringBuilder(pre).append(" help - this command").toString());
		            		ccs.sendMessage(new StringBuilder(pre).append(" list - lists all the whitelisted words").toString());
		            		ccs.sendMessage(new StringBuilder(pre).append(" add word - add an allowed word to the whitelist").toString());
		            		ccs.sendMessage(new StringBuilder(pre).append(" delete [word] - delete a word from the whitelist").toString());
	        			}
		            	return true;
	            	}
	            	if(param.equalsIgnoreCase("blacklist"))
		            {

						if(args.length == 1)
						{
							ccs.sendMessage(new StringBuilder(pre).append(" /mutenizer blacklist help").toString());
							return true;
						}
						
						if(!args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("delete") && !args[1].equalsIgnoreCase("help"))
						{
							ccs.sendMessage(new StringBuilder().append(RED).append(" ").append(args[1]).append(" is not a valid parameter /mutenizer blacklist help").toString());			            		
							return true;
						}		            			

						if(args[1].equalsIgnoreCase("list"))
						{

							String thisWord;
							String replaceAppend;
							ccs.sendMessage((new StringBuilder(pre).append(" Blacklisted words:")).toString());
							for(Iterator iterator = blackList.entrySet().iterator(); iterator.hasNext(); sender.sendMessage((new StringBuilder(String.valueOf(thisWord))).append(replaceAppend).toString()))
							{
								java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
								thisWord = (String)entry.getKey();
								String thisReplace = (String)entry.getValue();
								replaceAppend = thisReplace.length() <= 0 ? "" : (new StringBuilder(":")).append(thisReplace).toString();
							}
							return true;

						}
						if(args[1].equalsIgnoreCase("add"))
						{

							if(args.length > 2)
							{
								String blackWord = "";
								if(args.length > 3)
								{
									if(args.length > 4)
									{
										ccs.sendMessage(new StringBuilder(pre).append("Too many arguments - /mutenizer blacklist help").toString());
									}else
									{
										StringBuilder sb = new StringBuilder();
										for(int i = 2; i < args.length; i++)
										{
											sb.append(args[i]);
											if(i < args.length - 1)
											{
												sb.append(":");
											}
										}
										blackWord = sb.toString().toLowerCase();
									}
								} else
								{
									blackWord = args[2].toLowerCase();
								}
								if(plugin.addBlackWord(blackWord))
								{
									ccs.sendMessage(new StringBuilder(pre).append(" ").append(blackWord).append(" was added to the blacklist.").toString());
								} else
								{
									ccs.sendMessage(new StringBuilder(pre).append(" ").append(blackWord).append(" is already in the blacklist.").toString());
								}
								return true;
							}

						}
						if(args[1].equalsIgnoreCase("delete"))
						{

							if(args.length > 2)
							{
								
								String blackWord = "";
								if(args.length > 3)
								{
									if(args.length > 4)
									{
										ccs.sendMessage(new StringBuilder(pre).append("Too many arguments - /mutenizer blacklist help").toString());
										return true;
									}else
									{
										StringBuilder sb = new StringBuilder();
										for(int i = 2; i < args.length; i++)
										{
											sb.append(args[i]);
											if(i < args.length - 1)
											{
												sb.append(":");
											}
										}
										blackWord = sb.toString().toLowerCase();
									}
								}else
								{
									blackWord = args[2].toLowerCase();
								}
								if(plugin.delBlackWord(blackWord))
								{
									ccs.sendMessage(new StringBuilder(pre).append(" ").append(blackWord).append(" was deleted from the blacklist.").toString());
								} else
								{
									ccs.sendMessage(new StringBuilder(pre).append(" ").append(blackWord).append(" is not in the blacklist.").toString());
								}
								return true;
							}

						}
						if(args[1].equalsIgnoreCase("help"))
						{
							ccs.sendMessage(new StringBuilder(pre).append(" /mutenizer blacklist parameters are:").toString());
							ccs.sendMessage(new StringBuilder(pre2).append("help - this command").toString());
							ccs.sendMessage(new StringBuilder(pre2).append("add:").toString());
							ccs.sendMessage(new StringBuilder(pre2).append("    - to add a banned word to the blacklist use add [word]").toString());
							ccs.sendMessage(new StringBuilder(pre2).append("    - to add a replacement use add [wordtoreplace] [wordtoreplacewith]").toString());
							ccs.sendMessage(new StringBuilder(pre2).append("#see config for formatting styles#").toString());
							ccs.sendMessage(new StringBuilder(pre2).append("delete:").toString());
							ccs.sendMessage(new StringBuilder(pre2).append("       - to delete a single word delete [word]").toString());	
							ccs.sendMessage(new StringBuilder(pre2).append("       - to delete a word and its replacement delete [replacedword] [replacingword]").toString());
							ccs.sendMessage(new StringBuilder(pre2).append("list - lists all the blacklisted words").toString());
						}
						return true;
	        		}

		            if(param.equalsIgnoreCase("reset"))
		            {	   
						if(args.length < 2)
						{
							ccs.sendMessage(new StringBuilder(pre).append(" Not enough arguments /mutenizer reset <player>").toString());
						}
						else
						if(args.length > 2)
						{
							ccs.sendMessage(new StringBuilder(pre).append(" Too many arguments /mutenizer reset <player>").toString());
						}
						else
						if(args.length == 2)
						{
							String uName = args[1];
							if(plugin.getConfig().getString(new StringBuilder("Warnings.Warned.").append(uName.toLowerCase()).toString()) == null)
							{
								ccs.sendMessage(new StringBuilder(pre).append(" ").append(uName.toUpperCase()).append(" does not exist. Check config if you believe this is a mistake.").toString());
							}else
							{
								plugin.resetWarn(uName);
								ccs.sendMessage(new StringBuilder(pre).append(" ").append(uName.toUpperCase()).append(" has had their warnings reset to ").append(plugin.getTotWarn()).toString());
							}
						}
						return true;
	        		}
	            	if(param.equalsIgnoreCase("info"))
	            	{
	            		ccs.sendMessage(new StringBuilder(pre).append("\nVersion: 2.1\nAuthor: Hatixon\n").toString());
	            		return true;
	            	}
		            if(param.equalsIgnoreCase("instaban"))
	            	{
	            		if(args.length < 2)
	            		{
	            			ccs.sendMessage(new StringBuilder(pre).append(" /mutenizer instaban help").toString());
	            			return true;
	            		}
	            		
	            		if(!args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("delete")&& !args[1].equalsIgnoreCase("help"))
	            		{
	            			ccs.sendMessage(new StringBuilder().append(RED).append(" ").append(args[1]).append(" is not a valid parameter /mutenizer instaban help").toString());
	            			return true;
	            		}
	            		
	            		if(args[1].equalsIgnoreCase("list"))
		            	{

		        	            String thisWord;
		        	            String replaceAppend;
		        	            ccs.sendMessage((new StringBuilder(pre).append(" Instaban words:")).toString());
		            			for(Iterator iterator = instaBanList.entrySet().iterator(); iterator.hasNext(); sender.sendMessage((new StringBuilder(String.valueOf(thisWord))).append(replaceAppend).toString()))
		        	            {
		        	                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
		        	                thisWord = (String)entry.getKey();
		        	                String thisReplace = (String)entry.getValue();
		        	                replaceAppend = thisReplace.length() <= 0 ? "" : (new StringBuilder(":")).append(thisReplace).toString();
		        	            }
		            			return true;

	            		}
	            		

		            	if(args[1].equalsIgnoreCase("add"))
		            	{

			            		if(args.length > 3)
			            		{
			            			ccs.sendMessage(new StringBuilder(pre).append(" Too many arguments!").toString());
			            			return true;
			            		}
		            			if(args.length > 2)
		                        {
		                            String banWord = args[2].toLowerCase();
		                            if(plugin.addBanWord(banWord))
		                            {
		                                ccs.sendMessage(new StringBuilder(pre).append(" ").append(banWord).append(" was added to the instaban.").toString());
		                            } else
		                            {
		                                ccs.sendMessage(new StringBuilder(pre).append(" ").append(banWord).append(" was already in the instaban list.").toString());
		                            }
		                            return true;
		                        }else
		                        if(args.length < 3)
			            		{
			            			ccs.sendMessage(new StringBuilder(pre).append(" Not enough arguments /mutenizer instaban add <word>").toString());
			            			return true;
			            		}

		            	}
		            	if(args[1].equalsIgnoreCase("delete"))
		            	{
	
			            		if(args.length > 3)
			            		{
			            			ccs.sendMessage(new StringBuilder(pre).append(" Too many arguments!").toString());
			            			return true;
			            		}else
		                        if(args.length > 2)
		                        {
		                            String banWord = args[2].toLowerCase();
		                            if(plugin.delBanWord(banWord))
		                            {
		                                ccs.sendMessage(new StringBuilder(pre).append(" ").append(banWord).append(" was deleted from the instaban list.").toString());
		                            } else
		                            {
		                                ccs.sendMessage(new StringBuilder(pre).append(" ").append(banWord).append(" is not in the instaban list.").toString());
		                            }
		                            return true;
		                            
		                        }else
		                        	
		                        if(args.length < 3)
			            		{
			            			ccs.sendMessage(new StringBuilder(pre).append(" Not enough arguments /mutenizer instaban delete <words>").toString());
			            			return true;
			            		}
		            		
	            		}
		            	if(args[1].equalsIgnoreCase("help"))
		            	{
		            		ccs.sendMessage(new StringBuilder(pre).append(" /mutenizer instaban list parameters are:").toString());
		            		ccs.sendMessage(new StringBuilder(pre).append(" help - this command").toString());
		            		ccs.sendMessage(new StringBuilder(pre).append(" list - lists all the instaban listed instaban list").toString());
		            		ccs.sendMessage(new StringBuilder(pre).append(" delete [word] - delete a word from the instaban list").toString());
	        			}
		            	return true;
	            	}
		            if(param.equalsIgnoreCase("help"))
		            {
		            	ccs.sendMessage(new StringBuilder(pre).append(" List of mutenizer's available commands:").toString());
		            	ccs.sendMessage(new StringBuilder().append(RED).append("/mutenizer whitelist - use /mutenizer whitelist help for more").toString());
		            	ccs.sendMessage(new StringBuilder().append(RED).append("/mutenizer blacklist - use /mutenizer blacklist help for more").toString());
		            	ccs.sendMessage(new StringBuilder().append(RED).append("/mutenizer instaban - use /mutenizer instaban help for more").toString());
		            	ccs.sendMessage(new StringBuilder().append(RED).append("/mutenizer warnings [player] - checks a players warnings").toString());
		            	ccs.sendMessage(new StringBuilder().append(RED).append("/mutenizer reset [player] - resets a players warnings to default").toString());
		            	ccs.sendMessage(new StringBuilder().append(RED).append("/mutenizer help - this command").toString());
		            	return true;
		            }
				}
	        }
		}
		else
		{
			Player p = (Player)sender;
			if(p.hasPermission("mutenizer"))
			{
				if(cmd.getName().equalsIgnoreCase("mutenizer"))
		        {
					if(args.length < 1)
					{
						p.sendMessage(new StringBuilder(pre).append("/mutenizer help").toString());
						return true;
					}
		            if(args.length > 0)
		            {
		                String param = args[0].toLowerCase();
		                if(!param.equalsIgnoreCase("whitelist") && !param.equalsIgnoreCase("blacklist") && !param.equalsIgnoreCase("reset") && !param.equalsIgnoreCase("warnings") && !param.equalsIgnoreCase("help") && !param.equalsIgnoreCase("info") && !param.equalsIgnoreCase("instaban")  && !param.equalsIgnoreCase("reload"))
		                {
		                	p.sendMessage(new StringBuilder(pre).append("No such command. Use /mutenizer help").toString());
		                	return true;
		                }
		            	if(p.hasPermission("mutenizer"))
		            	{
			            	if(param.equalsIgnoreCase("whitelist"))
			            	{
			            		if(args.length < 2)
			            		{
			            			p.sendMessage(new StringBuilder(pre).append(" /mutenizer whitelist help").toString());
			            			return true;
			            		}
			            		
			            		if(!args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("delete")&& !args[1].equalsIgnoreCase("help"))
			            		{
			            			p.sendMessage(new StringBuilder().append(RED).append(" ").append(args[1]).append(" is not a valid parameter /mutenizer whitelist help").toString());
			            			return true;
			            		}
			            		
			            		if(args[1].equalsIgnoreCase("list"))
				            	{
				            		if(p.hasPermission("mutenizer.list"))
				            		{
				        	            String thisWord;
				        	            String replaceAppend;
				        	            p.sendMessage((new StringBuilder(pre).append(" Whitelisted words:")).toString());
				            			for(Iterator iterator = whiteList.entrySet().iterator(); iterator.hasNext(); sender.sendMessage((new StringBuilder(String.valueOf(thisWord))).append(replaceAppend).toString()))
				        	            {
				        	                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
				        	                thisWord = (String)entry.getKey();
				        	                String thisReplace = (String)entry.getValue();
				        	                replaceAppend = thisReplace.length() <= 0 ? "" : (new StringBuilder(":")).append(thisReplace).toString();
				        	            }
				            			return true;
				            		}else
				            		{
				            			p.sendMessage(cantUse);
				            			return true;
				            		}
			            		}
			            		

				            	if(args[1].equalsIgnoreCase("add"))
				            	{
				            		if(p.hasPermission("mutenizer.edit"))
				            		{
					            		if(args.length > 3)
					            		{
					            			p.sendMessage(new StringBuilder(pre).append(" Too many arguments!").toString());
					            			return true;
					            		}
				            			if(args.length > 2)
				                        {
				                            String whiteWord = args[2].toLowerCase();
				                            if(plugin.addWhiteWord(whiteWord))
				                            {
				                                p.sendMessage(new StringBuilder(pre).append(" ").append(whiteWord).append(" was added to the whitelist.").toString());
				                            } else
				                            {
				                                p.sendMessage(new StringBuilder(pre).append(" ").append(whiteWord).append(" was already in the whitelist.").toString());
				                            }
				                            return true;
				                        }else
				                        if(args.length < 3)
					            		{
					            			p.sendMessage(new StringBuilder(pre).append(" Not enough arguments /mutenizer whitelist add <word>").toString());
					            			return true;
					            		}
				            		}else
				            		{
				            			p.sendMessage(cantUse);
				            			return true;
				            		}
				            	}
				            	if(args[1].equalsIgnoreCase("delete"))
				            	{
				            		if(p.hasPermission("mutenizer.edit"))
				            		{		
					            		if(args.length > 3)
					            		{
					            			p.sendMessage(new StringBuilder(pre).append(" Too many arguments!").toString());
					            			return true;
					            		}else
				                        if(args.length > 2)
				                        {
				                            String whiteWord = args[2].toLowerCase();
				                            if(plugin.delWhiteWord(whiteWord))
				                            {
				                                p.sendMessage(new StringBuilder(pre).append(" ").append(whiteWord).append(" was deleted from the whitelist.").toString());
				                            } else
				                            {
				                                p.sendMessage(new StringBuilder(pre).append(" ").append(whiteWord).append(" is not in the whitelist.").toString());
				                            }
				                            return true;
				                            
				                        }else
				                        	
				                        if(args.length < 3)
					            		{
					            			p.sendMessage(new StringBuilder(pre).append(" Not enough arguments /mutenizer whitelist delete <words>").toString());
					            			return true;
					            		}
				            		}else
				            		{
				            			p.sendMessage(cantUse);
				            			return true;
				            		}
			            		}
				            	if(args[1].equalsIgnoreCase("help"))
				            	{
				            		p.sendMessage(new StringBuilder(pre).append(" /mutenizer whitelist parameters are:").toString());
				            		p.sendMessage(new StringBuilder(pre).append(" help - this command").toString());
				            		p.sendMessage(new StringBuilder(pre).append(" list - lists all the whitelisted words").toString());
				            		p.sendMessage(new StringBuilder(pre).append(" add word - add an allowed word to the white").toString());
				            		p.sendMessage(new StringBuilder(pre).append(" delete [word] - delete a word from the whitelist").toString());
			        			}
				            	return true;
			            	}
		            	}else
		        		{
		        			p.sendMessage(cantUse);
		        			return true;
		        		}

		            	if(param.equalsIgnoreCase("blacklist"))
			            {
			        		if(p.hasPermission("mutenizer"))
			        		{
			            		if(args.length == 1)
			            		{
			            			p.sendMessage(new StringBuilder(pre).append(" /mutenizer blacklist help").toString());
			            			return true;
			            		}
			            		
			            		if(!args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("delete") && !args[1].equalsIgnoreCase("help"))
			            		{
			            			p.sendMessage(new StringBuilder().append(RED).append(" ").append(args[1]).append(" is not a valid parameter /mutenizer blacklist help").toString());			            		
			            			return true;
			            		}		            			

				            	if(args[1].equalsIgnoreCase("list"))
				            	{
				            		if(p.hasPermission("mutenizer.list"))
				            		{
				        	            String thisWord;
				        	            String replaceAppend;
				        	            p.sendMessage((new StringBuilder(pre).append(" Blacklisted words:")).toString());
				            			for(Iterator iterator = blackList.entrySet().iterator(); iterator.hasNext(); sender.sendMessage((new StringBuilder(String.valueOf(thisWord))).append(replaceAppend).toString()))
				        	            {
				        	                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
				        	                thisWord = (String)entry.getKey();
				        	                String thisReplace = (String)entry.getValue();
				        	                replaceAppend = thisReplace.length() <= 0 ? "" : (new StringBuilder(":")).append(thisReplace).toString();
				        	            }
				            			return true;
				            		}else
				            		{
				            			p.sendMessage(cantUse);
				            			return true;
				            		}
			            		}
				            	if(args[1].equalsIgnoreCase("add"))
				                {
				            		if(p.hasPermission("mutenizer.edit"))
				            		{
				                        if(args.length > 2)
				                        {
				                            String blackWord = "";
				                            if(args.length > 3)
				                            {
				                            	if(args.length > 4)
				                            	{
				                            		p.sendMessage(new StringBuilder(pre).append("Too many arguments - /mutenizer blacklist help").toString());
				                            	}else
				                            	{
					                                StringBuilder sb = new StringBuilder();
					                                for(int i = 2; i < args.length; i++)
					                                {
					                                    sb.append(args[i]);
					                                    if(i < args.length - 1)
					                                    {
					                                        sb.append(":");
					                                    }
					                                }
					                                blackWord = sb.toString().toLowerCase();
				                            	}
				                            } else
				                            {
				                                blackWord = args[2].toLowerCase();
				                            }
				                            if(plugin.addBlackWord(blackWord))
				                            {
				                                p.sendMessage(new StringBuilder(pre).append(" ").append(blackWord).append(" was added to the blacklist.").toString());
				                            } else
				                            {
				                                p.sendMessage(new StringBuilder(pre).append(" ").append(blackWord).append(" is already in the blacklist.").toString());
				                            }
				                            return true;
				                        }
				                    }
				            		else
				            		{
				            			p.sendMessage(cantUse);
				            			return true;
				            		}
				                }
				            	if(args[1].equalsIgnoreCase("delete"))
				            	{
				            		if(p.hasPermission("mutenizer.edit"))
					            	{
				            			if(args.length > 2)
				                        {
				            				
				                            String blackWord = "";
				                            if(args.length > 3)
				                            {
				                            	if(args.length > 4)
				                            	{
				                            		p.sendMessage(new StringBuilder(pre).append("Too many arguments - /mutenizer blacklist help").toString());
				                            		return true;
				                            	}else
				                            	{
					                                StringBuilder sb = new StringBuilder();
					                                for(int i = 2; i < args.length; i++)
					                                {
					                                    sb.append(args[i]);
					                                    if(i < args.length - 1)
					                                    {
					                                        sb.append(":");
					                                    }
					                                }
					                                blackWord = sb.toString().toLowerCase();
				                            	}
				                            }else
				                            {
				                            	blackWord = args[2].toLowerCase();
				                            }
				                            if(plugin.delBlackWord(blackWord))
				                            {
				                                p.sendMessage(new StringBuilder(pre).append(" ").append(blackWord).append(" was deleted from the blacklist.").toString());
				                            } else
				                            {
				                                p.sendMessage(new StringBuilder(pre).append(" ").append(blackWord).append(" is not in the blacklist.").toString());
				                            }
				                            return true;
			                        	}
			            			}else
				            		{
				            			p.sendMessage(cantUse);
				            			return true;
				            		}
				            	}
				            	if(args[1].equalsIgnoreCase("help"))
				            	{
				            		p.sendMessage(new StringBuilder(pre).append(" /mutenizer blacklist parameters are:").toString());
				            		p.sendMessage(new StringBuilder(pre2).append("help - this command").toString());
				            		p.sendMessage(new StringBuilder(pre2).append("add:").toString());
				            		p.sendMessage(new StringBuilder(pre2).append("    - to add a banned word to the blacklist use add [word]").toString());
				            		p.sendMessage(new StringBuilder(pre2).append("    - to add a replacement use add [wordtoreplace] [wordtoreplacewith]").toString());
				            		p.sendMessage(new StringBuilder(pre2).append("#see config for formatting styles#").toString());
				            		p.sendMessage(new StringBuilder(pre2).append("delete:").toString());
				            		p.sendMessage(new StringBuilder(pre2).append("       - to delete a single word delete [word]").toString());	
				            		p.sendMessage(new StringBuilder(pre2).append("       - to delete a word and its replacement delete [replacedword] [replacingword]").toString());
				            		p.sendMessage(new StringBuilder(pre2).append("list - lists all the blacklisted words").toString());
			        			}
			        		}else
			        		{
			        			p.sendMessage(cantUse);
			        			return true;
			        		}
			        		return true;
		        		}

			            if(param.equalsIgnoreCase("reset"))
			            {	    
			        		if(p.hasPermission("mutenizer.reset"))
			        		{
			            		if(args.length < 2)
			            		{
			            			p.sendMessage(new StringBuilder(pre).append(" Not enough arguments /mutenizer reset <player>").toString());
			            		}
			            		else
			            		if(args.length > 2)
			            		{
			            			p.sendMessage(new StringBuilder(pre).append(" Too many arguments /mutenizer reset <player>").toString());
			            		}
			            		else
			            		if(args.length == 2)
			            		{
			            			String uName = args[1];
			            			if(plugin.getConfig().getString(new StringBuilder("Warnings.Warned.").append(uName.toLowerCase()).toString()) == null)
			            			{
			            				p.sendMessage(new StringBuilder(pre).append(" ").append(uName.toUpperCase()).append(" does not exist. Check config if you believe this is a mistake.").toString());
			            			}else
			            			{
			            				plugin.resetWarn(uName);
				            			p.sendMessage(new StringBuilder(pre).append(" ").append(uName.toUpperCase()).append(" has had their warnings reset to ").append(plugin.getTotWarn()).toString());
			            			}
			            		}
			            		return true;
			            	}else
			        		{
			        			p.sendMessage(cantUse);
			        			return true;
			        		}
		        		}
			            
		            	if(param.equalsIgnoreCase("reload"))
		            	{	
		            		if(p.hasPermission("mutenizer.reload"))
		            		{
		            			plugin.getServer().getPluginManager().disablePlugin(plugin);
		            			plugin.getServer().getPluginManager().enablePlugin(plugin);
		            			p.sendMessage(new StringBuilder(pre).append(" Successfully reloaded!").toString());
		            		}
		            	}
			            
		            	if(param.equalsIgnoreCase("warnings"))
		            	{	
			        		if(p.hasPermission("mutenizer.warnings"))
			        		{
			            		String uName = "";
			            		int remWarn;
			            		if(args.length == 1)
			            		{
			            			uName = p.getName().toLowerCase();
			            			remWarn = plugin.getRemWarn(uName);
			            			p.sendMessage(new StringBuilder(pre).append(" You have ").append(remWarn).append(" warning(s) left.").toString());
			            			return true;
			            		}
			            		if(args.length > 1)
			            		{
			            			uName = args[1].toLowerCase();
			            			String uNameMatch = p.getName().toLowerCase();
			            			if(uName.equalsIgnoreCase(uNameMatch))
			            			{
				            			remWarn = plugin.getRemWarn(uName);
				            			p.sendMessage(new StringBuilder(pre).append(" You have ").append(remWarn).append(" warning(s) left.").toString());
			            			}else
			            			{
			            				if(p.hasPermission("mutenizer.warnings.others"))
			            				{
					            			remWarn = plugin.getRemWarn(uName.toLowerCase());
					            			if(remWarn != 0)
					            			{
					            				p.sendMessage(new StringBuilder(pre).append(" ").append(uName.toUpperCase()).append(" has ").append(remWarn).append(" warning(s) left.").toString());
					            			}
					            			else
					            			{
					            				p.sendMessage(new StringBuilder(pre).append(" ").append(uName.toUpperCase()).append(" does not exist. Make sure you spelt the name right.").toString());
					            			}
			            				}else
			            				{
			            					p.sendMessage(new StringBuilder(pre).append(" You do not have the permission to view another users warnings").toString());
			            				}
			            			}
			            			return true;
			            		}
			            	}else
			        		{
			        			p.sendMessage(cantUse);
			        			return true;
			        		}
			        		return true;
		        		}
		            	if(param.equalsIgnoreCase("info"))
		            	{
		            		p.sendMessage(new StringBuilder(pre).append("\nVersion: 2.1\nAuthor: Hatixon\n").toString());
		            		return true;
		            	}
			            if(param.equalsIgnoreCase("instaban"))
		            	{
		            		if(args.length < 2)
		            		{
		            			p.sendMessage(new StringBuilder(pre).append(" /mutenizer instaban help").toString());
		            			return true;
		            		}
		            		
		            		if(!args[1].equalsIgnoreCase("list") && !args[1].equalsIgnoreCase("add") && !args[1].equalsIgnoreCase("delete")&& !args[1].equalsIgnoreCase("help"))
		            		{
		            			p.sendMessage(new StringBuilder().append(RED).append(" ").append(args[1]).append(" is not a valid parameter /mutenizer instaban help").toString());
		            			return true;
		            		}
		            		
		            		if(args[1].equalsIgnoreCase("list"))
			            	{

			        	            String thisWord;
			        	            String replaceAppend;
			        	            p.sendMessage((new StringBuilder(pre).append(" Instaban words:")).toString());
			            			for(Iterator iterator = instaBanList.entrySet().iterator(); iterator.hasNext(); sender.sendMessage((new StringBuilder(String.valueOf(thisWord))).append(replaceAppend).toString()))
			        	            {
			        	                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
			        	                thisWord = (String)entry.getKey();
			        	                String thisReplace = (String)entry.getValue();
			        	                replaceAppend = thisReplace.length() <= 0 ? "" : (new StringBuilder(":")).append(thisReplace).toString();
			        	            }
			            			return true;

		            		}
			            	if(args[1].equalsIgnoreCase("add"))
			            	{
			            		if(p.hasPermission("mutenizer.edit"))
			            		{
				            		if(args.length > 3)
				            		{
				            			p.sendMessage(new StringBuilder(pre).append(" Too many arguments!").toString());
				            			return true;
				            		}
			            			if(args.length > 2)
			                        {
			                            String banWord = args[2].toLowerCase();
			                            if(plugin.addBanWord(banWord))
			                            {
			                                p.sendMessage(new StringBuilder(pre).append(" ").append(banWord).append(" was added to the instaban.").toString());
			                            } else
			                            {
			                                p.sendMessage(new StringBuilder(pre).append(" ").append(banWord).append(" was already in the instaban list.").toString());
			                            }
			                            return true;
			                        }else
			                        if(args.length < 3)
				            		{
				            			p.sendMessage(new StringBuilder(pre).append(" Not enough arguments /mutenizer instaban add <word>").toString());
				            			return true;
				            		}
			            		}else
			            		{
			            			p.sendMessage(cantUse);
			            		}
			            	}
			            	if(args[1].equalsIgnoreCase("delete"))
			            	{
			            		if(p.hasPermission("mutenizer.edit"))
			            		{
				            		if(args.length > 3)
				            		{
				            			p.sendMessage(new StringBuilder(pre).append(" Too many arguments!").toString());
				            			return true;
				            		}else
			                        if(args.length > 2)
			                        {
			                            String banWord = args[2].toLowerCase();
			                            if(plugin.delBanWord(banWord))
			                            {
			                                p.sendMessage(new StringBuilder(pre).append(" ").append(banWord).append(" was deleted from the instaban list.").toString());
			                            } else
			                            {
			                                p.sendMessage(new StringBuilder(pre).append(" ").append(banWord).append(" is not in the instaban list.").toString());
			                            }
			                            return true;
			                            
			                        }else
			                        	
			                        if(args.length < 3)
				            		{
				            			p.sendMessage(new StringBuilder(pre).append(" Not enough arguments /mutenizer instaban delete <words>").toString());
				            			return true;
				            		}
			            		}else
			            		{
			            			p.sendMessage(cantUse);
			            		}
			            		
		            		}
			            	if(args[1].equalsIgnoreCase("help"))
			            	{
			            		p.sendMessage(new StringBuilder(pre).append(" /mutenizer instaban list parameters are:").toString());
			            		p.sendMessage(new StringBuilder(pre).append(" help - this command").toString());
			            		p.sendMessage(new StringBuilder(pre).append(" list - lists all the instaban listed instaban list").toString());
			            		p.sendMessage(new StringBuilder(pre).append(" delete [word] - delete a word from the instaban list").toString());
		        			}
			            	return true;
		            	}
			            if(param.equalsIgnoreCase("help"))
			            {
			            	p.sendMessage(new StringBuilder(pre).append(" List of mutenizer's available commands:").toString());
			            	p.sendMessage(new StringBuilder().append(RED).append("/mutenizer whitelist - use /mutenizer whitelist help for more").toString());
			            	p.sendMessage(new StringBuilder().append(RED).append("/mutenizer blacklist - use /mutenizer blacklist help for more").toString());
			            	p.sendMessage(new StringBuilder().append(RED).append("/mutenizer warnings - checks your own warnings").toString());
			            	p.sendMessage(new StringBuilder().append(RED).append("/mutenizer warnings [player] - checks a players warnings including your own").toString());
			            	p.sendMessage(new StringBuilder().append(RED).append("/mutenizer reset [player] - resets a players warnings to default").toString());
			            	p.sendMessage(new StringBuilder().append(RED).append("/mutenizer help - this command").toString());
			            	return true;
			            }
		            }
		        }
			}
		}
		return true;
	}	
}
