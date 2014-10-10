package huter.rustyores.main.blocks;

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import huter.rustyores.main.lib.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public abstract class Thermite extends Block {
	
	protected boolean isReacting = false;
    protected long reactionStartTime;
	
	protected Thermite() {
		super(Material.circuits);
        //setTickRandomly(true);
	}
	
    /**
     * How many world ticks before ticking
     */
    public int tickRate(World p_149738_1_)
    {
        return 2;
    }
    
    protected void onReactionNotification(World w, int x, int y, int z){
        System.out.println("Thermite notified of reaction." + " " + w.getWorldTime());
    	startReaction(w, x, y, z);
    	w.scheduleBlockUpdate(x, y, z, w.getBlock(x, y, z), 5);
    }
 
    protected abstract void extraEffects(World w, int x, int y, int z);

    protected void react(World w, int x, int y, int z){
        System.out.println("Thermite item now reacting" + " " + w.getWorldTime());
        
        extraEffects(w, x, y, z);
		
		// notify neighbors of reaction 
		notifyNeighborsOfReaction(w, x, y, z);
    }
    
    protected void notifyReaction(World w, int x, int y, int z){
    	Block target = w.getBlock(x, y, z);
    	if (target == ModBlocks.thermitewire || target == ModBlocks.thermiteBlock){
    		Thermite twTarget = (Thermite) target;
    		twTarget.onReactionNotification(w, x, y, z);
    	}
    }
    
    protected void notifyNeighborsOfReaction(World w, int x, int y, int z){
    	notifyReaction(w, x, y-1, z+1);
    	notifyReaction(w, x, y-1, z-1);
    	notifyReaction(w, x+1, y-1, z);
    	notifyReaction(w, x-1, y-1, z);
    	notifyReaction(w, x, y+1, z+1);
    	notifyReaction(w, x, y+1, z-1);
    	notifyReaction(w, x+1, y+1, z);
    	notifyReaction(w, x-1, y+1, z);
    	notifyReaction(w, x, y, z+1);
    	notifyReaction(w, x, y, z-1);
    	notifyReaction(w, x+1, y, z);
    	notifyReaction(w, x-1, y, z);
    }
    
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
    	if(!world.isRemote){
    		if(player.getEquipmentInSlot(0).getItem() == Items.flint_and_steel){
                System.out.println("Thermite activated with flint and steel" + " " + world.getWorldTime());
                this.startReaction(world, x, y, z);
    			this.react(world, x, y, z);
    			player.getEquipmentInSlot(0).damageItem(1, player);
    			return true;
    		}
    		return false;
    	}
        return false;
    }
    
    public void startReaction(World w, int x, int y, int z){
        this.isReacting = true;
        reactionStartTime = w.getWorldTime();
    }
}
