package me.imfr0zen.guiapi.listeners;

/**
 * 
 * Used by {@link me.imfr0zen.guiapi.components.GuiSlider}
 * @author ImFr0zen
 *
 */
public interface ValueListener {
	
	/**
	 * Gets called after moving the cursor.
	 * @param current
	 */
	void valueUpdated(double current);
	
	/**
	 * Gets called after releasing the cursor.
	 * @param current
	 */
	void valueChanged(double current);
	
}
