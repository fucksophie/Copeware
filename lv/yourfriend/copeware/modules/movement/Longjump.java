package lv.yourfriend.copeware.modules.movement;

import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.modules.Module.Category;
import lv.yourfriend.copeware.settings.ModeSetting;
import lv.yourfriend.copeware.util.MovementUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Longjump extends Module {
	public ModeSetting mode = new ModeSetting("Mode", "Matrix", "Matrix", "Redesky", "Redesky2");
	double oldY = 0;
	public Longjump() {
		super("Longjump", Keyboard.KEY_NONE, Category.MOVEMENT);
		this.addSettings(mode);
	}
    int ticks = 0;
    boolean wasOnGround = false;
	public int airTicks = 0;
	
	   public void onEnable() {
		   if(mode.getMode().equalsIgnoreCase("redesky")) {
		        mc.timer.timerSpeed = 0.7f;
		        mc.thePlayer.ticksExisted = 0;
		   } else if(mode.getMode().equalsIgnoreCase("redesky2")) {
			   
		   }
	    }
	 
	    public void onDisable() {
            if(mode.getMode().equalsIgnoreCase("redesky")) {
		        mc.thePlayer.speedInAir = 0.02f;
		        mc.timer.timerSpeed = 1.0f;
		        mc.thePlayer.ticksExisted = 0;
            } else if(mode.getMode().equalsIgnoreCase("matrix")) {
            	mc.timer.timerSpeed = 1;
            	ticks = 0;
            	mc.thePlayer.motionX = 0;
            	mc.thePlayer.motionZ = 0;
            } else if(mode.getMode().equalsIgnoreCase("redesky2")) {
            	mc.timer.timerSpeed = 1f;
            	mc.thePlayer.speedInAir = 0.02f;
            	oldY = 0;
            }
 	    }
	    
	public void onEvent(Event event) {
		this.setDisplayName(name+" (" +mode.getMode()+")");
		if(event instanceof EventMotion) {
			if(event.isPre()) {
                if(mode.getMode().equalsIgnoreCase("redesky")) {
	                mc.timer.timerSpeed = 0.15f;
	                float as = (0.04f - (airTicks * (0.04f / 100f)));
	                mc.thePlayer.speedInAir = as;
	
	                  mc.thePlayer.cameraYaw = 0.1f;
	                if (mc.thePlayer.onGround) {
	                    airTicks = 0;
	                    mc.thePlayer.jump();
	                } else {
	                    float motY = (0.05f - (airTicks * (0.05f / 1000f)));
	                    airTicks++;
	                    mc.thePlayer.motionY += motY;
	                }
                } else if(mode.getMode().equalsIgnoreCase("matrix")) {
                	if (!mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWeb) {
                        mc.timer.timerSpeed = 0.2f;
                        ticks++;
						if (mc.thePlayer.onGround) {
                            wasOnGround = true;
                        }
                        if (!MovementUtils.isMoving()) {
                            if (ticks == 1 && wasOnGround) {
                                double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                                mc.thePlayer.motionX = 6 * -Math.sin(yaw);
                                mc.thePlayer.motionZ = 6 * Math.cos(yaw);
                                mc.thePlayer.jump();
                            }
                            if (ticks == 2 && wasOnGround) {
                                double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                                mc.thePlayer.motionX = 5 * -Math.sin(yaw);
                                mc.thePlayer.motionZ = 5 * Math.cos(yaw);
                                mc.thePlayer.jump();
                                wasOnGround = false;
                            }
                        } else {
                            this.toggle();
                        }
                        if ((mc.thePlayer.onGround || mc.thePlayer.isCollidedHorizontally) && ticks > 2) {
                        	this.toggle();
                        }
                    } else {
                    	this.toggle();
                    }
                } else if(mode.getMode().equalsIgnoreCase("redesky2")) {
                	if(mc.thePlayer.onGround) oldY = mc.thePlayer.posY;
                	
                	mc.thePlayer.speedInAir = 0.06f;
                	
                	if(mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                		mc.thePlayer.motionY = 0.52727;
                		mc.timer.timerSpeed = 0.04f;
                	} else {
                		mc.timer.timerSpeed = 1f;
                	}
                }
 		 	}

		}
	}
}