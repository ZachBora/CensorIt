package com.worldcretornica.censorit;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class TextListener extends PlayerListener {

	public static CensorIt plugin;
	
	public TextListener(CensorIt instance)
	{
		plugin = instance;
	}
	
	@Override
	public void onPlayerChat(PlayerChatEvent event) {	
		if(plugin.isEnabled)
			event.setMessage(CensorItAPI.censor(event.getMessage()));
		
	}
	
}
