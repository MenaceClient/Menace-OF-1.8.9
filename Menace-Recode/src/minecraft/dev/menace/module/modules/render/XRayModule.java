package dev.menace.module.modules.render;

import dev.menace.module.BaseModule;
import dev.menace.module.Category;

public class XRayModule extends BaseModule {

	public XRayModule() {
		super("XRay", Category.RENDER, 0);
	}

	@Override
	public void onEnable() {
		mc.renderGlobal.loadRenderers();
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.renderGlobal.loadRenderers();
		super.onDisable();
	}
}
