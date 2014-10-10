package huter.rustyores.main.proxies;

import huter.rustyores.main.renderers.ThermiteWireBlocksRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{

	public static int thermiteWireID;

	@Override
    public void registerRenderInformation() {
        thermiteWireID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new ThermiteWireBlocksRenderer(thermiteWireID));
    }
}
