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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Bullet extends Entity {

	private float speed = 0;
	private float damage = 0;
	private float drop = 0;
	private int numTicks = 0;
	private long maxAge = 200;
	private boolean hasHit = false;
	private float hitRange = 0.3f;
	private EntityPlayer shooter;
	
	public Bullet(World world) {
		super(world);
		
	}
	
	public Bullet(World world, double x, double y, double z, float yaw, float pitch, float speed, float damage, float drop, EntityPlayer player){
		super(world);
		
		this.setSize(1f, 1f);
		this.setPosition(x, y, z);
		
		//Updates the bounding box with new position
		this.boundingBox.setBounds(posX-hitRange, posY-hitRange, posZ-hitRange, posX+hitRange, posY+hitRange, posZ+hitRange);
		
		this.rotationYaw = yaw;
		this.rotationPitch = -pitch;
		
		this.speed = speed;
		this.damage = damage;
		this.drop = drop;
		
		this.renderDistanceWeight = 10.0D;
		
		this.shooter = player;
		
	}
	
	@Override
	public void onEntityUpdate(){
		
		if(!this.worldObj.isRemote){
			
			//Despawns the bullet after a set time
			if(maxAge < numTicks++){
				
				this.setDead();
				return;
				
			}
			
			//Applies bullet drop if the bullet is not heading straight down
			if(this.rotationPitch > -90f){
				this.rotationPitch -= drop;
			}else{
				this.rotationPitch = -90f;
			}
			
			System.out.println(this.rotationPitch);
			
			//Calculates the movement of the bullet based of yaw and pitch
			this.motionX = Math.sin(Math.toRadians(-(this.rotationYaw%360)))*speed*Math.cos(Math.toRadians(this.rotationPitch%360));
			this.motionY = Math.sin(Math.toRadians(this.rotationPitch%360))*speed;
			this.motionZ = Math.cos(Math.toRadians(-(this.rotationYaw%360)))*speed*Math.cos(Math.toRadians(this.rotationPitch%360));
			
			//Ray tracing bullet movement for world blocks
			Vec3 vecPos = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
			Vec3 vecNextPos = Vec3.createVectorHelper(this.posX + motionX, posY + motionY, this.posZ + motionZ);
			MovingObjectPosition movingobjectposition = this.worldObj.clip(vecPos, vecNextPos);
			
			//Re-Creates vectors for current position and next position, as raytracing messes it up
			vecPos = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
			vecNextPos = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			
			//If there is a block collision, check if the block is solid
			if (movingobjectposition != null && worldObj.isBlockNormalCube(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ))
			{
				//Set the next position to the collision point on the block. This will avoid damage from the bullet ghosting through the block
				vecNextPos = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		    	
				//Mark the bullet for later removal
				hasHit = true;
			}
			
			//Expanding the bounding box of the bullet to cover both current and next position, and gets all entities within
			List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			
			//Loops through all entities found above. This is necessary as we need to do path tracking on the bullets movement, for precise hit detection
			for (int k = 0; k < list.size(); k++){
				
				Entity entity2 = (Entity) list.get(k);
				
				//Expands the bounding box of the hit entity, to make up for the bounding box of the bullet, which is not included with the path vectors (vecPos and vecNextPos)
				AxisAlignedBB axisalignedbb = entity2.boundingBox.expand(hitRange, hitRange, hitRange);
				//Creating a position object where the bullets movement path intersects with the bounding box of the entity
				MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vecPos, vecNextPos);
				
				//If the movement path does not intersect with the bounding box of the entity, the position object will be null, and we should in this case not handle a hit
				if (movingobjectposition1 == null){
					
					continue;
					
				}
				
				//If the hit entity is a living entity, and is not the shooter, handle a hit
				if(entity2 instanceof EntityLivingBase && !entity2.equals(shooter)){
					
					//Providing damage to the hit entity, despawn the bullet, and end the update method to avoid further hits before the bullet despawns
					entity2.attackEntityFrom(DamageSource.generic, damage);
					setDead();
					return;
					
				}
				
			}
			
			//Applies movement to the position
			this.posX = vecNextPos.xCoord;
			this.posY = vecNextPos.yCoord;
			this.posZ = vecNextPos.zCoord;
			
			//Updates the bounding box with new position
			this.boundingBox.setBounds(posX-hitRange, posY-hitRange, posZ-hitRange, posX+hitRange, posY+hitRange, posZ+hitRange);
			
			//If the bullet has hit something previously in the code, it should now be removed
			if(hasHit){
				
				setDead();
		    	return;
				
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
			GL11.glScalef(0.1f, 0.1f, 0.1f);

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
