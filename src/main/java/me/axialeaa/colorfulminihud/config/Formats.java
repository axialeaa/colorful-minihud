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
    COLOR0  = new ConfigColor("color0",  "#0021222C", "Variable color 0"),
    COLOR1  = new ConfigColor("color1",  "#00FF5555", "Variable color 1"),
    COLOR2  = new ConfigColor("color2",  "#0050FA7B", "Variable color 2"),
    COLOR3  = new ConfigColor("color3",  "#00F1FA8C", "Variable color 3"),
    COLOR4  = new ConfigColor("color4",  "#00BD93F9", "Variable color 4"),
    COLOR5  = new ConfigColor("color5",  "#00FF79C6", "Variable color 5"),
    COLOR6  = new ConfigColor("color6",  "#008BE9FD", "Variable color 6"),
    COLOR7  = new ConfigColor("color7",  "#00F8F8F2", "Variable color 7"),
    COLOR8  = new ConfigColor("color8",  "#006272A4", "Variable color 8"),
    COLOR9  = new ConfigColor("color9",  "#00FF6E6E", "Variable color 9"),
    COLOR10 = new ConfigColor("color10", "#0069FF94", "Variable color 10"),
    COLOR11 = new ConfigColor("color11", "#00FFFFA5", "Variable color 11"),
    COLOR12 = new ConfigColor("color12", "#00D6ACFF", "Variable color 12"),
    COLOR13 = new ConfigColor("color13", "#00FF92DF", "Variable color 13"),
    COLOR14 = new ConfigColor("color14", "#00A4FFFF", "Variable color 14"),
    COLOR15 = new ConfigColor("color15", "#00FFFFFF", "Variable color 15");

  public static final ConfigString
    BEE_COUNT_FORMAT                    = new ConfigString("infoBeeCountFormat",
      "\"Bees: \", #aqua\"%count\"",
      "The text formatting used for infoBeeCount\n§6%%%%count - The number of bees stored inside the nest/hive you're looking at"),

    BIOME_FORMAT                        = new ConfigString("infoBiomeFormat",
      "\"Biome: %biome\"",
      "The text formatting used for infoBiome\n§6%%%%biome - The name of the biome you're standing in"),

    BIOME_REG_NAME_FORMAT               = new ConfigString("infoBiomeRegistryNameFormat",
      "\"Biome reg name: %regName\"",
      "The text formatting used for infoBiomeRegistryName\n§6%%%%regName - The registry name of the current biome"),

    BLOCK_BREAK_SPEED_FORMAT            = new ConfigString("infoBlockBreakSpeedFormat",
      "\"BBS: %speed\"",
      "The text formatting used for infoBlockBreakSpeed\n§6%%%%speed - The block breaking speed in blocks per second"),

    BLOCK_IN_CHUNK_FORMAT               = new ConfigString("infoBlockInChunkFormat",
      "\"Block: %x, %y, %z within Sub-Chunk: %chunkX, %chunkY, %chunkZ\"",
      "The text formatting used for infoBlockInChunk\n§6%%%%x, %%%%y, %%%%z - The chunk coordinates of the block position you're standing at\n§6%%%%chunkX, %%%%chunkY, %%%%chunkZ - The coordinates of the subchunk which contains this position"),

    BLOCK_POS_FORMAT                    = new ConfigString("infoBlockPositionFormat",
      "\"Block: %x, %y, %z\"",
      "The text formatting used for infoBlockPosition\n§6%%%%x, %%%%y, %%%%z - The block coordinates of the current player position"),

    BLOCK_PROPS_BLOCK_FORMAT          = new ConfigString("infoBlockPropertiesBlockFormat",
      "\"%block\"",
      "The text formatting used for block ID in infoBlockPosition\n§6%%%%block - The name of the block you're looking at"),

    BLOCK_PROPS_TRUE_FORMAT     = new ConfigString("infoBlockPropertiesTrueFormat",
      "\"%property: \", #green\"TRUE\"",
      "The text formatting used for boolean properties in infoBlockProperties, when true\n§6%%%%property - The name of this block property"),

    BLOCK_PROPS_FALSE_FORMAT    = new ConfigString("infoBlockPropertiesFalseFormat",
      "\"%property: \", #red\"FALSE\"",
      "The text formatting used for boolean properties in infoBlockProperties, when false\n§6%%%%property - The name of this block property"),

    BLOCK_PROPS_DIRECTION_FORMAT        = new ConfigString("infoBlockPropertiesDirectionFormat",
      "\"%property: \", #gold\"%value\"",
      "The text formatting used for direction properties in infoBlockProperties\n§6%%%%property - The name of this block property"),

    BLOCK_PROPS_INT_FORMAT              = new ConfigString("infoBlockPropertiesIntFormat",
      "\"%property: \", #aqua\"%value\"",
      "The text formatting used for integer properties in infoBlockProperties\n§6%%%%property - The name of this block property"),

    BLOCK_PROPS_STRING_FORMAT           = new ConfigString("infoBlockPropertiesStringFormat",
      "\"%property: \", \"%value\"",
      "The text formatting used for enum properties in infoBlockProperties\n§6%%%%property - The name of this block property"),

    CHUNK_POS_FORMAT                    = new ConfigString("infoChunkPositionFormat",
      "\"%separator\", \"Sub-Chunk: %chunkX, %chunkY, %chunkZ\"",
      "The text formatting used for infoChunkPosition\n§6%%%%separator - The multi-line separator to visually cut this off from other info\n§6%%%%chunkX, %%%%chunkY, %%%%chunkZ - The coordinates of the subchunk which contains the position you're standing at"),

    CHUNK_SECTIONS_FORMAT               = new ConfigString("infoChunkSectionsFormat",
      "\"C: %count\"",
      "The text formatting used for infoChunkSections\n§6%%%%count - The number of rendered chunk sections (entirely culled subchunks will be unrendered)"),

    CHUNK_SECTIONS_FULL_FORMAT          = new ConfigString("infoChunkSectionsLineFormat",
      "\"%count\"",
      "The text formatting used for infoChunkSectionsLine\n§6%%%%count - Extra statistics about the number of rendered chunk sections (entirely culled subchunks will be unrendered)"),

    CHUNK_UPDATES_FORMAT                = new ConfigString("infoChunkUpdatesFormat",
      "\"TODO\"",
      "The text formatting used for infoChunkSectionsLine\n§6Note: This is unused in vanilla MiniHUD and is left in for parity.\nAs a trick, you can use this to display memos or other unique strings!"),

    COORDINATES_FORMAT                  = new ConfigString("infoCoordinatesFormat",
      "\"XYZ: %x / %y / %z\"",
      "The text formatting used for infoCoordinates\n§6%%%%x, %%%%y, %%%%z - The coordinates of the current player position.\nCheck the README for how to change precision."),

    COORDINATES_SCALED_NETHER_FORMAT    = new ConfigString("infoCoordinatesScaledNetherFormat",
      "\"%separator\", \"Nether: %x / %y / %z\"",
      "The text formatting used for infoCoordinatesScaled when in the overworld\n§6%%%%separator - The multi-line separator to visually cut this off from other info\n§6%%%%x, %%%%y, %%%%z - The nether-scaled coordinates of the current player position.\nCheck the README for how to change precision."),

    COORDINATES_SCALED_OVERWORLD_FORMAT = new ConfigString("infoCoordinatesScaledOverworldFormat",
      "\"%separator\", \"Overworld: %x / %y / %z\"",
      "The text formatting used for infoCoordinatesScaled when in the nether\n§6%%%%separator - The multi-line separator to visually cut this off from other info\n§6%%%%x, %%%%y, %%%%z - The overworld-scaled coordinates of the current player position.\nCheck the README for how to change precision."),

    DIFFICULTY_FORMAT                   = new ConfigString("infoDifficultyFormat",
      "\"Local Difficulty: %local // %clamped (Day %day)\"",
      "The text formatting used for infoDifficulty\n§6%%%%local - The local difficulty of this chunk\n§6%%%%clamped - The local difficulty of this chunk, with a lower and upper bound\n§6%%%%day - The current time of day"),

    DIMENSION_FORMAT                    = new ConfigString("infoDimensionIdFormat",
      "\"%separator\", \"dim: %id\"",
      "The text formatting used for infoDimensionId\n§6%%%%separator - The multi-line separator to visually cut this off from other info\n§6%%%%id - The identifier of the dimension you're standing in"),

    DISTANCE_FORMAT                     = new ConfigString("infoDistanceFormat",
      "\"Distance: %dist (x: %dx y: %dy z: %dz) [to x: %refX y: %refY z: %refZ]\"",
      "The text formatting used for infoDistance\n§6%%%%dist - The distance in blocks to the reference point set by Generic/setDistanceReferencePoint\n§6%%%%dx, %%%%dy, %%%%%dz - the distance in each axis to the reference point\n§6%%%%refX, %%%%refY, %%%%refZ - The coordinates of the reference point"),

    ENTITIES_CLIENT_WORLD_FORMAT        = new ConfigString("infoEntitiesClientWorldFormat",
      "\"Entities - Client: %count\"",
      "The text formatting used for infoEntitiesClient\n§6%%%%count - The number of entities in the world list"),

    ENTITIES_FORMAT                     = new ConfigString("infoEntitiesFormat",
      "\", Server: %count\"",
      "The text formatting used for infoEntities\n§6%%%%count - The number of loaded entities"),

    ENTITY_REG_NAME_FORMAT              = new ConfigString("infoEntityRegistryNameFormat",
      "\"Entity reg name: %name\"",
      "The text formatting used for infoEntityRegistryName\n§6%%%%name - The ID of the entity you're looking at"),

    FACING_NORTH_FORMAT                 = new ConfigString("infoFacingNorthFormat",
      "\"Facing: north (Positive X)\"",
      "The text formatting used for infoFacing when facing north"),

    FACING_SOUTH_FORMAT                 = new ConfigString("infoFacingSouthFormat",
      "\"Facing: south (Negative X)\"",
      "The text formatting used for infoFacing when facing south"),

    FACING_EAST_FORMAT                  = new ConfigString("infoFacingEastFormat",
      "\"Facing: east (Positive Z)\"",
      "The text formatting used for infoFacing when facing east"),

    FACING_WEST_FORMAT                  = new ConfigString("infoFacingWestFormat",
      "\"Facing: west (Negative Z)\"",
      "The text formatting used for infoFacing when facing west"),

    FPS_FORMAT                          = new ConfigString("infoFPSFormat",
      "\"%fps fps\"",
      "The text formatting used for infoFPS\n§6%%%%fps - The FPS your monitor is running at"),

    FURNACE_XP_FORMAT                   = new ConfigString("infoFurnaceXpFormat",
      "\"Furnace XP: \", #aqua\"%count\"",
      "The text formatting used for infoFurnaceXp\n§6%%%%count - The amount of XP stored inside the furnace you're looking at"),

    HONEY_LEVEL_FORMAT                  = new ConfigString("infoHoneyLevelFormat",
      "\"Honey: \", #aqua\"%level\"",
      "The text formatting used for infoHoneyLevel\n§6%%%%level - The amount of honey stored inside the nest/hive you're looking at"),

    HORSE_JUMP_FORMAT                   = new ConfigString("infoHorseJumpFormat",
      "\"Horse Jump: %jump m\"",
      "The text formatting used for infoHorseJump\n§6%%%%jump - The maximum jump height of the horse you're riding"),

    HORSE_SPEED_FORMAT                  = new ConfigString("infoHorseSpeedFormat",
      "\"Horse Speed: %speed m/s\"",
      "The text formatting used for infoHorseSpeed\n§6%%%%speed - The maximum speed of the horse you're riding"),

    LIGHT_LEVEL_CLIENT_FORMAT           = new ConfigString("infoLightLevelClientFormat",
      "\"Client Light: %total (block: %block, sky: %sky)\"",
      "The text formatting used for infoLightLevelClient\n§6%%%%total - The raw brightness at where you're standing\n§6%%%%block - The block light level at where you're standing\n§6%%%%sky - The sky light level at where you're standing"),

    LIGHT_LEVEL_SERVER_FORMAT           = new ConfigString("infoLightLevelServerFormat",
      "\"Server Light: %total (block: %block, sky: %sky)\"",
      "The text formatting used for infoLightLevelServer\n§6%%%%total - The raw brightness at where you're standing\n§6%%%%block - The block light level at where you're standing\n§6%%%%sky - The sky light level at where you're standing"),

    LOOKING_AT_BLOCK_FORMAT             = new ConfigString("infoLookingAtBlockFormat",
      "\"Looking at block: %x, %y, %z\"",
      "The text formatting used for infoLookingAtBlock\n§6%%%%x, %%%%y, %%%%z - The coordinates of the block you're looking at"),

    LOOKING_AT_BLOCK_CHUNK_FORMAT       = new ConfigString("infoLookingAtBlockInChunkFormat",
      "\"%separator\", \"Block: %x, %y, %z in Sub-Chunk: %chunkX, %chunkY, %chunkZ\"",
      "The text formatting used for infoLookingAtBlockInChunk\n§6%%%%separator - The multi-line separator to visually cut this off from other info\n§6%%%%x, %%%%y, %%%%z - The chunk coordinates of the block you're looking at\n§6%%%%chunkX, %%%%chunkY, %%%%chunkZ - The coordinates of the subchunk which contains this block"),

    LOOKING_AT_ENTITY_FORMAT            = new ConfigString("infoLookingAtEntityFormat",
      "\"Entity: %entity\"",
      "The text formatting used for infoLookingAtEntity\n§6%%%%entity - The entity you're looking at"),

    LOOKING_AT_ENTITY_LIVING_FORMAT     = new ConfigString("infoLookingAtEntityLivingFormat",
      "\"Entity: %entity - HP: %health$.1f / %max$.1f\"",
      "The text formatting used for infoLookingAtEntity when the entity is living\n§6%%%%entity - The living entity you're looking at\n§6%%%%health - The health of the entity\n§6%%%%max - The maximum health of the entity"),

    LOADED_CHUNKS_COUNT_SERVER_FORMAT   = new ConfigString("infoLoadedChunksCountServerFormat",
      "\"Server: %loaded / %total - Client: %stats\"",
      "The text formatting used for infoLoadedChunksCount when playing singleplayer\n§6%%%%loaded - The number of loaded chunks\n§6%%%%total - The number of chunks that have been generated\n§6%%%%stats - The client-side chunk source information"),

    LOADED_CHUNKS_COUNT_CLIENT_FORMAT   = new ConfigString("infoLoadedChunksCountClientFormat",
      "\"%stats\"",
      "The text formatting used for infoLoadedChunksCount when playing on a server\n§6%%%%stats - The client-side chunk source information"),

    MEMORY_USAGE_FORMAT                 = new ConfigString("infoMemoryUsageFormat",
      "\"Mem: %pctUsed% %used/%max | Allocated: %pctAllocated% %total\"",
      "The text formatting used for infoMemoryUsage\n§6%%%%pctUsed - The percentage of RAM being used by the game out of the max\n§6%%%%used - The exact amount of RAM being used by the game\n§6%%%%max - The maximum amount of RAM the game will attempt to use\n§6%%%%pctAllocated - The percentage of RAM allocated to the game out of the max\n§6%%%%allocated - The amount of RAM allocated to the game"),

    PARTICLE_COUNT_FORMAT               = new ConfigString("infoParticleCountFormat",
      "\"P: %count\"",
      "The text formatting used for infoParticleCount\n§6%%%%count - The number of rendered particles"),

    PING_FORMAT                         = new ConfigString("infoPingFormat",
      "\"Ping: %ping\"",
      "The text formatting used for infoPing\n§6%%%%ping - The current latency in milliseconds"),

    REGION_FILE_FORMAT                  = new ConfigString("infoRegionFileFormat",
      "\"%separator\", \"Region: r.%x.%z\"",
      "The text formatting used for infoRegionFile\n§6%%%%separator - The multi-line separator to visually cut this off from other info\n§6%%%%x, %%%%z - The coordinates of this region file"),

    ROTATION_PITCH_FORMAT               = new ConfigString("infoRotationPitchFormat",
      "\"%separator\", \"Pitch: %pitch\"",
      "The text formatting used for infoRotationPitch\n§6%%%%separator - The multi-line separator to visually cut this off from other info\n§6%%%%pitch - The camera pitch in degrees, truncated to the specified decimal position. Check the README for more info"),

    ROTATION_YAW_FORMAT                 = new ConfigString("infoRotationYawFormat",
      "\"Yaw: %yaw\"",
      "The text formatting used for infoRotationYaw\n§6%%%%separator - The multi-line separator to visually cut this off from other info\n§6%%%%yaw - The camera yaw in degrees, truncated to the specified decimal position. Check the README for more info"),

    SEPARATOR_FORMAT                    = new ConfigString("infoSeparatorFormat",
      " / ",
      "The text formatting used for the multi-line separators"),

    TPS_BELOW_20_FORMAT                 = new ConfigString("tpsBelow20Format",
      "#green\"%tps\"",
      "The text formatting used for infoServerTPS when the TPS is at or below 20\n§6%%%%tps - The current ticks per second value"),

    TPS_ABOVE_20_FORMAT                 = new ConfigString("tpsAbove20Format",
      "#red\"%tps\"",
      "The text formatting used for infoServerTPS when the TPS is above 20\n§6%%%%tps - The current ticks per second value"),

    MSPT_BELOW_40_FORMAT                = new ConfigString("msptBelow40Format",
      "#green\"%mspt\"",
      "The text formatting used for infoServerTPS mspt when MSPT is at or below 40\n§6%%%%mspt - The current milliseconds per tick value"),

    MSPT_40_45_FORMAT                   = new ConfigString("mspt40to45Format",
      "#yellow\"%mspt\"",
      "The text formatting used for infoServerTPS when the MSPT is between 40 exclusive and 45 inclusive\n§6%%%%mspt - The current milliseconds per tick value"),

    MSPT_45_50_FORMAT                   = new ConfigString("mspt45to50Format",
      "#gold\"%mspt\"",
      "The text formatting used for infoServerTPS when the MSPT is between 45 exclusive and 50 inclusive\n§6%%%%mspt - The current milliseconds per tick value"),

    MSPT_ABOVE_50_FORMAT                = new ConfigString("msptAbove50Format",
      "#red\"%mspt\"",
      "The text formatting used for infoServerTPS when the MSPT is above 50\n§6%%%%mspt - The current milliseconds per tick value"),

    SERVER_TPS_VANILLA_FORMAT           = new ConfigString("infoServerTPSVanillaFormat",
      "\"Server TPS: \", %tps, \" (MSPT [est]: \", %mspt, \")\"",
      "The text formatting used for infoServerTPS when playing on a vanilla server\n§6%%%%tps - The formatted TPS string to be inputted\n§6%%%%mspt - The formatted MSPT string to be inputted"),

    SERVER_TPS_CARPET_FORMAT            = new ConfigString("infoServerTPSCarpetFormat",
      "\"Server TPS: \", %tps, \" MSPT: \", %mspt",
      "The text formatting used for infoServerTPS when playing on a carpet server\n§6%%%%tps - The formatted TPS string to be inputted\n§6%%%%mspt - The formatted MSPT string to be inputted"),

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
      "The text formatting used for infoSpeed\n§6%%%%separator - The multi-line separator to visually cut this off from other info\n§6%%%%speed - The speed you're travelling in meters per second, truncated to the specified decimal position. Check the README for more info"),

    SPEED_AXIS_FORMAT                   = new ConfigString("infoSpeedAxisFormat",
      "\"Speed: x: %x y: %y z: %z m/s\"",
      "The text formatting used for infoSpeedAxis\n§6%%%%x, %%%%y, %%%%z - The speed per axis of travel in meters per second, truncated to the specified decimal position. Check the README for more info"),

    SPEED_HV_FORMAT                     = new ConfigString("infoSpeedHVFormat",
      "\"Speed: xz: %h y: %v m/s\"",
      "The text formatting used for infoSpeedHV\n§6%%%%h, %%%%v - The horizontal and vertical speed you're travelling in meters per second.\nCheck the README for more how to change precision."),

    TILE_ENTITIES_FORMAT                = new ConfigString("infoTileEntitiesFormat",
      "\"Client world TE - L: %loaded, T: %ticking - TODO\"",
      "The text formatting used for infoTileEntities\n§6%%%%loaded - The number of tile entities in loaded chunks\n§6%%%%ticking - The number of tile entities being processed"),

    TIME_DAY_MODULO_FORMAT              = new ConfigString("infoTimeDayModuloFormat",
      "\"Day time % %mod: %time\"",
      "The text formatting used for infoTimeDayModulo\n§6%%%%mod - The timeDayDivisor value in the Generic tab\n§6%%%%time - The day time value after performing the mod operation"),

    TIME_REAL_FORMAT                    = new ConfigString("infoTimeIRLFormat",
      "\"%time$tk:%time$tM:%time$tS\"",
      "The format string for real time, see the Java Formatter\nclass for the format patterns, if needed.\n§6%%%%time - The time variable"),

    TIME_TOTAL_MODULO_FORMAT            = new ConfigString("infoTimeTotalModuloFormat",
      "\"Total time % %mod: %time\"",
      "The text formatting used for infoTimeTotalModulo\n§6%%%%mod - The timeTotalDivisor value in the Generic tab\n§6%%%%time - The total game time value after performing the mod operation"),

    TIME_WORLD_FORMAT                   = new ConfigString("infoTimeWorldFormat",
      "\"World time: %day - total: %total\"",
      "The text formatting used for infoTimeWorld\n§6%%%%day - The current day time value\n§6%%%%total - The total game time value"),

    TIME_WORLD_FORMATTED_FORMAT         = new ConfigString("infoTimeWorldFormattedFormat",
      "\"MC time: (day %dayFrom0) %hour:%min:xx\"",
      "The format string for the Minecraft time.\nThe supported placeholders are: %%%%dayFrom0, %%%%dayFrom1, %%%%hour, %%%%min, %%%%sec.\n%%%%dayFrom0 starts the day counter from 0, %%%%dayFrom1 starts from 1.");

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
    ENTITIES_CLIENT_WORLD_FORMAT,
    ENTITIES_FORMAT,
    ENTITY_REG_NAME_FORMAT,
    FACING_NORTH_FORMAT,
    FACING_SOUTH_FORMAT,
    FACING_EAST_FORMAT,
    FACING_WEST_FORMAT,
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
