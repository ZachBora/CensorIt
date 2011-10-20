package com.worldcretornica.censorit;

import org.bukkit.event.player.PlayerListener;

public class TextListener extends PlayerListener {

	public static CensorIt plugin;
	
	public TextListener(CensorIt instance)
	{
		plugin = instance;
	}
	
}
