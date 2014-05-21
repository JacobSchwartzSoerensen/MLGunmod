package net.minelegion.gunmod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Bullet extends Entity {

	private float pitch = 0;
	private float yaw = 0;
	private float speed = 0;
	private float damage = 0;
	
	public Bullet(World world) {
		super(world);
		
	}
	
	public Bullet(World world, double x, double y, double z, float yaw, float pitch, float speed, float damage){
		super(world);
		
		this.setSize(0.3f, 0.3f);
		this.setPosition(x, y, z);
		
		this.yaw = yaw;
		this.pitch = pitch;
		this.speed = speed;
		this.damage = damage;
		
	}
	
	@Override
	public void onEntityUpdate(){
		
		double newX, newY, newZ;
		
		
	}

	@Override
	protected void entityInit() {
		
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		
	}

	@SideOnly(Side.CLIENT)
	public static class RenderBullet extends RenderLiving{

		private static final ResourceLocation bulletTexture = new ResourceLocation(Gunmod.MOD_ID, "/bullet.png");
		
		public RenderBullet(ModelBase par1ModelBase, float par2) {
			super(par1ModelBase, par2);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected ResourceLocation getEntityTexture(Entity entity) {
			
			return bulletTexture;
		}
		
		
		
	}
	
}
