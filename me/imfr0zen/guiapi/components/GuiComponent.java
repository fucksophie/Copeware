package me.imfr0zen.guiapi.components;

public interface GuiComponent {

	/**
	 * Renders the Component
	 * @param posX
	 * @param posY
	 * @param mouseX
	 * @param mouseY
	 */
	void render(int posX, int posY, int width, int mouseX, int mouseY);
	
	/**
	 * Gets called when the user clicks with the mouse
	 * @param mouseX
	 * @param mouseY
	 * @param mouseButton
	 */
	void mouseClicked(int mouseX, int mouseY, int mouseButton);
	
	/**
	 * Gets called when the user presses a key
	 * @param keyCode
	 * @param typedChar
	 */
	void keyTyped(int keyCode, char typedChar);
	
	/**
	 * @return the width of this Component
	 */
	int getWidth();
	
	/**
	 * @return the height of this Component
	 */
	int getHeight();

}
