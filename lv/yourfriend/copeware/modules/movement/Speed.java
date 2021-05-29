package lv.yourfriend.copeware.modules.movement;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.ModeSetting;
import lv.yourfriend.copeware.settings.NumberSetting;
import lv.yourfriend.copeware.util.MoveUtils;
import lv.yourfriend.copeware.util.MovementUtils;
import lv.yourfriend.copeware.util.TickTimer;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
public class Speed extends Module {
	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "OnGround", "Vanilla", "VortexHVH");
	public NumberSetting vanillaspeed = new NumberSetting("VSpeed", 0.5, 0, 1, 0.1);
    private int jumps;
	public Speed() {
		super("Speed", Keyboard.KEY_V, Category.MOVEMENT);
		this.addSettings(mode, vanillaspeed);
	}

	public void onDisable() {
		mc.timer.timerSpeed = 1f;
	}
	public void onEvent(Event event) {
		this.setDisplayName(name+" (" +mode.getMode()+")");
		if(event instanceof EventMotion) {
			EventMotion eventm = (EventMotion)event;
			
			if(mode.getMode().equalsIgnoreCase("vortexhvh")) {
				if(mc.gameSettings.keyBindForward.getIsKeyPressed()) {
					if(mc.thePlayer.onGround) {
						double speedValue = 0.29;
						double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
						double x = -Math.sin(yaw) * speedValue;
						double z = Math.cos(yaw) * speedValue;
						
						mc.thePlayer.motionX = x;
						mc.thePlayer.motionZ = z;
						mc.thePlayer.jump();
						mc.thePlayer.jump();
					}
				}
			}
		}
		
		if(event instanceof EventUpdate) {

			if(mode.getMode().equalsIgnoreCase("onground")) {
	        	EntityPlayerSP thePlayer = mc.thePlayer;
	
	            if (thePlayer == null || !MovementUtils.isMoving())
	                 return;
	
	             if (thePlayer.fallDistance > 3.994)
	                 return;
	             
	             if (thePlayer.isInWater() || thePlayer.isOnLadder() || thePlayer.isCollidedHorizontally)
	                 return;
	
	             thePlayer.posY -= 0.3993000090122223;
	             thePlayer.motionY = -1000.0;
	             thePlayer.cameraPitch = 0.3f;
	             thePlayer.distanceWalkedModified = 44.0f;
	             mc.timer.timerSpeed = 1f;
	
	             if (thePlayer.onGround) {
	                 thePlayer.posY += 0.3993000090122223;
	                 thePlayer.motionY = 0.3993000090122223;
	                 thePlayer.distanceWalkedOnStepModified = 44.0f;
	                 thePlayer.motionX *= 1.590000033378601;
	                 thePlayer.motionZ *= 1.590000033378601;
	                 thePlayer.cameraPitch = 0.0f;
	                 mc.timer.timerSpeed = 1.199f;
	            }
			} else if(mode.getMode().equalsIgnoreCase("vanilla")){
				if(MovementUtils.isMoving() && mc.thePlayer.onGround) mc.thePlayer.jump();
				MoveUtils.setSpeed(vanillaspeed.getValue());
			} 
		}
	}
	
	 
	    private double getBaseMoveSpeed() {
	        double baseSpeed = 0.2873;
	        if(mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
	            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
	            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
	        }
	        return baseSpeed;
	    }

	    private Block getBlock(AxisAlignedBB axisAlignedBB) {
	        for(int x = MathHelper.floor_double(axisAlignedBB.minX); x < MathHelper.floor_double(axisAlignedBB.maxX) + 1; ++x) {
	            for(int z = MathHelper.floor_double(axisAlignedBB.minZ); z < MathHelper.floor_double(axisAlignedBB.maxZ) + 1; ++z) {
	                final Block block = mc.theWorld.getBlockState(new BlockPos(x, (int) axisAlignedBB.minY, z)).getBlock();

	                if(block != null)
	                    return block;
	            }
	        }

	        return null;
	    }

	    private Block getBlock(double offset) {
	        return this.getBlock(mc.thePlayer.getEntityBoundingBox().offset(0.0, offset, 0.0));
	    }

	    private double round(double value) {
	        BigDecimal bd = new BigDecimal(value);
	        bd = bd.setScale(3, RoundingMode.HALF_UP);
	        return bd.doubleValue();
	    }
}