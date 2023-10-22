package me.axialeaa.colorfulminihud;

import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.WorldUtils;
import fi.dy.masa.minihud.config.Configs;
import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.mixin.IMixinServerWorld;
import fi.dy.masa.minihud.mixin.IMixinWorldRenderer;
import fi.dy.masa.minihud.util.DataStorage;
import fi.dy.masa.minihud.util.IServerEntityManager;
import fi.dy.masa.minihud.util.MiscUtils;
import me.axialeaa.colorfulminihud.config.Formats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.BiConsumer;

import static java.util.Map.entry;

public class ColorfulLines
{
  private static int fps;
  private static DataStorage data;

  private static final Minecraft mc = Minecraft.getInstance();
  private static Level level;
  private static LocalPlayer player;
  private static double x, y, z;
  private static double dx, dy, dz;
  private static BlockPos pos;
  private static ChunkPos chunkPos;
  private static LevelChunk clientChunk, serverChunk;
  private static BlockState targetedBlockState;
  private static BlockEntity targetedBlockEntity;

  public static void setup(int fps, DataStorage data, Level level, LocalPlayer player, double x, double y, double z, BlockPos pos, ChunkPos chunkPos, LevelChunk clientChunk, LevelChunk serverChunk, BlockState targetedBlockState, BlockEntity targetedBlockEntity)
  {
    ColorfulLines.fps = fps;
    ColorfulLines.data = data;
    ColorfulLines.level = level;
    ColorfulLines.player = player;
    ColorfulLines.x = x;
    ColorfulLines.y = y;
    ColorfulLines.z = z;
    dx = x - player.xOld;
    dy = y - player.yOld;
    dz = z - player.zOld;
    ColorfulLines.pos = pos;
    ColorfulLines.chunkPos = chunkPos;
    ColorfulLines.clientChunk = clientChunk;
    ColorfulLines.serverChunk = serverChunk;
    ColorfulLines.targetedBlockState = targetedBlockState;
    ColorfulLines.targetedBlockEntity = targetedBlockEntity;
  }

  public static void addLine(InfoToggle type, List<String> lines, Set<InfoToggle> addedTypes)
  {
    lineMap.get(type).accept(lines, addedTypes);
  }

  private record Variable<T>(String key, String defaultFormat, T value)
  {
    public String apply(String text, int i)
    {
      text = text.replace("%" + key + "$", "\0" + i + "$");
      return text.replace("%" + key, "\0" + i + "$" + defaultFormat);
    }
  }

  private static String line(ConfigString config, Variable<?>... vars)
  {
    String text = ("[\"\"," + config.getStringValue() + "]")
      .replaceAll("#15(?=\\D)", Formats.COLOR15.getStringValue())
      .replaceAll("#14(?=\\D)", Formats.COLOR14.getStringValue())
      .replaceAll("#13(?=\\D)", Formats.COLOR13.getStringValue())
      .replaceAll("#12(?=\\D)", Formats.COLOR12.getStringValue())
      .replaceAll("#11(?=\\D)", Formats.COLOR11.getStringValue())
      .replaceAll("#10(?=\\D)", Formats.COLOR10.getStringValue())
      .replaceAll("#6(?=\\D)", Formats.COLOR6.getStringValue())
      .replaceAll("#7(?=\\D)", Formats.COLOR7.getStringValue())
      .replaceAll("#8(?=\\D)", Formats.COLOR8.getStringValue())
      .replaceAll("#9(?=\\D)", Formats.COLOR9.getStringValue())
      .replaceAll("#5(?=\\D)", Formats.COLOR5.getStringValue())
      .replaceAll("#4(?=\\D)", Formats.COLOR4.getStringValue())
      .replaceAll("#3(?=\\D)", Formats.COLOR3.getStringValue())
      .replaceAll("#2(?=\\D)", Formats.COLOR2.getStringValue())
      .replaceAll("#1(?=\\D])", Formats.COLOR1.getStringValue())
      .replaceAll("#0(?=\\D)", Formats.COLOR0.getStringValue())

      .replace("#black",        "#000000")
      .replace("#dark_red",     "#AA0000")
      .replace("#dark_green",   "#00AA00")
      .replace("#gold",         "#FFAA00")
      .replace("#dark_blue",    "#0000AA")
      .replace("#dark_purple",  "#AA00AA")
      .replace("#dark_aqua",    "#00AAAA")
      .replace("#gray",         "#AAAAAA")

      .replace("#dark_gray",    "#555555")
      .replace("#red",          "#FF5555")
      .replace("#green",        "#55FF55")
      .replace("#yellow",       "#FFFF55")
      .replace("#blue",         "#5555FF")
      .replace("#light_purple", "#FF55FF")
      .replace("#aqua",         "#55FFFF")
      .replace("#white",        "#FFFFFF");

    // Replaces #abcdef"stuff" with {"color":"abcdef","text":"stuff"}
    text = text.replaceAll("(#[a-fA-F\\d]{6,8})(\".*?(?<!\\\\)\")",
      "{\"color\":\"$1\",\"text\":$2}");

    Object[] values = new Object[vars.length];
    int i = 0;
    for(Variable<?> var : vars)
    {
      values[i] = var.value;
      text = var.apply(text, ++i);
    }
    text = text
      .replace("%", "§")
      .replace("\0", "%");
    text = String.format(text, values);

    return text.replace("§", "%");
  }

  private static <T> Variable<T> var(String key, String defaultFormat, T value)
  {
    return new Variable<>(key, defaultFormat, value);
  }

  private static Variable<String> var(String key, String value)
  {
    return new Variable<>(key, "s", value);
  }

  private static Variable<Integer> var(String key, int value)
  {
    return new Variable<>(key, "d", value);
  }

  private static Variable<Long> var(String key, long value)
  {
    return new Variable<>(key, "d", value);
  }

  private static Variable<Float> var(String key, float value)
  {
    return new Variable<>(key, "f", value);
  }

  private static Variable<String> separator(boolean condition) {
    return var("separator", condition ? Formats.SEPARATOR_FORMAT.getStringValue() : "");
  }

  private static final BiConsumer<List<String>, Set<InfoToggle>> COORDINATES_DIMENSION = (List<String> lines, Set<InfoToggle> addedTypes) ->
  {
    if(addedTypes.contains(InfoToggle.COORDINATES) || addedTypes.contains(InfoToggle.COORDINATES_SCALED) || addedTypes.contains(InfoToggle.DIMENSION))
      return;

    StringBuilder str = new StringBuilder(128);
    str.append("[");
    boolean hasOther = false; // initialize the variable. if nothing is toggled, this will never change

    if(InfoToggle.COORDINATES.getBooleanValue())
    {
      str.append(line(Formats.COORDINATES_FORMAT,
        var("x", ".2f", x),
        var("y", ".4f", y),
        var("z", ".2f", z)));
      addedTypes.add(InfoToggle.COORDINATES);
      hasOther = true; // after each entry that can have something following it, reassign hasOther so we can check for it later on
    }

    if(InfoToggle.COORDINATES_SCALED.getBooleanValue() && (level.dimension() == Level.NETHER || level.dimension() == Level.OVERWORLD))
    {
      if(hasOther)
        str.append(","); // to correct the json structure, we need to separate compound line entries with commas
      boolean isNether = level.dimension() == Level.NETHER;
      double dist = isNether ? 8.0 : 0.125;
      x *= dist;
      z *= dist;
        str.append(line(isNether ?
            Formats.COORDINATES_SCALED_OVERWORLD_FORMAT :
            Formats.COORDINATES_SCALED_NETHER_FORMAT,
          separator(hasOther), // if a preceding entry is toggled, add a separator before the main info
          var("x", ".2f", x),
          var("y", ".4f", y),
          var("z", ".2f", z)));
      addedTypes.add(InfoToggle.COORDINATES_SCALED);
      hasOther = true; // the dimension info toggle follows this, so we need to reassign hasOther here too
    }

    if(InfoToggle.DIMENSION.getBooleanValue())
    {
      if(hasOther)
        str.append(",");
      str.append(line(Formats.DIMENSION_FORMAT,
        separator(hasOther),
        var("dim", Objects.requireNonNull(level).dimension().location().toString())));
      addedTypes.add(InfoToggle.DIMENSION);
      // we don't need to reassign hasOther here because this is the last entry in the compound; we don't need to separate this from anything in front
    }
    lines.add(str.append("]").toString());
  };

  private static final BiConsumer<List<String>, Set<InfoToggle>> BLOCK_CHUNK_REGION = (List<String> lines, Set<InfoToggle> addedTypes) ->
  {
    if(addedTypes.contains(InfoToggle.BLOCK_POS) || addedTypes.contains(InfoToggle.CHUNK_POS) || addedTypes.contains(InfoToggle.REGION_FILE))
      return;

    StringBuilder str1 = new StringBuilder(256);
    str1.append("[");
    boolean hasOther = false;

    if(InfoToggle.BLOCK_POS.getBooleanValue())
    {
      str1.append(line(Formats.BLOCK_POS_FORMAT,
        var("x", pos.getX()),
        var("y", pos.getY()),
        var("z", pos.getZ())));
      addedTypes.add(InfoToggle.BLOCK_POS);
      hasOther = true;
    }

    if(InfoToggle.CHUNK_POS.getBooleanValue())
    {
      if(hasOther)
        str1.append(",");
      str1.append(line(Formats.CHUNK_POS_FORMAT,
        separator(hasOther),
        var("subX", chunkPos.x),
        var("subY", pos.getY() >> 4),
        var("subZ", chunkPos.z)));
      addedTypes.add(InfoToggle.CHUNK_POS);
      hasOther = true;
    }

    if(InfoToggle.REGION_FILE.getBooleanValue())
    {
      if(hasOther)
        str1.append(",");
      str1.append(line(Formats.REGION_FILE_FORMAT,
        separator(hasOther),
        var("x", pos.getX() >> 9),
        var("z", pos.getZ() >> 9)));
      addedTypes.add(InfoToggle.REGION_FILE);
    }
    lines.add(str1.append("]").toString());
  };

  private static final BiConsumer<List<String>, Set<InfoToggle>> HORSE = (List<String> lines, Set<InfoToggle> addedTypes) ->
  {
    if(addedTypes.contains(InfoToggle.HORSE_SPEED) || addedTypes.contains(InfoToggle.HORSE_JUMP))
      return;

    Entity vehicle = player.getVehicle();
    if(!(vehicle instanceof AbstractHorse horse))
      return;

    if(horse.isSaddled())
    {
      if(InfoToggle.HORSE_SPEED.getBooleanValue())
      {
        float horseSpeed = horse.getSpeed();
        horseSpeed *= 42.163F;
        lines.add(line(Formats.HORSE_SPEED_FORMAT,
          var("speed", ".3f", horseSpeed)));
      }

      if(InfoToggle.HORSE_JUMP.getBooleanValue())
      {
        double jump = horse.getCustomJump();
        double calculatedJumpHeight = -0.1817584952 * jump*jump*jump + 3.689713992 * jump*jump + 2.128599134 * jump - 0.343930367;
        lines.add(line(Formats.HORSE_JUMP_FORMAT,
          var("height", ".3f", calculatedJumpHeight)));
      }

      addedTypes.add(InfoToggle.HORSE_SPEED);
      addedTypes.add(InfoToggle.HORSE_JUMP);
    }
  };

  private static final BiConsumer<List<String>, Set<InfoToggle>> ENTITIES = (List<String> lines, Set<InfoToggle> addedTypes) ->
  {
    if(addedTypes.contains(InfoToggle.ENTITIES_CLIENT_WORLD) || addedTypes.contains(InfoToggle.ENTITIES))
      return;

    StringBuilder str = new StringBuilder(128);
    str.append("[");
    boolean hasOther = false;

    if(InfoToggle.ENTITIES_CLIENT_WORLD.getBooleanValue())
    {
      str.append(line(Formats.ENTITIES_CLIENT_WORLD_FORMAT,
        var("count", Objects.requireNonNull(mc.level).getEntityCount())));
      addedTypes.add(InfoToggle.ENTITIES_CLIENT_WORLD);
      hasOther = true;
    }

    if(InfoToggle.ENTITIES.getBooleanValue() && mc.hasSingleplayerServer())
    {
      Level world = WorldUtils.getBestWorld(mc);
      if(world instanceof ServerLevel)
      {
        if(hasOther)
          str.append(",");
        str.append(line(Formats.ENTITIES_FORMAT,
          separator(hasOther),
          var("count", ((IServerEntityManager) ((IMixinServerWorld) world).minihud_getEntityManager()).getIndexSize())));
        addedTypes.add(InfoToggle.ENTITIES);
        hasOther = true; // we need to reassign hasOther here in case if(world instanceof ServerLevel) returns false
      }
    }
    if(hasOther)
      lines.add(str.append("]").toString());
  };

  private static final BiConsumer<List<String>, Set<InfoToggle>> LOOKING_BLOCK_CHUNK = (List<String> lines, Set<InfoToggle> addedTypes) ->
  {
    if(addedTypes.contains(InfoToggle.LOOKING_AT_BLOCK) || addedTypes.contains(InfoToggle.LOOKING_AT_BLOCK_CHUNK))
      return;

    if(mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK)
    {
      BlockPos lookPos = ((BlockHitResult)mc.hitResult).getBlockPos();

      StringBuilder str = new StringBuilder(128);
      str.append("[");
      boolean hasOther = false;

      if(InfoToggle.LOOKING_AT_BLOCK.getBooleanValue())
      {
        str.append(line(Formats.LOOKING_AT_BLOCK_FORMAT,
          var("x", lookPos.getX()),
          var("y", lookPos.getY()),
          var("z", lookPos.getZ())
        ));
        addedTypes.add(InfoToggle.LOOKING_AT_BLOCK);
        hasOther = true;
      }

      if(InfoToggle.LOOKING_AT_BLOCK_CHUNK.getBooleanValue())
      {
        if(hasOther)
          str.append(",");
        str.append(line(Formats.LOOKING_AT_BLOCK_CHUNK_FORMAT,
          separator(hasOther),
          var("x", lookPos.getX() & 15),
          var("y", lookPos.getY() & 15),
          var("z", lookPos.getZ() & 15),
          var("subX", lookPos.getX() >> 4),
          var("subY", lookPos.getY() >> 4),
          var("subZ", lookPos.getZ() >> 4)
        ));
        addedTypes.add(InfoToggle.LOOKING_AT_BLOCK_CHUNK);
      }
      lines.add(str.append("]").toString());
    }
  };

  private static final BiConsumer<List<String>, Set<InfoToggle>> ROTATION_SPEED = (List<String> lines, Set<InfoToggle> addedTypes) ->
  {
    if(addedTypes.contains(InfoToggle.ROTATION_YAW) || addedTypes.contains(InfoToggle.ROTATION_PITCH) || addedTypes.contains(InfoToggle.SPEED))
      return;

    StringBuilder str = new StringBuilder(128);
    str.append("[");
    boolean hasOther = false;

    if(InfoToggle.ROTATION_YAW.getBooleanValue())
    {
      str.append(line(Formats.ROTATION_YAW_FORMAT,
        var("yaw", ".1f", Mth.wrapDegrees(player.getYRot()))));
      addedTypes.add(InfoToggle.ROTATION_YAW);
      hasOther = true;
    }

    if(InfoToggle.ROTATION_PITCH.getBooleanValue())
    {
      if(hasOther)
        str.append(",");
      str.append(line(Formats.ROTATION_PITCH_FORMAT,
        separator(hasOther),
        var("pitch", ".1f", Mth.wrapDegrees(player.getXRot()))));
      addedTypes.add(InfoToggle.ROTATION_PITCH);
      hasOther = true;
    }

    if(InfoToggle.SPEED.getBooleanValue())
    {
      if(hasOther)
        str.append(",");
      double dist1 = Math.sqrt(dx*dx + dy*dy + dz*dz);
      str.append(line(Formats.SPEED_FORMAT,
        separator(hasOther),
        var("speed", ".3f",dist1 * 20)));
      addedTypes.add(InfoToggle.SPEED);
    }
    lines.add(str.append("]").toString());
  };

  private static final Map<InfoToggle, BiConsumer<List<String>, Set<InfoToggle>>> lineMap = Map.ofEntries(
    entry(InfoToggle.FPS, (List<String> lines, Set<InfoToggle> addedTypes) ->
      lines.add(line(Formats.FPS_FORMAT,
        var("fps", fps)))),

    entry(InfoToggle.MEMORY_USAGE, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      long memMax = Runtime.getRuntime().maxMemory();
      long memTotal = Runtime.getRuntime().totalMemory();
      long memFree = Runtime.getRuntime().freeMemory();
      long memUsed = memTotal - memFree;
      lines.add(line(Formats.MEMORY_USAGE_FORMAT,
        var("pctUsed", "2d", memUsed * 100L / memMax),
        var("pctAllocated", "2d", memTotal * 100L / memMax),
        var("used", "3d", MiscUtils.bytesToMb(memUsed)),
        var("max", "3d", MiscUtils.bytesToMb(memMax)),
        var("total", "3d", MiscUtils.bytesToMb(memTotal))));
    }),

    entry(InfoToggle.TIME_REAL, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.TIME_REAL_FORMAT,
      var("time", "tk", new Date(System.currentTimeMillis()))))),

    entry(InfoToggle.TIME_WORLD, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.TIME_WORLD_FORMAT,
      var("day", "5d", Objects.requireNonNull(level).getDayTime()),
      var("total", level.getGameTime())))),

    entry(InfoToggle.TIME_WORLD_FORMATTED, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      long timeDay = Objects.requireNonNull(level).getDayTime();
      long day = timeDay / 24000;
      int dayTicks = (int)(timeDay % 24000);
      lines.add(line(Formats.TIME_WORLD_FORMATTED_FORMAT,
        var("dayFrom0", day),
        var("dayFrom1", day + 1),
        var("hour", "02d", (dayTicks / 1000 + 6) % 24),
        var("min", "02d", (int)(dayTicks * 0.06) % 60),
        var("sec", "02d", (int)(dayTicks * 3.6) % 60)));
    }),

    entry(InfoToggle.TIME_DAY_MODULO, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      int mod = Configs.Generic.TIME_DAY_DIVISOR.getIntegerValue();
      lines.add(line(Formats.TIME_DAY_MODULO_FORMAT,
        var("mod", mod),
        var("time", "5d", Objects.requireNonNull(level).getDayTime() % mod)));
    }),

    entry(InfoToggle.TIME_TOTAL_MODULO, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      int mod1 = Configs.Generic.TIME_TOTAL_DIVISOR.getIntegerValue();
      lines.add(line(Formats.TIME_TOTAL_MODULO_FORMAT,
        var("mod", mod1),
        var("time", "5d", Objects.requireNonNull(level).getGameTime() % mod1)));
    }),

    entry(InfoToggle.SERVER_TPS, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(mc.hasSingleplayerServer() && Objects.requireNonNull(mc.getSingleplayerServer()).getTickCount() % 10 == 0)
        data.updateIntegratedServerTPS();

      if(!data.hasTPSData())
      {
        lines.add(line(Formats.SERVER_TPS_NULL_FORMAT));
        return;
      }

      double tps = data.getServerTPS();
      double mspt = data.getServerMSPT();

      String msptString = line(
        mspt <= 40 ? Formats.MSPT_GOOD_FORMAT :
        mspt <= 45 ? Formats.MSPT_MEDIUM_FORMAT :
        mspt <= 50 ? Formats.MSPT_BAD_FORMAT :
        Formats.MSPT_IMPACTFUL_FORMAT,
        var("value", ".1f", mspt));

      String tpsString = line(
        tps >= 20 ? Formats.TPS_IMPACTFUL_FORMAT :
        Formats.TPS_GOOD_FORMAT,
        var("value", ".1f", tps));

      lines.add(line(data.isCarpetServer() || mc.isLocalServer() ?
        Formats.SERVER_TPS_CARPET_FORMAT :
        Formats.SERVER_TPS_VANILLA_FORMAT,
        var("tps", tpsString),
        var("mspt", msptString)));
    }),

    entry(InfoToggle.PING, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      PlayerInfo info = player.connection.getPlayerInfo(player.getUUID());
      if(info != null)
        lines.add(line(Formats.PING_FORMAT,
          var("ping", info.getLatency())));
    }),

    entry(InfoToggle.COORDINATES_SCALED, COORDINATES_DIMENSION),
    entry(InfoToggle.COORDINATES, COORDINATES_DIMENSION),
    entry(InfoToggle.DIMENSION, COORDINATES_DIMENSION),

    entry(InfoToggle.BLOCK_POS, BLOCK_CHUNK_REGION),
    entry(InfoToggle.CHUNK_POS, BLOCK_CHUNK_REGION),
    entry(InfoToggle.REGION_FILE, BLOCK_CHUNK_REGION),

    entry(InfoToggle.BLOCK_IN_CHUNK, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.BLOCK_IN_CHUNK_FORMAT,
      var("x", pos.getX() & 15),
      var("y", pos.getY() & 15),
      var("z", pos.getZ() & 15),
      var("subX", chunkPos.x),
      var("subY", pos.getY() >> 4),
      var("subZ", chunkPos.z)))),

    entry(InfoToggle.BLOCK_BREAK_SPEED, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.BLOCK_BREAK_SPEED_FORMAT,
      var("speed", ".2f", data.getBlockBreakingSpeed())))),

    entry(InfoToggle.DISTANCE, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      Vec3 ref = data.getDistanceReferencePoint();
      lines.add(line(Formats.DISTANCE_FORMAT,
        var("dist", ".2f", Math.sqrt(ref.distanceToSqr(x, y, z))),
        var("dx", ".2f", x - ref.x),
        var("dy", ".2f", y - ref.y),
        var("dz", ".2f", z - ref.z),
        var("refX", ".2f", ref.x),
        var("refY", ".2f", ref.y),
        var("refZ", ".2f", ref.z)));
    }),

    entry(InfoToggle.FACING, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      Direction facing = player.getDirection();
      lines.add(line(
        facing == Direction.NORTH ?
          Formats.FACING_NORTH_FORMAT :
        facing == Direction.EAST ?
          Formats.FACING_EAST_FORMAT :
        facing == Direction.SOUTH ?
          Formats.FACING_SOUTH_FORMAT :
          Formats.FACING_WEST_FORMAT));
    }),

    //#if MC >= 11800
    //$$ entry(InfoToggle.LIGHT_LEVEL, (List<String> lines, Set<InfoToggle> addedTypes) ->
    //$$ {
    //$$   if(clientChunk.isEmpty())
    //$$     return;
    //$$
    //$$   LevelLightEngine lightEngine = Objects.requireNonNull(level).getChunkSource().getLightEngine();
    //$$   lines.add(line(Formats.LIGHT_LEVEL_FORMAT,
    //$$     var("light", lightEngine.getLayerListener(LightLayer.BLOCK).getLightValue(pos))));
    //$$ }),
    //#else
    entry(InfoToggle.LIGHT_LEVEL, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(clientChunk.isEmpty())
        return;

      LevelLightEngine lightEngine = Objects.requireNonNull(level).getChunkSource().getLightEngine();
      lines.add(line(Formats.LIGHT_LEVEL_CLIENT_FORMAT,
        var("light", lightEngine.getRawBrightness(pos, 0)),
        var("block", lightEngine.getLayerListener(LightLayer.BLOCK).getLightValue(pos)),
        var("sky", lightEngine.getLayerListener(LightLayer.SKY).getLightValue(pos))));

      Level bestLevel = WorldUtils.getBestWorld(mc);
      if(serverChunk == null || serverChunk == clientChunk)
        return;

      lightEngine = Objects.requireNonNull(bestLevel).getChunkSource().getLightEngine();
      lines.add(line(Formats.LIGHT_LEVEL_SERVER_FORMAT,
        var("light", lightEngine.getRawBrightness(pos, 0)),
        var("block", lightEngine.getLayerListener(LightLayer.BLOCK).getLightValue(pos)),
        var("sky", lightEngine.getLayerListener(LightLayer.SKY).getLightValue(pos))));
    }),
    //#endif

    entry(InfoToggle.BEE_COUNT, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(targetedBlockEntity instanceof BeehiveBlockEntity bbe)
        lines.add(line(Formats.BEE_COUNT_FORMAT,
          var("count", bbe.getOccupantCount())));
    }),

    entry(InfoToggle.FURNACE_XP, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(targetedBlockEntity instanceof AbstractFurnaceBlockEntity furnace)
        lines.add(line(Formats.FURNACE_XP_FORMAT,
          var("count", MiscUtils.getFurnaceXpAmount(furnace))));
    }),

    entry(InfoToggle.HONEY_LEVEL, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(targetedBlockState != null && targetedBlockState.getBlock() instanceof BeehiveBlock)
        lines.add(line(Formats.HONEY_LEVEL_FORMAT,
          var("level", BeehiveBlockEntity.getHoneyLevel(targetedBlockState))));
    }),

    entry(InfoToggle.ROTATION_YAW, ROTATION_SPEED),
    entry(InfoToggle.ROTATION_PITCH, ROTATION_SPEED),
    entry(InfoToggle.SPEED, ROTATION_SPEED),

    entry(InfoToggle.SPEED_HV, (List<String> lines, Set<InfoToggle> addedTypes) ->
      lines.add(line(Formats.SPEED_HV_FORMAT,
        var("h", ".3f", Math.sqrt(dx*dx + dz*dz) * 20.0),
        var("v", ".3f", dy * 20.0)))),

    //#if MC >= 11800
    //$$ entry(InfoToggle.SPRINTING, (List<String> lines, Set<InfoToggle> addedTypes) ->
    //$$ {
    //$$   if(player.isSprinting())
    //$$     lines.add(line(Formats.SPRINTING_FORMAT));
    //$$ }),
    //#endif

    entry(InfoToggle.SPEED_AXIS, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.SPEED_AXIS_FORMAT,
      var("x", ".3f", dx * 20),
      var("y", ".3f", dy * 20),
      var("z", ".3f", dz * 20)))),

    entry(InfoToggle.HORSE_SPEED, HORSE),
    entry(InfoToggle.HORSE_JUMP, HORSE),

    entry(InfoToggle.CHUNK_SECTIONS, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.CHUNK_SECTIONS_FORMAT,
      var("count", ((IMixinWorldRenderer)mc.levelRenderer).getRenderedChunksInvoker())))),

    entry(InfoToggle.CHUNK_SECTIONS_FULL, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.CHUNK_SECTIONS_FULL_FORMAT,
      var("count", mc.levelRenderer.getChunkStatistics())))),

    entry(InfoToggle.CHUNK_UPDATES, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.CHUNK_UPDATES_FORMAT))),

    entry(InfoToggle.LOADED_CHUNKS_COUNT, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      String chunksClient = Objects.requireNonNull(level).gatherChunkSourceStats();
      Level levelServer = WorldUtils.getBestWorld(mc);
      if(levelServer == null || levelServer != level)
        lines.add(line(Formats.LOADED_CHUNKS_COUNT_CLIENT_FORMAT,
          var("stats", chunksClient)));

      ServerChunkCache cache = (ServerChunkCache)Objects.requireNonNull(levelServer).getChunkSource();
      lines.add(line(Formats.LOADED_CHUNKS_COUNT_SERVER_FORMAT,
        var("loaded", cache.getLoadedChunksCount()),
        var("total", cache.getTickingGenerated()),
        var("stats", chunksClient)));
    }),

    entry(InfoToggle.PARTICLE_COUNT, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.PARTICLE_COUNT_FORMAT,
      var("count", mc.particleEngine.countParticles())))),

    entry(InfoToggle.DIFFICULTY, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      ChunkAccess serverChunk = Objects.requireNonNull(level).getChunk(chunkPos.getWorldPosition());

      float moonPhaseFactor = level.getMoonBrightness();
      long chunkInhabitedTime = serverChunk.getInhabitedTime();
      DifficultyInstance diff = new DifficultyInstance(level.getDifficulty(), level.getDayTime(), chunkInhabitedTime, moonPhaseFactor);
      lines.add(line(Formats.DIFFICULTY_FORMAT,
        var("local", ".2f", diff.getEffectiveDifficulty()),
        var("clamped", ".2f", diff.getSpecialMultiplier()),
        var("day", level.getDayTime() / 24000L)));
    }),

    entry(InfoToggle.BIOME, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(serverChunk.isEmpty())
        return;

      //#if MC >= 11800
      //$$ Biome biome = Objects.requireNonNull(level).getBiome(pos).value();
      //#else
      Biome biome = Objects.requireNonNull(level).getBiome(pos);
      //#endif
      ResourceLocation resourceLocation = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);
      lines.add(line(Formats.BIOME_FORMAT,
        var("biome", StringUtils.translate("biome." + Objects.requireNonNull(resourceLocation).toString().replace(":", ".")))));
    }),

    entry(InfoToggle.BIOME_REG_NAME, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(serverChunk.isEmpty())
        return;

      //#if MC >= 11800
      //$$ Biome biome = Objects.requireNonNull(level).getBiome(pos).value();
      //#else
      Biome biome = Objects.requireNonNull(level).getBiome(pos);
      //#endif
      ResourceLocation resourceLocation = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);
      lines.add(line(Formats.BIOME_REG_NAME_FORMAT,
        var("name", resourceLocation != null ? resourceLocation.toString() : "?")));
    }),

    entry(InfoToggle.TILE_ENTITIES, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.TILE_ENTITIES_FORMAT))),

    entry(InfoToggle.ENTITIES_CLIENT_WORLD, ENTITIES),
    entry(InfoToggle.ENTITIES, ENTITIES),

    entry(InfoToggle.SLIME_CHUNK, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(!MiscUtils.isOverworld(Objects.requireNonNull(level)))
        return;

      if(data.isWorldSeedKnown(level))
      {
        long seed = data.getWorldSeed(level);
        lines.add(line(MiscUtils.canSlimeSpawnAt(pos.getX(), pos.getZ(), seed) ?
          Formats.SLIME_CHUNK_YES_FORMAT :
          Formats.SLIME_CHUNK_NO_FORMAT));
      }
      else
        lines.add(line(Formats.SLIME_CHUNK_NO_SEED_FORMAT));
    }),

    entry(InfoToggle.LOOKING_AT_ENTITY, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.ENTITY)
        return;

      Entity lookedEntity = mc.crosshairPickEntity;
      lines.add(lookedEntity instanceof LivingEntity living ?
        line(Formats.LOOKING_AT_ENTITY_LIVING_FORMAT,
          var("entity", lookedEntity.getName().getString()),
          var("health", living.getHealth()),
          var("max", living.getMaxHealth())) :
        line(Formats.LOOKING_AT_ENTITY_LIVING_FORMAT,
          var("entity", Objects.requireNonNull(lookedEntity).getName().getString())));
    }),

    entry(InfoToggle.ENTITY_REG_NAME, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.ENTITY)
        return;

      Entity lookedEntity = ((EntityHitResult) mc.hitResult).getEntity();
      ResourceLocation resourceLocation = EntityType.getKey(lookedEntity.getType());
      lines.add(line(Formats.ENTITY_REG_NAME_FORMAT,
        var("name", resourceLocation.toString())));
    }),

    entry(InfoToggle.LOOKING_AT_BLOCK, LOOKING_BLOCK_CHUNK),
    entry(InfoToggle.LOOKING_AT_BLOCK_CHUNK, LOOKING_BLOCK_CHUNK),

    entry(InfoToggle.BLOCK_PROPS, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK)
      {
        BlockPos posLooking = ((BlockHitResult)mc.hitResult).getBlockPos();
        BlockState state = Objects.requireNonNull(mc.level).getBlockState(posLooking);
        ResourceLocation resourceLocation = Registry.BLOCK.getKey(state.getBlock());

        lines.add(line(Formats.BLOCK_PROPS_BLOCK_FORMAT,
          var("block", resourceLocation.toString())));

        for(Property<?> property : state.getProperties())
        {
          Comparable<?> value = state.getValue(property);
          lines.add(line(
            property instanceof BooleanProperty ?
              value.equals(Boolean.TRUE) ?
                Formats.BLOCK_PROPS_TRUE_FORMAT :
                Formats.BLOCK_PROPS_FALSE_FORMAT :
            property instanceof DirectionProperty ?
              Formats.BLOCK_PROPS_DIRECTION_FORMAT :
            property instanceof IntegerProperty ?
              Formats.BLOCK_PROPS_INT_FORMAT :
              Formats.BLOCK_PROPS_STRING_FORMAT,
              var("property", property.getName()),
              var("value", value.toString())));
        }
      }
    })
  );

  public static final class StringHolder implements Comparator<Component>
  {
    @Override
    public int compare(Component a, Component b)
    {
      int as = a.getString().length(); int bs = b.getString().length();
      return Integer.compare(as, bs);
    }
  }
}
