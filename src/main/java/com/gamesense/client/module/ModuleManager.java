package com.gamesense.client.module;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.modules.combat.*;
import com.gamesense.client.module.modules.exploits.*;
import com.gamesense.client.module.modules.movement.*;
import com.gamesense.client.module.modules.misc.*;
import com.gamesense.client.module.modules.render.*;
import com.gamesense.client.module.modules.hud.*;
import com.gamesense.client.module.modules.gui.*;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ModuleManager {
	public static ArrayList<Module> modules;

	public ModuleManager(){
		modules = new ArrayList<>();
		//Combat
		addMod(new AutoAnvil());
		addMod(new AutoArmor());
		addMod(new Auto Terrorism());
		addMod(new AutoTotem());
		addMod(new AutoTrap());
		addMod(new AutoWeb());
		addMod(new FastBow());
		addMod(new HoleFill());
		addMod(new Aggravated Assault());
		addMod(new Offhand Terrorism());
		addMod(new OffhandGap());
		addMod(new Quiver());
		addMod(new SelfTrap());
		addMod(new SelfWeb());
		addMod(new Surround());
		//Exploits
		addMod(new CoordExploit());
		addMod(new FastBreak());
		addMod(new LiquidInteract());
		addMod(new NoInteract());
		addMod(new NoSwing());
		addMod(new Reach());
		addMod(new PacketXP());
		addMod(new PortalGodMode());
		//Movement
		addMod(new Anchor());
		addMod(new Blink());
		addMod(new HoleTP());
		addMod(new PlayerTweaks());
		addMod(new ReverseStep());
		addMod(new Speed());
		addMod(new Sprint());
		addMod(new Step());
		//Misc
		addMod(new Announcer());
		addMod(new AutoGG());
		addMod(new AutoReply());
		addMod(new AutoTool());
		addMod(new ChatModifier());
		addMod(new ChatSuffix());
		addMod(new DiscordRPCModule());
		addMod(new FastPlace());
		addMod(new FakePlayer());
		addMod(new Dupe());
		addMod(new HotbarRefill());
		addMod(new MCF());
		addMod(new MultiTask());
		addMod(new NoEntityTrace());
		addMod(new NoKick());
		addMod(new PvPInfo());
		//Render
		addMod(new BlockHighlight());
		addMod(new BreakESP());
		addMod(new CapesModule());
		addMod(new CityESP());
		addMod(new ESP());
		addMod(new Freecam());
		addMod(new Fullbright());
		addMod(new HitSpheres());
		addMod(new HoleESP());
		addMod(new LogoutSpots());
		addMod(new Nametags());
		addMod(new NoRender());
		addMod(new RenderTweaks());
		addMod(new ShulkerViewer());
		addMod(new SkyColor());
		addMod(new Tracers());
		addMod(new ViewModelChanger());
		addMod(new VoidESP());
		//HUD
		addMod(new ArmorHUD());
		addMod(new ModuleArrayList());
		addMod(new CombatInfo());
		addMod(new InventoryViewer());
		addMod(new Notifications());
		addMod(new PotionEffects());
		addMod(new Radar());
		addMod(new TabGUIModule());
		addMod(new TargetHUD());
		addMod(new TargetInfo());
		addMod(new TextRadar());
		addMod(new Watermark());
		addMod(new Welcomer());
		//GUI
		addMod(new ClickGuiModule());
		addMod(new ColorMain());
	}

	public static void addMod(Module m){
		modules.add(m);
	}

	public static void onUpdate() {
		modules.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
	}

	public static void onRender() {
		modules.stream().filter(Module::isEnabled).forEach(Module::onRender);
		GameSenseMod.getInstance().clickGUI.render();
	}

	public static void onWorldRender(RenderWorldLastEvent event) {
		Minecraft.getMinecraft().profiler.startSection("NFNC");
		Minecraft.getMinecraft().profiler.startSection("setup");
		GameSenseTessellator.prepare();
		RenderEvent e = new RenderEvent(event.getPartialTicks());
		Minecraft.getMinecraft().profiler.endSection();

		modules.stream().filter(module -> module.isEnabled()).forEach(module -> {
			Minecraft.getMinecraft().profiler.startSection(module.getName());
			module.onWorldRender(e);
			Minecraft.getMinecraft().profiler.endSection();
		});

		Minecraft.getMinecraft().profiler.startSection("release");
		GameSenseTessellator.release();
		Minecraft.getMinecraft().profiler.endSection();
		Minecraft.getMinecraft().profiler.endSection();
	}

	public static ArrayList<Module> getModules() {
		return modules;
	}

	public static ArrayList<Module> getModulesInCategory(Module.Category c){
		ArrayList<Module> list = (ArrayList<Module>) getModules().stream().filter(m -> m.getCategory().equals(c)).collect(Collectors.toList());
		return list;
	}

	public static void onBind(int key) {
		if (key == 0 || key == Keyboard.KEY_NONE) return;
		modules.forEach(module -> {
			if(module.getBind() == key){
				module.toggle();
			}
		});
	}

	public static Module getModuleByName(String name){
		Module m = getModules().stream().filter(mm->mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
		return m;
	}

	public static boolean isModuleEnabled(String name){
		Module m = getModules().stream().filter(mm->mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
		return m.isEnabled();
	}

	public static boolean isModuleEnabled(Module m){
		return m.isEnabled();
	}
}
