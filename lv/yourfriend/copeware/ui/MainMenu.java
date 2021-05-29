package lv.yourfriend.copeware.ui;

import java.awt.Color;
import java.awt.Font;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.util.font.FontUtil;
import me.tireman.hexa.alts.GuiAltManager;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import optifine.FontUtils;

public class MainMenu extends GuiScreen {
	String[] buttons = {"Singleplayer", "Multiplayer", "Settings", "Language", "quit w", "Alt Manager"};

	public MainMenu() {
		
	}
	
	public void initGui() {
		
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(new ResourceLocation("femboys/background.jpg"));
		this.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
		
//		/this.drawGradientRect(0, height-100, width, height, 0x00000000, Client.color.getRGB());
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.99, 0.99, 1);
		
		int count = 0;
		for(String button: buttons) {
			float x = (width/buttons.length)* count + (width/buttons.length)/2f +8 - mc.fontRendererObj.getStringWidth(button)/2f;
			float y = height-20;
			
			boolean hovering = (mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth(button) && mouseY < y + mc.fontRendererObj.FONT_HEIGHT);
			
			//this.drawCenteredString(mc.fontRendererObj, button, (width/buttons.length)* count + (width/buttons.length)/2.3f +8, height-20, hovering ? Client.color.getRGB() : -1);
			FontUtil.opensans20.drawCenteredString(button, (width/buttons.length)* count + (width/buttons.length)/2.3f +8, height-20, hovering ? Client.color.getRGB() : -1);
			
			count++;
		}
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(width/2f, height/2f, 0);
		GlStateManager.scale(5,5, 1);
		
		GlStateManager.translate(-(width/2f), -(height/2f), 0);
		
		this.drawCenteredString(mc.fontRendererObj, Client.name, width/2f, height/2f - mc.fontRendererObj.FONT_HEIGHT/2f, -1);
		
		GlStateManager.popMatrix();
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		int count = 0;
		
		for(String b: buttons) {
			float x = (width/buttons.length)* count + (width/buttons.length)/2f +8 - mc.fontRendererObj.getStringWidth(b)/2f;
			float y = height-20;
			
			if(mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth(b)+10 && mouseY < y + mc.fontRendererObj.FONT_HEIGHT+10) {
				switch(b) {
					case "Singleplayer":
						mc.displayGuiScreen(new GuiSelectWorld(this));
						break;
					case "Multiplayer":
						mc.displayGuiScreen(new GuiMultiplayer(this));
						break;
					case "Settings":
						mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
						break;
					case "Language":
						mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
						break;
					case "quit w":
						mc.shutdown();
						break;
					case "Alt Manager":
						mc.displayGuiScreen(new GuiAltManager());
						break;
				}
			}
			
			count++;
		}
	}
	
	public void onGuiClosed() {
		
	}
}
