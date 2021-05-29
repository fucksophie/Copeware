package me.imfr0zen.guiapi.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import me.imfr0zen.guiapi.ClickGui;
import me.imfr0zen.guiapi.Colors;
import me.imfr0zen.guiapi.GuiFrame;
import me.imfr0zen.guiapi.RenderUtil;
import me.imfr0zen.guiapi.listeners.ExtendListener;

/**
 * 
 * @author ImFr0zen
 *
 */
public class Button implements GuiComponent {

	public static int extendedId = -1;

	private int id;
	private int textWidth;
	private int width;
	private int posX;
	private int posY;

	private String text;

	private ArrayList<ActionListener> clicklisteners = new ArrayList<ActionListener>();
	private ArrayList<GuiComponent> guicomponents = new ArrayList<GuiComponent>();

	public Button(String text) {
		this.text = text;
		this.textWidth = ClickGui.FONTRENDERER.getStringWidth(getText());
		this.id = ClickGui.currentId += 1;
	}

	/**
	 * @param actionlistener
	 *            gets called when clicking this button.
	 */
	public void addClickListener(ActionListener actionlistener) {
		clicklisteners.add(actionlistener);
	}

	/**
	 * @param actionlistener
	 *            gets called when rightclicking this button.
	 */
	public void addExtendListener(ExtendListener listener) {
		listener.addComponents();

		guicomponents.addAll(listener.getComponents());
	}

	/**
	 * @return the text on this button
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return The unique ID of this button.
	 */
	public int getButtonId() {
		return id;
	}

	@Override
	public void render(int posX, int posY, int width, int mouseX, int mouseY) {
		this.posX = posX;
		this.posY = posY;

		this.width = width;

		int height = getHeight();
		int color = Colors.buttonColorLight;

		/**
		 * If you want to use the buttons in Apinity Style: <code>
		 * if (!module.isEnabled()) {
		 * 	color = Colors.backGroundColor;
		 * }
		 * </code>
		 */
		if (RenderUtil.isHovered(posX, posY, width, height, mouseX, mouseY)) {
			color = Colors.buttonColor;

			if (Mouse.getEventButtonState()) {
				color = Colors.buttonColorDark;
			}
		}

		RenderUtil.drawRect(posX, posY, posX + width - 1, posY + height, color);

		/**
		 * Change this to false or remove the if clause, if you want the text to
		 * be centered.
		 */
		RenderUtil.drawString(text, posX + 1, posY + 2, Colors.FONT_COLOR);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (GuiFrame.draggingId == -1 && RenderUtil.isHovered(posX, posY, width, getHeight(), mouseX, mouseY)) {
			if (mouseButton == 1) {
				if (extendedId != id) {
					extendedId = id;
				} else {
					extendedId = -1;
				}
			} else if (mouseButton == 0) {
				for (ActionListener listener : clicklisteners) {
					listener.actionPerformed(new ActionEvent(this, id, "click", System.currentTimeMillis(), 0));
				}
			}
		}
		
		if (extendedId == id) {
			for (GuiComponent component : guicomponents) {
				component.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}

	@Override
	public void keyTyped(int keyCode, char typedChar) {
		if (extendedId == id) {
			for (GuiComponent component : guicomponents) {
				component.keyTyped(keyCode, typedChar);
			}
		}
	}

	@Override
	public int getWidth() {
		return textWidth + 5;
	}

	@Override
	public int getHeight() {
		return ClickGui.FONTRENDERER.FONT_HEIGHT + 3;
	}

	public ArrayList<GuiComponent> getComponents() {
		return guicomponents;
	}

}
