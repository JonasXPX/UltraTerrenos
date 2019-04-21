package br.com.endcraft.ultraterrenos;

import org.bukkit.Location;
import org.bukkit.Material;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.patterns.SingleBlockPattern;
import com.sk89q.worldedit.regions.CuboidRegionSelector;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class Terreno {

	private String id;
	private Size size;
	private Vector max;
	private Vector min;
	private BukkitWorld localWorld;
	
	
	public Terreno(String id, Size size) {
		this.id = id;
		this.size = size;
	}
	
	public boolean isRegion(Location center) {
		return false;
	}
	
	/**
	 * Cria uma region através do WorldGuard
	 * 
	 * @param center Local onde o jogador estiver, por padão.
	 * @param priority prioridade da region >= 0
	 */
	public ProtectedCuboidRegion createRegion(Location center, int priority) throws IllegalArgumentException{
		org.bukkit.util.Vector vec = center.toVector();
		max = new Vector(vec.getBlockX() + size.getX(), 0, vec.getBlockZ() + size.getZ());
		min = new Vector(vec.getBlockX() - size.getX(), 255, vec.getBlockZ() - size.getZ());
		localWorld = new BukkitWorld(center.getWorld());
		ProtectedCuboidRegion pro = new ProtectedCuboidRegion(id, min.toBlockVector(), max.toBlockVector());
		

		ApplicableRegionSet applicableRegions = UltraTerrenos.getInstance().getWorldGuard().getRegionManager(center.getWorld()).getApplicableRegions(pro);
		if(applicableRegions.size() > 0) {
			throw new IllegalArgumentException("Já existe um terreno proxímo.");
		}
		
		pro.setPriority(priority);

		UltraTerrenos.getInstance().getWorldGuard().getRegionManager(center.getWorld()).addRegion(pro);
		return pro;
	}
	
	/**
	 * Cria uma region com o WorldEdit
	 * 
	 * @param center Local padrão do jogador
	 */
	public Region createCuboidSelection(Location center) {
		try {
			CuboidRegionSelector cuboid = new CuboidRegionSelector(localWorld, min, max);
			return cuboid.getRegion();
		} catch (IncompleteRegionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Cria uma parede em volta da region, usando o bloco 'fance'
	 * 	
	 * @param region
	 */
	public void buildWalls(Region region) {
		EditSession edit = new EditSession(localWorld, -1);
		try {
			edit.makeCuboidWalls(region, new SingleBlockPattern(new BaseBlock(Material.FENCE.getId())));
		} catch (MaxChangedBlocksException e) {e.printStackTrace();}
	}
	
}
