package lv.yourfriend.copeware.command.impl;

import com.mojang.realmsclient.dto.RealmsServer.McoServerComparator;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.command.Command;
import lv.yourfriend.copeware.modules.Module;
import net.minecraft.client.Minecraft;

public class Vclip  extends Command {
	Minecraft mc = Minecraft.getMinecraft();
	public Vclip() {
		super("vclip", "vcvlips", "vclip <num>", "v");
	}

	@Override
	public void onCommand(String[] args, String command) {
		
		if(args.length > 0) {
			try { 
				int num = Integer.parseInt(args[0]);
				
				mc.thePlayer.setPosition(mc.thePlayer.posX,mc.thePlayer.posY+num, mc.thePlayer.posZ);
			} catch(NumberFormatException e) {
				Client.chat(args[0] + "was not a valid number.");
			}
		}
	}
}