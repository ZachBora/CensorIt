package com.worldcretornica.censorit;

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
	
	// Permissions
    public PermissionHandler permissions;
    boolean permissions3;
	
	@Override
	public void onDisable() {
		this.logger.info(pdfdescription + " disabled.");
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_CHAT, this.chatlistener, Event.Priority.Highest, this);
		
		PluginDescriptionFile pdfFile = this.getDescription();
		pdfdescription = pdfFile.getName();
		pdfversion = pdfFile.getVersion();
		
		setupPermissions();
		
		this.logger.info(pdfdescription + " version " + pdfversion + " is enabled!");
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
		if (l.equalsIgnoreCase("censorit"))
		{
			if(args.length == 0)
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
						s.sendMessage(ChatColor.RED + "/censorit censor|uncensor " + ChatColor.GREEN + "<word>" + ChatColor.WHITE + " Censor or uncensor a word. (e.g. ass, fuck)");
						s.sendMessage(ChatColor.RED + "/censorit allow|unallow " + ChatColor.GREEN + "<word>" + ChatColor.WHITE + " Allow or unallow a word. (e.g. gr" + ChatColor.RED + "ass" + ChatColor.WHITE + ", " + ChatColor.RED + "assass" + ChatColor.WHITE + "in)");
						s.sendMessage(ChatColor.RED + "/censorit replace|unreplace " + ChatColor.GREEN + "<word>" + ChatColor.WHITE + " Add or remove a replacement word. (e.g. bird, flower)");
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
						//TODO
					}
				}else if(args[0].toString().equalsIgnoreCase("verify"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.admin"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						CensorItAPI.setVerifyWordOnline(true);
					}
				}else if(args[0].toString().equalsIgnoreCase("unverify"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.admin"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						CensorItAPI.setVerifyWordOnline(false);
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
						CensorItAPI.addCensoredWord(args[1]);
					}
				}else if(args[0].toString().equalsIgnoreCase("uncensor"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.word"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						CensorItAPI.removeCensoredWord(args[1]);
					}
				}else if(args[0].toString().equalsIgnoreCase("allow"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.word"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						CensorItAPI.addAllowedWord(args[1]);
					}
				}else if(args[0].toString().equalsIgnoreCase("unallow"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.word"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						CensorItAPI.removeAllowedWord(args[1]);
					}
				}else if(args[0].toString().equalsIgnoreCase("replace"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.word"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						CensorItAPI.addHappyWord(args[1]);
					}
				}else if(args[0].toString().equalsIgnoreCase("unreplace"))
				{
					if(s instanceof Player && !this.checkPermissions((Player) s, "CensorIt.word"))
					{
						s.sendMessage(ChatColor.RED +"[" + pdfdescription + "] " + " Permissions denied.");
					}else{
						CensorItAPI.removeHappyWord(args[1]);
					}
				}
			}
			return true;
		}
		
		return false;
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
