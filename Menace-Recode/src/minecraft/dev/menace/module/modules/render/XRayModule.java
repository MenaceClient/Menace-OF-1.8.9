package dev.menace.module.modules.render;

import dev.menace.module.BaseModule;
import dev.menace.module.Category;

public class XRayModule extends BaseModule {

	public XRayModule() {
		super("XRay", Category.RENDER, 0);
	}

	@Override
	public void onEnable() {
		MC.renderGlobal.loadRenderers();
		super.onEnable();
	}

	@Override
	public void onDisable() {
		MC.renderGlobal.loadRenderers();
		super.onDisable();
	}
}
