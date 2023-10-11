package me.axialeaa.colorfulminihud.config;

import com.google.common.collect.ImmutableList;

import fi.dy.masa.malilib.config.options.ConfigString;

public class Formats
{
  public static final ConfigString
    BEE_COUNT_FORMAT                 = new ConfigString("infoBeeCountFormat", "[{\"color\":\"white\",\"text\":\"" +
    "Bees: \"}, {\"color\":\"aqua\",\"text\":\"{bees}\"}]", "Format of infoBeeCount"),
    BIOME_FORMAT                     = new ConfigString("infoBiomeFormat", "{\"color\":\"white\",\"text\":\"" +
    "Biome: {biome}\"}", "Format of infoBiome"),
    BIOME_REG_NAME_FORMAT            = new ConfigString("infoBiomeRegistryNameFormat", "{\"color\":\"white\",\"text\":\"" +
    "Biome reg name: {name}\"}", "Format of infoBiomeRegistryName"),
    BLOCK_BREAK_SPEED_FORMAT         = new ConfigString("infoBlockBreakSpeedFormat", "{\"color\":\"white\",\"text\":\"" +
    "BBS: {bbs}.2f\"}", "Format of infoBlockBreakSpeed"),
    BLOCK_IN_CHUNK_FORMAT            = new ConfigString("infoBlockInChunkFormat", "{\"color\":\"white\",\"text\":\"" +
    "Block: {x}d, {y}d, {z}d within Sub-Chunk: {cx}d, {cy}d, {cz}d\"}", "Format of infoBlockInChunk"),
    BLOCK_POS_FORMAT                 = new ConfigString("infoBlockPositionFormat", "{\"color\":\"white\",\"text\":\"" +
    "Block: {x}d, {y}d, {z}d\"}", "Format of infoBlockPosition"),
    BLOCK_PROPS_SEPARATOR_FORMAT     = new ConfigString("infoBlockPropertiesSeparatorFormat", "{\"color\":\"white\",\"text\":\"" +
    ":\"}", "Format of the separator used in infoBlockProperties"),
    BLOCK_PROPS_BOOLEAN_TRUE_FORMAT  = new ConfigString("infoBlockPropertiesBooleanTrueFormat", "[{\"color\":\"white\",\"text\":\"" +
    "{prop}\"}, {separator}, {\"color\":\"green\",\"text\":\"TRUE\"}]", "Format of boolean properties in infoBlockProperties when they're true"),
    BLOCK_PROPS_BOOLEAN_FALSE_FORMAT = new ConfigString("infoBlockPropertiesBooleanFalseFormat", "[{\"color\":\"white\",\"text\":\"" +
    "{prop}\"}, {separator}, {\"color\":\"red\",\"text\":\"FALSE\"}]", "Format of boolean properties in infoBlockProperties when they're false"),
    BLOCK_PROPS_DIRECTION_FORMAT     = new ConfigString("infoBlockPropertiesDirectionFormat", "[{\"color\":\"white\",\"text\":\"" +
    "{prop}\"}, {separator}, {\"color\":\"gold\",\"text\":\"{value}\"}]", "Format of direction properties in infoBlockProperties"),
    BLOCK_PROPS_INT_FORMAT           = new ConfigString("infoBlockPropertiesIntFormat", "[{\"color\":\"white\",\"text\":\"" +
    "{prop}\"}, {separator}, {\"color\":\"aqua\",\"text\":\"{value}\"}]", "Format of int properties in infoBlockProperties"),
    BLOCK_PROPS_STRING_FORMAT        = new ConfigString("infoBlockPropertiesStringFormat", "[{\"color\":\"white\",\"text\":\"" +
    "{prop}\"}, {separator}, {\"color\":\"white\",\"text\":\"{value}\"}]", "Format of string properties in infoBlockProperties"),
    BLOCK_PROPS_HEADING_FORMAT       = new ConfigString("infoBlockPropertiesHeadingFormat", "{\"color\":\"white\",\"text\":\"" +
    "{name}\"}", "Format of the heading of infoBlockProperties"),
    CHUNK_POS_FORMAT                 = new ConfigString("infoChunkPositionFormat", "{\"color\":\"white\",\"text\":\"" +
    " / Sub-Chunk: {x}d, {y}d, {z}d\"}", "Format of infoChunkPosition"),
    CHUNK_SECTIONS_FORMAT            = new ConfigString("infoChunkSectionsFormat", "{\"color\":\"white\",\"text\":\"" +
    "C: {c}d\"}", "Format of infoChunkSections"),
    CHUNK_SECTIONS_FULL_FORMAT       = new ConfigString("infoChunkSectionsLineFormat", "{\"color\":\"white\",\"text\":\"" +
    "{c}\"}", "Format of infoChunkSectionsLine"),
    CHUNK_UPDATES_FORMAT             = new ConfigString("infoChunkUpdatesFormat", "{\"color\":\"white\",\"text\":\"" +
    "\"}", "Format of infoChunkUpdates"),
    COORDINATES_FORMAT               = new ConfigString("infoCoordinatesFormat", "{\"color\":\"white\",\"text\":\"" +
    "XYZ: {x}.2f / {y}.4f / {z}.2f\"}", "Format of infoCoordinates, change precision by changing the numbers"),
    DIFFICULTY_FORMAT                = new ConfigString("infoDifficultyFormat", "{\"color\":\"white\",\"text\":\"" +
    "Local Difficulty: {local}.2f // {clamped}.2f (Day {day}d)\"}", "Format of infoDifficulty"),
    DIMENSION_FORMAT                 = new ConfigString("infoDimensionIdFormat", "{\"color\":\"white\",\"text\":\"" +
    " / dim: {dim}\"}", "Format of infoDimensionId"),
    DISTANCE_FORMAT                  = new ConfigString("infoDistanceFormat", "{\"color\":\"white\",\"text\":\"" +
    "Distance: {d}.2f (x: {dx}.2f y: {dy}.2f z: {dz}.2f) [to x: {rx}.2f y: {ry}.2f z: {rz}.2f]\"}", "Format of infoDistance"),
    ENTITIES_CLIENT_FORMAT           = new ConfigString("infoEntitiesClientFormat", "{\"color\":\"white\",\"text\":\"" +
    "Entities - Client: {e}d\"}", "Format of infoEntitiesClient"),
    ENTITIES_SERVER_FORMAT           = new ConfigString("infoEntitiesServerFormat", "{\"color\":\"white\",\"text\":\"" +
    ", Server: {e}d\"}", "Format of infoEntitiesServer"),
    ENTITY_REG_NAME_FORMAT           = new ConfigString("infoEntityRegistryNameFormat", "{\"color\":\"white\",\"text\":\"" +
    "Entity reg name: {name}s\"}", "Format of infoEntityRegistryName"),
    FACING_FORMAT                    = new ConfigString("infoFacingFormat", "[{\"color\":\"white\",\"text\":\"" +
    "Facing: {dir} (\"}, {coord}, {\"color\":\"white\",\"text\":\")\"}]", "Format of infoFacing"),
    FACING_PX_FORMAT                 = new ConfigString("infoFacingPosXFormat", "{\"color\":\"white\",\"text\":\"" +
    "Positive X\"}", "Text for infoFacing when facing positive X"),
    FACING_NX_FORMAT                 = new ConfigString("infoFacingNegXFormat", "{\"color\":\"white\",\"text\":\"" +
    "Negative X\"}", "Text for infoFacing when facing negative X"),
    FACING_PZ_FORMAT                 = new ConfigString("infoFacingPosZFormat", "{\"color\":\"white\",\"text\":\"" +
    "Positive Z\"}", "Text for infoFacing when facing positive Z"),
    FACING_NZ_FORMAT                 = new ConfigString("infoFacingNegZFormat", "{\"color\":\"white\",\"text\":\"" +
    "Negative Z\"}", "Text for infoFacing when facing negative Z"),
    FPS_FORMAT                       = new ConfigString("infoFPSFormat", "{\"color\":\"white\",\"text\":\"" +
    "{FPS}d fps\"}", "Format of infoFPS"),
    HONEY_LEVEL_FORMAT               = new ConfigString("infoHoneyLevelFormat", "[{\"color\":\"white\",\"text\":\"" +
    "Honey: \"}, {\"color\":\"aqua\",\"text\":\"{honey}\"}]", "Format of infoHoneyLevel"),
    LIGHT_LEVEL_CLIENT_FORMAT        = new ConfigString("infoLightLevelClientFormat", "{\"color\":\"white\",\"text\":\"" +
    "Client Light: {light}d (block: {block}d, sky: {sky}d)\"}", "Format of infoLightLevelClient"),
    LIGHT_LEVEL_SERVER_FORMAT        = new ConfigString("infoLightLevelServerFormat", "{\"color\":\"white\",\"text\":\"" +
    "Server Light: {light}d (block: {block}d, sky: {sky}d)\"}", "Format of infoLightLevelServer"),
    LOOKING_AT_BLOCK_FORMAT          = new ConfigString("infoLookingAtBlockFormat", "{\"color\":\"white\",\"text\":\"" +
    "Looking at block: {x}d, {y}d, {z}d\"}", "Format of infoLookingAtBlock"),
    LOOKING_AT_BLOCK_CHUNK_FORMAT    = new ConfigString("infoLookingAtBlockInChunkFormat", "{\"color\":\"white\",\"text\":\"" +
    " // Block: {x}d, {y}d, {z}d in Sub-Chunk: {cx}d, {cy}d, {cz}d\"}", "Format of infoLookingAtBlockInChunk"),
    LOOKING_AT_ENTITY_FORMAT         = new ConfigString("infoLookingAtEntityFormat", "{\"color\":\"white\",\"text\":\"" +
    "Entity: {entity}\"}", "Format of infoLookingAtEntity"),
    LOOKING_AT_ENTITY_LIVING_FORMAT  = new ConfigString("infoLookingAtEntityLivingFormat", "{\"color\":\"white\",\"text\":\"" +
    "Entity: {entity} - HP: {hp}.1f / {maxhp}.1f\"}", "Format of infoLookingAtEntity when entity is living"),
    MEMORY_USAGE_FORMAT              = new ConfigString("infoMemoryUsageFormat", "{\"color\":\"white\",\"text\":\"" +
    "Mem: {pused}2d% {used}03d/{max}03dMB | Allocated: {pallocated}2d% {total}03dMB\"}", "Format of infoMemoryUsage"),
    LOADED_CHUNKS_COUNT_SERVER_FORMAT= new ConfigString("infoLoadedChunksCountServerFormat", "{\"color\":\"white\",\"text\":\"" +
    "Server: {chunks}d / {total}d - Client: {client}\"}", "Format of infoLoadedChunksCount when playing singleplayer"),
    LOADED_CHUNKS_COUNT_CLIENT_FORMAT= new ConfigString("infoLoadedChunksCountClientFormat", "{\"color\":\"white\",\"text\":\"" +
    "{client}\"}", "Format of infoLoadedChunksCount when playing on a server"),
    PARTICLE_COUNT_FORMAT            = new ConfigString("infoParticleCountFormat", "{\"color\":\"white\",\"text\":\"" +
    "P: {p}\"}", "Format of infoParticleCount"),
    PING_FORMAT                      = new ConfigString("infoPingFormat", "{\"color\":\"white\",\"text\":\"" +
    "Ping: {ping}dms\"}", "Format of infoPing"),
    REGION_FILE_FORMAT               = new ConfigString("infoRegionFileFormat", "{\"color\":\"white\",\"text\":\"" +
    " / Region: r.{x}d.{z}d\"}", "Format of infoRegionFile"),
    ROTATION_PITCH_FORMAT            = new ConfigString("infoRotationPitchFormat", "{\"color\":\"white\",\"text\":\"" +
    " / Pitch: {pitch}.1f\"}", "Format of infoRotationPitch"),
    ROTATION_YAW_FORMAT              = new ConfigString("infoRotationYawFormat", "{\"color\":\"white\",\"text\":\"" +
    "Yaw: {yaw}.1f\"}", "Format of infoRotationYaw"),
    SERVER_TPS_VANILLA_FORMAT        = new ConfigString("infoServerTPSVanillaFormat", "{\"color\":\"white\",\"text\":\"" +
    "Server TPS: {preTps}{tps}.1f{rst} (MSPT [est]: {preMspt}{mspt}.1f{rst})\"}", "Format of infoServerTPS for vanilla servers"),
    SERVER_TPS_CARPET_FORMAT         = new ConfigString("infoServerTPSCarpetFormat", "{\"color\":\"white\",\"text\":\"" +
    "Server TPS: {preTps}{tps}.1f{rst} MSPT: {preMspt}{mspt}.1f{rst}\"}", "Format of infoServerTPS for carpet servers"),
    SERVER_TPS_NULL_FORMAT           = new ConfigString("infoServerTPSNullFormat", "{\"color\":\"white\",\"text\":\"" +
    "Server TPS: <no valid data>\"}", "Format of infoServerTPS when info is unavailable"),
    SLIME_CHUNK_FORMAT               = new ConfigString("infoSlimeChunkFormat", "[{\"color\":\"white\",\"text\":\"" +
    "Slime chunk: \"}, {result}]", "Format of infoSlimeChunk"),
    SLIME_CHUNK_YES_FORMAT           = new ConfigString("infoSlimeChunkYesFormat", "{\"color\":\"green\",\"text\":\"" +
    "YES\"}", "Format of the infoSlimeChunk result when it's positive"),
    SLIME_CHUNK_NO_FORMAT            = new ConfigString("infoSlimeChunkNoFormat", "{\"color\":\"red\",\"text\":\"" +
    "NO\"}", "Format of the infoSlimeChunk result when it's negative"),
    SLIME_CHUNK_NO_SEED_FORMAT       = new ConfigString("infoSlimeChunkNoSeedFormat", "{\"color\":\"white\",\"text\":\"" +
    "Slime chunk: <world seed not known>\"}", "Format of infoSlimeChunk when there's no seed"),
    SPEED_FORMAT                     = new ConfigString("infoSpeedFormat", "{\"color\":\"white\",\"text\":\"" +
    " / Speed: {speed}.3f m/s\"}", "Format of infoSpeed"),
    SPEED_AXIS_FORMAT                = new ConfigString("infoSpeedAxisFormat", "{\"color\":\"white\",\"text\":\"" +
    "Speed: x: {x}.3f y: {y}.3f z: {z}.3f m/s\"}", "Format of infoSpeedAxis"),
    TILE_ENTITIES_FORMAT             = new ConfigString("infoTileEntitiesFormat", "{\"color\":\"white\",\"text\":\"" +
    "Client world TE - L: {loaded}d, T: {ticking}d\"}", "Format of infoTileEntities"),
    TIME_DAY_MODULO_FORMAT           = new ConfigString("infoTimeDayModuloFormat", "{\"color\":\"white\",\"text\":\"" +
    "Day time % {mod}d: {time}5d\"}", "Format of infoTimeDayModulo"),
    TIME_REAL_FORMAT                 = new ConfigString("infoTimeIRLFormat", "{\"color\":\"white\",\"text\":\"" +
    "{yyyy}-{MM}-{dd} {HH}:{mm}:{ss}\"}", "The format string for real time, see the Java SimpleDateFormat\nclass for the format patterns, if needed."),
    TIME_TOTAL_MODULO_FORMAT         = new ConfigString("infoTimeTotalModuloFormat", "{\"color\":\"white\",\"text\":\"" +
    "Total time % {mod}d: {time}5d\"}", "Format of infoTimeTotalModulo"),
    TIME_WORLD_FORMAT                = new ConfigString("infoTimeWorldFormat", "{\"color\":\"white\",\"text\":\"" +
    "World time: {time}5d - total: {total}d\"}", "Format of infoTimeWorld"),
    TIME_WORLD_FORMATTED_FORMAT      = new ConfigString("infoWorldTimeFormattedFormat", "{\"color\":\"white\",\"text\":\"" +
    "MC time: (day {day}d) {hour}02d:{min}02d:xx\"}", "The format string for the Minecraft time.\nThe supported placeholders are: {DAY_1}, {DAY}, {HOUR}, {MIN}, {SEC}.\n{DAY_1} starts the day counter from 1, {DAY} starts from 0.");

  public static final ImmutableList<IConfigValue> OPTIONS = ImmutableList.of(
    BEE_COUNT_FORMAT,
    BIOME_FORMAT,
    BIOME_REG_NAME_FORMAT,
    BLOCK_BREAK_SPEED_FORMAT,
    BLOCK_IN_CHUNK_FORMAT,
    BLOCK_POS_FORMAT,
    BLOCK_PROPS_BOOLEAN_TRUE_FORMAT,
    BLOCK_PROPS_BOOLEAN_FALSE_FORMAT,
    BLOCK_PROPS_DIRECTION_FORMAT,
    BLOCK_PROPS_HEADING_FORMAT,
    BLOCK_PROPS_INT_FORMAT,
    BLOCK_PROPS_SEPARATOR_FORMAT,
    BLOCK_PROPS_STRING_FORMAT,
    CHUNK_POS_FORMAT,
    CHUNK_SECTIONS_FORMAT,
    CHUNK_SECTIONS_FULL_FORMAT,
    CHUNK_UPDATES_FORMAT,
    COORDINATES_FORMAT,
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
    HONEY_LEVEL_FORMAT,
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
    SERVER_TPS_VANILLA_FORMAT,
    SERVER_TPS_CARPET_FORMAT,
    SERVER_TPS_NULL_FORMAT,
    SLIME_CHUNK_FORMAT,
    SLIME_CHUNK_YES_FORMAT,
    SLIME_CHUNK_NO_FORMAT,
    SPEED_FORMAT,
    SPEED_AXIS_FORMAT,
    TILE_ENTITIES_FORMAT,
    TIME_DAY_MODULO_FORMAT,
    TIME_REAL_FORMAT,
    TIME_TOTAL_MODULO_FORMAT,
    TIME_WORLD_FORMAT,
    TIME_WORLD_FORMATTED_FORMAT
  );
}
