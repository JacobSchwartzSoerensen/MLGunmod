package net.minelegion.mlgunmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Gun extends Item {

	private long shootDelay;//Delay between shots in milliseconds
	private long lastShot = System.currentTimeMillis();//Timestamp for last shot
	
	public Gun(int id, long shootDelay) {
		super(id);
		
		this.shootDelay = shootDelay;
		
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.setUnlocalizedName("gun");
		
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player){
		
		if((System.currentTimeMillis()-lastShot) > shootDelay && !world.isRemote){
			
			Bullet bullet = new Bullet(world, player.posX, player.posY+player.eyeHeight, player.posZ, player.rotationYaw, player.rotationPitch, 5f, 5, player);
			
			world.spawnEntityInWorld(bullet);
			
			lastShot = System.currentTimeMillis();
			
		}
		
		return itemStack;
	}

}