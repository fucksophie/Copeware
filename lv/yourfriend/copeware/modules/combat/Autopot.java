package lv.yourfriend.copeware.modules.combat;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.EventType;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventPacket;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.*;
import lv.yourfriend.copeware.util.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Autopot extends Module {
	private boolean jumping;
	private TimeHelper timer = new TimeHelper();
	BooleanSetting jump = new BooleanSetting("Jump", false);
	NumberSetting delay = new NumberSetting("delay", 100, 0.0, 2000, 1);
	NumberSetting health = new NumberSetting("Health", 12, 1, 20, 1);
	
	public Autopot() {
		super("Autopot", Keyboard.KEY_NONE, Category.COMBAT);
		this.addSettings(jump, health, delay);
	}

	public void onEvent(Event e) {
		if(e instanceof EventMotion) {
			EventMotion event = (EventMotion)e;
			if(e.isPre()) {
				int potSlot = this.getPotFromInventory();
		         if (mc.thePlayer.getHealth() < health.getValue() && this.timer.hasPassed(delay.getValue()) && potSlot != -1) {
		            if (jump.isEnabled()) {
		               event.pitch = -89.5F;
		               if (mc.thePlayer.onGround) {
		                  mc.thePlayer.jump();
		  				System.out.println("askf");
		                  this.jumping = true;
		               }
		            } else {
		               event.pitch = 89.5F;
		            }
		         }

		      }
			
			if(event.type == EventType.POST) {
		        int potSlot = this.getPotFromInventory();
		         if (mc.thePlayer.getHealth() < health.getValue() && this.timer.hasPassed(delay.getValue()) && potSlot != -1 && mc.thePlayer.isCollidedVertically) {
		            int prevSlot = mc.thePlayer.inventory.currentItem;
		            System.out.println("askf5");
		            if (potSlot < 9) {
		            	System.out.println("askf2");
		            	mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(potSlot));
		               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
		               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(prevSlot));
		               this.mc.playerController.syncCurrentPlayItem();
		               mc.thePlayer.inventory.currentItem = prevSlot;
		            } else {
		            	System.out.println("askf3");
		               this.swap(potSlot, mc.thePlayer.inventory.currentItem + (mc.thePlayer.inventory.currentItem < 8 ? 1 : -1));
		               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem + (mc.thePlayer.inventory.currentItem < 8 ? 1 : -1)));
		               mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
		               mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(prevSlot));
		            }

		            this.timer.reset();
		         }
			}
			
			}
		
		}

protected void swap(int slot, int hotbarNum) {
    this.mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
 }

 private int getPotFromInventory() {
    for(int i = 0; i < 35; ++i) {
       if (mc.thePlayer.inventory.mainInventory[i] != null) {
          ItemStack is = mc.thePlayer.inventory.mainInventory[i];
          Item item = is.getItem();
          if (item instanceof ItemPotion) {
             List poteffs = ((ItemPotion)item).getEffects(is);
             Iterator var6 = poteffs.iterator();

             while(var6.hasNext()) {
                PotionEffect effect = (PotionEffect)var6.next();
                if (effect.getPotionID() == Potion.heal.id || effect.getPotionID() == Potion.regeneration.id) {
                   return i;
                }
             }
          }
       }
    }

    return -1;
 }
}