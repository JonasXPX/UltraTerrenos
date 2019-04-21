package br.com.endcraft.ultraterrenos;

import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class UltraTerrenos extends JavaPlugin{

	private WorldGuardPlugin worldGuard;
	private WorldEditPlugin worldEdit;
	private static UltraTerrenos instance;
	
	@Override
	public void onEnable() {
		getCommand("terreno").setExecutor(new TerrenoCommand());
		
		instance = this;
		
		worldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
		worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
		
		
		if(worldEdit == null || worldGuard == null)
			getPluginLoader().disablePlugin(this);
	}
	
	
	
	public WorldEditPlugin getWorldEdit() {
		return worldEdit;
	}
	
	public WorldGuardPlugin getWorldGuard() {
		return worldGuard;
	}
	
	public static UltraTerrenos getInstance() {
		return instance;
	}
}
