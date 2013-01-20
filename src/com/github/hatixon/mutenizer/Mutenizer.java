package com.github.hatixon.mutenizer;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipException;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.*;
import org.bukkit.potion.*;

@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public class Mutenizer extends JavaPlugin
{
	public static Mutenizer plugin;
    public final Logger logger = Logger.getLogger("Minecraft");
	public final ServerChatPlayerListener playerChatListener = new ServerChatPlayerListener(this);
	public final CapsCheck playerCapsCheck = new CapsCheck(this);
	public final CommandListener commandCheck = new CommandListener(this);
	public final SignPlaceListener signPlace = new SignPlaceListener(this);
	
	private FileConfiguration usersConfig = null;
	private File usersConfigFile = null;
	
	private FileConfiguration whiteConfig = null;
	private File whiteFile = null;
	
	private FileConfiguration blackConfig = null;
	private File blackFile = null;
	
	private FileConfiguration instantConfig = null;
	private File instantFile = null;
	
	private Map blackWordList;
	 
	private Map whiteWordList;
	
	private Map instaBanList;
	
	private Map userList;                  
	
	public Map getUserMap()
	{
		return userList;
	}
	
	public void getUserList()
	{
		List blackW = getUserConfig().getStringList("UserList");
		for(Iterator i = blackW.iterator(); i.hasNext();)
		{
            String thisLine = (String)i.next();
            userList.put(thisLine, "");
		}
	}
	
	public void reloadUserConfig()
	{
		if(usersConfig == null)
		{
			usersConfigFile = new File(getDataFolder(), "users.yml");
		}
		usersConfig = YamlConfiguration.loadConfiguration(usersConfigFile);
		
	    InputStream defConfigStream = this.getResource("users.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        usersConfig.setDefaults(defConfig);
	    }
	}
	
	public void reloadWhiteConfig()
	{
		if(whiteConfig == null)
		{
			whiteFile = new File(getDataFolder(), "whitelist.yml");
		}
		whiteConfig = YamlConfiguration.loadConfiguration(whiteFile);
		
	    InputStream defConfigStream = this.getResource("whitelist.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        whiteConfig.setDefaults(defConfig);
	    }
	}

	public void reloadBlackConfig()
	{
		if(blackConfig == null)
		{
			blackFile = new File(getDataFolder(), "blacklist.yml");
		}
		blackConfig = YamlConfiguration.loadConfiguration(blackFile);
		
	    InputStream defConfigStream = this.getResource("blacklist.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        blackConfig.setDefaults(defConfig);
	    }
	}
	
	public void reloadInstantConfig()
	{
		if(instantConfig == null)
		{
			instantFile = new File(getDataFolder(), "instaban.yml");
		}
		instantConfig = YamlConfiguration.loadConfiguration(instantFile);
		
	    InputStream defConfigStream = this.getResource("instaban.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        instantConfig.setDefaults(defConfig);
	    }
	}
	
	public void addUserL(String uName)
	{
        userList.put(uName, "");
        saveUserList();
	}
	
	public void saveUserList()
	{
		
        ArrayList userListA = new ArrayList();
        String printLine;
        for(Iterator iterator = userList.entrySet().iterator(); iterator.hasNext(); userListA.add(printLine))
        {
        	Map.Entry entry = (Map.Entry)iterator.next();
            String thisEntry = (String)entry.getKey();
            String thisReplacementment = (String)entry.getValue();
            printLine = thisReplacementment.length() <= 0 ? thisEntry : (new StringBuilder(String.valueOf(thisEntry))).append(":").append(thisReplacementment).toString();
        }
        getUserConfig().set("UserList", ((Object) (userListA.toArray())));
        saveUserConfig();
	}
	
    public FileConfiguration getUserConfig() {
        if (usersConfig == null) {
            this.reloadUserConfig();
        }
        return usersConfig;
    }
    
    public void saveUserConfig() {
        if (usersConfig == null || usersConfigFile == null) {
        return;
        }
        try {
            getUserConfig().save(usersConfigFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + usersConfigFile, ex);
        }
    }
    
	
    public FileConfiguration getWhiteConfig() {
        if (whiteConfig == null) {
            this.reloadWhiteConfig();
        }
        return whiteConfig;
    }
    
    public void saveWhiteConfig() {
        if (whiteConfig == null || whiteFile == null) {
        return;
        }
        try {
            getWhiteConfig().save(whiteFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + whiteFile, ex);        }
    }
    
	
    public FileConfiguration getBlackConfig() {
        if (blackConfig == null) {
            this.reloadBlackConfig();
        }
        return blackConfig;
    }
    
    public void saveBlackConfig() {
        if (blackConfig == null || blackFile == null) {
        return;
        }
        try {
            getBlackConfig().save(blackFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + blackFile, ex);
        }
    }
    
	
    public FileConfiguration getInstantConfig() {
        if (instantConfig == null) {
            this.reloadInstantConfig();
        }
        return instantConfig;
    }
    
    public void saveInstaConfig() {
        if (instantConfig == null || instantFile == null) {
        return;
        }
        try {
            getInstantConfig().save(instantFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + instantFile, ex);
        }
    }
	
	public void executeMoneyRemoval(String uName)
	{
		getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder().append("eco take ").append(uName).append(" ").append(getMonPen()).toString());
	}

	public Integer getMonPen()
	{
		return getConfig().getInt("Money.Penalty");
	}
	
	public boolean getMoneyEnabled()
	{
		return getConfig().getBoolean("Money.Enabled");
	}
	
	public Map getWhiteMap()
	{
		return whiteWordList;
		
	}
	
	public Map getBlackMap()
	{
		return blackWordList;
	}
	
	public Map getBanList()
	{
		return instaBanList;
	}
	
	public void onDisable()
	{
        PluginDescriptionFile pdffile = getDescription();
		String pre = (new StringBuilder().append("[Mutenizer] ")).toString();
		logger.info(new StringBuilder(pre).append("Mutenizer v").append(pdffile.getVersion()).append(" has been disabled!").toString());
	}
	
	public void onEnable()
	{
		String pre = (new StringBuilder().append("[Mutenizer] ")).toString();
        PluginDescriptionFile pdffile = getDescription();
		loadConfiguration();
		logger.info(new StringBuilder(pre).append("Mutenizer v").append(pdffile.getVersion()).append(" has been enabled!").toString());
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerChatListener, this);
		pm.registerEvents(playerCapsCheck, this);
		pm.registerEvents(commandCheck, this);
		pm.registerEvents(signPlace, this);
        getCommand("mutenizer").setExecutor(new ServerCommandExecutor(this));
		getBlackList();
		getWhiteList();
		getInstaBan();
		getUserList();
		getApetureSymbol();
		moveFileFromJar("readme.txt", "plugins/Mutenizer/readmev2.1.1.txt", Boolean.valueOf(true));
	}
	
	public String getMessageInstaBan()
	{
		return getConfig().getString("Message.InstaBan");
	}
	
	public void instaBanPlayer(Player player)
	{
		String uName = player.getName();
		String banCom = getConfig().getString("InstaBanCommand");
		String comSplit[] = banCom.split(" ", 3);
		if(comSplit.length == 1)
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(getMessageInstaBan()).toString());
		}else
		if(comSplit.length == 2)
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(comSplit[1]).append(getMessageInstaBan()).toString());
		}else
		if(comSplit.length == 3)
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(comSplit[1]).append(" ").append(comSplit[2]).append(" ").append(getMessageInstaBan()).toString());
		}
	}
	
	public String getMessageCapsBan()
	{
		return getConfig().getString("Message.Caps.Ban");
	}
	
	public void capsBunnyRabbit(Player player)
	{
		String uName = player.getName();
		String banCom = getConfig().getString("BanCommand");
		String comSplit[] = banCom.split(" ", 3);
		if(comSplit.length == 1)
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(getMessageCapsBan()).toString());
		}else
		if(comSplit.length == 2)
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(comSplit[1]).append(" ").append(getMessageCapsBan()).toString());
		}else
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(comSplit[1]).append(" ").append(comSplit[2]).append(getMessageCapsBan()).toString());
		}
	}
	
	public String getMessageCaps()
	{
		return getConfig().getString("Message.Caps.Warn");
	}
	
    public void moveFileFromJar(String jarFileName, String targetLocation, Boolean overwrite)
    {
		String pre = (new StringBuilder().append("[Mutenizer]")).toString();
        try
        {
            File targetFile = new File(targetLocation);
            if(overwrite.booleanValue() || targetFile.length() == 0L)
            {
                InputStream inFile = getClass().getClassLoader().getResourceAsStream(jarFileName);
                FileWriter outFile = new FileWriter(targetFile);
                int c;
                while((c = inFile.read()) != -1) 
                {
                    outFile.write(c);
                }
                inFile.close();
                outFile.close();
            }
        }
        catch(NullPointerException e)
        {
            logger.info(new StringBuilder("Failed to create ").append(jarFileName).append(".").toString());
        }
        catch(ZipException e)
        {
        	logger.info(new StringBuilder(pre).append(" Failed to create README.txt. Please delete the current one in /plugins/Mutenizer/ to remove this error!").toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean getCapsPunishment()
    {
    	return getConfig().getBoolean("Caps.Enforced");
    }
	
	public void bunnyRabbit(Player player)
	{
		String uName = player.getName();
		String banCom = getConfig().getString("BanCommand");
		String comSplit[] = banCom.split(" ", 3);
		if(comSplit.length == 1)
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(getMessageBanned()).toString());
		}else
		if(comSplit.length == 2)
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(comSplit[1]).append(getMessageBanned()).toString());
		}else
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(comSplit[1]).append(" ").append(comSplit[2]).append(getMessageBanned()).toString());
		}
	}
	
	private void getApetureSymbol() {
		logger.info("\n" +
		"             .,-:;//;:=,\n" +
		"          . :H@@@MM@M#H/.,+%;,\n" +
		"       ,/X+ +M@@M@MM%=,-%HMMM@X/,\n" +
		"     -+@MM; $M@@MH+-,;XMMMM@MMMM@+-\n" +
		"    ;@M@@M- XM@X;. -+XXXXXHHH@M@M#@/.\n"+
		"  ,%MM@@MH ,@%=             ._________.\n"+
		"  =@#@@@MX.,                -%HX$$%%%:;\n"+
		" =-./@M@M$                   .;@MMMM@MM:\n"+
		" X@/ -$MM/                    . +MM@@@M$\n"+
		",@M@H: :@:                    . =X#@@@@-\n"+
		",@@@MMX, .                    /H- ;@M@M=\n"+
		".H@@@@M@+,                    %MM+..%#$.\n"+
		" /MMMM@MMH/.                  XM@MH; =;\n"+
		"  /%+%$XHH@$=              , .H@@@@MX,\n"+
		"   ._________.           -%H.,@@@@@MX,\n"+
		"   .%MM@@@HHHXX$$$%+- .:$MMX =M@@MM%.\n"+
		"     =XMMM@MM@MM#H;,-+HMM@M+ /MMMX=\n"+
		"       =%@M@M#@$-.=$@MM@@@M; %M%=\n"+
		"         ,:+$+-,/H#MMMMMMM@= =,\n"+
		"               =++%%%%+/:-.\n");
	}

	public void loadConfiguration()
	{
		this.getConfig().options().copyDefaults(true);
		this.getUserConfig().options().copyDefaults(true);
		this.getWhiteConfig().options().copyDefaults(true);
		this.getBlackConfig().options().copyDefaults(true);
		this.getInstantConfig().options().copyDefaults(true);
		saveDefaultConfig();
		saveUserConfig();
		saveWhiteConfig();
		saveBlackConfig();
		saveInstaConfig();
	}
	
	 
	public Mutenizer()
	{
		blackWordList = new HashMap();
		whiteWordList = new HashMap();
		instaBanList = new HashMap();
		userList = new HashMap();
	}
	
	public String comPact(String message)
	{
        for(Iterator iterator = blackWordList.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String thisEntry = (String)entry.getKey();
            String thisNewEntry = "";
            if(thisEntry.indexOf("\\w*") > 0)
            {
            	thisNewEntry = thisEntry.replace("\\w*", "");
            }
            String thisRegex = (new StringBuilder("\\b")).append(thisNewEntry).append("\\b").toString();
            Pattern patt = Pattern.compile(thisRegex, 2);
            Matcher m = patt.matcher(message);
            if(m.find())
            {
                    message = message.replace(thisRegex, "");
            }
        }
        return message;
	}
	
	public double getCapsPercent()
	{
		int percentA = getConfig().getInt("Caps.Percentage");
		double percentB = (double)percentA / 100;
		return percentB;
	}
	
	public boolean getNotifyOp()
	{
		return getConfig().getBoolean("Notify.Op");
	}
	public boolean getResetOnBan()
	{
		return getConfig().getBoolean("ResetWarningsOnBan");
	}
	
	public Integer getRemWarn(String uName)
	{
		return getConfig().getInt((new StringBuilder().append("Warnings.Warned.").append((uName).toLowerCase())).toString());
	}
	
	public String getMessageKick()
	{
		return getConfig().getString("Message.Swear.Kick");
	}
	
	public String getMessageBanned()
	{
		return getConfig().getString("Message.Swear.Ban");	
	}
	
	public void resetBanned(String uName)
	{
		getConfig().set(((new StringBuilder().append("Warnings.Warned.").append(uName.toLowerCase())).toString()), getTotWarn());
	}
	
	public Integer getTotWarn()
	{
		return getConfig().getInt("TotalWarnings");		
	}
	
    public void setRemWarn(String uName, Integer remWarn)
    {
        getConfig().set((new StringBuilder("Warnings.Warned.")).append(uName.toLowerCase()).toString(), remWarn);
        saveConfig();
    }
    
    public Integer getWarnBKick()
    {
    	return getConfig().getInt("RemainingWarningsBeforeKick");
    }
    
	public void getBlackList()
	{
		List blackW = getBlackConfig().getStringList("Blacklist");
		for(Iterator i = blackW.iterator(); i.hasNext();)
		{
            String thisLine = (String)i.next();
            if(thisLine.indexOf(":") > 0)
            {
                String thisSplit[] = thisLine.split(":", 2);
                blackWordList.put(thisSplit[0], thisSplit[1]);
            } else
            {
                blackWordList.put(thisLine, "");
            }
		}
	}
	
	public void getInstaBan()
	{
		List banW = getInstantConfig().getStringList("InstaBanList");
		for(Iterator i = banW.iterator(); i.hasNext();) 
		{
            String thisLine = (String)i.next();
            if(thisLine.indexOf(":") > 0)
            {
                String thisSplit[] = thisLine.split(":", 2);
                instaBanList.put(thisSplit[0], thisSplit[1]);
            } else
            {
                instaBanList.put(thisLine, "");
            }
		}
	}
	
	public void setWarnJoin(String uName)
	{
		getConfig().set((new StringBuilder("Warnings.Warned.")).append(uName.toLowerCase()).toString(), getTotWarn());
		saveConfig();
	}
	
	public String getJoinMessage()
	{
		return getConfig().getString("Message.FirstJoin");
	}
	
	public void getWhiteList()
	{
		List whiteW = getWhiteConfig().getStringList("Whitelist");
		for(Iterator i = whiteW.iterator(); i.hasNext();)
		{
            String thisLine = (String)i.next();
            if(thisLine.indexOf(":") > 0)
            {
                String thisSplit[] = thisLine.split(":", 2);
                whiteWordList.put(thisSplit[0], thisSplit[1]);
            } else
            {
                whiteWordList.put(thisLine, "");
            }
		}
	}
	
	public void potionEffect(Player player)
	{
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 6000, 1));
	}
	 
	public String censorCheck(String message)
    {
        boolean censored = false;
        boolean uncensored = false;
        for(Iterator iterator = blackWordList.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String thisEntry = (String)entry.getKey();
            String thisValue = (String)entry.getValue();
            String thisRegex = (new StringBuilder("\\b")).append(thisEntry).append("\\b").toString();
            Pattern patt = Pattern.compile(thisRegex, 2);
            Matcher m = patt.matcher(message);
            if(m.find())
            {
                if(thisValue.length() > 0)
                {
                    message = message.replaceAll(thisRegex, thisValue);
                    censored = true;
                } else
                {
                    uncensored = true;
                }
            }
        }

        if(!censored && !uncensored || !uncensored && censored)
        {
            return message;
        } else
        {
            return "";
        }
    }
	
	 
	public boolean didTheySwear(String message)
	{
		boolean swore = false;
        for(Iterator iterator = blackWordList.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String theWord = (String)entry.getKey();
            String theRegex = (new StringBuilder("\\b")).append(theWord).append("\\b").toString();
            Pattern patt = Pattern.compile(theRegex, 2);
            Matcher m = patt.matcher(message);
            if(m.find())
            {
                swore = true;
            }
        }

        return swore;
    }
	
	public boolean getCommandCheck()
	{
		return getConfig().getBoolean("Check.Commands");
	}
	
	public boolean instaBanCheck(String message)
	{
		boolean instaBan = false;
        for(Iterator iterator = instaBanList.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String theWord = (String)entry.getKey();
            String theRegex = (new StringBuilder("\\b")).append(theWord).append("\\b").toString();
            Pattern patt = Pattern.compile(theRegex, 2);
            Matcher m = patt.matcher(message);
            if(m.find())
            {
                instaBan = true;
            }
        }

        return instaBan;
    }	
	
	public boolean getNotifyPlayer()
	{
		return getConfig().getBoolean("Notify.Player");
	}
	
	public String getMessageWarn()
	{
		return getConfig().getString("Message.Swear.Warning");
	}

	 
	public boolean isItAllowed(String message)
	{
        boolean allowed = false;
        for(Iterator iterator = whiteWordList.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String theWord = (String)entry.getKey();
            String theRegexMatcher = (new StringBuilder("\\b")).append(theWord).append("\\b").toString();
            Pattern patt = Pattern.compile(theRegexMatcher, 2);
            Matcher m = patt.matcher(message);
            if(m.find())
            {
                allowed = true;
            }
        }

        return allowed;
    }
	
	public void resetWarn(String uName)
	{
		getConfig().set((new StringBuilder("Warnings.Warned.").append(uName).toString()), getTotWarn());
		saveConfig();
	}
	
	public void saveWhiteList()
	{
        ArrayList whiteListA = new ArrayList();
        String printLine;
        for(Iterator iterator = whiteWordList.entrySet().iterator(); iterator.hasNext(); whiteListA.add(printLine))
        {
            Map.Entry entry = (Map.Entry)iterator.next();
            String thisEntry = (String)entry.getKey();
            String thisReplacementment = (String)entry.getValue();
            printLine = thisReplacementment.length() <= 0 ? thisEntry : (new StringBuilder(String.valueOf(thisEntry))).append(":").append(thisReplacementment).toString();
        }

        getWhiteConfig().set("Whitelist", ((Object) (whiteListA.toArray())));
        saveWhiteConfig();
    }
	
	public void saveBlackList()
	{
        ArrayList blackListA = new ArrayList();
        String printLine;
        for(Iterator iterator = blackWordList.entrySet().iterator(); iterator.hasNext(); blackListA.add(printLine))
        {
            Map.Entry entry = (Map.Entry)iterator.next();
            String thisEntry = (String)entry.getKey();
            String thisReplacement = (String)entry.getValue();
            printLine = thisReplacement.length() <= 0 ? thisEntry : (new StringBuilder(String.valueOf(thisEntry))).append(":").append(thisReplacement).toString();
        }

        getBlackConfig().set("Blacklist", ((Object) (blackListA.toArray())));
        saveBlackConfig();
    }
	
    public boolean addWhiteWord(String whiteWord)
    {
    	if(whiteWordList.containsKey(whiteWord))
    	{
    		return false;
    	}
    	else
        {
    		if(whiteWord.indexOf(":") > 0)
	        {
	            String thisSplit[] = whiteWord.split(":", 2);
	            whiteWordList.put(thisSplit[0], thisSplit[1]);
	        } else
	        {
	            whiteWordList.put(whiteWord, "");
	        }
        saveWhiteList();
        return true;
        }
    }
    
    public boolean delWhiteWord(String whiteWord)
    {
        if(whiteWord.indexOf(":") > 0)
        {
            String thisSplit[] = whiteWord.split(":", 2);
            whiteWord = thisSplit[0];
        }
        if(whiteWordList.containsKey(whiteWord))
        {
            whiteWordList.remove(whiteWord);
            saveWhiteList();
            return true;
        } else
        {
            return false;
        }
    }
    
    public boolean addBlackWord(String blackWord)
    {
    	if(blackWordList.containsKey(blackWord))
    	{
    		return false;
    	}
    	else
        {
    		if(blackWord.indexOf(":") > 0)
	        {
	            String thisSplit[] = blackWord.split(":", 2);
	            blackWordList.put(thisSplit[0], thisSplit[1]);
	        } else
	        {
	            blackWordList.put(blackWord, "");
	        }
        saveWhiteList();
        return true;
        }
    }

    public boolean delBlackWord(String blackWord)
    {
        if(blackWord.indexOf(":") > 0)
        {
            String thisSplit[] = blackWord.split(":", 2);
            blackWord = thisSplit[0];
        }
        if(blackWordList.containsKey(blackWord))
        {
            blackWordList.remove(blackWord);
            saveBlackList();
            return true;
        } else
        {
            return false;
        }
    }
    
	
    public boolean addBanWord(String banWord)
    {
    	if(instaBanList.containsKey(banWord))
    	{
    		return false;
    	}
    	else
        {
    		if(banWord.indexOf(":") > 0)
	        {
	            String thisSplit[] = banWord.split(":", 2);
	            instaBanList.put(thisSplit[0], thisSplit[1]);
	        } else
	        {
	            instaBanList.put(banWord, "");
	        }
        saveBanList();
        return true;
        }
    }
    
    public boolean delBanWord(String banWord)
    {
        if(banWord.indexOf(":") > 0)
        {
            String thisSplit[] = banWord.split(":", 2);
            banWord = thisSplit[0];
        }
        if(instaBanList.containsKey(banWord))
        {
            instaBanList.remove(banWord);
            saveBanList();
            return true;
        } else
        {
            return false;
        }
    }
    
	public void saveBanList()
	{
        ArrayList banListA = new ArrayList();
        String printLine;
        for(Iterator iterator = instaBanList.entrySet().iterator(); iterator.hasNext(); banListA.add(printLine))
        {
            Map.Entry entry = (Map.Entry)iterator.next();
            String thisEntry = (String)entry.getKey();
            String thisReplacementment = (String)entry.getValue();
            printLine = thisReplacementment.length() <= 0 ? thisEntry : (new StringBuilder(String.valueOf(thisEntry))).append(":").append(thisReplacementment).toString();
        }

        getInstantConfig().set("InstaBanList", ((Object) (banListA.toArray())));
        saveInstaConfig();
    }
	
    

	public boolean getCapsOn()
	{
		return getConfig().getBoolean("Caps.Enabled");
	}

	public String getMessageCapsKick() 
	{
		return getConfig().getString("Message.Caps.Kick");
	}
    
}