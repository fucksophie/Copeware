package lv.yourfriend.copeware.command;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.command.impl.*;
import lv.yourfriend.copeware.events.listeners.EventChat;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class CommandManager {
	public List<Command> commands = new ArrayList<Command>();
	public String prefix = "!";
	
	public CommandManager() {
		setup();
	}
	
	public void setup() {
		Reflections reflections = new Reflections("lv.yourfriend.copeware.command.impl");
		
        for (Class <? extends Command> command : reflections.getSubTypesOf(Command.class)) {
        	try {
				commands.add(command.getDeclaredConstructor().newInstance());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
        }
	}
	
	public void handleChat(EventChat event) {
		String message = event.getMessage();
		
		if(!message.startsWith(prefix)) return;
		
		event.setCancelled(true);
		
		message = message.substring(prefix.length());
		boolean foundCommand = false;
		if(message.split("").length > 0) {
			String commandName = message.split(" ")[0];
			
			for(Command command: commands) {
				if(command.aliases.contains(commandName) || command.name.equalsIgnoreCase(commandName)) {
					command.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
					foundCommand = true;
					break;
				}
			}
		}
		
		if(!foundCommand) {
			Client.chat("Your command didn't exist, so I sent it as it is.");
			
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
		}
	}
}
