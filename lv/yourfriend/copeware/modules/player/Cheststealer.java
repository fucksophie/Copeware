package lv.yourfriend.copeware.modules.player;
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
import lv.yourfriend.copeware.util.TickTimer;
import lv.yourfriend.copeware.util.TimeHelper;
import lv.yourfriend.copeware.util.Timer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
public class Cheststealer extends Module {
	
	public NumberSetting delay = new NumberSetting("Delay", 2, 0, 7, 0.5);
	   private TickTimer timer = new TickTimer();
	public Cheststealer() {
		super("Cheststealer", Keyboard.KEY_NONE, Category.PLAYER);
		this.addSettings(delay);
	}

	public void onEvent(Event y) {
		if(y instanceof EventUpdate) {
			if(!mc.inGameHasFocus && mc.currentScreen instanceof GuiChest) {
				if(!isContainerEmpty(mc.thePlayer.openContainer)) {
					int slotId = this.getNextSlotInContainer(mc.thePlayer.openContainer);
					
					if(timer.hasTimePassed(delay.getValue())) {
						mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, slotId, 0, 1, mc.thePlayer);
						timer.reset();
					}
					timer.update();
				} else {
					mc.thePlayer.closeScreen();
				}
			}
		}
	}
	
    private boolean isContainerEmpty(Container container) {
        int slotAmount;
        int n = slotAmount = container.inventorySlots.size() == 90 ? 54 : 27;
        for (int i = 0; i < slotAmount; ++i) {
            if (!container.getSlot(i).getHasStack()) {
                continue;
            }
            return false;
        }
        return true;
    }
    
    private int getNextSlotInContainer(Container container) {
        int slotAmount;
        int n = slotAmount = container.inventorySlots.size() == 90 ? 54 : 27;
        for (int i = 0; i < slotAmount; ++i) {
            if (container.getInventory().get(i) == null) {
                continue;
            }
            return i;
        }
        return -1;
    }
  
}