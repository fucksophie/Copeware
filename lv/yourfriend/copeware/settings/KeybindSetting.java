package lv.yourfriend.copeware.settings;

public class KeybindSetting extends Setting {
	public int code;
	
	public KeybindSetting(int keyCode) {
		this.name = "Keybind";
		this.code = keyCode;
	}

	public int getKeyCode() {
		return code;
	}

	public void setKeyCode(int keyCode) {
		this.code = keyCode;
	}
}
