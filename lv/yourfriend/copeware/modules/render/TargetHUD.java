package lv.yourfriend.copeware.modules.render;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventRender;
import lv.yourfriend.copeware.events.listeners.EventRenderGui;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.BooleanSetting;
import lv.yourfriend.copeware.settings.NumberSetting;
import lv.yourfriend.copeware.ui.HUD;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class TargetHUD extends Module {
	public static boolean isOn = false;
	
	public TargetHUD() {
		super("TargetHUD", Keyboard.KEY_NONE, Category.RENDER);
	}
	FontRenderer fr = mc.fontRendererObj; 
	
	public void onEvent(Event event) {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		if(event instanceof EventRenderGui) {
			
			List<EntityLivingBase> targets = (List<EntityLivingBase>) mc.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).collect(Collectors.toList());
			
			targets = targets.stream().filter(e -> e.getDistanceToEntity(mc.thePlayer) < ((NumberSetting)Client.getSettingByName("range", "killaura")).getValue() && e != mc.thePlayer && e.getHealth() != 0).collect(Collectors.toList());
			
			targets.sort(Comparator.comparingDouble(entity ->((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
			
			targets = targets.stream().filter(EntityPlayer.class::isInstance).collect(Collectors.toList());
			if(!targets.isEmpty() && Client.getModuleByName("Killaura").isEnabled()) {
				isOn = true;
				EntityLivingBase target = targets.get(0);
				Gui.drawRect(120+fr.getStringWidth(target.getName())/2, 36, 6, 60, 0x90000000);
				fr.drawStringWithDropShadow(Math.floor(target.getHealth()) + "/" + target.getMaxHealth(), 70+fr.getStringWidth(target.getName())/2, 45, HUD.rainbow(100));
				fr.drawStringWithDropShadow(target.getName(), 15, 40, HUD.rainbow(200));
				fr.drawStringWithDropShadow((target.getHeldItem() != null ? target.getHeldItem().getDisplayName() : "None"), 15, 50, HUD.rainbow(300));
			} else {
				isOn = false;
			}
		}
	}
	
	public void printCentered(String text, double y, int t) {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		fr.drawStringWithDropShadow(text, sr.getScaledWidth()-500, y, t);
	}
}