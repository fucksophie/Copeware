package me.imfr0zen.guiapi.components;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import me.imfr0zen.guiapi.Colors;
import me.imfr0zen.guiapi.RenderUtil;
import me.imfr0zen.guiapi.listeners.KeyListener;

/**
 * 
 * Get a key from Keyboard
 * 
 * @author ImFr0zen
 *
 */
public class GuiGetKey implements GuiComponent {

	private boolean wasChanged;

	private int key;
	private int blinkState;

	private int posX;
	private int posY;

	private String text;
	
	private ArrayList<KeyListener> keylisteners = new ArrayList<KeyListener>();

	public GuiGetKey(int key) {
		this("Key:", key);
	}

	public GuiGetKey(String text, int key) {
		this.key = key;
		this.blinkState = -1;
		this.wasChanged = false;
		this.text = text;
	}
	
	/**
	 * @param listener gets called when getting a new key.
	 */
	public void addKeyListener(KeyListener listener) {
		keylisteners.add(listener);
	}

	@Override
	public void render(int posX, int posY, int width, int mouseX, int mouseY) {
		this.posX = posX;
		this.posY = posY;
		
		String keyString = Keyboard.getKeyName(key);

		final int w = RenderUtil.getWidth(text);
		final int w0 = RenderUtil.getWidth(keyString);

		int color = Colors.buttonColorLight;

		if (RenderUtil.isHovered(posX + w + 8, posY, 10, 10, mouseX, mouseY)) {
			if (Mouse.getEventButtonState()) {
				color = Colors.buttonColorDark;
			} else {
				color = Colors.buttonColor;
			}
		}

		int i = posX + w + w0 + 11;

		RenderUtil.drawRect(posX + w + 8, posY, i, posY + 11, color);

		RenderUtil.drawHorizontalLine(posX + w + 7, i, posY, 0xFF363636);
		RenderUtil.drawHorizontalLine(posX + w + 7, i, posY + 11, 0xFF363636);

		RenderUtil.drawVerticalLine(posX + w + 7, posY, posY + 11, 0xFF363636);
		RenderUtil.drawVerticalLine(i, posY, posY + 11, 0xFF363636);

		RenderUtil.drawString(text, posX + 3, posY + 2, 13158600);
		RenderUtil.drawString(keyString, posX + 10 + w, posY + 2, 13158600);

		if (blinkState != -1) {
			blinkState++;
			if (blinkState < 20) {
				RenderUtil.drawHorizontalLine(posX + 10 + w, posX + 9 + w + w0, posY + 10, 0xFFc8c8c8);
			} else if (blinkState > 40) {
				blinkState = 0;
			}
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		String keyString = Keyboard.getKeyName(key);

		final int w = RenderUtil.getWidth(text);
		final int w0 = RenderUtil.getWidth(keyString);
		
		boolean b = true;

		if (RenderUtil.isHovered(posX + w + 8, posY, w0 + 11, 11, mouseX, mouseY) && blinkState == -1) {
			blinkState = 11;
			b = false;
		}

		if (blinkState != -1 && b) {
			blinkState = -1;
		}
	}

	@Override
	public void keyTyped(int keyCode, char typedChar) {
		if (blinkState != -1) {
			for (KeyListener listener : keylisteners) {
				listener.keyChanged(keyCode);
			}
			
			key = keyCode;
			blinkState = -1;
		}
	}

	@Override
	public int getWidth() {
		return RenderUtil.getWidth(text + Keyboard.getKeyName(key)) + 16;
	}

	@Override
	public int getHeight() {
		return 14;
	}

}
