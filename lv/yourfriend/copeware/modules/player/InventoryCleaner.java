package lv.yourfriend.copeware.modules.player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventRender;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.util.MovementUtils;
import lv.yourfriend.copeware.util.TickTimer;
import lv.yourfriend.copeware.util.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InventoryCleaner extends Module {
	public InventoryCleaner() {
		super("InventoryCleaner", Keyboard.KEY_NONE, Category.PLAYER);
	}
	   private int currentSlot = 9;
	   private TimeHelper timeHelper = new TimeHelper();
	   private double handitemAttackValue;
    public static List<Item> itemBlacklist = Arrays.asList(Items.fishing_rod);
    public void onEnable() {
        this.currentSlot = 9;
        this.handitemAttackValue = getAttackDamage(mc.thePlayer.getHeldItem());
     }

     public void onEvent(Event event) {
        if (this.isEnabled()) {
           if (this.currentSlot >= 45) {
              this.toggle();
           } else if (this.timeHelper.hasPassed(130.0D)) {
              this.handitemAttackValue = getAttackDamage(mc.thePlayer.getHeldItem());
              ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(this.currentSlot).getStack();
              if (isShit(itemStack) && getAttackDamage(itemStack) <= this.handitemAttackValue && itemStack != mc.thePlayer.getHeldItem()) {
                 this.mc.playerController.windowClick(0, this.currentSlot, 1, 4, mc.thePlayer);
                 this.timeHelper.reset();
              }

              ++this.currentSlot;
           }
        }
     }

     public static boolean isShit(ItemStack itemStack) {
        if (itemStack == null) {
           return false;
        } else if (itemStack.getItem().getUnlocalizedName().contains("bow")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("arrow")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("stick")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("egg")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("flower pot")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("stick")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("string")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("flint")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("compass")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("feather")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("bucket")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("chest")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("snow")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("fish")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("enchant")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("exp")) {
           return true;
        } else if (itemStack.getItem().getUnlocalizedName().contains("tnt")) {
           return true;
        } else if (itemStack.getItem() instanceof ItemPickaxe) {
           return true;
        } else if (itemStack.getItem() instanceof ItemTool) {
           return true;
        } else if (itemStack.getItem() instanceof ItemArmor) {
           return true;
        } else if (itemStack.getItem() instanceof ItemSword) {
           return true;
        } else {
           return itemStack.getItem().getUnlocalizedName().contains("potion") && isBadPotion(itemStack);
        }
     }

     public static boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
           ItemPotion potion = (ItemPotion)stack.getItem();
           Iterator var3 = potion.getEffects(stack).iterator();

           while(var3.hasNext()) {
              Object o = var3.next();
              PotionEffect effect = (PotionEffect)o;
              if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.harm.getId()) {
                 return true;
              }
           }
        }

        return false;
     }

     private static double getAttackDamage(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
           ItemSword sword = (ItemSword)itemStack.getItem();
           return (double)((float)EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, itemStack) + sword.func_150931_i());
        } else {
           return 0.0D;
        }
     }

}