package lv.yourfriend.copeware.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

/*
* Movement Utils - https://github.com/CCBlueX/LiquidBounce/blob/legacy/shared/main/java/net/ccbluex/liquidbounce/utils/MovementUtils.kt
* 				   CCBlueX and Contributors
*/

public class MovementUtils {
	public static Minecraft mc = Minecraft.getMinecraft();

	public static double getDirection() {
        EntityPlayerSP thePlayer = mc.thePlayer;
        double rotationYaw = thePlayer.rotationYaw;
        if (thePlayer.moveForward < 0f) rotationYaw += 180f;
        float forward = 1f;
        if (thePlayer.moveForward < 0f) forward = -0.5f; else if (thePlayer.moveForward > 0f) forward = 0.5f;
        if (thePlayer.moveStrafing > 0f) rotationYaw -= 90f * forward;
        if (thePlayer.moveStrafing < 0f) rotationYaw += 90f * forward;
        return Math.toRadians(rotationYaw);
	}

    public static boolean isOnGround(double height) {
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    
	public static Boolean isMoving() {
		return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0f || mc.thePlayer.movementInput.moveStrafe != 0f);
	}
	
    public static void setMotion(double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
        	mc.thePlayer.motionX = 0;
        	mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)); 
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
        }
    }
    
    public static boolean hasMotion() {
        return mc.thePlayer.motionX != 0D && mc.thePlayer.motionZ != 0D && mc.thePlayer.motionY != 0D;
    }
    
	public static void strafe(float speed) {
		if(!isMoving()) return;
		
		mc.thePlayer.motionX = -Math.sin(getDirection()) * speed;

		mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
	}
	
	public static double getBlocksPerSecond() {
		
		if (mc.thePlayer == null || mc.thePlayer.ticksExisted < 1) {
			return 0;
		}
		
		return mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ) * (Minecraft.getMinecraft().timer.ticksPerSecond * Minecraft.getMinecraft().timer.timerSpeed);
	}
	
    public static float getSpeed() {
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

    public static void strafe() {
        strafe(getSpeed());
    }
}

