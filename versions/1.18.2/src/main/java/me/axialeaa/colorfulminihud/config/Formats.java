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
  public static final String COLOR_DESC = "A customizable color usable in format strings";

  public static final ConfigColor
    COLOR0  = new ConfigColor("color0",  "#0021222C", COLOR_DESC),
    COLOR1  = new ConfigColor("color1",  "#00FF5555", COLOR_DESC),
    COLOR2  = new ConfigColor("color2",  "#0050FA7B", COLOR_DESC),
    COLOR3  = new ConfigColor("color3",  "#00F1FA8C", COLOR_DESC),
    COLOR4  = new ConfigColor("color4",  "#00BD93F9", COLOR_DESC),
    COLOR5  = new ConfigColor("color5",  "#00FF79C6", COLOR_DESC),
    COLOR6  = new ConfigColor("color6",  "#008BE9FD", COLOR_DESC),
    COLOR7  = new ConfigColor("color7",  "#00F8F8F2", COLOR_DESC),
    COLOR8  = new ConfigColor("color8",  "#006272A4", COLOR_DESC),
    COLOR9  = new ConfigColor("color9",  "#00FF6E6E", COLOR_DESC),
    COLOR10 = new ConfigColor("color10", "#0069FF94", COLOR_DESC),
    COLOR11 = new ConfigColor("color11", "#00FFFFA5", COLOR_DESC),
    COLOR12 = new ConfigColor("color12", "#00D6ACFF", COLOR_DESC),
    COLOR13 = new ConfigColor("color13", "#00FF92DF", COLOR_DESC),
    COLOR14 = new ConfigColor("color14", "#00A4FFFF", COLOR_DESC),
    COLOR15 = new ConfigColor("color15", "#00FFFFFF", COLOR_DESC);

  public static final ConfigString
    BEE_COUNT_FORMAT                    = new ConfigString("infoBeeCountFormat",
    "\"Bees: \", #aqua\"%count\"",
    "The text formatting used for infoBeeCount\n§6%%count - The number of bees stored inside the nest or hive you're looking at"),

  BIOME_FORMAT                        = new ConfigString("infoBiomeFormat",
    "\"Biome: %biome\"",
    "The text formatting used for infoBiome\n§6%%biome - The name of the biome you're standing in"),

  BIOME_REG_NAME_FORMAT               = new ConfigString("infoBiomeRegistryNameFormat",
    "\"Biome reg name: %name\"",
    "The text formatting used for infoBiomeRegistryName\n§6%%name - The identifier of the biome you're standing in"),

  BLOCK_BREAK_SPEED_FORMAT            = new ConfigString("infoBlockBreakSpeedFormat",
    "\"BBS: %speed\"",
    "The text formatting used for infoBlockBreakSpeed\n§6%%speed - The speed at which you're mining blocks, in blocks per second"),

  BLOCK_IN_CHUNK_FORMAT               = new ConfigString("infoBlockInChunkFormat",
    "\"Block: %x, %y, %z within Sub-Chunk: %subX, %subY, %subZ\"",
    "The text formatting used for infoBlockInChunk\n§6%%x, %%y, %%z - The chunk coordinates of the block position you're standing at\n§6%%subX, %%subY, %%subZ - The coordinates of the subchunk which contains this position"),

  BLOCK_POS_FORMAT                    = new ConfigString("infoBlockPositionFormat",
    "\"Block: %x, %y, %z\"",
    "The text formatting used for infoBlockPosition\n§6%%x, %%y, %%z - The block coordinates of the position you're standing at"),

  BLOCK_PROPS_BLOCK_FORMAT            = new ConfigString("infoBlockPropertiesBlockFormat",
    "\"%block\"",
    "The text formatting used for the block identifier in infoBlockProperties\n§6%%block - The identifier of the block you're looking at"),

  BLOCK_PROPS_TRUE_FORMAT             = new ConfigString("infoBlockPropertiesTrueFormat",
    "\"%property: \", #green\"TRUE\"",
    "The text formatting used for boolean properties in infoBlockProperties, when true\n§6%%property - The name of this block property"),

  BLOCK_PROPS_FALSE_FORMAT            = new ConfigString("infoBlockPropertiesFalseFormat",
    "\"%property: \", #red\"FALSE\"",
    "The text formatting used for boolean properties in infoBlockProperties, when false\n§6%%property - The name of this block property"),

  BLOCK_PROPS_DIRECTION_FORMAT        = new ConfigString("infoBlockPropertiesDirectionFormat",
    "\"%property: \", #gold\"%value\"",
    "The text formatting used for direction properties in infoBlockProperties\n§6%%property - The name of this block property"),

  BLOCK_PROPS_INT_FORMAT              = new ConfigString("infoBlockPropertiesIntFormat",
    "\"%property: \", #aqua\"%value\"",
    "The text formatting used for integer properties in infoBlockProperties\n§6%%property - The name of this block property"),

  BLOCK_PROPS_STRING_FORMAT           = new ConfigString("infoBlockPropertiesStringFormat",
    "\"%property: \", \"%value\"",
    "The text formatting used for enum properties in infoBlockProperties\n§6%%property - The name of this block property"),

  CHUNK_POS_FORMAT                    = new ConfigString("infoChunkPositionFormat",
    "\"%separator\", \"Sub-Chunk: %subX, %subY, %subZ\"",
    "The text formatting used for infoChunkPosition\n§6%%separator - The multi-line separator to visually cut this off from other info\n§6%%subX, %%subY, %%subZ - The coordinates of the sub-chunk which contains the position you're standing at"),

  CHUNK_SECTIONS_FORMAT               = new ConfigString("infoChunkSectionsFormat",
    "\"C: %count\"",
    "The text formatting used for infoChunkSections\n§6%%count - The number of rendered chunk sections (entirely culled subchunks are not rendered)"),

  CHUNK_SECTIONS_FULL_FORMAT          = new ConfigString("infoChunkSectionsLineFormat",
    "\"%count\"",
    "The text formatting used for infoChunkSectionsLine\n§6%%count - Extra statistics about the number of rendered chunk sections (entirely culled subchunks are not rendered)"),

  CHUNK_UPDATES_FORMAT                = new ConfigString("infoChunkUpdatesFormat",
    "\"TODO\"",
    "The text formatting used for infoChunkSectionsLine\n§6Note: This is unused in vanilla MiniHUD and is left in for parity\nAs a trick, you can use this to display memos or other unique strings!"),

  COORDINATES_FORMAT                  = new ConfigString("infoCoordinatesFormat",
    "\"XYZ: %x / %y / %z\"",
    "The text formatting used for infoCoordinates\n§6%%x, %%y, %%z - The coordinates of the position you're standing at\nCheck the README for more info on how to change precision."),

  COORDINATES_SCALED_NETHER_FORMAT    = new ConfigString("infoCoordinatesScaledNetherFormat",
    "\"%separator\", \"Nether: %x / %y / %z\"",
    "The text formatting used for infoCoordinatesScaled when in the overworld\n§6%%separator - The multi-line separator to visually cut this off from other info\n§6%%x, %%y, %%z - The nether-scaled coordinates of the position you're standing at\nCheck the README for more info on how to change precision."),

  COORDINATES_SCALED_OVERWORLD_FORMAT = new ConfigString("infoCoordinatesScaledOverworldFormat",
    "\"%separator\", \"Overworld: %x / %y / %z\"",
    "The text formatting used for infoCoordinatesScaled when in the nether\n§6%%separator - The multi-line separator to visually cut this off from other info\n§6%%x, %%y, %%z - The overworld-scaled coordinates of the position you're standing at\nCheck the README for more info on how to change precision."),

  DIFFICULTY_FORMAT                   = new ConfigString("infoDifficultyFormat",
    "\"Local Difficulty: %local // %clamped (Day %day)\"",
    "The text formatting used for infoDifficulty\n§6%%local - The local difficulty of this chunk\n§6%%clamped - The local difficulty of this chunk, with a lower and upper bound\n§6%%day - The current time of day, in ticks"),

  DIMENSION_FORMAT                    = new ConfigString("infoDimensionIdFormat",
    "\"%separator\", \"dim: %dim\"",
    "The text formatting used for infoDimensionId\n§6%%separator - The multi-line separator to visually cut this off from other info\n§6%%dim - The identifier of the dimension you're standing in"),

  DISTANCE_FORMAT                     = new ConfigString("infoDistanceFormat",
    "\"Distance: %dist (x: %dx y: %dy z: %dz) [to x: %refX y: %refY z: %refZ]\"",
    "The text formatting used for infoDistance\n§6%%dist - The distance in blocks to the reference point set by Generic/setDistanceReferencePoint\n§6%%dx, %%dy, %%dz - The distance in each axis to the reference point\n§6%%refX, %%refY, %%refZ - The coordinates of the reference point"),

  ENTITIES_FORMAT                     = new ConfigString("infoEntitiesFormat",
    "\", Server: %count\"",
    "The text formatting used for infoEntities\n§6%%count - The number of loaded entities"),

  ENTITIES_CLIENT_WORLD_FORMAT        = new ConfigString("infoEntitiesClientWorldFormat",
    "\"Entities - Client: %count\"",
    "The text formatting used for infoEntitiesClient\n§6%%count - The number of entities in the world list"),

  ENTITY_REG_NAME_FORMAT              = new ConfigString("infoEntityRegistryNameFormat",
    "\"Entity reg name: %name\"",
    "The text formatting used for infoEntityRegistryName\n§6%%name - The identifier of the entity you're looking at"),

  FACING_NORTH_FORMAT                 = new ConfigString("infoFacingNorthFormat",
    "\"Facing: north (Positive X)\"",
    "The text formatting used for infoFacing when facing north"),

  FACING_EAST_FORMAT                  = new ConfigString("infoFacingEastFormat",
    "\"Facing: east (Positive Z)\"",
    "The text formatting used for infoFacing when facing east"),

  FACING_SOUTH_FORMAT                 = new ConfigString("infoFacingSouthFormat",
    "\"Facing: south (Negative X)\"",
    "The text formatting used for infoFacing when facing south"),

  FACING_WEST_FORMAT                  = new ConfigString("infoFacingWestFormat",
    "\"Facing: west (Negative Z)\"",
    "The text formatting used for infoFacing when facing west"),

  FURNACE_XP_FORMAT                   = new ConfigString("infoFurnaceXpFormat",
    "\"Furnace XP: \", #aqua\"%count\"",
    "The text formatting used for infoFurnaceXp\n§6%%count - The amount of XP stored inside the furnace you're looking at"),

  FPS_FORMAT                          = new ConfigString("infoFPSFormat",
    "\"%fps fps\"",
    "The text formatting used for infoFPS\n§6%%fps - The FPS your monitor is running at"),

  HONEY_LEVEL_FORMAT                  = new ConfigString("infoHoneyLevelFormat",
    "\"Honey: \", #aqua\"%level\"",
    "The text formatting used for infoHoneyLevel\n§6%%level - The amount of honey stored inside the nest or hive you're looking at"),

  HORSE_SPEED_FORMAT                  = new ConfigString("infoHorseSpeedFormat",
    "\"Horse Speed: %speed m/s\"",
    "The text formatting used for infoHorseSpeed\n§6%%speed - The maximum speed of the horse you're riding, in meters per second"),

  HORSE_JUMP_FORMAT                   = new ConfigString("infoHorseJumpFormat",
    "\"Horse Jump: %height m\"",
    "The text formatting used for infoHorseJump\n§6%%height - The maximum jump height of the horse you're riding, in meters"),

  LIGHT_LEVEL_CLIENT_FORMAT           = new ConfigString("infoLightLevelClientFormat",
    "\"Client Light (block): %block\"",
    "The text formatting used for infoLightLevel for client light\n§6%%total - The raw brightness at where you're standing\n§6%%block - The block light level at where you're standing\n§6%%sky - The sky light level at where you're standing"),
  
  LIGHT_LEVEL_SERVER_FORMAT           = new ConfigString("infoLightLevelServerFormat",
    "\"Server Light (block): %block\"",
    "The text formatting used for infoLightLevel for server light\n§6%%total - The raw brightness at where you're standing\n§6%%block - The block light level at where you're standing\n§6%%sky - The sky light level at where you're standing"),

  LOOKING_AT_BLOCK_FORMAT             = new ConfigString("infoLookingAtBlockFormat",
    "\"Looking at block: %x, %y, %z\"",
    "The text formatting used for infoLookingAtBlock\n§6%%x, %%y, %%z - The coordinates of the block you're looking at"),

  LOOKING_AT_BLOCK_CHUNK_FORMAT       = new ConfigString("infoLookingAtBlockInChunkFormat",
    "\"%separator\", \"Block: %x, %y, %z in Sub-Chunk: %subX, %subY, %subZ\"",
    "The text formatting used for infoLookingAtBlockInChunk\n§6%%separator - The multi-line separator to visually cut this off from other info\n§6%%x, %%y, %%z - The chunk coordinates of the block you're looking at\n§6%%subX, %%subY, %%subZ - The coordinates of the sub-chunk which contains this block"),

  LOOKING_AT_ENTITY_FORMAT            = new ConfigString("infoLookingAtEntityFormat",
    "\"Entity: %entity\"",
    "The text formatting used for infoLookingAtEntity\n§6%%entity - The entity you're looking at"),

  LOOKING_AT_ENTITY_LIVING_FORMAT     = new ConfigString("infoLookingAtEntityLivingFormat",
    "\"Entity: %entity - HP: %health$.1f / %max$.1f\"",
    "The text formatting used for infoLookingAtEntity when the entity is living\n§6%%entity - The living entity you're looking at\n§6%%health - The current health of this entity\n§6%%max - The maximum health of this entity"),

  MEMORY_USAGE_FORMAT                 = new ConfigString("infoMemoryUsageFormat",
    "\"Mem: %pctUsed% %used/%max | Allocated: %pctAllocated% %total\"",
    "The text formatting used for infoMemoryUsage\n§6%%pctUsed - The percentage of RAM being used by the game out of the max\n§6%%used - The exact amount of RAM being used by the game\n§6%%max - The maximum amount of RAM the game will attempt to use\n§6%%pctAllocated - The percentage of RAM allocated to the game out of the max\n§6%%total - The amount of RAM allocated to the game"),

  LOADED_CHUNKS_COUNT_CLIENT_FORMAT   = new ConfigString("infoLoadedChunksCountClientFormat",
    "\"%stats\"",
    "The text formatting used for infoLoadedChunksCount when playing on a server\n§6%%stats - The client-side chunk source information"),

  LOADED_CHUNKS_COUNT_SERVER_FORMAT   = new ConfigString("infoLoadedChunksCountServerFormat",
    "\"Server: %loaded / %total - Client: %stats\"",
    "The text formatting used for infoLoadedChunksCount when playing singleplayer\n§6%%loaded - The number of loaded chunks\n§6%%total - The number of chunks that have been generated\n§6%%stats - The client-side chunk source information"),

  PARTICLE_COUNT_FORMAT               = new ConfigString("infoParticleCountFormat",
    "\"P: %count\"",
    "The text formatting used for infoParticleCount\n§6%%count - The number of rendered particles"),

  PING_FORMAT                         = new ConfigString("infoPingFormat",
    "\"Ping: %ping\"",
    "The text formatting used for infoPing\n§6%%ping - The current latency in milliseconds"),

  REGION_FILE_FORMAT                  = new ConfigString("infoRegionFileFormat",
    "\"%separator\", \"Region: r.%x.%z\"",
    "The text formatting used for infoRegionFile\n§6%%separator - The multi-line separator to visually cut this off from other info\n§6%%x, %%z - The coordinates of this region file"),

  ROTATION_PITCH_FORMAT               = new ConfigString("infoRotationPitchFormat",
    "\"%separator\", \"Pitch: %pitch\"",
    "The text formatting used for infoRotationPitch\n§6%%separator - The multi-line separator to visually cut this off from other info\n§6%%pitch - The camera pitch in degrees, truncated to the specified decimal position\nCheck the README for more info on how to change precision."),

  ROTATION_YAW_FORMAT                 = new ConfigString("infoRotationYawFormat",
    "\"Yaw: %yaw\"",
    "The text formatting used for infoRotationYaw\n§6%%separator - The multi-line separator to visually cut this off from other info\n§6%%yaw - The camera yaw in degrees, truncated to the specified decimal position\nCheck the README for more info on how to change precision."),

  SEPARATOR_FORMAT                    = new ConfigString("separatorFormat",
    " / ",
    "The text formatting used for the multi-line separators"),

  TPS_GOOD_FORMAT                     = new ConfigString("tpsGoodFormat",
    "#green\"%value\"",
    "The text formatting used for infoServerTPS when the TPS is at or below 20\n§6%%tps - The number of gameticks processed by the server per second"),

  TPS_IMPACTFUL_FORMAT                = new ConfigString("tpsImpactfulFormat",
    "#red\"%value\"",
    "The text formatting used for infoServerTPS when the TPS is above 20\n§6%%value - The number of gameticks processed by the server per second"),

  MSPT_GOOD_FORMAT                    = new ConfigString("msptGoodFormat",
    "#green\"%value\"",
    "The text formatting used for infoServerTPS mspt when MSPT is at or below 40\n§6%%value - The time it takes for the server to process 1 gametick, in milliseconds"),

  MSPT_MEDIUM_FORMAT                  = new ConfigString("msptMediumFormat",
    "#yellow\"%value\"",
    "The text formatting used for infoServerTPS when the MSPT is between 40 exclusive and 45 inclusive\n§6%%value - The time it takes for the server to process 1 gametick, in milliseconds"),

  MSPT_BAD_FORMAT                     = new ConfigString("msptBadFormat",
    "#gold\"%value\"",
    "The text formatting used for infoServerTPS when the MSPT is between 45 exclusive and 50 inclusive\n§6%%value - The time it takes for the server to process 1 gametick, in milliseconds"),

  MSPT_IMPACTFUL_FORMAT               = new ConfigString("msptImpactfulFormat",
    "#red\"%value\"",
    "The text formatting used for infoServerTPS when the MSPT is above 50\n§6%%value - The time it takes for the server to process 1 gametick, in milliseconds"),

  SERVER_TPS_VANILLA_FORMAT           = new ConfigString("infoServerTPSVanillaFormat",
    "\"Server TPS: \", %tps, \" (MSPT [est]: \", %mspt, \")\"",
    "The text formatting used for infoServerTPS when playing on a vanilla server\n§6%%tps - The formatted TPS string to be inputted\n§6%%mspt - The formatted MSPT string to be inputted"),

  SERVER_TPS_CARPET_FORMAT            = new ConfigString("infoServerTPSCarpetFormat",
    "\"Server TPS: \", %tps, \" MSPT: \", %mspt",
    "The text formatting used for infoServerTPS when playing on a carpet server\n§6%%tps - The formatted TPS string to be inputted\n§6%%mspt - The formatted MSPT string to be inputted"),

  SERVER_TPS_NULL_FORMAT              = new ConfigString("infoServerTPSNullFormat",
    "\"Server TPS: <no valid data>\"",
    "The text formatting used for infoServerTPS when the server info is unavailable"),

  SLIME_CHUNK_YES_FORMAT              = new ConfigString("infoSlimeChunkYesFormat",
    "\"Slime chunk: \", #green\"YES\"",
    "The text formatting used for the infoSlimeChunk when standing in a slime chunk"),

  SLIME_CHUNK_NO_FORMAT               = new ConfigString("infoSlimeChunkNoFormat",
    "\"Slime chunk: \", #red\"NO\"",
    "The text formatting used for the infoSlimeChunk when not standing in a slime chunk"),

  SLIME_CHUNK_NO_SEED_FORMAT          = new ConfigString("infoSlimeChunkNoSeedFormat",
    "\"Slime chunk: \", \"<world seed not known>\"",
    "The text formatting used for infoSlimeChunk when the seed info is unavailable"),

  SPEED_FORMAT                        = new ConfigString("infoSpeedFormat",
    "\"%separator\", \"Speed: %speed m/s\"",
    "The text formatting used for infoSpeed\n§6%%separator - The multi-line separator to visually cut this off from other info\n§6%%speed - The speed you're travelling at in meters per second, truncated to the specified decimal position\nCheck the README for more info on how to change precision."),

  SPEED_AXIS_FORMAT                   = new ConfigString("infoSpeedAxisFormat",
    "\"Speed: x: %x y: %y z: %z m/s\"",
    "The text formatting used for infoSpeedAxis\n§6%%x, %%y, %%z - The speed per axis of travel in meters per second, truncated to the specified decimal position\nCheck the README for more info on how to change precision."),

  SPEED_HV_FORMAT                     = new ConfigString("infoSpeedHVFormat",
    "\"Speed: xz: %h y: %v m/s\"",
    "The text formatting used for infoSpeedHV\n§6%%h, %%v - The horizontal and vertical speed you're travelling at in meters per second\nCheck the README for more info on how to change precision."),

  SPRINTING_FORMAT                    = new ConfigString("infoSprintingFormat",
    "#gold\"Sprinting\"",
    "The text formatting used for infoSprinting"),

  TILE_ENTITIES_FORMAT                = new ConfigString("infoTileEntitiesFormat",
    "\"Client world TE - L: ?, T: ? - TODO\"",
    "The text formatting used for infoTileEntities"),

  TIME_DAY_MODULO_FORMAT              = new ConfigString("infoTimeDayModuloFormat",
    "\"Day time % %mod: %time\"",
    "The text formatting used for infoTimeDayModulo\n§6%%mod - The timeDayDivisor value in the Generic tab\n§6%%time - The time-of-day value after performing the mod operation"),

  TIME_REAL_FORMAT                    = new ConfigString("infoTimeIRLFormat",
    "\"%time$tk:%time$tM:%time$tS\"",
    "The text formatting used for infoTimeIRL\n§6%%time - The generic time variable used for all formatting inputs\nCheck the README for more info on time formatting."),

  TIME_TOTAL_MODULO_FORMAT            = new ConfigString("infoTimeTotalModuloFormat",
    "\"Total time % %mod: %time\"",
    "The text formatting used for infoTimeTotalModulo\n§6%%mod - The timeTotalDivisor value in the Generic tab\n§6%%time - The total game time value after performing the mod operation"),

  TIME_WORLD_FORMAT                   = new ConfigString("infoTimeWorldFormat",
    "\"World time: %day - total: %total\"",
    "The text formatting used for infoTimeWorld\n§6%%day - The current time of day, in ticks\n§6%%total - The total time played in this world, in ticks"),

  //#if MC >= 11900
  //$$ TIME_WORLD_FORMATTED_FORMAT         = new ConfigString("infoWorldTimeFormattedFormat",
  //$$   "\"MC time: (day %dayFrom0) %hour:%min:xx\"",
  //$$   "The text formatting used for infoTimeWorldFormatted\n§6The supported placeholders are: %%dayFrom0, %%dayFrom1, %%hour, %%min, %%sec and %%moon\n§6%%dayFrom0 starts the day counter from 0. %%dayFrom1 starts from 1.");
  //#else
  TIME_WORLD_FORMATTED_FORMAT         = new ConfigString("infoWorldTimeFormattedFormat",
    "\"MC time: (day %dayFrom0) %hour:%min:xx\"",
    "The text formatting used for infoTimeWorldFormatted\n§6The supported placeholders are: %%dayFrom0, %%dayFrom1, %%hour, %%min and %%sec\n§6%%dayFrom0 starts the day counter from 0. %%dayFrom1 starts from 1.");
  //#endif

  public static final ImmutableList<IConfigValue> COLORS = ImmutableList.of(
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
    COLOR15
  );

  public static final ImmutableList<IConfigValue> OPTIONS = ImmutableList.of(
    BEE_COUNT_FORMAT,
    BIOME_FORMAT,
    BIOME_REG_NAME_FORMAT,
    BLOCK_BREAK_SPEED_FORMAT,
    BLOCK_IN_CHUNK_FORMAT,
    BLOCK_POS_FORMAT,
    BLOCK_PROPS_BLOCK_FORMAT,
    BLOCK_PROPS_TRUE_FORMAT,
    BLOCK_PROPS_FALSE_FORMAT,
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
    ENTITIES_FORMAT,
    ENTITIES_CLIENT_WORLD_FORMAT,
    ENTITY_REG_NAME_FORMAT,
    FACING_NORTH_FORMAT,
    FACING_EAST_FORMAT,
    FACING_SOUTH_FORMAT,
    FACING_WEST_FORMAT,
    FURNACE_XP_FORMAT,
    FPS_FORMAT,
    HONEY_LEVEL_FORMAT,
    HORSE_SPEED_FORMAT,
    HORSE_JUMP_FORMAT,
    LIGHT_LEVEL_CLIENT_FORMAT,
    LIGHT_LEVEL_SERVER_FORMAT,
    LOOKING_AT_BLOCK_FORMAT,
    LOOKING_AT_BLOCK_CHUNK_FORMAT,
    LOOKING_AT_ENTITY_FORMAT,
    LOOKING_AT_ENTITY_LIVING_FORMAT,
    MEMORY_USAGE_FORMAT,
    LOADED_CHUNKS_COUNT_CLIENT_FORMAT,
    LOADED_CHUNKS_COUNT_SERVER_FORMAT,
    PARTICLE_COUNT_FORMAT,
    PING_FORMAT,
    REGION_FILE_FORMAT,
    ROTATION_PITCH_FORMAT,
    ROTATION_YAW_FORMAT,
    SEPARATOR_FORMAT,
    TPS_GOOD_FORMAT,
    TPS_IMPACTFUL_FORMAT,
    MSPT_GOOD_FORMAT,
    MSPT_MEDIUM_FORMAT,
    MSPT_BAD_FORMAT,
    MSPT_IMPACTFUL_FORMAT,
    SERVER_TPS_VANILLA_FORMAT,
    SERVER_TPS_CARPET_FORMAT,
    SERVER_TPS_NULL_FORMAT,
    SLIME_CHUNK_YES_FORMAT,
    SLIME_CHUNK_NO_FORMAT,
    SLIME_CHUNK_NO_SEED_FORMAT,
    SPEED_FORMAT,
    SPEED_AXIS_FORMAT,
    SPEED_HV_FORMAT,
    SPRINTING_FORMAT,
    TILE_ENTITIES_FORMAT,
    TIME_DAY_MODULO_FORMAT,
    TIME_REAL_FORMAT,
    TIME_TOTAL_MODULO_FORMAT,
    TIME_WORLD_FORMAT,
    TIME_WORLD_FORMATTED_FORMAT
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
    ConfigUtils.readConfigBase(root, "Formats", Formats.COLORS);
    ConfigUtils.readConfigBase(root, "Formats", Formats.OPTIONS);
  }

  @Override
  public void save()
  {
    File dir = FileUtils.getConfigDirectory();
    if(!(dir.exists() && dir.isDirectory()) && !dir.mkdirs())
      return;

    JsonObject root = new JsonObject();
    ConfigUtils.writeConfigBase(root, "Formats", Formats.COLORS);
    ConfigUtils.writeConfigBase(root, "Formats", Formats.OPTIONS);
    JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
  }
}