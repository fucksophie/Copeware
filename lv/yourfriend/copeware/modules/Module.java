package lv.yourfriend.copeware.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import lv.yourfriend.copeware.events.Event;
import lv.yourfriend.copeware.settings.KeybindSetting;
import lv.yourfriend.copeware.settings.Setting;
import net.minecraft.client.Minecraft;

public class Module {
	
	public String name;
	public String displayName;
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean toggled;
	public KeybindSetting keyCode = new KeybindSetting(0);
	public Category category;
	public Minecraft mc = Minecraft.getMinecraft();
	public List<Setting> settings = new ArrayList<Setting>();
	
	public Module(String name, int key, Category category) {
		this.name = name;
		keyCode.code = key;
		this.category = category;
		this.addSettings(keyCode);
		this.displayName = name;
	}
	
	public void addSettings(Setting... settings) {
		this.settings.addAll(Arrays.asList(settings));
		this.settings.sort(Comparator.comparingInt(s->s==keyCode?1:0));
	}
	
	public Setting getSetting(String name) {
		Setting wow = new Setting();
		
		for(Setting s: this.settings) {
			if(s.name.equalsIgnoreCase(name)) {
				wow = s;
			}
		}
		
		return wow;
	}
	
	public boolean isEnabled() {
		return toggled;
	}
	
	public void onEvent(Event event) {}
	
	public int getKey() {
		return keyCode.code;
	}
	
	public void toggle() {
		toggled = !toggled;
		
		if(toggled) {
			onEnable();
		} else {
			onDisable();
		}
	}
	
	public void onEnable() {}
	
	public void onDisable() {}
	
	public enum Category {
		COMBAT("Combat"),
		MOVEMENT("Movement"),
		RENDER("Render"),
		PLAYER("Player"),
		CONFIGS("Configs"),
		WORLD("World");
		
		public String name;

		Category(String name) {
			this.name = name;
		}
	}
}
