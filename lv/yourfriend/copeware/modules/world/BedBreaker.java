package lv.yourfriend.copeware.modules.world;
import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventPacket;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.ModeSetting;
import lv.yourfriend.copeware.settings.NumberSetting;
import lv.yourfriend.copeware.util.MovementUtils;
import lv.yourfriend.copeware.util.TickTimer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C07PacketPlayerDigging;


public class BedBreaker extends Module {
	private int posX,posY,posZ;
	private int radius = 6;
	
	public BedBreaker() {
		super("BedBreaker", Keyboard.KEY_NONE, Category.WORLD);
	}
	
	public void onEvent(Event event) {
		if(event instanceof EventUpdate) {
			int y;
            for (int reach = y = 2; y >= -reach; --y) {
                for (int x = -reach; x <= reach; ++x) {
                    for (int z = -reach; z <= reach; ++z) {
                        final double posX = this.mc.thePlayer.posX + x;
                        final double posY = this.mc.thePlayer.posY + y;
                        final double posZ = this.mc.thePlayer.posZ + z;
                        final BlockPos determinedPos = new BlockPos(posX, posY, posZ);
                        if (determinedPos != null && mc.theWorld.getBlockState(determinedPos).getBlock() == Blocks.bed && this.mc.thePlayer.getDistance(posX, posY, posZ) < 3) {
                        	this.mc.playerController.func_180512_c(determinedPos, getFace(determinedPos));
                        	this.mc.thePlayer.swingItem();
                        }
                    }
                }
            }
		}
	}
	
    private EnumFacing getFace(final BlockPos pos) {
        final BlockPos[] addons = { new BlockPos(0, 1, 0), new BlockPos(0, -1, 0), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };
        for (int length2 = addons.length, j = 0; j < length2; ++j) {
            final BlockPos offset = pos.add(addons[j].getX(), 0, addons[j].getZ());
            if (this.mc.theWorld.getBlockState(offset).getBlock() != Blocks.bed) {
                for (int k = 0; k < EnumFacing.values().length; ++k) {
                    if (this.mc.theWorld.getBlockState(offset.offset(EnumFacing.values()[k])).getBlock() != Blocks.bed) {
                        return getInverted(EnumFacing.values()[k]);
                    }
                }
            }
        }
        return null;
    }
    
    public static EnumFacing getInverted(final EnumFacing face) {
        EnumFacing invert = null;
        switch (face) {
            case NORTH:
                invert = EnumFacing.SOUTH;
                break;
            case SOUTH:
                invert = EnumFacing.NORTH;
                break;
            case UP:
                invert = EnumFacing.DOWN;
                break;
            case DOWN:
                invert = EnumFacing.UP;
                break;
            case EAST:
                invert = EnumFacing.WEST;
                break;
            case WEST:
                invert = EnumFacing.EAST;
                break;
        }
        return invert;
    }

}