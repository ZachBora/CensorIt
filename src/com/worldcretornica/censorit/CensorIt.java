package com.worldcretornica.censorit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class CensorIt extends JavaPlugin {

	public String pdfdescription;
	private String pdfversion;
	
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public final TextListener chatlistener = new TextListener(this);
	
	public final HashSet<Player> notcensoredplayers = new HashSet<Player>();
	
	public boolean isEnabled = true;
	
	public final String censorfilename = "censored.txt";
	public final String allowedfilename = "allowed.txt";
	public final String replacementfilename = "replacement.txt";
	
	// Permissions
    public PermissionHandler permissions;
    boolean permissions3;
	
	@Override
	public void onDisable() {
		saveAllConfig();
		this.logger.info(pdfdescription + " disabled.");
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_CHAT, this.chatlistener, Event.Priority.Lowest, this);
		
		PluginDescriptionFile pdfFile = this.getDescription();
		pdfdescription = pdfFile.getName();
		pdfversion = pdfFile.getVersion();
		
		setupPermissions();
		loadAllConfig();
		
		//Auto-save code
		/*
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
		    public void run() {
		    	saveAllConfig();
		        logger.info("[" + pdfdescription + "] Auto-Saving configuration");
		    }
		}, 60L, 6000L);
		*/
		
		this.logger.info(pdfdescription + " version " + pdfversion + " is enabled!");
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
		if (l.equalsIgnoreCase("censorit"))
		{
			if(args.length == 0 || args.length > 2)
			{
				s.sendMessage(ChatColor.BLUE + pdfdescription + " v" + pdfversion + " - Help");
				s.sendMessage(ChatColor.RED + "/censorit on" + ChatColor.WHITE + " Turn on censoring.");
				s.sendMessage(ChatColor.RED + "/censorit off" + ChatColor.WHITE + " Turn off censoring.");
				if(s instanceof Player && this.checkPermissions((Player) s, "CensorIt.admin"))
				{
					s.sendMessage(ChatColor.RED + "/censorit config " + ChatColor.WHITE + " List configuration commands.");
				}
				if(s instanceof Player && this.checkPermissions((Player) s, "CensorIt.word"))
				{
					s.sendMessage(ChatColor.RED + "/censorit word " + ChatColor.WHITE + " List word management commands.");
				}
			}else if(args.length == 1)
			{
				if(args[0].toString().equalsIgnoreCase("config"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.admin"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						s.sendMessage(ChatColor.BLUE + pdfdescription + " v" + pdfversion + " - Configuration Commands");
						s.sendMessage(ChatColor.RED + "/censorit enable " + ChatColor.WHITE + " Server-wide enable censoring.");
						s.sendMessage(ChatColor.RED + "/censorit disable " + ChatColor.WHITE + " Server-wide disable censoring.");
						s.sendMessage(ChatColor.RED + "/censorit reload " + ChatColor.WHITE + " Reload configuration.");
						s.sendMessage(ChatColor.RED + "/censorit verify " + ChatColor.WHITE + " SLOW! Verify words online (added to allowed list).");
						s.sendMessage(ChatColor.RED + "/censorit unverify " + ChatColor.WHITE + " Stop verifying words online.");
					}
				}else if(args[0].toString().equalsIgnoreCase("word"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.word"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						s.sendMessage(ChatColor.BLUE + pdfdescription + " v" + pdfversion + " - Configuration Commands");
						s.sendMessage(ChatColor.RED + "/censorit censor|uncensor " + ChatColor.GREEN + "<word>");
						s.sendMessage(ChatColor.WHITE + " Censor or uncensor a word. (e.g. ass, fuck)");
						s.sendMessage(ChatColor.RED + "/censorit allow|unallow " + ChatColor.GREEN + "<word>");
						s.sendMessage(ChatColor.WHITE + " Allow or unallow a word. (e.g. gr" + ChatColor.RED + "ass" + ChatColor.WHITE + ", " + ChatColor.RED + "assass" + ChatColor.WHITE + "in)");
						s.sendMessage(ChatColor.RED + "/censorit replace|unreplace " + ChatColor.GREEN + "<word>");
						s.sendMessage(ChatColor.WHITE + " Add or remove a replacement word. (e.g. bird, flower)");
					}
				}else if(args[0].toString().equalsIgnoreCase("on"))
				{
					if(s instanceof Player && notcensoredplayers.contains((Player) s))
					{
						notcensoredplayers.remove((Player) s);
						s.sendMessage("Censoring disabled");
					}
				}else if(args[0].toString().equalsIgnoreCase("off"))
				{
					if(s instanceof Player && !notcensoredplayers.contains((Player) s))
					{
						notcensoredplayers.add((Player) s);
						s.sendMessage("Censoring enabled");
					}
				}else if(args[0].toString().equalsIgnoreCase("enable"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.admin"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						isEnabled = true;
						s.sendMessage("Global censoring enabled");
					}
				}else if(args[0].toString().equalsIgnoreCase("disable"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.admin"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						isEnabled = false;
						s.sendMessage("Global censoring disabled");
					}
				}else if(args[0].toString().equalsIgnoreCase("reload"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.admin"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						loadAllConfig();
						s.sendMessage(pdfdescription + " configuration reloaded!");
					}
				}else if(args[0].toString().equalsIgnoreCase("verify"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.admin"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						CensorItAPI.setVerifyWordOnline(true);
						s.sendMessage("Words are now verified online, system might get slower!");
					}
				}else if(args[0].toString().equalsIgnoreCase("unverify"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.admin"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						CensorItAPI.setVerifyWordOnline(false);
						s.sendMessage("Words are no longer verified online.");
					}
				}
			}else if(args.length == 2)
			{
				if(args[0].toString().equalsIgnoreCase("censor"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.word"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						if(CensorItAPI.isCensoredWord(args[1]))
						{
							s.sendMessage(ChatColor.RED + args[1] + " is already censored.");
						}else{
							CensorItAPI.addCensoredWord(args[1]);
							s.sendMessage(args[1] + " is now censored.");
							saveFile(CensorItAPI.getCensoredWords(), censorfilename);
						}
					}
				}else if(args[0].toString().equalsIgnoreCase("uncensor"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.word"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						if(!CensorItAPI.isCensoredWord(args[1]))
						{
							s.sendMessage(ChatColor.RED + args[1] + " is not censored.");
						}else{
							CensorItAPI.removeCensoredWord(args[1]);
							s.sendMessage(args[1] + " is no longer censored.");
							saveFile(CensorItAPI.getCensoredWords(), censorfilename);
						}
					}
				}else if(args[0].toString().equalsIgnoreCase("allow"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.word"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						if(CensorItAPI.isAllowedWord(args[1]))
						{
							s.sendMessage(ChatColor.RED + args[1] + " is already allowed.");
						}else{
							CensorItAPI.addAllowedWord(args[1]);
							s.sendMessage(args[1] + " is now allowed.");
							saveFile(CensorItAPI.getAllowedWords(), allowedfilename);
						}
					}
				}else if(args[0].toString().equalsIgnoreCase("unallow"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.word"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						if(!CensorItAPI.isAllowedWord(args[1]))
						{
							s.sendMessage(ChatColor.RED + args[1] + " is not allowed.");
						}else{
							CensorItAPI.removeAllowedWord(args[1]);
							s.sendMessage(args[1] + " is no longer allowed.");
							saveFile(CensorItAPI.getAllowedWords(), allowedfilename);
						}
					}
				}else if(args[0].toString().equalsIgnoreCase("replace"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.word"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						if(CensorItAPI.isHappyWord(args[1]))
						{
							s.sendMessage(ChatColor.RED + args[1] + " is already a replacement.");
						}else{
							CensorItAPI.addHappyWord(args[1]);
							s.sendMessage(args[1] + " is now a replacement.");
							saveFile(CensorItAPI.getHappyWords(), replacementfilename);
						}
					}
				}else if(args[0].toString().equalsIgnoreCase("unreplace"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.word"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						if(!CensorItAPI.isHappyWord(args[1]))
						{
							s.sendMessage(ChatColor.RED + args[1] + " is not a replacement.");
						}else{
							CensorItAPI.removeHappyWord(args[1]);
							s.sendMessage(args[1] + " is no longer a replacement.");
							saveFile(CensorItAPI.getHappyWords(), replacementfilename);
						}
					}
				}
			}
			return true;
		}
		
		return false;
	}
	
	
	
	
	
	private void loadAllConfig() {
		File dir = new File(this.getDataFolder(), "");
		dir.mkdirs();
				
		CensorItAPI.setCensoredWords(loadFile(censorfilename));
		CensorItAPI.setAllowedWords(loadFile(allowedfilename));
		CensorItAPI.setHappyWords(loadFile(replacementfilename));
	}
	
	private void saveAllConfig() {
		saveFile(CensorItAPI.getCensoredWords(), censorfilename);
		saveFile(CensorItAPI.getAllowedWords(), allowedfilename);
		saveFile(CensorItAPI.getHappyWords(), replacementfilename);
	}
	
	private void saveFile(HashSet<String> words, String name)
	{
		File file;
		FileWriter writer = null;
		
		file = new File(this.getDataFolder(), name);
		
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.severe("[" + pdfdescription + "] Unable to create login config file!");
				logger.severe(e.getMessage());
			}
		}
		
		try{		
			writer = new FileWriter(file);
			
			for(String str: words)
			{
				writer.write(str + "\n");
			}
			
			writer.close();
			
		}catch (IOException e){
			logger.severe("[" + pdfdescription + "] Unable to create login config file!");
			logger.severe(e.getMessage());
		} finally {                      
			if (writer != null) try {
				writer.close();
			} catch (IOException e2) {}
		}
	}
	
	private HashSet<String> loadFile(String name) {
		HashSet<String> words = new HashSet<String>();
		FileReader reader = null;
		BufferedReader br = null;
		
		File file  = new File(this.getDataFolder(), name);
		
		if(!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.severe("[" + pdfdescription + "] Unable to create login config file!");
				logger.severe(e.getMessage());
			}
			words = new HashSet<String>();
		}else{
			try{
				reader = new FileReader(file);
				br = new BufferedReader(reader);
							
				String line;
			    while((line = br.readLine()) != null)
			    	words.add(line);
			    br.close();
			    reader.close();
				
			}catch (IOException e){
				logger.severe("[" + pdfdescription + "] Unable to read quote config file!");
				logger.severe(e.getMessage());
			} finally {     
				if (br != null) try{
					br.close();
				} catch (IOException e2) {}
				if (reader != null) try {
					reader.close();
				} catch (IOException e2) {}
			}
		}
		
		return words;
	}
	
	
	
	
	
	
	
	
	private void setupPermissions() {
        if(permissions != null)
            return;
        
        Plugin permTest = this.getServer().getPluginManager().getPlugin("Permissions");
        
        // Check to see if Permissions exists
        if (permTest == null) {
        	logger.info("[" + pdfdescription + "] Permissions not found, using SuperPerms");
        	return;
        }
    	// Check if it's a bridge
    	if (permTest.getDescription().getVersion().startsWith("2.7.7")) {
    		logger.info("[" + pdfdescription + "] Found Permissions Bridge. Using SuperPerms");
    		return;
    	}
    	
    	// We're using Permissions
    	permissions = ((Permissions) permTest).getHandler();
    	// Check for Permissions 3
    	permissions3 = permTest.getDescription().getVersion().startsWith("3");
    	logger.info("[" + pdfdescription + "] Permissions " + permTest.getDescription().getVersion() + " found");
    }
	
	
	
	
	
	
	
	public Boolean checkPermissions(Player player, String node) {
    	// Permissions
        if (this.permissions != null) {
            if (this.permissions.has(player, node))
                return true;
        // SuperPerms
        } else if (player.hasPermission(node)) {
              return true;
        } else if (player.isOp()) {
            return true;
        }
        return false;
    }

}
