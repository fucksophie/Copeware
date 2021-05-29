package me.imfr0zen.guiapi.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import me.imfr0zen.guiapi.Colors;
import me.imfr0zen.guiapi.RenderUtil;

/**
 * 
 * A Button that gets toggled when clicking
 * 
 * @author ImFr0zen
 *
 */
public class GuiToggleButton implements GuiComponent {

	private boolean toggled;

	private int posX;
	private int posY;

	private String text;

	private ArrayList<ActionListener> clicklisteners = new ArrayList<ActionListener>();

	public GuiToggleButton(String text) {
		this.text = text;
	}

	/**
	 * @param actionlistener
	 *            gets called when clicking this button.
	 */
	public void addClickListener(ActionListener actionlistener) {
		clicklisteners.add(actionlistener);
	}
	
	/**
	 * @return The button-state
	 */
	public boolean isToggled() {
		return toggled;
	}

	/**
	 * Sets the button state
	 * @param toggled
	 */
	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}

	@Override
	public void render(int posX, int posY, int width, int mouseX, int mouseY) {
		this.posX = posX;
		this.posY = posY;

		int w = RenderUtil.getWidth(text);

		int color = 0x45000000;

		if (toggled) {
			color = Colors.buttonColorLight;

			if (RenderUtil.isHovered(posX + w + 8, posY, 10, 10, mouseX, mouseY)) {
				if (Mouse.getEventButtonState()) {
					color = Colors.buttonColorDark;
				} else {
					color = Colors.buttonColor;
				}
			}
		}

		final int i = posX + w + 8;
		final int i0 = posX + w + 18;
		final int i1 = posY + 11;
		
		RenderUtil.drawRect(i + 1, posY + 2, i0, i1, color);

		RenderUtil.drawHorizontalLine(i, i0, posY + 1, Colors.OUTLINE_COLOR);
		RenderUtil.drawHorizontalLine(i, i0, i1, Colors.OUTLINE_COLOR);

		RenderUtil.drawVerticalLine(i, posY, i1, Colors.OUTLINE_COLOR);
		RenderUtil.drawVerticalLine(i0, posY, i1, Colors.OUTLINE_COLOR);

		RenderUtil.drawString(text, posX + 3, posY + 3, Colors.FONT_COLOR);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		int w = RenderUtil.getWidth(text);

		
		if (RenderUtil.isHovered(posX + w + 8, posY, 10, 10, mouseX, mouseY)) {
			toggled = !toggled;

			for (ActionListener listener : clicklisteners) {
				listener.actionPerformed(new ActionEvent(this, hashCode(), "click", System.currentTimeMillis(), 0));
			}
		}
	}

	@Override
	public void keyTyped(int keyCode, char typedChar) {}

	@Override
	public int getWidth() {
		return RenderUtil.getWidth(text) + 22;
	}

	@Override
	public int getHeight() {
		return 13;
	}

}
