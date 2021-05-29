package lv.yourfriend.copeware;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import me.tireman.hexa.alts.Alt;
import me.tireman.hexa.alts.AltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Session;

import org.lwjgl.opengl.Display;
import org.reflections.Reflections;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import lv.yourfriend.copeware.command.Command;
import lv.yourfriend.copeware.command.CommandManager;
import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.events.listeners.EventChat;
import lv.yourfriend.copeware.events.listeners.EventKey;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.modules.Module.Category;
import lv.yourfriend.copeware.modules.combat.*;
import lv.yourfriend.copeware.modules.configs.*;
import lv.yourfriend.copeware.modules.movement.*;
import lv.yourfriend.copeware.modules.player.*;
import lv.yourfriend.copeware.modules.render.*;
import lv.yourfriend.copeware.settings.BooleanSetting;
import lv.yourfriend.copeware.settings.KeybindSetting;
import lv.yourfriend.copeware.settings.ModeSetting;
import lv.yourfriend.copeware.settings.NumberSetting;
import lv.yourfriend.copeware.settings.Setting;
import lv.yourfriend.copeware.ui.HUD;
import lv.yourfriend.copeware.util.font.FontUtil;

public class Client {
	
	public static String name = "Copeware", version = "1.0";
	public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();
	public static HUD hud = new HUD();
	public static Color color = Color.cyan;
	public static CommandManager commandManager = new CommandManager();
	
	public static boolean testing = true;
	
	public static void start() {
		FontUtil.bootstrap();;
		System.out.println("Starting " + name + " (" + version + ")");
		Display.setTitle(name + " " + version);
	
		Reflections reflections = new Reflections("lv.yourfriend.copeware.modules");
		
        for (Class <? extends Module> module : reflections.getSubTypesOf(Module.class)) {
        	try {
				modules.add(module.getDeclaredConstructor().newInstance());
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
        }
        
		/*
		 * Injected help:
		 * 
		 * EventRender is injected in EntityRenderer.java:1624
		 * EventRenderGui is injected in HUD.java:38
		 * HUD is injected in GuiIngame.java:354
		 * EventKey is injected in Client.java:75
		 * EventUpdate is injected in EntityPlayerSP.java:159 
		 * EventMotion is injected in EntityPlayerSP.java:163
		 * keyPress (EventKey) is injected in Minecraft.java:1936
		 * ESP is injected in RendererLivingEntity.java:320
		 * EventPacket is injected in NetworkManager.java:146
		 * EventChat is injected in EntityPlayerSP.java:270
		 * onShutdown is injected in Minecraft.java:1434
		 */
		
    	File alts = new File("alts.cope");

        try {
        	alts.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(alts));
            
            reader.lines().forEach(line -> {
                String[] fields = line.split("\\|"); 
                String pass = "";
                if(fields.length > 1) pass = fields[1];
                Alt a = new Alt(fields[0], pass);
                AltManager.registry.add(a);
            });
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadConfig("default", false);
	}
	
	public static void onEvent(Event event) {
		if(event instanceof EventChat) {
			commandManager.handleChat((EventChat)event);
		}
		
		for(Module module: modules) {
			if(!module.toggled) continue;
			
			module.onEvent(event);
		}
	}
	
	public static void loadConfig(String name, boolean userAction) {
    	File config = new File("config_"+name+".cope");
    	
        try {
        	if(!config.exists()) config.createNewFile();
        	
            BufferedReader reader = new BufferedReader(new FileReader(config));
            
            reader.lines().forEach(line -> {
                if(line.isEmpty()) return;
                
            	String[] fields = line.split(":"); 
    			for(Module module: modules) {
    				if(module.name.equalsIgnoreCase(fields[0])) {
    					if(fields[1].equals("STATUS")) {
    						
    						module.toggled = Boolean.parseBoolean(fields[2]);
    						if(module.toggled) {					
    							module.onEnable();
    						}
    					}
    					module.settings.forEach(setting -> {
    						if(setting.name.equalsIgnoreCase(fields[1])) {
    							if(setting instanceof BooleanSetting) {
    								((BooleanSetting)setting).setEnabled(Boolean.parseBoolean(fields[2]));
    							} else if(setting instanceof KeybindSetting) {
    								((KeybindSetting)setting).setKeyCode(Integer.parseInt(fields[2]));
    							} else if(setting instanceof ModeSetting) {
    								ModeSetting mode = (ModeSetting)setting;
    								mode.index = mode.modes.indexOf(fields[2]); 
    							} else if(setting instanceof NumberSetting) {
    								((NumberSetting)setting).setValue(Double.parseDouble(fields[2]));
    							}
    						}
    					});
    				}
    			}
    			
            	
            });
            
            if(userAction) {
            	Client.chat("Config " +name+ "sucessfully loaded.");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void saveConfig(String name, boolean userAction) {
		try {
			FileWriter fw = new FileWriter("config_"+name+".cope", false);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw);

			for(Module module: modules) {
				module.settings.forEach(setting -> {
					String value = "";

					if(setting instanceof BooleanSetting) {
						value = Boolean.toString(((BooleanSetting)setting).isEnabled());
					} else if(setting instanceof KeybindSetting) {
						value = Integer.toString(((KeybindSetting)setting).getKeyCode());
					} else if(setting instanceof ModeSetting) {
						value = ((ModeSetting)setting).getMode();
					} else if(setting instanceof NumberSetting) {
						value = Double.toString(((NumberSetting)setting).getValue());
					} else {
						System.out.println("Config fucked.");
						return;
					}
					
					out.println(module.name + ":" + setting.name + ":" + value);
				});
				out.println(module.name + ":STATUS:" + module.isEnabled());
			}
			if(userAction) {
				Client.chat("Config " + name + " sucessfully saved.");
			}
			out.close();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void keyPress(int key) {
		
		Client.onEvent(new EventKey(key));
		for(Module module: modules) {
			if(module.getKey() == key) {
				module.toggle();
			}
		}
	}
	
	public static List<Module> getModulesByCategory(Category category) {
		List<Module> modules = new ArrayList<Module>();
		
		for(Module module: Client.modules) {
			if(module.category == category) modules.add(module);
		}
			
		return modules;
	}
	
	public static Module getModuleByName(String name) {
		List<Module> modules = new ArrayList<Module>();
		
		for(Module module: Client.modules) {
			if(module.name.equalsIgnoreCase(name)) modules.add(module);
		}
		
		if(modules.size() == 0) {
			return null;
		} else {
			return modules.get(0);
		}
	}
	
	public static Setting getSettingByName(String settingname, String module) {
		List<Setting> settings = getModuleByName(module).settings;
		Setting settingresult = null;
		for(Setting setting: settings) {
			if(setting.name.equalsIgnoreCase(settingname)) {
				settingresult = setting;
			}
		}
		return settingresult;
	}

	public static void chat(String message) {
		message = name + " > " + message;
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
	}

	public static void onShutdown() {
		saveConfig("default", false);
	}

}
