package lv.yourfriend.copeware.ui;

import java.awt.Color;
import java.util.Collections;
import java.util.Comparator;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.listeners.EventRenderGui;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class HUD {
	public Minecraft mc = Minecraft.getMinecraft();
	
	public void draw() {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		FontRenderer fr = mc.fontRendererObj;
		
		Client.modules.sort(Comparator.comparingInt(m -> 
			fr.getStringWidth(((Module)m).getDisplayName()))
					.reversed()
		);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(4, 4, 0);
		GlStateManager.scale(3,3, 1);
		GlStateManager.translate(-4, -4, 0);
		int lol = 6;
		for(String chr: Client.name.split("")) {
			fr.drawString(chr, 6*lol-30, 6, rainbow(150*lol));
			lol++;
		}
		//fr.drawStringWithDropShadow(Client.name, 6, 6, rainbow(0));
		
		GlStateManager.popMatrix();
		fr.drawStringWithDropShadow("v" + Client.version, 158, 26, -1);
		
		if(Client.testing && mc.currentScreen == null && mc.getCurrentServerData() != null) {
			fr.drawStringWithDropShadow("Testing " + Client.name + " on " + mc.getCurrentServerData().serverIP, 0, sr.getScaledHeight()-10, rainbow(0));
		}
		
		int count = 0;
		Client.onEvent(new EventRenderGui());
		
		for(Module module: Client.modules) {
			if(!module.toggled || module.getDisplayName() == "Tabgui") continue;
			
			double offset = count*(fr.FONT_HEIGHT+6);
			count++;
			
			Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(module.getDisplayName()) - 12, offset, sr.getScaledWidth()-4, 6 + fr.FONT_HEIGHT+offset, 0x90000000);
			Gui.drawRect(sr.getScaledWidth() - 4, offset, sr.getScaledWidth(), 6 + fr.FONT_HEIGHT+offset, rainbow(count * 150));
			// coruppots all fonts idk whats wrong with it tbh..
			//FontUtil.bootstrap();
			//FontUtil.opensans19.drawString(module.getDisplayName(), sr.getScaledWidth() - fr.getStringWidth(module.getDisplayName()) - 7, 4+offset, -1);
			fr.drawStringWithDropShadow(module.getDisplayName(), sr.getScaledWidth() - fr.getStringWidth(module.getDisplayName()) - 7, 4+offset, rainbow(count * 150));
		}
	}

	public static int rainbow(long offset) {
		float hue = ((System.currentTimeMillis()+offset)% (int)(4*1000) / (float)(4*1000));
		int color = Color.HSBtoRGB(hue, 0.8f, 1);
		return color;
	}

}
