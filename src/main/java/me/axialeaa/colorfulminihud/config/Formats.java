package me.axialeaa.colorfulminihud.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.config.options.ConfigColor;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;

import java.io.File;

public class Formats implements IConfigHandler
{
  public static final ConfigColor
    COLOR0 = new ConfigColor("Color0", "#0021222C", "Variable color 0"),
    COLOR1 = new ConfigColor("Color1", "#00FF5555", "Variable color 1"),
    COLOR2 = new ConfigColor("Color2", "#0050FA7B", "Variable color 2"),
    COLOR3 = new ConfigColor("Color3", "#00F1FA8C", "Variable color 3"),
    COLOR4 = new ConfigColor("Color4", "#00BD93F9", "Variable color 4"),
    COLOR5 = new ConfigColor("Color5", "#00FF79C6", "Variable color 5"),
    COLOR6 = new ConfigColor("Color6", "#008BE9FD", "Variable color 6"),
    COLOR7 = new ConfigColor("Color7", "#00F8F8F2", "Variable color 7"),
    COLOR8 = new ConfigColor("Color8", "#006272A4", "Variable color 8"),
    COLOR9 = new ConfigColor("Color9", "#00FF6E6E", "Variable color 9"),
    COLOR10 = new ConfigColor("Color10", "#0069FF94", "Variable color 10"),
    COLOR11 = new ConfigColor("Color11", "#00FFFFA5", "Variable color 11"),
    COLOR12 = new ConfigColor("Color12", "#00D6ACFF", "Variable color 12"),
    COLOR13 = new ConfigColor("Color13", "#00FF92DF", "Variable color 13"),
    COLOR14 = new ConfigColor("Color14", "#00A4FFFF", "Variable color 14"),
    COLOR15 = new ConfigColor("Color15", "#00FFFFFF", "Variable color 15");
//    COLOROPTIMUM = new ConfigColor("ColorOptimal", ChatFormatting.GREEN.toString(), "The color used for optimal TPS, MSPT and FPS values"),
//    COLORGOOD = new ConfigColor("ColorGood", ChatFormatting.YELLOW.toString(), "The color used for good TPS, MSPT and FPS values"),
//    COLORLACKING = new ConfigColor("ColorLacking", ChatFormatting.GOLD.toString(), "The color used for lacking TPS, MSPT and FPS values"),
//    COLORPOOR = new ConfigColor("ColorPoor", ChatFormatting.RED.toString(), "The color used for poor TPS, MSPT and FPS values");

  public static final ConfigString
    BEE_COUNT_FORMAT                 = new ConfigString("infoBeeCountFormat",
    "\"Bees: \", #aqua\"%count\"", "Format of infoBeeCount"),

  BIOME_FORMAT                     = new ConfigString("infoBiomeFormat",
    "\"Biome: %biome\"", "Format of infoBiome"),

  BIOME_REG_NAME_FORMAT            = new ConfigString("infoBiomeRegistryNameFormat",
    "\"Biome reg name: %regName\"", "Format of infoBiomeRegistryName"),

  BLOCK_BREAK_SPEED_FORMAT         = new ConfigString("infoBlockBreakSpeedFormat",
    "\"BBS: %speed\"", "Format of infoBlockBreakSpeed"),

  BLOCK_IN_CHUNK_FORMAT            = new ConfigString("infoBlockInChunkFormat",
    "\"Block: %x, %y, %z within Sub-Chunk: %chunkX, %chunkY, %chunkZ\"", "Format of infoBlockInChunk"),

  BLOCK_POS_FORMAT                 = new ConfigString("infoBlockPositionFormat",
    "\"Block: %x, %y, %z\"", "Format of infoBlockPosition"),

  BLOCK_PROPERTIES_FORMAT          = new ConfigString("infoBlockPropertiesFormat",
    "\"%property: \", %value", "Format of infoBlockProperties"),

  BLOCK_PROPS_HEADING_FORMAT       = new ConfigString("infoBlockPropertiesHeadingFormat",
    "\"%block\"", "Format of the heading of infoBlockProperties"),

  BLOCK_PROPS_BOOLEAN_TRUE_FORMAT  = new ConfigString("infoBlockPropertiesBooleanTrueFormat",
    "#green\"TRUE\"", "Format of boolean properties in infoBlockProperties when they're true"),

  BLOCK_PROPS_BOOLEAN_FALSE_FORMAT = new ConfigString("infoBlockPropertiesBooleanFalseFormat",
    "#red\"FALSE\"", "Format of boolean properties in infoBlockProperties when they're false"),

  BLOCK_PROPS_DIRECTION_FORMAT     = new ConfigString("infoBlockPropertiesDirectionFormat",
    "#gold\"%direction\"", "Format of direction properties in infoBlockProperties"),

  BLOCK_PROPS_INT_FORMAT           = new ConfigString("infoBlockPropertiesIntFormat",
    "#aqua\"%int\"", "Format of int properties in infoBlockProperties"),

  BLOCK_PROPS_STRING_FORMAT        = new ConfigString("infoBlockPropertiesStringFormat",
    "\"%string\"", "Format of String properties in infoBlockProperties"),

  CHUNK_POS_FORMAT                 = new ConfigString("infoChunkPositionFormat",
    "\"%separator\", \"Sub-Chunk: %x, %y, %z\"", "Format of infoChunkPosition"),

  CHUNK_SECTIONS_FORMAT            = new ConfigString("infoChunkSectionsFormat",
    "\"C: %count\"", "Format of infoChunkSections"),

  CHUNK_SECTIONS_FULL_FORMAT       = new ConfigString("infoChunkSectionsLineFormat",
    "\"%count\"", "Format of infoChunkSectionsLine"),

  CHUNK_UPDATES_FORMAT             = new ConfigString("infoChunkUpdatesFormat",
    "\"TODO\"", "Format of infoChunkUpdates"),

  COORDINATES_FORMAT               = new ConfigString("infoCoordinatesFormat",
    "\"XYZ: %x / %y / %z\"", "Format of infoCoordinates, change precision by changing the numbers"),

  COORDINATES_SCALED_NETHER_FORMAT = new ConfigString("infoCoordinatesScaledNetherFormat",
    "\"%separator\", \"Nether: %x / %y / %z\"", "Format of infoCoordinatesScaled when in the nether"),

  COORDINATES_SCALED_OVERWORLD_FORMAT = new ConfigString("infoCoordinatesScaledOverworldFormat",
    "\"%separator\", \"Overworld: %x / %y / %z\"", "Format of infoCoordinatesScaled when in the overworld"),

  DIFFICULTY_FORMAT                = new ConfigString("infoDifficultyFormat",
    "\"Local Difficulty: %local // %clamped (Day %day)\"", "Format of infoDifficulty"),

  DIMENSION_FORMAT                 = new ConfigString("infoDimensionIdFormat",
    "\"%separator\", \"dim: %dimension\"", "Format of infoDimensionId"),

  DISTANCE_FORMAT                  = new ConfigString("infoDistanceFormat",
    "\"Distance: %dist (x: %distX y: %distY z: %distZ) [to x: %refX y: %refY z: %refZ]\"", "Format of infoDistance"),

  ENTITIES_CLIENT_FORMAT           = new ConfigString("infoEntitiesClientFormat",
    "\"Entities - Client: %count\"", "Format of infoEntitiesClient"),

  ENTITIES_SERVER_FORMAT           = new ConfigString("infoEntitiesServerFormat",
    "\", Server: %count\"", "Format of infoEntitiesServer"),

  ENTITY_REG_NAME_FORMAT           = new ConfigString("infoEntityRegistryNameFormat",
    "\"Entity reg name: %regName\"", "Format of infoEntityRegistryName"),

  FACING_FORMAT                    = new ConfigString("infoFacingFormat",
    "\"Facing: %cardinal (\", %cartesian, \")\"", "Format of infoFacing"),

  FACING_PX_FORMAT                 = new ConfigString("infoFacingPosXFormat",
    "\"Positive X\"", "Text for infoFacing when facing positive X"),

  FACING_NX_FORMAT                 = new ConfigString("infoFacingNegXFormat",
    "\"Negative X\"", "Text for infoFacing when facing negative X"),

  FACING_PZ_FORMAT                 = new ConfigString("infoFacingPosZFormat",
    "\"Positive Z\"", "Text for infoFacing when facing positive Z"),

  FACING_NZ_FORMAT                 = new ConfigString("infoFacingNegZFormat",
    "\"Negative Z\"", "Text for infoFacing when facing negative Z"),

  FPS_FORMAT                       = new ConfigString("infoFPSFormat",
    "\"%fps fps\"", "Format of infoFPS"),

  FURNACE_XP_FORMAT                = new ConfigString("infoFurnaceXpFormat",
    "\"Furnace XP: \", #aqua\"%count\"", "Format of infoFurnaceXp"),

  HONEY_LEVEL_FORMAT               = new ConfigString("infoHoneyLevelFormat",
    "\"Honey: \", #aqua\"%level\"", "Format of infoHoneyLevel"),

  HORSE_JUMP_FORMAT                = new ConfigString("infoHorseJumpFormat",
    "\"Horse Jump: %jump m\"", "Format of infoHorseJump"),

  HORSE_SPEED_FORMAT               = new ConfigString("infoHorseSpeedFormat",
    "\"Horse Speed: %speed m/s\"", "Format of infoHorseSpeed"),

  LIGHT_LEVEL_CLIENT_FORMAT        = new ConfigString("infoLightLevelClientFormat",
    "\"Client Light: %light (block: %block, sky: %sky)\"", "Format of infoLightLevelClient"),

  LIGHT_LEVEL_SERVER_FORMAT        = new ConfigString("infoLightLevelServerFormat",
    "\"Server Light: %light (block: %block, sky: %sky)\"", "Format of infoLightLevelServer"),

  LOOKING_AT_BLOCK_FORMAT          = new ConfigString("infoLookingAtBlockFormat",
    "\"Looking at block: %x, %y, %z\"", "Format of infoLookingAtBlock"),

  LOOKING_AT_BLOCK_CHUNK_FORMAT    = new ConfigString("infoLookingAtBlockInChunkFormat",
    "\"%separator\", \"Block: %x, %y, %z in Sub-Chunk: %chunkX, %chunkY, %chunkZ\"", "Format of infoLookingAtBlockInChunk"),

  LOOKING_AT_ENTITY_FORMAT         = new ConfigString("infoLookingAtEntityFormat",
    "\"Entity: %entity\"", "Format of infoLookingAtEntity"),

  LOOKING_AT_ENTITY_LIVING_FORMAT  = new ConfigString("infoLookingAtEntityLivingFormat",
    "\"Entity: %entity - HP: %health$.1f / %maxHealth$.1f\"", "Format of infoLookingAtEntity when entity is living"),

  LOADED_CHUNKS_COUNT_SERVER_FORMAT= new ConfigString("infoLoadedChunksCountServerFormat",
    "\"Server: %loaded / %total - Client: %stats\"", "Format of infoLoadedChunksCount when playing singleplayer"),

  LOADED_CHUNKS_COUNT_CLIENT_FORMAT= new ConfigString("infoLoadedChunksCountClientFormat",
    "\"%stats\"", "Format of infoLoadedChunksCount when playing on a server"),

  MEMORY_USAGE_FORMAT              = new ConfigString("infoMemoryUsageFormat",
    "\"Mem: %pctUsed%% %used/%max | Allocated: %pctAllocated%% %allocated\"", "Format of infoMemoryUsage"),

  PARTICLE_COUNT_FORMAT            = new ConfigString("infoParticleCountFormat",
    "\"P: %count\"", "Format of infoParticleCount"),

  PING_FORMAT                      = new ConfigString("infoPingFormat",
    "\"Ping: %ping\"", "Format of infoPing"),

  REGION_FILE_FORMAT               = new ConfigString("infoRegionFileFormat",
    "\"%separator\", \"Region: r.%x.%z\"", "Format of infoRegionFile"),

  ROTATION_PITCH_FORMAT            = new ConfigString("infoRotationPitchFormat",
    "\"%separator\", \"Pitch: %pitch$.1f\"", "Format of infoRotationPitch"),

  ROTATION_YAW_FORMAT              = new ConfigString("infoRotationYawFormat",
    "\"Yaw: %yaw$.1f\"", "Format of infoRotationYaw"),

  SEPARATOR_FORMAT                 = new ConfigString("infoSeparatorFormat",
    " / ", "Format of the separators used for some compound info lines"),

  TPS_BELOW_20_FORMAT              = new ConfigString("tpsBelow40Format",
    "#green\"%mspt\"", "Format of infoServerTPS tps when it is below 40"),

  TPS_ABOVE_20_FORMAT              = new ConfigString("tpsAbove50Format",
    "#red\"%mspt\"", "Format of infoServerTPS tps when it is below 40"),

  MSPT_BELOW_40_FORMAT             = new ConfigString("msptBelow40Format",
    "#green\"%mspt\"", "Format of infoServerTPS mspt when MSPT is below 40"),

  MSPT_40_45_FORMAT                = new ConfigString("mspt40to45Format",
    "#yellow\"%mspt\"", "Format of infoServerTPS mspt when MSPT is below 40"),

  MSPT_45_50_FORMAT                = new ConfigString("mspt45to50Format",
    "#gold\"%mspt\"", "Format of infoServerTPS mspt when MSPT is below 40"),

  MSPT_ABOVE_50_FORMAT             = new ConfigString("msptAbove50Format",
    "#red\"%mspt\"", "Format of infoServerTPS mspt when MSPT is below 40"),

  SERVER_TPS_VANILLA_FORMAT        = new ConfigString("infoServerTPSVanillaFormat",
    "\"Server TPS: \", %tps, \" (MSPT [est]: \", %mspt, \")\"", "Format of infoServerTPS for vanilla servers"),

  SERVER_TPS_CARPET_FORMAT         = new ConfigString("infoServerTPSCarpetFormat",
    "\"Server TPS: \", %tps, \" MSPT: \", %mspt", "Format of infoServerTPS for carpet servers"),

  SERVER_TPS_NULL_FORMAT           = new ConfigString("infoServerTPSNullFormat",
    "\"Server TPS: <no valid data>\"", "Format of infoServerTPS when info is unavailable"),

  SLIME_CHUNK_YES_FORMAT           = new ConfigString("infoSlimeChunkYesFormat",
    "\"Slime chunk: \", #green\"YES\"", "Format of the infoSlimeChunk result when it's positive"),

  SLIME_CHUNK_NO_FORMAT            = new ConfigString("infoSlimeChunkNoFormat",
    "\"Slime chunk: \", #red\"NO\"", "Format of the infoSlimeChunk result when it's negative"),

  SLIME_CHUNK_NO_SEED_FORMAT       = new ConfigString("infoSlimeChunkNoSeedFormat",
    "\"Slime chunk: \", \"<world seed not known>\"", "Format of infoSlimeChunk when there's no seed"),

  SPEED_FORMAT                     = new ConfigString("infoSpeedFormat",
    "\"%separator\", \"Speed: %speed$.3f m/s\"", "Format of infoSpeed"),

  SPEED_AXIS_FORMAT                = new ConfigString("infoSpeedAxisFormat",
    "\"Speed: x: %x$.3f y: %y$.3f z: %z$.3f m/s\"", "Format of infoSpeedAxis"),

  SPEED_HV_FORMAT                  = new ConfigString("infoSpeedHVFormat",
    "\"Speed: xz: %h y: %v m/s\"", "Format of infoSpeedHV"),

  TILE_ENTITIES_FORMAT             = new ConfigString("infoTileEntitiesFormat",
    "\"Client world TE - L: %loaded, T: %ticking - TODO\"", "Format of infoTileEntities"),

  TIME_DAY_MODULO_FORMAT           = new ConfigString("infoTimeDayModuloFormat",
    "\"Day time %% %mod: %time\"", "Format of infoTimeDayModulo"),

  TIME_REAL_FORMAT                 = new ConfigString("infoTimeIRLFormat",
    "\"%time$tk:%time$tM:%time$tS\"", "The format string for real time, see the Java Formatter\nclass for the format patterns, if needed."),

  TIME_TOTAL_MODULO_FORMAT         = new ConfigString("infoTimeTotalModuloFormat",
    "\"Total time %% %mod: %time\"", "Format of infoTimeTotalModulo"),

  TIME_WORLD_FORMAT                = new ConfigString("infoTimeWorldFormat",
    "\"World time: %time - total: %total\"", "Format of infoTimeWorld"),

  TIME_WORLD_FORMATTED_FORMAT      = new ConfigString("infoWorldTimeFormattedFormat",
    "\"MC time: (day %dayZeroBased) %hour:%min:xx\"", "The format string for the Minecraft time.\nThe supported placeholders are: %DAYONEBASED, %DAYZEROBASED, %HOUR, %MIN, %SEC.\n%DAYONEBASED starts the day counter from 1, %DAYZEROBASED starts from 0.");

  public static final ImmutableList<IConfigValue> OPTIONS = ImmutableList.of(
    COLOR0,
    COLOR1,
    COLOR2,
    COLOR3,
    COLOR4,
    COLOR5,
    COLOR6,
    COLOR7,
    COLOR8,
    COLOR9,
    COLOR10,
    COLOR11,
    COLOR12,
    COLOR13,
    COLOR14,
    COLOR15,

    BEE_COUNT_FORMAT,
    BIOME_FORMAT,
    BIOME_REG_NAME_FORMAT,
    BLOCK_BREAK_SPEED_FORMAT,
    BLOCK_IN_CHUNK_FORMAT,
    BLOCK_POS_FORMAT,
    BLOCK_PROPERTIES_FORMAT,
    BLOCK_PROPS_HEADING_FORMAT,
    BLOCK_PROPS_BOOLEAN_TRUE_FORMAT,
    BLOCK_PROPS_BOOLEAN_FALSE_FORMAT,
    BLOCK_PROPS_DIRECTION_FORMAT,
    BLOCK_PROPS_INT_FORMAT,
    BLOCK_PROPS_STRING_FORMAT,
    CHUNK_POS_FORMAT,
    CHUNK_SECTIONS_FORMAT,
    CHUNK_SECTIONS_FULL_FORMAT,
    CHUNK_UPDATES_FORMAT,
    COORDINATES_FORMAT,
    COORDINATES_SCALED_NETHER_FORMAT,
    COORDINATES_SCALED_OVERWORLD_FORMAT,
    DIFFICULTY_FORMAT,
    DIMENSION_FORMAT,
    DISTANCE_FORMAT,
    ENTITIES_CLIENT_FORMAT,
    ENTITIES_SERVER_FORMAT,
    ENTITY_REG_NAME_FORMAT,
    FACING_FORMAT,
    FACING_PX_FORMAT,
    FACING_NX_FORMAT,
    FACING_PZ_FORMAT,
    FACING_NZ_FORMAT,
    FPS_FORMAT,
    FURNACE_XP_FORMAT,
    HONEY_LEVEL_FORMAT,
    HORSE_JUMP_FORMAT,
    HORSE_SPEED_FORMAT,
    LIGHT_LEVEL_CLIENT_FORMAT,
    LIGHT_LEVEL_SERVER_FORMAT,
    LOOKING_AT_BLOCK_FORMAT,
    LOOKING_AT_BLOCK_CHUNK_FORMAT,
    LOOKING_AT_ENTITY_FORMAT,
    LOOKING_AT_ENTITY_LIVING_FORMAT,
    MEMORY_USAGE_FORMAT,
    LOADED_CHUNKS_COUNT_SERVER_FORMAT,
    LOADED_CHUNKS_COUNT_CLIENT_FORMAT,
    PARTICLE_COUNT_FORMAT,
    PING_FORMAT,
    REGION_FILE_FORMAT,
    ROTATION_PITCH_FORMAT,
    ROTATION_YAW_FORMAT,
    SEPARATOR_FORMAT,
    TPS_BELOW_20_FORMAT,
    TPS_ABOVE_20_FORMAT,
    MSPT_BELOW_40_FORMAT,
    MSPT_40_45_FORMAT,
    MSPT_45_50_FORMAT,
    MSPT_ABOVE_50_FORMAT,
    SERVER_TPS_VANILLA_FORMAT,
    SERVER_TPS_CARPET_FORMAT,
    SERVER_TPS_NULL_FORMAT,
    SLIME_CHUNK_YES_FORMAT,
    SLIME_CHUNK_NO_FORMAT,
    SLIME_CHUNK_NO_SEED_FORMAT,
    SPEED_FORMAT,
    SPEED_AXIS_FORMAT,
    SPEED_HV_FORMAT,
    TILE_ENTITIES_FORMAT,
    TIME_DAY_MODULO_FORMAT,
    TIME_REAL_FORMAT,
    TIME_TOTAL_MODULO_FORMAT,
    TIME_WORLD_FORMAT,
    TIME_WORLD_FORMATTED_FORMAT

//    COLOROPTIMUM,
//    COLORGOOD,
//    COLORLACKING,
//    COLORPOOR
  );

  private static final String CONFIG_FILE_NAME = "colorfulminihud.json";

  @Override
  public void load()
  {
    File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);
    if(!configFile.exists() || !configFile.isFile() || !configFile.canRead())
      return;

    JsonElement element = JsonUtils.parseJsonFile(configFile);
    if(element == null || !element.isJsonObject())
      return;

    JsonObject root = element.getAsJsonObject();
    ConfigUtils.readConfigBase(root, "Formats", Formats.OPTIONS);
  }

  @Override
  public void save()
  {
    File dir = FileUtils.getConfigDirectory();
    if(!(dir.exists() && dir.isDirectory()) && !dir.mkdirs())
      return;

    JsonObject root = new JsonObject();
    ConfigUtils.writeConfigBase(root, "Formats", Formats.OPTIONS);
    JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
  }
}