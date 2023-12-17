package com.axialeaa.minideco;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

import com.chocohead.mm.api.ClassTinkerers;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.minihud.gui.GuiConfigs.ConfigGuiTab;
import com.axialeaa.minideco.config.Formats;

public class MiniDecoMod implements ModInitializer, IInitializationHandler
{
//  public static final Logger LOGGER = LogManager.getLogger();

  public static final String MOD_ID = "minideco";
  public static String MOD_VERSION = "unknown";
  public static String MOD_NAME = "unknown";
  public static ConfigGuiTab FORMATS;

  @Override
  public void onInitialize()
  {
    ModMetadata metadata = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata();
    MOD_NAME = metadata.getName();
    MOD_VERSION = metadata.getVersion().getFriendlyString();
    FORMATS = ClassTinkerers.getEnum(ConfigGuiTab.class, "FORMATS");
    InitializationHandler.getInstance().registerInitializationHandler(this);
  }

  @Override
  public void registerModHandlers()
  {
    ConfigManager.getInstance().registerConfigHandler(MOD_ID, new Formats());
  }
}
