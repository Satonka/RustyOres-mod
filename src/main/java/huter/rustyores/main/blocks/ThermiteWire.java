package huter.rustyores.main.blocks;

import huter.rustyores.main.RustyOres;
import huter.rustyores.main.items.ModItems;
import huter.rustyores.main.lib.Constants;
import huter.rustyores.main.proxies.ClientProxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ThermiteWire extends Thermite{
	
	@SideOnly(Side.CLIENT)
	public static IIcon crossicon;
	@SideOnly(Side.CLIENT)
	public static IIcon lineicon;
	@SideOnly(Side.CLIENT)
	public static IIcon cross_overlayicon;
	@SideOnly(Side.CLIENT)
	public static IIcon line_overlayicon;
	public static final String name = "thermitewire";
	private boolean wiresProvidePower = true;
	private boolean reacting = false;
    private Set blocksNeedingUpdate = new HashSet();

	protected ThermiteWire() {
		super();
        this.setBlockTextureName(Constants.MODID + ":" + name);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.disableStats();
        GameRegistry.registerBlock(this, name);
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return ClientProxy.thermiteWireID;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return RustyOres.thermiteWireColor;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int x, int y, int z)
    {
        return World.doesBlockHaveSolidTopSurface(par1World, x, y - 1, z) || par1World.getBlock(x, y - 1, z) == Blocks.glowstone;
    }

    /**
     * Calls World.notifyBlocksOfNeighborChange() for all neighboring blocks, but only if the given block is a redstone
     * wire.
     */
    private void notifyWireNeighborsOfNeighborChange(World world, int x, int y, int z)
    {
        if (world.getBlock(x, y, z) == this)
        {
            world.notifyBlocksOfNeighborChange(x, y, z, this);
            world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
            world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
            world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
            world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);

        if (!world.isRemote)
        {
            world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            this.notifyWireNeighborsOfNeighborChange(world, x - 1, y, z);
            this.notifyWireNeighborsOfNeighborChange(world, x + 1, y, z);
            this.notifyWireNeighborsOfNeighborChange(world, x, y, z - 1);
            this.notifyWireNeighborsOfNeighborChange(world, x, y, z + 1);

            if (world.getBlock(x - 1, y, z).isNormalCube())
            {
                this.notifyWireNeighborsOfNeighborChange(world, x - 1, y + 1, z);
            }
            else
            {
                this.notifyWireNeighborsOfNeighborChange(world, x - 1, y - 1, z);
            }

            if (world.getBlock(x + 1, y, z).isNormalCube())
            {
                this.notifyWireNeighborsOfNeighborChange(world, x + 1, y + 1, z);
            }
            else
            {
                this.notifyWireNeighborsOfNeighborChange(world, x + 1, y - 1, z);
            }

            if (world.getBlock(x, y, z - 1).isNormalCube())
            {
                this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z - 1);
            }
            else
            {
                this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z - 1);
            }

            if (world.getBlock(x, y, z + 1).isNormalCube())
            {
                this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z + 1);
            }
            else
            {
                this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z + 1);
            }
        }
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
    {
        super.breakBlock(par1World, par2, par3, par4, par5, par6);

        if (!par1World.isRemote)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this);
            par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this);
            par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this);
            par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this);
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this);
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this);
            this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3, par4);
            this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3, par4);
            this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3, par4 - 1);
            this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3, par4 + 1);

            if (par1World.getBlock(par2 - 1, par3, par4).isNormalCube())
            {
                this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3 + 1, par4);
            }
            else
            {
                this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3 - 1, par4);
            }

            if (par1World.getBlock(par2 + 1, par3, par4).isNormalCube())
            {
                this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3 + 1, par4);
            }
            else
            {
                this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3 - 1, par4);
            }

            if (par1World.getBlock(par2, par3, par4 - 1).isNormalCube())
            {
                this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 + 1, par4 - 1);
            }
            else
            {
                this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 - 1, par4 - 1);
            }

            if (par1World.getBlock(par2, par3, par4 + 1).isNormalCube())
            {
                this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 + 1, par4 + 1);
            }
            else
            {
                this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 - 1, par4 + 1);
            }
        }
    }
    
    /**
     * Returns the ID of the items to drop on destruction.
     */
    public Item getItemDropped(int par1, Random par2Random, int par3)
    {
        return ModItems.thermitedust;
    }

    /**
     * Returns true if redstone wire can connect to the specified block. Params: World, X, Y, Z, side (not a normal
     * notch-side, this can be 0, 1, 2, 3 or -1)
     */
    public static boolean isPowerProviderOrWire(IBlockAccess par0IBlockAccess, int par1, int par2, int par3, int par4)
    {
        return par0IBlockAccess.getBlock(par1, par2, par3) == ModBlocks.thermitewire || par0IBlockAccess.getBlock(par1, par2, par3) == ModBlocks.thermiteBlock;
    }

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z)
    {
        return ModItems.thermitedust;
    }
    
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.crossicon = par1IconRegister.registerIcon("redstone_dust_cross");
        this.lineicon = par1IconRegister.registerIcon("redstone_dust_line");
        this.cross_overlayicon = par1IconRegister.registerIcon("redstone_dust_overlay");
        this.line_overlayicon = par1IconRegister.registerIcon("redstone_dust_overlay");
        this.blockIcon = this.crossicon;
    }

    @SideOnly(Side.CLIENT)
    public static IIcon getRedstoneWireIcon(String par0Str)
    {
        return par0Str.equals("cross") ? crossicon : (par0Str.equals("line") ? lineicon : (par0Str.equals("cross_overlay") ? cross_overlayicon : (par0Str.equals("line_overlay") ? line_overlayicon : null)));
    }
          
    public void extraEffects(World w, int x, int y, int z){
    	// Destroy the current block
        w.func_147480_a(x, y, z, false);
        // Destroy the block below this
		w.func_147480_a(x, y-1, z, true);
<<<<<<< HEAD
    }
	
	public void updateTick(World w, int x, int y, int z, Random r)
    {    	
        System.out.println("Thermite tick updated." + w.getWorldTime()); 
        if (isReacting){
            react(w, x, y, z);
        }
=======
		
		// React all the other blocks around it too.
		notifyNeighborsOfReaction(w, x, y, z);
		
    }
    
    private void notifyReaction(World w, int x, int y, int z){
    	Block target = w.getBlock(x, y, z);
    	if (target == ModBlocks.thermitewire){
    		ThermiteWire twTarget = (ThermiteWire) target;
    		twTarget.onReactionNotification(w, x, y, z);
    	}
    }
    
    private void notifyNeighborsOfReaction(World w, int x, int y, int z){
    	notifyReaction(w, x, y, z+1);
    	notifyReaction(w, x, y, z-1);
    	notifyReaction(w, x+1, y, z);
    	notifyReaction(w, x-1, y, z);
    }
    
    public void destroyNeighbor(World world, int x, int y, int z){
    	
>>>>>>> parent of 486d48b... Added tick delay between thermite reactions
    }
    
}
