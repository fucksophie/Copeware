package lv.yourfriend.copeware.modules.world;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventPacket;
import lv.yourfriend.copeware.events.listeners.EventRender;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.BooleanSetting;
import lv.yourfriend.copeware.settings.ModeSetting;
import lv.yourfriend.copeware.settings.NumberSetting;
import lv.yourfriend.copeware.util.MovementUtils;
import lv.yourfriend.copeware.util.RenderUtils;
import lv.yourfriend.copeware.util.TickTimer;
import lv.yourfriend.copeware.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
public class Scaffold extends Module {

	public NumberSetting timerBoost = new NumberSetting("Timer Boost", 1.0, 1.0, 2.0, 0.1);
	public BooleanSetting tower = new BooleanSetting("Tower", true);
	public BooleanSetting swing = new BooleanSetting("Swing", true);
	public BooleanSetting sprint = new BooleanSetting("Sprint", false);
	
	public Scaffold() {
		super("Scaffold", Keyboard.KEY_NONE, Category.WORLD);
		this.addSettings(sprint, timerBoost, tower, swing);
	}
	

	public static Timer timer = new Timer();
	public static int heldItem = 0;
	
	@Override
	public void onEnable() {
        if (mc.thePlayer != null) {
            heldItem = mc.thePlayer.inventory.currentItem;
        }
	}
	
	@Override
	public void onDisable() {
        mc.gameSettings.keyBindSneak.pressed = false;
        mc.thePlayer.movementInput.sneak = false;
        mc.thePlayer.inventory.currentItem = heldItem;
        mc.timer.timerSpeed = 1.0f;
	}
	
	@Override
	public void onEvent(Event e) {
		
		if (e instanceof EventMotion) {
			
			
			EventMotion event = (EventMotion)e;
			
			mc.thePlayer.setSprinting(sprint.isEnabled());
			
			double x = mc.thePlayer.posX;
            double z = mc.thePlayer.posZ;

            if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock().getMaterial().isReplaceable()) {
                x = mc.thePlayer.posX;
                z = mc.thePlayer.posZ;
            }
            final BlockPos underPos = new BlockPos(x, mc.thePlayer.posY - 1, z);
            final BlockData data = getBlockData(underPos);
            
            if (getBlockSlot() == -1) {
                return;
            }
            
            
            //Tower
            if (e.isPre() && getBlockSlot() != -1 && mc.gameSettings.keyBindJump.getIsKeyPressed() && !MovementUtils.isMoving() && tower.isEnabled()) {
                mc.timer.timerSpeed = (float) timerBoost.getValue();
            	MovementUtils.setMotion(0);
                if (mc.thePlayer.onGround) {
                    if (MovementUtils.isOnGround(0.76) && !MovementUtils.isOnGround(0.75) && mc.thePlayer.motionY > 0.23 && mc.thePlayer.motionY < 0.25) {
                        mc.thePlayer.motionY = Math.round(mc.thePlayer.posY) - mc.thePlayer.posY;
                    }
                    if (MovementUtils.isOnGround(1.0E-4)) {
                        mc.thePlayer.motionY = 0.41999998688697815;
                        if (timer.hasTimeElapsed(1500, false)) {
                            mc.thePlayer.motionY = -0.28;
                            timer.reset();
                        }
                    } else if (mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 1.0E-4 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 1.0E-4) {
                        mc.thePlayer.motionY = 0.0;
                    }
                } else if (mc.theWorld.getBlockState(underPos).getBlock().getMaterial().isReplaceable() && data != null) {
                    mc.thePlayer.motionY = 0.41955;
                }
            }

            if (mc.theWorld.getBlockState(underPos).getBlock().getMaterial().isReplaceable() && data != null) {
                if (e.isPre()) {

                    event.setYaw(mc.thePlayer.rotationYaw + RandomUtils.nextFloat(178, 179));
                    event.setPitch(70f);
                    RenderUtils.setCustomYaw(event.yaw);
                    RenderUtils.setCustomPitch(event.pitch);
                    
                    if (data.face == EnumFacing.UP) {
                    	mc.timer.timerSpeed = 1;
                    	event.setPitch(90f);
                    	RenderUtils.setCustomPitch(event.pitch);
                    }

                    if (!mc.gameSettings.keyBindJump.getIsKeyPressed() && mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                    	mc.thePlayer.onGround = false;
                    }

                } else if (getBlockSlot() != -1) {

                    if (!mc.gameSettings.keyBindJump.getIsKeyPressed() && mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                        mc.thePlayer.onGround = false;
                    }

                    mc.thePlayer.inventory.currentItem = getBlockSlot();

                    mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), data.position, data.face, getVec3(data.position, data.face));

                    if (swing.isEnabled()) {
                        mc.thePlayer.swingItem();
                    } else {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    }
                }
            } else {
                event.setYaw(mc.thePlayer.rotationYaw + RandomUtils.nextFloat(178, 179));
                event.setPitch(70f);
                RenderUtils.setCustomYaw(event.yaw);
                RenderUtils.setCustomPitch(event.pitch);
            }
		}
		
	}
	
	private class BlockData {
        public BlockPos position;
        public EnumFacing face;

        private BlockData(final BlockPos position, final EnumFacing face, final BlockData blockData) {
            this.position = position;
            this.face = face;
        }
    }
	
	private static transient List<Block> invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane,
			Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava,
			Blocks.flowing_lava, Blocks.snow_layer, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest,
			Blocks.noteblock, Blocks.jukebox, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate,
			Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button,
			Blocks.wooden_button, Blocks.lever, Blocks.crafting_table, Blocks.furnace, Blocks.stone_slab,
			Blocks.wooden_slab, Blocks.stone_slab2, Blocks.brown_mushroom, Blocks.red_mushroom, Blocks.red_flower,
			Blocks.yellow_flower, Blocks.flower_pot);
	
	private int getBlockSlot() {
		int item = -1;
		int stacksize = 0;
        for (int i = 36; i < 45; ++i) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock) mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()).getBlock()) && mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize >= stacksize) {
                item = i - 36;
                stacksize = mc.thePlayer.inventoryContainer.getSlot(i).getStack().stackSize;
            }
        }
        return item;
    }
	
	public Vec3 getVec3(final BlockPos pos, final EnumFacing face) {
        double x = pos.getX() + 0.500;
        double y = pos.getY() + 0.500;
        double z = pos.getZ() + 0.500;
        x += face.getFrontOffsetX() / 2.0;
        z += face.getFrontOffsetZ() / 2.0;
        y += face.getFrontOffsetY() / 2.0;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += (new Random().nextDouble() / 2) - 0.25;
            z += (new Random().nextDouble() / 2) - 0.25;
        } else {
            y += (new Random().nextDouble() / 2) - 0.25;
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += (new Random().nextDouble() / 2) - 0.25;
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += (new Random().nextDouble() / 2) - 0.25;
        }
        return new Vec3(x, y, z);
    }
	
	private BlockData getBlockData(final BlockPos pos) {
        if (isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(-1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, 1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, 0, -1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(0, -1, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos.add(0, -1, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(0, -1, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        final BlockPos pos2 = pos.add(0, -1, 0).add(1, 0, 0);
        final BlockPos pos3 = pos.add(0, -1, 0).add(0, 0, 1);
        final BlockPos pos4 = pos.add(0, -1, 0).add(-1, 0, 0);
        final BlockPos pos5 = pos.add(0, -1, 0).add(0, 0, -1);
        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        if (isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP, (BlockData) null);
        }
        if (isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST, (BlockData) null);
        }
        if (isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST, (BlockData) null);
        }
        if (isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH, (BlockData) null);
        }
        if (isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH, (BlockData) null);
        }
        return null;
    }
	
	public static boolean isPosSolid(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        if ((block.getMaterial().isSolid() || !block.isTranslucent() || block instanceof BlockLadder || block instanceof BlockCarpet
                || block instanceof BlockSnow || block instanceof BlockSkull)
                && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer)) {
            return true;
        }
        return false;
	}
}