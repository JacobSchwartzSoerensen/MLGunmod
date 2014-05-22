package net.minelegion.mlgunmod;


import net.minecraft.item.Item;
import net.minelegion.mlgunmod.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
 
@Mod(modid=MLGunmod.MOD_ID, name=MLGunmod.MOD_NAME, version=MLGunmod.MOD_VERSION)
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class MLGunmod {

	public static final String MOD_ID = "mlgunmod";
	public static final String MOD_NAME = "ML Gunmod";
	public static final String MOD_VERSION = "0.0.1";
	public static final String CLIENT_PROXY = "net.minelegion.mlgunmod.client.ClientProxy";
	public static final String COMMON_PROXY = "net.minelegion.mlgunmod.CommonProxy";
	
	//Items
	public Item gun = new Gun(1000, 200);
	
	
	// The instance of your mod that Forge uses.
	@Instance(MLGunmod.MOD_ID)
	public static MLGunmod instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide=MLGunmod.CLIENT_PROXY, serverSide=MLGunmod.COMMON_PROXY)
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Stub Method
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
		
		addBlocks();
		addItems();
		addEntities();
		addRecipes();
		
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
	
	private void addBlocks(){
		
		
		
	}
	
	private void addItems(){
		
		GameRegistry.registerItem(gun, gun.getUnlocalizedName());
		LanguageRegistry.addName(gun, "Gun");
		
	}
	
	private void addEntities(){
		
		EntityRegistry.registerModEntity(Bullet.class, "Bullet", 1, MLGunmod.instance, 128, 1, true);
		EntityRegistry.registerGlobalEntityID(Bullet.class, "Bullet", EntityRegistry.findGlobalUniqueEntityId());
		LanguageRegistry.instance().addStringLocalization("entity.Bullet.name", "Bullet");
		
	}
	
	private void addRecipes(){
		
		
		
	}
	
	
       
}