package lv.yourfriend.copeware.modules.player;

import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.settings.BooleanSetting;
import lv.yourfriend.copeware.settings.NumberSetting;
import lv.yourfriend.copeware.util.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {
	TimeHelper timer = new TimeHelper();
	
	public static BooleanSetting openinv = new BooleanSetting("openinv", false);
	public static NumberSetting delay = new NumberSetting("Speed", 350.0F, 1.0F, 1000.0F, 50.0F);

	public AutoArmor() {
		super("AutoArmor", Keyboard.KEY_NONE, Category.PLAYER);
		this.addSettings(openinv, delay);
	}
	
	
	public void onEvent(Event event) {
		if(event instanceof EventUpdate) {
		      if (this.timer.hasPassed(delay.getValue()) && (this.mc.currentScreen != null || !openinv.isEnabled()) && !(this.mc.currentScreen instanceof GuiChat)) {
		          for(int b = 5; b <= 8; ++b) {
		             if (this.equipArmor(b)) {
		                this.timer.reset();
		                break;
		             }
		          }
		       }
		}
	}
	private boolean equipArmor(int b) {
	      int currentProtection = -1;
	      byte slot = -1;
	      ItemArmor current = null;
	      if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b).getStack() != null && Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b).getStack().getItem() instanceof ItemArmor) {
	         current = (ItemArmor)Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b).getStack().getItem();
	         currentProtection = current.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b).getStack());
	      }

	      for(byte i = 9; i <= 44; ++i) {
	         ItemStack stack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
	         if (stack != null && stack.getItem() instanceof ItemArmor) {
	            ItemArmor armor = (ItemArmor)stack.getItem();
	            int armorProtection = armor.damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack);
	            if (this.checkArmor(armor, b) && (current == null || currentProtection < armorProtection)) {
	               currentProtection = armorProtection;
	               current = armor;
	               slot = i;
	            }
	         }
	      }

	      if (slot != -1) {
	         boolean isNull = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b).getStack() == null;
	         if (!isNull) {
	            this.dropSlot(b);
	            return true;
	         } else {
	            this.clickSlot(slot, 0, true);
	            return true;
	         }
	      } else {
	         return false;
	      }
	   }

	   private boolean checkArmor(ItemArmor item, int b) {
	      return b == 5 && item.getUnlocalizedName().startsWith("item.helmet") || b == 6 && item.getUnlocalizedName().startsWith("item.chestplate") || b == 7 && item.getUnlocalizedName().startsWith("item.leggings") || b == 8 && item.getUnlocalizedName().startsWith("item.boots");
	   }

	   private void clickSlot(int slot, int mouseButton, boolean shiftClick) {
	      this.mc.playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0, Minecraft.getMinecraft().thePlayer);
	   }

	   private void dropSlot(int slot) {
	      this.mc.playerController.windowClick(0, slot, 1, 4, Minecraft.getMinecraft().thePlayer);
	   }
}
