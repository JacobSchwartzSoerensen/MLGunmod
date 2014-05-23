package net.minelegion.mlgunmod;

import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Bullet extends Entity {

	private float pitch = 0;
	private float yaw = 0;
	private float speed = 0;
	private float damage = 0;
	private int numTicks = 0;
	private long maxAge = 200;
	private boolean hasHit = false;
	private float hitRange = 0.3f;
	
	public Bullet(World world) {
		super(world);
		
	}
	
	public Bullet(World world, double x, double y, double z, float yaw, float pitch, float speed, float damage){
		super(world);
		
		this.setSize(1f, 1f);
		this.setPosition(x, y, z);
		
		this.rotationYaw = yaw;
		this.rotationPitch = -pitch;
		
		this.yaw = yaw;
		this.pitch = pitch;
		this.speed = speed;
		this.damage = damage;
		
		this.renderDistanceWeight = 10.0D;
		
	}
	
	@Override
	public void onEntityUpdate(){
		
		
		//Moving the bullet, if it has not hit anything
		if(!hasHit){
			
			
			
		}
		
		//Checking if the bullet hit the ground, and despawn if it did
		/*if(!worldObj.isRemote && worldObj.isBlockNormalCube((int) (this.posX+motionX),(int) (this.posY+motionY),(int) (this.posZ+motionZ)) && !hasHit){
			
			System.out.println("Hit");
			hasHit = true;
			this.setDead();
			
		}*/
		
		if(!this.worldObj.isRemote){
			
			/*List<Entity> entities = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBox(posX+hitRange, posY+hitRange, posZ+hitRange, posX-hitRange, posY-hitRange, posZ-hitRange));
			
			for(int i = 0; i < entities.size(); i++){
				
				if(entities.get(i) instanceof EntityLivingBase){
					System.out.println("Hit creature");
					EntityLivingBase entity = (EntityLivingBase) entities.get(i);
					entity.attackEntityFrom(DamageSource.generic, damage);
					
				}
				
			}*/
			
			
			
			//Despawns the bullet after a set time
			if(maxAge < numTicks++){
				
				this.setDead();
				return;
				
			}
			
			this.motionX = Math.sin(Math.toRadians(-(this.rotationYaw%360)))*speed*Math.cos(Math.toRadians(this.rotationPitch%360));
			this.motionY = Math.sin(Math.toRadians(this.rotationPitch%360))*speed;
			this.motionZ = Math.cos(Math.toRadians(-(this.rotationYaw%360)))*speed*Math.cos(Math.toRadians(this.rotationPitch%360));
			
			this.posX += motionX;
			this.posY += motionY;
			this.posZ += motionZ;
			
			this.boundingBox.setBounds(posX-hitRange, posY-hitRange, posZ-hitRange, posX+hitRange, posY+hitRange, posZ+hitRange);
			
			Vec3 vecPos = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
	        Vec3 vecNextPos = Vec3.createVectorHelper(this.posX + motionX, posY + motionY, this.posZ + motionZ);
	        MovingObjectPosition movingobjectposition = this.worldObj.clip(vecPos, vecNextPos);
	        
	        vecPos = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
	        vecNextPos = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
	        /*if (movingobjectposition != null)
	        {
	            vecNextPos = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
	        }*/
			
			List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			System.out.println(boundingBox);
			double d = 0.0D;
	        for (int k = 0; k < list.size(); k++)
	        {
	            Entity entity2 = (Entity) list.get(k);
	            
	            //System.out.println("Step 1");
	            
	            float f3 = this.hitRange;
	            AxisAlignedBB axisalignedbb = entity2.boundingBox.expand(f3, f3, f3);
	            MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vecPos, vecNextPos);
	            //System.out.println(entity2+":"+movingobjectposition1);
	            if (movingobjectposition1 == null)
	            {
	                //System.out.println("Abort");
	            	continue;
	            }
	            double d1 = vecPos.distanceTo(movingobjectposition1.hitVec);
	            if (d1 < d || d == 0.0D)
	            {
	                System.out.println("oh no :(");
	            	entity2.attackEntityFrom(DamageSource.generic, damage);
	            	
	            }
	        }
			
		}
		
	}

	@Override
	protected void entityInit() {
		//Nothing to do here
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		//No need to save bullet data
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		//No need to read bullet data
		
	}

	@SideOnly(Side.CLIENT)
	public static class RenderBullet extends Render{

		private static final ResourceLocation bulletTexture = new ResourceLocation(MLGunmod.MOD_ID, "Bullet.png");
		private ModelBase model;
		
		public RenderBullet(ModelBase model) {

			this.model = model;
			
		}
		
		protected ResourceLocation getEntityTexture(Entity entity) {
			
			return bulletTexture;
		}
		
		public void renderBullet(Bullet bullet, double x, double y, double z, float yaw, float pitch){
			
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x, (float) y, (float) z);
			GL11.glRotatef(-yaw + 180 , 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(bullet.rotationPitch + (bullet.rotationPitch - bullet.prevRotationPitch) * pitch, 1.0F, 0.0F, 0.0F);

			this.bindTexture(bulletTexture);
			
			model.render(bullet, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.02F);
			        
			//Resets rotation and translation
			GL11.glPopMatrix();
			
		}
		
		@Override
		public void doRender(Entity entity, double x, double y, double z,
				float yaw, float pitch) {

			renderBullet((Bullet) entity, x, y, z, yaw, pitch);
			
		}
		
		
		
	}
	
}
