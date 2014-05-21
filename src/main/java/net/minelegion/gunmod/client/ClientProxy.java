package net.minelegion.gunmod.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minelegion.gunmod.Bullet;
import net.minelegion.gunmod.Bullet.RenderBullet;
import net.minelegion.gunmod.CommonProxy;
import net.minelegion.gunmod.models.ModelBullet;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers(){
		
		RenderingRegistry.registerEntityRenderingHandler(Bullet.class, new RenderBullet(new ModelBullet(), 0.0f));
		
	}
	
}
