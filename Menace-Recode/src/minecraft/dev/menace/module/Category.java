package dev.menace.module;

public enum Category {

	COMBAT("Combat"),
	MOVEMENT("Movement"),
	PLAYER("Player"),
	RENDER("Render"),
	WORLD("World"),
	MISC("Misc"),
	SCRIPTS("Scripts");

	private final String name;

	Category(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
