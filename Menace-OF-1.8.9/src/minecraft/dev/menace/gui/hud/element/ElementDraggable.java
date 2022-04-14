package dev.menace.gui.hud.element;

import java.io.File;

import dev.menace.gui.hud.IRenderer;
import dev.menace.gui.hud.ScreenPosition;
import dev.menace.utils.misc.file.FileManager;

public abstract class ElementDraggable extends Element implements IRenderer {

	protected ScreenPosition pos;
	protected double x;
	
	public ElementDraggable() {
		pos = loadPosFromFile();
	}

	@Override
	public ScreenPosition load() {
		return this.pos;
	}
	
	@Override
	public void save(ScreenPosition pos) {
		this.pos = pos;
		savePosToFile();
	}
	
	protected File getBaseFolder() {
		File folder = new File(FileManager.getHudFolder(), this.getClass().getSimpleName());
		folder.mkdirs();
		return folder;
	}
	
	protected void savePosToFile() {
		FileManager.writeJsonToFile(new File(getBaseFolder(), "pos.json"), pos);
	}

	protected ScreenPosition loadPosFromFile() {

		ScreenPosition loaded = FileManager.readFromJson(new File(getBaseFolder(), "pos.json"), ScreenPosition.class);
		
		if (loaded == null) {
			loaded = ScreenPosition.fromRelativePosition(defaultX(), defaultY());
			this.pos = loaded;
			savePosToFile();
		}
		
		return loaded;
		
	}
	
	public final int getLineOffset(ScreenPosition pos, int lineNum) {
		return pos.getAbsoluteY() + getLineOffset(lineNum);
	}

	private int getLineOffset(int lineNum) {
		return (font.FONT_HEIGHT + 3) * lineNum;
	}
	
	protected double defaultX() {
		return 0.5;
	}
	
	protected double defaultY() {
		return 0.5;
	}
	
}
