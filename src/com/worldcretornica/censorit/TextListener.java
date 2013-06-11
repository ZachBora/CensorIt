package com.worldcretornica.censorit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TextListener implements Listener {

	private CensorIt plugin;
	
	public TextListener(CensorIt instance)
	{
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {	
		if(plugin.isEnabled)
			event.setMessage(CensorItAPI.censor(event.getMessage()));
		
	}
	
}
