package lv.yourfriend.copeware.modules.render;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventRenderGui;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.BooleanSetting;
import lv.yourfriend.copeware.settings.NumberSetting;
import lv.yourfriend.copeware.util.MovementUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class Animations extends Module {

    public static int AnimationsState = 0;
    public NumberSetting itemSize = new NumberSetting("itemsize", 1.0, 0.0, 2.0, 1);
    public NumberSetting swordBlockY = new NumberSetting("SwordBlockY", -0.2,-2.0,2.0, 1);
    public NumberSetting itemY = new NumberSetting("ItemY", -0.2, -2.0, 2.0, 1);
    public BooleanSetting ViewBobbing = new BooleanSetting("ViewBobbing", true);
   
	public Animations() {
		super("Animations", Keyboard.KEY_NONE, Category.RENDER);
		this.addSettings(itemSize, swordBlockY, itemY, ViewBobbing);
	}
	
    @Override
    public void onEnable() {
        AnimationsState = 1;
    }

    public void onDisable() {
        AnimationsState = 0;
    }


    public void onEvent(Event e) {
        if(!ViewBobbing.isEnabled()) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.cameraPitch = 0f;
                mc.thePlayer.cameraYaw = 0f;
            }
        }

    }
}