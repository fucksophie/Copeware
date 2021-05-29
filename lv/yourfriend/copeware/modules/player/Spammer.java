package lv.yourfriend.copeware.modules.player;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventMotion;
import lv.yourfriend.copeware.events.listeners.EventUpdate;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.util.MovementUtils;
import lv.yourfriend.copeware.util.TickTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
public class Spammer extends Module {
	public Spammer() {
		super("Spammer", Keyboard.KEY_NONE, Category.PLAYER);
	}
	TickTimer help = new TickTimer();
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			help.update();
			if(help.hasTimePassed(20)) {
help.reset();
				mc.thePlayer.sendChatMessage("This is the funniest Minecraft challenge EVER. "+ " " + String.valueOf(Math.random()).substring(2));
			}
			}
        }
}