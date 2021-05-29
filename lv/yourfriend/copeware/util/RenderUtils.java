package lv.yourfriend.copeware.util;
import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
public class RenderUtils {
	// Made by lavaflowglow 11/19/2020 3:39 AM
	
		public static boolean SetCustomYaw = false;
		public static float CustomYaw = 0;
		
		public static void setCustomYaw(float customYaw) {
			CustomYaw = customYaw;
			SetCustomYaw = true;
			mc.thePlayer.rotationYawHead = customYaw;
		}
		
		public static void resetPlayerYaw() {
			SetCustomYaw = false;
		}
		
		public static float getCustomYaw() {
			
			return CustomYaw;
			
		}
		public static boolean SetCustomPitch = false;
		public static float CustomPitch = 0;
		
		public static void setCustomPitch(float customPitch) {
			CustomPitch = customPitch;
			SetCustomPitch = true;
		}
		
		public static void resetPlayerPitch() {
			SetCustomPitch = false;
		}
		
		public static float getCustomPitch() {
			
			return CustomPitch;
			
		}
		
		// Made by lavaflowglow 11/19/2020 3:39 AM
		
		public static Minecraft mc = Minecraft.getMinecraft();
		public static WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
		public static Tessellator tessellator = Tessellator.getInstance();
		
		// Someone gave me this code

		

		
		public static void setColorForIcon(Color color) {
			GlStateManager.enableBlend();
			GlStateManager.color(((float) color.getRed()) / 255, ((float) color.getGreen()) / 255,
					((float) color.getBlue()) / 255);
		}
		
		public static void resetColor() {
			GlStateManager.color(1, 1, 1, 1);
		}
}
