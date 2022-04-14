package dev.menace.module;

import java.lang.reflect.Field;
import java.util.ArrayList;

import dev.menace.module.modules.combat.*;
import dev.menace.module.modules.fun.*;
import dev.menace.module.modules.misc.*;
import dev.menace.module.modules.movement.*;
import dev.menace.module.modules.player.*;
import dev.menace.module.modules.render.*;
import dev.menace.module.modules.world.*;
import net.minecraft.crash.CrashReport;

public class ModuleManager {
	
	public ArrayList<Module> modules = new ArrayList<Module>();
	
	//COMBAT
	public final AimAssist aimAssistModule = new AimAssist();
	public final AntiBot antiBotModule = new AntiBot();
	public final AutoClicker autoClickerModule = new AutoClicker();
	public final AutoLog autoLogModule = new AutoLog();
	//public final AutoSword autoSwordModule = new AutoSword();
	public final ComboOneTap comboOneTapModule = new ComboOneTap();
	public final Criticals criticalsModule = new Criticals();
	public final KillAura killAuraModule = new KillAura();
	public final TestAura testAuraModule = new TestAura();
	public final Velocity velocityModule = new Velocity();
	public final WTap wTapModule = new WTap();
	
	//MOVEMENT
	public final AutoHeadHitter autoHeadHitterModule = new AutoHeadHitter();
	public final Flight flightModule = new Flight();
	public final Glide glideModule = new Glide();
	public final HighJump highJumpModule = new HighJump();
	public final InfJump infJumpModule = new InfJump();
	public final InvMove invMoveModule = new InvMove();
	public final Jesus jesusModule = new Jesus();
	public final LongJump longJumpModule = new LongJump();
	public final NoSlow noSlowModule = new NoSlow();
	public final Parkour parkourModule = new Parkour();
	public final Phase phaseModule = new Phase();
	public final Sneak sneakModule = new Sneak();
	public final Speed speedModule = new Speed();
	public final Spider spiderModule = new Spider();
	public final Sprint sprintModule = new Sprint();
	public final Step stepModule = new Step();
	public final Strafe strafeModule = new Strafe();
	public final TargetStrafe targetStrafeModule = new TargetStrafe();
	//public final VerusTP verusTPModule = new VerusTP();
	
	//PLAYER
	public final AntiVoid antiVoidModule = new AntiVoid();
	public final AutoRespawn autoRespawnModule = new AutoRespawn();
	public final Blink blinkModule = new Blink();
	public final FastEat fasteatModule = new FastEat();
	public final InvManager invManagerModule = new InvManager();
	public final NoFall noFallModule = new NoFall();
	public final NoFlag noFlagModule = new NoFlag();
	public final NoRotate noRotateModule = new NoRotate();
	public final SafeWalk safeWalkModule = new SafeWalk();
	//public final Scaffold scaffoldModule = new Scaffold();
	
	//RENDER
	public final Animations animationsModule = new Animations();
	public final ChestEsp chestEspModule = new ChestEsp();
	public final ClickGui clickGuiModule = new ClickGui();
	public final Esp espModule = new Esp();
	public final Freecam freecamModule = new Freecam();
	public final Fullbright fullbrightModule = new Fullbright();
	public final Hud hudModule = new Hud();
	public final HudDesigner hudDesignerModule = new HudDesigner();
	public final PrevFallPos prevFallPosModule = new PrevFallPos();
	public final Xray xrayModule = new Xray();
	
	//MISC
	public final Anticheat antiCheatModule = new Anticheat();
	public final AutoLogin autoLoginModule = new AutoLogin();
	public final DevModule testModule = new DevModule();
	//public final AutoPlay autoPlayModule = new AutoPlay();
	public final ClientSpoofer clientSpooferModule = new ClientSpoofer();
	public final Disabler disablerModule = new Disabler();
	//public final KillSults killSultsModule = new KillSults();
	public final StaffDetector staffDetectorModule = new StaffDetector();
	
	//WORLD
	public final AntiCactus antiCactusModule = new AntiCactus();
	public final AntiWeb antiWebModule = new AntiWeb();
	public final ChestStealer chestStealerModule = new ChestStealer();
	public final FastPlace fastPlaceModule = new FastPlace();
	public final Fucker fuckerModule = new Fucker();
	public final ItemMagnet itemMagnetModule = new ItemMagnet();
	public final Timer timerModule = new Timer();
	
	//FUN
	public final Derp derpModule = new Derp();
	public final GameFucker gameFuckerModule = new GameFucker();

	
	public ModuleManager() {
		try
		{
			for(Field field : ModuleManager.class.getDeclaredFields())
			{
				if(!field.getName().endsWith("Module"))
					continue;
				
				Module module = (Module)field.get(this);
				modules.add(module);
			}
			
		}catch(Exception e)
		{
			String message = "Initializing Menace modules";
			CrashReport report = CrashReport.makeCrashReport(e, message);
		}
	}
	
    public ArrayList<Module> getModules() {
        return modules;
    }
    
    public ArrayList<Module> getActiveModules() {
    	ArrayList<Module> activeModules = new ArrayList<Module>();
    	
    	for (Module m : modules) {
    		if (m.isToggled()) activeModules.add(m);
    	}
    	
        return activeModules;
    }
    
    public Module getModuleByName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    
    public ArrayList<Module> getModulesByCategory(Category c) {
    	ArrayList<Module> list = new ArrayList<Module>();
    	modules.stream().forEach(module ->{ 
    		if (module.getCategory().equals(c)) {
    			list.add(module);
    		}
    	});
    	return list;
    }

}
