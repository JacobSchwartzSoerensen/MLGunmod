package net.minelegion.mlgunmod.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minelegion.mlgunmod.Bullet;
import net.minelegion.mlgunmod.CommonProxy;
import net.minelegion.mlgunmod.Bullet.RenderBullet;
import net.minelegion.mlgunmod.models.ModelBullet;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers(){
		
		RenderingRegistry.registerEntityRenderingHandler(Bullet.class, new RenderBullet(new ModelBullet()));
		
	}
	
}
