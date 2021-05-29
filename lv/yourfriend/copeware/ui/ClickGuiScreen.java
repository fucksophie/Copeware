package lv.yourfriend.copeware.ui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes.Name;

import lv.yourfriend.copeware.Client;
import lv.yourfriend.copeware.modules.Module;
import lv.yourfriend.copeware.modules.Module.Category;
import lv.yourfriend.copeware.settings.BooleanSetting;
import lv.yourfriend.copeware.settings.KeybindSetting;
import lv.yourfriend.copeware.settings.ModeSetting;
import lv.yourfriend.copeware.settings.NumberSetting;
import me.imfr0zen.guiapi.ClickGui;
import me.imfr0zen.guiapi.Colors;
import me.imfr0zen.guiapi.GuiFrame;
import me.imfr0zen.guiapi.components.Button;
import me.imfr0zen.guiapi.components.GuiComponent;
import me.imfr0zen.guiapi.components.GuiGetKey;
import me.imfr0zen.guiapi.components.GuiLabel;
import me.imfr0zen.guiapi.components.GuiSlider;
import me.imfr0zen.guiapi.components.GuiToggleButton;
import me.imfr0zen.guiapi.components.GuiTree;
import me.imfr0zen.guiapi.listeners.ExtendListener;
import me.imfr0zen.guiapi.listeners.KeyListener;
import me.imfr0zen.guiapi.listeners.ValueListener;

public class ClickGuiScreen extends ClickGui {

	public ClickGuiScreen() {}
	Map<Category, GuiFrame> frames = new HashMap<Category, GuiFrame>();
	Map<Integer, Integer> more = new HashMap<Integer, Integer>();
	@Override
	public void initGui() {
		Color color = Color.getColor(((ModeSetting)Client.getSettingByName("color", "ClickGui")).getMode());
		/**
		 * Set the Button Color.
		 */
		
		Colors.setButtonColor(Client.color.darker().darker().getRed(), Client.color.darker().darker().getGreen(), Client.color.darker().darker().getBlue(), 200);
		Colors.FONT_COLOR = Client.color.getRGB();
		int count = 1;
		for(Category category: Module.Category.values()) {
			count += 1;
			GuiFrame categoryFrame = new GuiFrame(category.name, 30*count, 50*count);
			for(Module module: Client.getModulesByCategory(category)) {
				Button moduleButton = new Button(module.name);
				moduleButton.addClickListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						module.toggle();
					}
				});
				
				moduleButton.addExtendListener(new ExtendListener() {

					@Override
					public void addComponents() {
						module.settings.forEach(setting -> {
							
							if(setting instanceof BooleanSetting) {
								BooleanSetting bool = (BooleanSetting)setting;
								GuiToggleButton button = new GuiToggleButton(bool.name);
								
								button.setToggled(bool.enabled);
								
								button.addClickListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										bool.toggle();
										button.setToggled(bool.enabled);
									}
								});
								
								add(button);
							}
							
							
							if(setting instanceof NumberSetting) {
								NumberSetting number = (NumberSetting)setting;
								
								GuiSlider slider = new GuiSlider(number.name, number.minimum, number.maximum, number.value, 1);
								
								slider.addValueListener(new ValueListener() {
									@Override
									public void valueUpdated(double value) {
										number.setValue(value);
									}

									@Override
									public void valueChanged(double current) {}
								});
								add(slider);
							}
							
							if(setting instanceof KeybindSetting) {
								KeybindSetting keybind = (KeybindSetting)setting;
								
								GuiGetKey key = new GuiGetKey("Keybind:", keybind.getKeyCode());
								
								key.addKeyListener(new KeyListener() {

									@Override
									public void keyChanged(int key) {
										keybind.setKeyCode(key);
									}
									
								});
								
								add(key);
							}
							
							if(setting instanceof ModeSetting) {
								ModeSetting mode = (ModeSetting)setting;
								
								List<GuiToggleButton> components = new ArrayList<GuiToggleButton>();
								
								for(String modes: mode.modes) {
									GuiToggleButton button = new GuiToggleButton(modes);
									button.addClickListener(new ActionListener() {

										@Override
										public void actionPerformed(ActionEvent e) {
											mode.index = mode.modes.indexOf(modes);
										}
										
									});
									components.add(button);
								}
								
								GuiTree tree = new GuiTree(mode.name, components);
								
								add(tree);
							}
						});
					}
					
				});
				categoryFrame.addButton(moduleButton);
			}
			
			frames.put(category, categoryFrame);
			addFrame(categoryFrame);
		}
		
		super.initGui();
	}

	public void onGuiClosed() {
	};
}
