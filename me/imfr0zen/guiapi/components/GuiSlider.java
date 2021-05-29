package me.imfr0zen.guiapi.components;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import me.imfr0zen.guiapi.Colors;
import me.imfr0zen.guiapi.MathUtil;
import me.imfr0zen.guiapi.RenderUtil;
import me.imfr0zen.guiapi.listeners.ValueListener;

/**
 * 
 * @author ImFr0zen
 *
 */
public class GuiSlider implements GuiComponent {
	
	private static int dragId = -1;

	private boolean wasSliding;

	private double min;
	private double max;
	private double current;
	
	private int c;
	private double round;
	private int id;
	
	private String text;
	
	private ArrayList<ValueListener> vallisteners = new ArrayList<ValueListener>();

	public GuiSlider(String text, float min, float max, float current) {
		this(text, min, max, current, 0);
	}

	public GuiSlider(String text, double minimum, double maximum, double value, double increment) {
		this.text = text;
		this.min = minimum;
		this.max = maximum;
		this.current = value;
		this.c = (int) (value > maximum ? 50 : value * 50f / maximum);
		this.round = increment;
	}
	
	/**
	 * Add an ValueListener.
	 * @param vallistener
	 */
	public void addValueListener(ValueListener vallistener) {
		vallisteners.add(vallistener);
	}

	@Override
	public void render(int posX, int posY, int width, int mouseX, int mouseY) {
		final int w = RenderUtil.getWidth(text);

		boolean hovered = RenderUtil.isHovered(posX + w + 7, posY + 1, 50, 12, mouseX, mouseY);
		
		if (Mouse.isButtonDown(0) && ((dragId == id) || (dragId == -1 && hovered))) {
			if (mouseX < posX + w + 7) {
				current = min;
				c = 0;
			} else if (mouseX > posX + w + 57) {
				current = max;
				c = 50;
			} else {
				if (round == 0) {
					current = Math.round((mouseX - posX - w - 7f) / 50f * max);
				} else {
					current = MathUtil.round((float) (mouseX - posX - w - 7) / 50f * max, round);
				}
				
				current += current + min >= max ? 0 : min;
				
				c = mouseX - posX - w - 7;
			}
			
			dragId = id;
			
			for (ValueListener listener : vallisteners) {
				listener.valueUpdated(current);
			}
			
			wasSliding = true;
		} else if (!Mouse.isButtonDown(0) && wasSliding) {
			for (ValueListener listener : vallisteners) {
				listener.valueChanged(current);
			}
			
			dragId = -1;
			wasSliding = false;
		}

		final int height = posY + 12;
		int i =  posX + c + 11 + w;
		
		RenderUtil.drawRect(posX + c + 11 + w, posY + 1, posX + w + 61, height, Colors.buttonColorDark);
		RenderUtil.drawRect(posX + w + 8, posY + 1, i, height, 0xBB1F1F1F);
		RenderUtil.drawRect(posX + w + c + 8, posY + 1, i, height, 0xFF000000);

		RenderUtil.drawHorizontalLine(posX + w + 7, posX + w + 61, posY, Colors.OUTLINE_COLOR);
		RenderUtil.drawHorizontalLine(posX + w + 7, posX + w + 61, height, Colors.OUTLINE_COLOR);

		RenderUtil.drawVerticalLine(posX + w + 7, posY, height, Colors.OUTLINE_COLOR);
		RenderUtil.drawVerticalLine(posX + w + 61, posY, height, Colors.OUTLINE_COLOR);

		RenderUtil.drawString(text, posX + 3, posY + 3, Colors.FONT_COLOR);
		
		final String value;
		
		if (round == 0) {
			value = Long.toString(Math.round(current));
		} else {
			value = Float.toString(MathUtil.round(current, round));
		}
		
		RenderUtil.drawString(value, posX + w + 64, posY + 3, Colors.FONT_COLOR);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

	@Override
	public void keyTyped(int keyCode, char typedChar) {}

	@Override
	public int getWidth() {
		return RenderUtil.getWidth(text + (round == 0 ? Long.toString(Math.round(current))
				: Float.toString(MathUtil.round(current, round)))) + 68;
	}

	@Override
	public int getHeight() {
		return 15;
	}

}
