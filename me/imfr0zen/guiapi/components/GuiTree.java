package me.imfr0zen.guiapi.components;

import java.util.List;

import org.lwjgl.input.Mouse;

import me.imfr0zen.guiapi.Colors;
import me.imfr0zen.guiapi.RenderUtil;

public class GuiTree implements GuiComponent {

	private boolean extended;

	private int posX;
	private int posY;

	private String text;

	private GuiComponent[] components;

	public GuiTree(String text, GuiComponent... components) {
		this.components = components;
		this.extended = false;
		this.text = text;
	}

	public GuiTree(String text, List<? extends GuiComponent> components) {
		this(text, components.toArray(new GuiComponent[components.size()]));
	}

	@Override
	public void render(int posX, int posY, int width, int mouseX, int mouseY) {
		this.posX = posX;
		this.posY = posY;

		int color = 0x45000000;

		if (extended) {
			color = Colors.buttonColorLight;

			if (RenderUtil.isHovered(posX + 2, posY, 10, posY + 11, mouseX, mouseY)) {
				if (Mouse.getEventButtonState()) {
					color = Colors.buttonColorDark;
				} else {
					color = Colors.buttonColor;
				}
			}
		}

		RenderUtil.drawRect(posX + 2, posY, posX + 13, posY + 11, color);

		RenderUtil.drawHorizontalLine(posX + 2, posX + 13, posY, Colors.OUTLINE_COLOR);
		RenderUtil.drawHorizontalLine(posX + 2, posX + 13, posY + 11, Colors.OUTLINE_COLOR);

		RenderUtil.drawVerticalLine(posX + 2, posY, posY + 11, Colors.OUTLINE_COLOR);
		RenderUtil.drawVerticalLine(posX + 13, posY, posY + 11, Colors.OUTLINE_COLOR);

		RenderUtil.drawString(text, posX + 16, posY + 3, 13158600);

		if (extended) {
			int height = 12;

			for (int i = 0; i < components.length; i++) {
				components[i].render(posX + 10, posY + height, width, mouseX, mouseY);
				height += components[i].getHeight();
			}
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && RenderUtil.isHovered(posX + 2, posY, 11, 11, mouseX, mouseY)) {
			extended = !extended;
		}

		if (extended) {
			for (int i = 0; i < components.length; i++) {
				components[i].mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}

	@Override
	public void keyTyped(int keyCode, char typedChar) {
		if (extended) {
			for (int i = 0; i < components.length; i++) {
				components[i].keyTyped(keyCode, typedChar);
			}
		}
	}

	@Override
	public int getWidth() {
		int width = RenderUtil.getWidth(text) + 19;

		if (extended) {
			for (GuiComponent component : components) {
				width = Math.max(width, component.getWidth() + 8);
			}
		}

		return width;
	}

	@Override
	public int getHeight() {
		int i = 13;

		if (extended) {
			for (int j = 0; j < components.length; j++) {
				i += components[j].getHeight();
			}
		}

		return i;
	}

}
