package me.imfr0zen.guiapi.components;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import me.imfr0zen.guiapi.Colors;
import me.imfr0zen.guiapi.KeyUtils;
import me.imfr0zen.guiapi.RenderUtil;
import me.imfr0zen.guiapi.listeners.KeyListener;
import me.imfr0zen.guiapi.listeners.TextListener;

public class GuiTextField implements GuiComponent {

	private boolean marked;

	private int blinkState;

	private int posX;
	private int posY;

	private String text;
	private String desc;

	private ArrayList<TextListener> textlisteners = new ArrayList<TextListener>();

	public GuiTextField(String desc, String input) {
		this.desc = desc;
		this.text = input;
		this.blinkState = -1;
	}

	public GuiTextField(String desc) {
		this(desc, "");
	}

	/**
	 * @param listener
	 */
	public void addTextListener(TextListener listener) {
		textlisteners.add(listener);
	}

	@Override
	public void render(int posX, int posY, int width, int mouseX, int mouseY) {
		this.posX = posX;
		this.posY = posY;

		final int w = RenderUtil.getWidth(desc);
		final int w0 = RenderUtil.getWidth(text);
		final int w1 = w0 < 50 ? 50 : w0;
		final int h = 11;

		RenderUtil.drawRect(posX + w + 8, posY, posX + w + w1 + 11, posY + h,
				blinkState == -1 ? Colors.buttonColor : marked ? Colors.buttonColorLight : Colors.buttonColorDark);

		RenderUtil.drawHorizontalLine(posX + w + 7, posX + w + w1 + 11, posY, 0xFF363636);
		RenderUtil.drawHorizontalLine(posX + w + 7, posX + w + w1 + 11, posY + h, 0xFF363636);

		RenderUtil.drawVerticalLine(posX + w + 7, posY, posY + h, 0xFF363636);
		RenderUtil.drawVerticalLine(posX + w + w1 + 11, posY, posY + h, 0xFF363636);

		RenderUtil.drawString(desc, posX + 3, posY + 2, 13158600);

		if (!text.isEmpty())
			RenderUtil.drawString(text, posX + 10 + w, posY + 2, 13158600);

		if (blinkState != -1) {
			blinkState++;
			if (blinkState < 20 && !text.isEmpty()) {
				RenderUtil.drawVerticalLine(posX + w + RenderUtil.getWidth(text) + 9, posY + 1, posY + 10, 0xFFc8c8c8);
			} else if (blinkState > 40) {
				blinkState = 0;
			}
		}

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		final int w = RenderUtil.getWidth(desc);
		final int w0 = RenderUtil.getWidth(text);
		final int w1 = w0 < 50 ? 50 : w0;

		if (RenderUtil.isHovered(posX + w + 8, posY, w1 + 4, 11, mouseX, mouseY)) {
			boolean b = true;
			if (blinkState == -1) {
				blinkState = 11;
				b = false;
			}

			if (blinkState != -1 && b) {
				blinkState = -1;

				for (TextListener listener : textlisteners) {
					listener.stringEntered(text);
				}
			}
		}
	}

	@Override
	public void keyTyped(int keyCode, char typedChar) {

		if (blinkState != -1) {

			if (keyCode == 14 && !text.isEmpty()) {
				if (!text.isEmpty()) {
					if (!marked) {
						text = text.substring(0, text.length() - 1);
						
						for (TextListener listener : textlisteners) {
							listener.keyTyped(typedChar, text);
						}
					} else {
						text = "";
						marked = false;
					}
				}
			} else if (keyCode == 28) {
				blinkState = -1;
				for (TextListener listener : textlisteners) {
					listener.stringEntered(text);
				}
				for (TextListener listener : textlisteners) {
					listener.stringEntered(text);
				}
			} else if (typedChar != 0 && keyCode != 14 && !KeyUtils.isCtrlKeyDown()) {
				if (!marked)
					text += typedChar;
				else {
					text = Character.toString(typedChar);
					marked = false;
				}

				for (TextListener listener : textlisteners) {
					listener.keyTyped(typedChar, text);
				}
			} else if (KeyUtils.isKeyComboCtrlC(keyCode) && marked) {
				KeyUtils.setClipboardString(text);
			} else if (KeyUtils.isKeyComboCtrlV(keyCode) && KeyUtils.getClipboardString() != null && !KeyUtils.getClipboardString().isEmpty()) {
				if (marked) {
					text = KeyUtils.getClipboardString();
					marked = false;
				} else {
					text += KeyUtils.getClipboardString();
				}

				for (TextListener listener : textlisteners) {
					listener.keyTyped(typedChar, text);
				}
			} else if (KeyUtils.isKeyComboCtrlA(keyCode)) {
				marked = true;
			} else if (marked && keyCode == 211) {
				text = "";
				marked = false;
			} else if (KeyUtils.isKeyComboCtrlX(keyCode) && marked) {
				KeyUtils.setClipboardString(text);
				text = "";
				marked = false;
			} else if (marked && !KeyUtils.isShiftKeyDown() && !KeyUtils.isKeyComboCtrlV(keyCode) && !KeyUtils.isKeyComboCtrlX(keyCode)) {
				marked = false;
			}
		}

	}

	@Override
	public int getWidth() {
		int w = RenderUtil.getWidth(text);

		return RenderUtil.getWidth(desc) + (w < 50 ? 50 : w) + 15;
	}

	@Override
	public int getHeight() {
		return 14;
	}

}
