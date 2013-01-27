package com.github.hatixon.mutenizer;
//    /\    /\
//   /  \  /  \
//  /    \/    \
//  \          /
//   \        /
//    \      /
//     \    /
//      \  /
//       \/
// Was told to add love to my plugin. So there you have it.

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
	private FileConfiguration warnConfig = null;
	private File warnFile = null;
	
	private Map blackWordList;
	 
	private Map whiteWordList;
	
	private Map instaBanList;
	
	private Map userList;
	
	private Map commandsList;  
	
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
	
	public void reloadWarnConfig()
	{
		if(warnConfig == null)
		{
			warnFile = new File(getDataFolder(), "warnings.yml");
		}
		warnConfig = YamlConfiguration.loadConfiguration(warnFile);
		
	    InputStream defConfigStream = this.getResource("warnings.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        warnConfig.setDefaults(defConfig);
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
        } catch (IOException ex) 
        {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + whiteFile, ex);        
        }
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
    
    public FileConfiguration getWarnConfig() {
        if (warnConfig == null) {
            this.reloadWarnConfig();
        }
        return warnConfig;
    }
    
    public void saveWarnConfig() {
        if (warnConfig == null || warnFile == null) {
        return;
        }
        try {
            getWarnConfig().save(warnFile);
        } catch (IOException ex) {
            this.getLogger().log(Level.SEVERE, "Could not save config to " + warnFile, ex);
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
		getCommandsList();
		getApetureSymbol();
		if(getConfig().getBoolean("CheckForUpdates"))
		{
			if(isUpdated())
			{
				logger.info(new StringBuilder(pre).append("There is an updated version of Mutenizer. Download at http://dev.bukkit.org/server-mods/mutenizer").toString());
			}
		}
	}
	
    public String getLatest()
    {
        StringBuilder responseData = new StringBuilder();
    	try
    	{
        String uri = "http://pastebin.com/raw.php?i=Tsaqvs1s";
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = null;
        while((line = in.readLine()) != null) 
        {
            responseData.append(line);
        }
    	}catch(Exception e)
    	{
    		
    	}
        return responseData.toString();
    }
    
    public boolean isUpdated()
    {
    	PluginDescriptionFile pdffile = getDescription();
    	String current = pdffile.getVersion();
    	String latest = getLatest();
    	boolean updated = false;
    	if(!current.equalsIgnoreCase(latest))
    	{
    		updated = true;
    	}
    	return updated;
    }
	
	public String getMessageInstaBan()
	{
		return getConfig().getString("Message.InstaBan");
	}
	
	public void instaBanPlayer(Player player)
	{
		String uName = player.getName();
		String banCom = getConfig().getString("BanCommands.InstaBan");
		String comSplit[] = banCom.split(" ");
		if(comSplit.length > 2)
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(getMessageInstaBan()).toString());
		}else
		if(comSplit.length > 3)
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(comSplit[1]).append(getMessageInstaBan()).toString());
		}else
		if(comSplit.length > 4)
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
		String banCom = getConfig().getString("BanCommands.CapsBan");
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
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(comSplit[1]).append(" ").append(comSplit[2]).append(" ").append(getMessageCapsBan()).toString());
		}
	}
	
	public String getMessageCaps()
	{
		return getConfig().getString("Message.Caps.Warn");
	}
    
    public boolean getCapsPunishment()
    {
    	return getConfig().getBoolean("Caps.Enforced");
    }
	
	public void bunnyRabbit(Player player)
	{
		String uName = player.getName();
		String banCom = getConfig().getString("BanCommands.Ban");
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
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), new StringBuilder(comSplit[0]).append(" ").append(uName).append(" ").append(comSplit[1]).append(" ").append(comSplit[2]).append(" ").append(getMessageBanned()).toString());
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
		this.getWarnConfig().options().copyDefaults(true);
		saveDefaultConfig();
		saveUserConfig();
		saveWhiteConfig();
		saveBlackConfig();
		saveInstaConfig();
		saveWarnConfig();
	}
	
	 
	public Mutenizer()
	{
		blackWordList = new HashMap();
		whiteWordList = new HashMap();
		instaBanList = new HashMap();
		userList = new HashMap();
		commandsList = new HashMap();
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
		return getWarnConfig().getInt((new StringBuilder().append("Warnings.Warned.").append((uName).toLowerCase())).toString());
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
		getWarnConfig().set(((new StringBuilder().append("Warnings.Warned.").append(uName.toLowerCase())).toString()), getTotWarn());
		saveWarnConfig();
	}
	
	public Integer getTotWarn()
	{
		return getConfig().getInt("TotalWarnings");		
	}
	
    public void setRemWarn(String uName, Integer remWarn)
    {
        getWarnConfig().set((new StringBuilder("Warnings.Warned.")).append(uName.toLowerCase()).toString(), remWarn);
        saveWarnConfig();
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
	
	public void getCommandsList()
	{
		List blackW = getConfig().getStringList("commandsList");
		for(Iterator i = blackW.iterator(); i.hasNext();)
		{
            String thisLine = (String)i.next();
            commandsList.put(thisLine, "");
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
		getWarnConfig().set((new StringBuilder("Warnings.Warned.")).append(uName.toLowerCase()).toString(), getTotWarn());
		saveWarnConfig();
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
		message = isItAllowed(message);
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
                    message = message.replaceFirst(thisRegex, thisValue);
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
		message = isItAllowed(message);
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
	
	public boolean commandSwear(String message)
	{
		boolean swore = false;
        for(Iterator iterator = commandsList.entrySet().iterator(); iterator.hasNext();)
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
		message = isItAllowed(message);
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

	 
	public String isItAllowed(String message)
	{
        for(Iterator iterator = whiteWordList.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            String theWord = (String)entry.getKey();
            String theRegex = (new StringBuilder("\\b")).append(theWord).append("\\b").toString();
            Pattern patt = Pattern.compile(theRegex, 2);
            Matcher m = patt.matcher(message);
            if(m.find())
            {
                message = message.replaceAll(theRegex, "");
            }
        }
        return message;
    }
	
	public void resetWarn(String uName)
	{
		getWarnConfig().set((new StringBuilder("Warnings.Warned.").append(uName).toString()), getTotWarn());
		saveWarnConfig();
	}
	
	public void editWarn(String uName, String amount)
	{
		getWarnConfig().set((new StringBuilder("Warnings.Warned.").append(uName).toString()), Integer.valueOf(amount));
		saveWarnConfig();
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
    	whiteWord = new StringBuilder().append("\\w*").append(whiteWord).append("\\w*").toString();
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
    	whiteWord = new StringBuilder().append("\\w*").append(whiteWord).append("\\w*").toString();
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
		if(blackWord.indexOf(":") > 0)
        {
            String thisSplit[] = blackWord.split(":", 2);
            if(blackWordList.containsKey(new StringBuilder().append("\\w*").append(thisSplit[0]).append("\\w*").toString()))
            {
            	return false;
            }else
            {
            	blackWordList.put(new StringBuilder().append("\\w*").append(thisSplit[0]).append("\\w*").toString(), thisSplit[1]);
            }
        } else
        {
        	if(!blackWordList.containsKey(new StringBuilder().append("\\w*").append(blackWord).append("\\w*").toString()))
        	{
        		blackWordList.put(new StringBuilder().append("\\w*").append(blackWord).append("\\w*").toString(), "");
        		return true;
        	}else
        	{
        		return false;
        	}
        }
        saveBlackList();
        return true;
    }

    public boolean delBlackWord(String blackWord)
    {
        if(blackWord.indexOf(":") > 0)
        {
            String thisSplit[] = blackWord.split(":", 2);
            blackWord = new StringBuilder().append("\\w*").append(thisSplit[0]).append("\\w*").toString();
        }else
        {
        	blackWord = new StringBuilder().append("\\w*").append(blackWord).append("\\w*").toString();
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
    	banWord = new StringBuilder().append("\\w*").append(banWord).append("\\w*").toString();
    	if(instaBanList.containsKey(banWord))
    	{
    		return false;
    	}
    	else
        {
    		instaBanList.put(banWord, "");
        }
        saveBanList();
        return true;
    }
    
    public boolean delBanWord(String banWord)
    {
    	banWord = new StringBuilder().append("\\w*").append(banWord).append("\\w*").toString();
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