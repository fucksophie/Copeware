package lv.yourfriend.copeware.modules.combat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.BooleanSetting;
import lv.yourfriend.copeware.settings.NumberSetting;
import lv.yourfriend.copeware.util.TimeHelper;
import lv.yourfriend.copeware.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.MathHelper;
public class Killaura extends Module {
	public Timer timer = new Timer();
	public NumberSetting range = new NumberSetting("range", 4, 1, 50, 0.1);
	public NumberSetting cps = new NumberSetting("cps", 12, 2, 20, 0.1);
	public BooleanSetting noswing = new BooleanSetting("noswing", false);
	public BooleanSetting autoblock = new BooleanSetting("autoblock", false);
	public BooleanSetting srots = new BooleanSetting("srotations", true);
	
	public Killaura() {
		super("Killaura", Keyboard.KEY_R, Category.COMBAT);
		
		this.addSettings(range, noswing, autoblock, srots, cps);
	}

	public void onEvent(Event y) {
		this.setDisplayName(name+" ("+range.getValue()+")");
		if(y instanceof EventMotion) {
			if(y.isPre()) {
				EventMotion event = (EventMotion)y;
				List<EntityLivingBase> targets = (List<EntityLivingBase>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
			
				targets = targets.stream().filter(e -> e.getDistanceToEntity(mc.thePlayer) < range.getValue() && e != mc.thePlayer && e.getHealth() != 0).collect(Collectors.toList());
				
				targets.sort(Comparator.comparingDouble(entity ->((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
				
				//targets = targets.stream().filter(EntityAnimal.class::isInstance).collect(Collectors.toList());
				if(!targets.isEmpty()) {
					EntityLivingBase target = targets.get(0);
					
					if(srots.isEnabled()) {
						event.setYaw(getRotations(target)[0]);
						event.setPitch(getRotations(target)[1]);
						mc.thePlayer.rotationYawHead = getRotations(target)[0];
					} else {
						mc.thePlayer.rotationYaw = getRotations(target)[0];
						mc.thePlayer.rotationPitch = getRotations(target)[1];
					}
					
					if(timer.hasTimeElapsed((long) (1000/cps.value), true)) {
						
						if(noswing.isEnabled()) {
							mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
						} else {
							mc.thePlayer.swingItem();
						}
						
						if(autoblock.isEnabled()) mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 20);
						
						mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
					}
				} 
			}
		}
	}

	public float[] getRotations(Entity entity) {
		if (entity == null)
			return null;
		double diffX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
		double diffY;
		if ((entity instanceof EntityLivingBase)) {
			EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
			diffY = entityLivingBase.posY + entityLivingBase.getEyeHeight() * 0.9D - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		} else {
			diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		}
		double diffZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);
		return new float[] { Minecraft.getMinecraft().thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw), Minecraft.getMinecraft().thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}

}