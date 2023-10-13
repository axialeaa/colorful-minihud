package me.axialeaa.colorfulminihud;

import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.WorldUtils;
import fi.dy.masa.minihud.config.Configs;
import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.event.RenderHandler;
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
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

import static java.util.Map.entry;

public class ColorfulLines
{
  private static final IRenderHandler accessor = (IRenderHandler)RenderHandler.getInstance();

  private static int fps;
  private static DataStorage data;

  private static final Minecraft mc = Minecraft.getInstance();
  private static Level level = mc.level;
  private static LocalPlayer player = mc.player;
  private static double x = Objects.requireNonNull(player).getX(), y = player.getY(), z = player.getZ();
  private static double dx = x - player.xOld, dy = y - player.yOld, dz = z - player.zOld;
  private static BlockPos pos = new BlockPos(x, y, z);
  private static ChunkPos chunkPos = new ChunkPos(pos);

  public static void setup(int fps, DataStorage data)
  {
    ColorfulLines.fps = fps;
    ColorfulLines.data = data;
    level = mc.level;
    player = mc.player;
    x = Objects.requireNonNull(player).getX(); y = player.getY(); z = player.getZ();
    dx = x - player.xOld; dy = y - player.yOld; dz = z - player.zOld;
    pos = new BlockPos(x, y, z);
    chunkPos = new ChunkPos(pos);
  }

  public static void addLine(InfoToggle type, List<String> lines, Set<InfoToggle> addedTypes)
  {
    lineMap.get(type).accept(lines, addedTypes);
  }

  private record Variable<T>(String key, String defaultFormat, T value)
  {
    public String apply(String text, int i)
    {
      text = text.replace("%" + key + "$", "%" + i + "$");
      return text.replace("%" + key, "%" + i + "$" + defaultFormat);
    }
  }

  private static String line(ConfigString config, Variable<?>... vars)
  {
    String text = config.getStringValue();
    Object[] values = new Object[vars.length];
    int i = 0;
    for(Variable<?> var : vars)
    {
      values[i] = var.value;
      text = var.apply(text, ++i);
    }

    text = text.replace("%colorFG", Formats.COLORFG.getStringValue())
      .replace("%colorBG", Formats.COLORBG.getStringValue())
      .replace("%color0", Formats.COLOR0.getStringValue())
      .replace("%color1", Formats.COLOR1.getStringValue())
      .replace("%color2", Formats.COLOR2.getStringValue())
      .replace("%color3", Formats.COLOR3.getStringValue())
      .replace("%color4", Formats.COLOR4.getStringValue())
      .replace("%color5", Formats.COLOR5.getStringValue())
      .replace("%color6", Formats.COLOR6.getStringValue())
      .replace("%color7", Formats.COLOR7.getStringValue())
      .replace("%color8", Formats.COLOR8.getStringValue())
      .replace("%color9", Formats.COLOR9.getStringValue())
      .replace("%color10", Formats.COLOR10.getStringValue())
      .replace("%color11", Formats.COLOR11.getStringValue())
      .replace("%color12", Formats.COLOR12.getStringValue())
      .replace("%color13", Formats.COLOR13.getStringValue())
      .replace("%color14", Formats.COLOR14.getStringValue())
      .replace("%color15", Formats.COLOR15.getStringValue());

    return String.format(text, values);
  }

  private static <T> Variable<T> va(String key, String defaultFormat, T value)
  {
    return new Variable<>(key, defaultFormat, value);
  }

  private static Variable<String> va(String key, String value)
  {
    return new Variable<>(key, "s", value);
  }

  private static Variable<Integer> va(String key, int value)
  {
    return new Variable<>(key, "d", value);
  }

  private static Variable<Long> va(String key, long value)
  {
    return new Variable<>(key, "d", value);
  }

  private static Variable<Float> va(String key, float value)
  {
    return new Variable<>(key, "f", value);
  }

  private static Variable<Double> va(String key, double value)
  {
    return new Variable<>(key, "f", value);
  }

  private static final BiConsumer<List<String>, Set<InfoToggle>> COORDINATES_DIMENSION = (List<String> lines, Set<InfoToggle> addedTypes) ->
  {
    if(addedTypes.contains(InfoToggle.COORDINATES) || addedTypes.contains(InfoToggle.DIMENSION))
      return;

    StringBuilder str = new StringBuilder(128);
    str.append("[");
    if(InfoToggle.COORDINATES.getBooleanValue())
    {
      str.append(line(Formats.COORDINATES_FORMAT,
        va("x", ".2f", x),
        va("y", ".4f", y),
        va("z", ".2f", z)));
      addedTypes.add(InfoToggle.COORDINATES);
    }
    if(InfoToggle.DIMENSION.getBooleanValue())
    {
      if(InfoToggle.COORDINATES.getBooleanValue())
        str.append(",");
      str.append(line(Formats.DIMENSION_FORMAT, va("dim", Objects.requireNonNull(level).dimension().location().toString())));
      addedTypes.add(InfoToggle.DIMENSION);
    }
    lines.add(str.append("]").toString());
  };

  private static final BiConsumer<List<String>, Set<InfoToggle>> BLOCK_CHUNK_REGION = (List<String> lines, Set<InfoToggle> addedTypes) ->
  {
    if(addedTypes.contains(InfoToggle.BLOCK_POS) || addedTypes.contains(InfoToggle.CHUNK_POS) || addedTypes.contains(InfoToggle.REGION_FILE))
      return;

    StringBuilder str1 = new StringBuilder(256);
    str1.append("[");
    boolean hasOther = InfoToggle.BLOCK_POS.getBooleanValue();
    if(hasOther)
    {
      str1.append(line(Formats.BLOCK_POS_FORMAT,
        va("x", pos.getX()),
        va("y", pos.getY()), va("z", pos.getZ())));
      addedTypes.add(InfoToggle.BLOCK_POS);
    }
    if(InfoToggle.CHUNK_POS.getBooleanValue())
    {
      if(hasOther)
        str1.append(",");
      hasOther = true;

      str1.append(line(Formats.CHUNK_POS_FORMAT,
        va("x", chunkPos.x),
        va("y", pos.getY() >> 4),
        va("z", chunkPos.z)));
      addedTypes.add(InfoToggle.CHUNK_POS);
    }
    if(InfoToggle.REGION_FILE.getBooleanValue())
    {
      if(hasOther)
        str1.append(",");
      str1.append(line(Formats.REGION_FILE_FORMAT,
        va("x", pos.getX() >> 9),
        va("z", pos.getZ() >> 9)));
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
        lines.add(line(Formats.HORSE_SPEED_FORMAT, va("speed", ".3f", horseSpeed)));
      }

      if(InfoToggle.HORSE_JUMP.getBooleanValue())
      {
        double jump = horse.getCustomJump();
        double calculatedJumpHeight = -0.1817584952 * jump*jump*jump + 3.689713992 * jump*jump + 2.128599134 * jump - 0.343930367;
        lines.add(line(Formats.HORSE_JUMP_FORMAT, va("speed", ".3f", calculatedJumpHeight)));
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
    boolean hasOther2 = InfoToggle.ENTITIES_CLIENT_WORLD.getBooleanValue();
    if(hasOther2)
    {
      str.append(line(Formats.ENTITIES_CLIENT_FORMAT, va("e", Objects.requireNonNull(mc.level).getEntityCount())));
      addedTypes.add(InfoToggle.ENTITIES_CLIENT_WORLD);
    }
    if(InfoToggle.ENTITIES.getBooleanValue() && mc.hasSingleplayerServer())
    {
      Level world = WorldUtils.getBestWorld(mc);
      if(world instanceof ServerLevel)
      {
        if(hasOther2)
          str.append(",");
        str.append(line(Formats.ENTITIES_SERVER_FORMAT, va("e", ((IServerEntityManager) ((IMixinServerWorld) world).minihud_getEntityManager()).getIndexSize())));
        addedTypes.add(InfoToggle.ENTITIES);
        hasOther2 = true;
      }
    }
    if(hasOther2)
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
      boolean hasOther3 = InfoToggle.LOOKING_AT_BLOCK.getBooleanValue();
      if(hasOther3)
      {
        str.append(line(Formats.LOOKING_AT_BLOCK_FORMAT,
          va("x", lookPos.getX()),
          va("y", lookPos.getY()),
          va("z", lookPos.getZ())
        ));
        addedTypes.add(InfoToggle.LOOKING_AT_BLOCK);
      }

      if(InfoToggle.LOOKING_AT_BLOCK_CHUNK.getBooleanValue())
      {
        if(hasOther3) str.append(",");
        str.append(line(Formats.LOOKING_AT_BLOCK_CHUNK_FORMAT,
          va("x", lookPos.getX()),
          va("y", lookPos.getY()),
          va("z", lookPos.getZ()),
          va("cx", lookPos.getX() >> 4),
          va("cy", lookPos.getY() >> 4),
          va("cz", lookPos.getZ() >> 4)
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
    boolean hasOther1 = InfoToggle.ROTATION_YAW.getBooleanValue();
    if(hasOther1)
    {
      str.append(line(Formats.ROTATION_YAW_FORMAT, va("yaw", Mth.wrapDegrees(player.getYRot()))));
      addedTypes.add(InfoToggle.ROTATION_YAW);
    }
    if(InfoToggle.ROTATION_PITCH.getBooleanValue())
    {
      if(hasOther1)
        str.append(",");
      hasOther1 = true;
      str.append(line(Formats.ROTATION_PITCH_FORMAT, va("pitch", Mth.wrapDegrees(player.getXRot()))));
      addedTypes.add(InfoToggle.ROTATION_PITCH);
    }
    if(InfoToggle.SPEED.getBooleanValue())
    {
      if(hasOther1)
        str.append(",");
      double dist1 = Math.sqrt(dx*dx + dy*dy + dz*dz);
      str.append(line(Formats.SPEED_FORMAT, va("speed", dist1 * 20)));
      addedTypes.add(InfoToggle.SPEED);
    }
    lines.add(str.append("]").toString());
  };

  private static final Map<InfoToggle, BiConsumer<List<String>, Set<InfoToggle>>> lineMap = Map.ofEntries(
    entry(InfoToggle.FPS, (List<String> lines, Set<InfoToggle> addedTypes) ->
      lines.add(line(Formats.FPS_FORMAT, va("fps", fps)))),

    entry(InfoToggle.MEMORY_USAGE, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      long memMax = Runtime.getRuntime().maxMemory();
      long memTotal = Runtime.getRuntime().totalMemory();
      long memFree = Runtime.getRuntime().freeMemory();
      long memUsed = memTotal - memFree;
      lines.add(line(Formats.MEMORY_USAGE_FORMAT,
        va("pused", "2d", memUsed * 100L / memMax),
        va("pallocated", "2d", memTotal * 100L / memMax),
        va("used", "3d", MiscUtils.bytesToMb(memUsed)),
        va("max", "3d", MiscUtils.bytesToMb(memMax)),
        va("total", "3d", MiscUtils.bytesToMb(memTotal))));
    }),

    entry(InfoToggle.TIME_REAL, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.TIME_REAL_FORMAT, va("time", "tk", new Date(System.currentTimeMillis()))))),

    entry(InfoToggle.TIME_WORLD, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.TIME_WORLD_FORMAT,
      va("time", "5d", Objects.requireNonNull(level).getDayTime()),
      va("total", level.getGameTime())))),

    entry(InfoToggle.TIME_WORLD_FORMATTED, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      long timeDay = Objects.requireNonNull(level).getDayTime();
      long day = timeDay / 24000;
      int dayTicks = (int)(timeDay % 24000);
      lines.add(line(Formats.TIME_WORLD_FORMATTED_FORMAT,
        va("day", day),
        va("day_1", day + 1),
        va("hour", "02d", (dayTicks / 1000 + 6) % 24),
        va("min", "02d", (int)(dayTicks * 0.06) % 60),
        va("sec", "02d", (int)(dayTicks * 3.6) % 60)));
    }),

    entry(InfoToggle.TIME_DAY_MODULO, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      int mod = Configs.Generic.TIME_DAY_DIVISOR.getIntegerValue();
      lines.add(line(Formats.TIME_DAY_MODULO_FORMAT,
        va("mod", mod),
        va("time", "5d", Objects.requireNonNull(level).getDayTime() % mod)));
    }),

    entry(InfoToggle.TIME_TOTAL_MODULO, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      int mod1 = Configs.Generic.TIME_TOTAL_DIVISOR.getIntegerValue();
      lines.add(line(Formats.TIME_TOTAL_MODULO_FORMAT,
        va("mod", mod1),
        va("time", "5d", Objects.requireNonNull(level).getGameTime() % mod1)));
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
      String preTps = tps >= 20d ? GuiBase.TXT_GREEN : GuiBase.TXT_RED;

      if(data.isCarpetServer() || mc.isLocalServer())
      {
        String preMspt = mspt <= 40 ? GuiBase.TXT_GREEN :
          mspt <= 45 ? GuiBase.TXT_YELLOW :
          mspt <= 50 ? GuiBase.TXT_GOLD :
          GuiBase.TXT_RED;
        lines.add(line(Formats.SERVER_TPS_CARPET_FORMAT,
          va("preTps", preTps),
          va("tps", ".1f", tps),
          va("rst", GuiBase.TXT_RST),
          va("preMspt", preMspt),
          va("mspt", ".1f", mspt)));
        return;
      }

      String preMspt = mspt <= 51 ? GuiBase.TXT_GREEN : GuiBase.TXT_RED;
      lines.add(line(Formats.SERVER_TPS_VANILLA_FORMAT,
        va("preTps", preTps),
        va("tps", ".1f", tps),
        va("rst", GuiBase.TXT_RST),
        va("preMspt", preMspt),
        va("mspt", ".1f", mspt)));
    }),

    entry(InfoToggle.PING, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      PlayerInfo info = player.connection.getPlayerInfo(player.getUUID());
      if(info != null)
        lines.add(line(Formats.PING_FORMAT, va("ping", info.getLatency())));
    }),

    entry(InfoToggle.COORDINATES_SCALED, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      //TODO - will be merged with the below case;
    }),

    entry(InfoToggle.COORDINATES, COORDINATES_DIMENSION),
    entry(InfoToggle.DIMENSION, COORDINATES_DIMENSION),

    entry(InfoToggle.BLOCK_POS, BLOCK_CHUNK_REGION),
    entry(InfoToggle.CHUNK_POS, BLOCK_CHUNK_REGION),
    entry(InfoToggle.REGION_FILE, BLOCK_CHUNK_REGION),

    entry(InfoToggle.BLOCK_IN_CHUNK, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.BLOCK_IN_CHUNK_FORMAT,
      va("x", pos.getX() & 0xf),
      va("y", pos.getY() & 0xf),
      va("z", pos.getZ() & 0xf),
      va("cx", chunkPos.x),
      va("cy", pos.getY() >> 4),
      va("cz", chunkPos.z)))),

    entry(InfoToggle.BLOCK_BREAK_SPEED, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.BLOCK_BREAK_SPEED_FORMAT, va("bbs", ".2f", data.getBlockBreakingSpeed())))),

    entry(InfoToggle.DISTANCE, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      Vec3 ref = data.getDistanceReferencePoint();
      lines.add(line(Formats.DISTANCE_FORMAT,
        va("d", ".2f", Math.sqrt(ref.distanceToSqr(x, y, z))),
        va("dx", ".2f", x - ref.x),
        va("dy", ".2f", y - ref.y),
        va("dz", ".2f", z - ref.z),
        va("rx", ".2f", ref.x),
        va("ry", ".2f", ref.y),
        va("rz", ".2f", ref.z)));
    }),

    entry(InfoToggle.FACING, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      Direction facing = player.getDirection();
      String str = switch(facing)
      {
        case NORTH -> Formats.FACING_PZ_FORMAT.getStringValue();
        case SOUTH -> Formats.FACING_NZ_FORMAT.getStringValue();
        case EAST -> Formats.FACING_PX_FORMAT.getStringValue();
        case WEST -> Formats.FACING_NX_FORMAT.getStringValue();
        default -> "Invalid direction";
      };
      lines.add(line(Formats.FACING_FORMAT,
        va("dir", facing.toString()),
        va("coord", str)
      ));
    }),

    entry(InfoToggle.LIGHT_LEVEL, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(accessor.getChunkPublic(chunkPos).isEmpty())
        return;

      LevelLightEngine lightEngine = Objects.requireNonNull(level).getChunkSource().getLightEngine();
      lines.add(line(Formats.LIGHT_LEVEL_CLIENT_FORMAT,
        va("light", lightEngine.getRawBrightness(pos, 0)),
        va("block", lightEngine.getLayerListener(LightLayer.BLOCK).getLightValue(pos)),
        va("sky", lightEngine.getLayerListener(LightLayer.SKY).getLightValue(pos))));
    }),

    entry(InfoToggle.BEE_COUNT, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      Level bestLevel = WorldUtils.getBestWorld(mc);
      BlockEntity be = accessor.getTargetedBlockEntityPublic(bestLevel, mc);
      if(be instanceof BeehiveBlockEntity bbe)
        lines.add(line(Formats.BEE_COUNT_FORMAT, va("bees", bbe.getOccupantCount())));
    }),

    entry(InfoToggle.FURNACE_XP, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      Level bestLevel = WorldUtils.getBestWorld(mc);
      BlockEntity be = accessor.getTargetedBlockEntityPublic(bestLevel, mc);
      if(be instanceof AbstractFurnaceBlockEntity furnace)
        lines.add(line(Formats.FURNACE_XP_FORMAT, va("xp", MiscUtils.getFurnaceXpAmount(furnace))));
    }),

    entry(InfoToggle.HONEY_LEVEL, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      BlockState state = accessor.getTargetedBlockPublic(mc);
      if(state != null && state.getBlock() instanceof BeehiveBlock)
        lines.add(line(Formats.HONEY_LEVEL_FORMAT, va("honey", BeehiveBlockEntity.getHoneyLevel(state))));
    }),

    entry(InfoToggle.ROTATION_YAW, ROTATION_SPEED),
    entry(InfoToggle.ROTATION_PITCH, ROTATION_SPEED),
    entry(InfoToggle.SPEED, ROTATION_SPEED),

    entry(InfoToggle.SPEED_HV, (List<String> lines, Set<InfoToggle> addedTypes) ->
      lines.add(line(Formats.SPEED_HV_FORMAT,
        va("xz", ".3f", Math.sqrt(dx*dx + dz*dz) * 20.0),
        va("y", ".3f", dy * 20.0)))),

    entry(InfoToggle.SPEED_AXIS, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.SPEED_AXIS_FORMAT,
      va("x", dx * 20),
      va("y", dy * 20),
      va("z", dz * 20)))),

    entry(InfoToggle.HORSE_SPEED, HORSE),
    entry(InfoToggle.HORSE_JUMP, HORSE),

    entry(InfoToggle.CHUNK_SECTIONS, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.CHUNK_SECTIONS_FORMAT, va("c", ((IMixinWorldRenderer)mc.levelRenderer).getRenderedChunksInvoker())))),

    entry(InfoToggle.CHUNK_SECTIONS_FULL, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.CHUNK_SECTIONS_FULL_FORMAT, va("c", mc.levelRenderer.getChunkStatistics())))),

    entry(InfoToggle.CHUNK_UPDATES, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add("TODO")),

    entry(InfoToggle.LOADED_CHUNKS_COUNT, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      String chunksClient = Objects.requireNonNull(level).gatherChunkSourceStats();
      Level levelServer = WorldUtils.getBestWorld(mc);
      if(levelServer == null || levelServer != level)
        lines.add(line(Formats.LOADED_CHUNKS_COUNT_CLIENT_FORMAT, va("client", chunksClient)));

      ServerChunkCache cache = (ServerChunkCache)levelServer.getChunkSource();
      lines.add(line(Formats.LOADED_CHUNKS_COUNT_SERVER_FORMAT,
        va("chunks", cache.getLoadedChunksCount()),
        va("total", cache.getTickingGenerated()),
        va("client", chunksClient)));
    }),

    entry(InfoToggle.PARTICLE_COUNT, (List<String> lines, Set<InfoToggle> addedTypes) -> lines.add(line(Formats.PARTICLE_COUNT_FORMAT, va("p", mc.particleEngine.countParticles())))),

    entry(InfoToggle.DIFFICULTY, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      ChunkAccess serverChunk = Objects.requireNonNull(level).getChunk(chunkPos.getWorldPosition());

      float moonPhaseFactor = level.getMoonBrightness();
      long chunkInhabitedTime = serverChunk.getInhabitedTime();
      DifficultyInstance diff = new DifficultyInstance(level.getDifficulty(), level.getDayTime(), chunkInhabitedTime, moonPhaseFactor);
      lines.add(line(Formats.DIFFICULTY_FORMAT,
        va("local", ".2f", diff.getEffectiveDifficulty()),
        va("clamped", ".2f", diff.getSpecialMultiplier()),
        va("day", level.getDayTime() / 24000L)));
    }),

    entry(InfoToggle.BIOME, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(accessor.getChunkPublic(chunkPos).isEmpty())
        return;

      Biome biome = Objects.requireNonNull(level).getBiome(pos);
      ResourceLocation resourceLocation = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);
      lines.add(line(Formats.BIOME_FORMAT, va("biome", StringUtils.translate("biome." + Objects.requireNonNull(resourceLocation).toString().replace(":", ".")))));
    }),

    entry(InfoToggle.BIOME_REG_NAME, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(accessor.getChunkPublic(chunkPos).isEmpty())
        return;

      Biome biome = Objects.requireNonNull(level).getBiome(pos);
      ResourceLocation resourceLocation = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);
      lines.add(line(Formats.BIOME_REG_NAME_FORMAT, va("name", resourceLocation != null ? resourceLocation.toString() : "?")));
    }),

    entry(InfoToggle.TILE_ENTITIES, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      //TODO
    }),

    entry(InfoToggle.ENTITIES_CLIENT_WORLD, ENTITIES),
    entry(InfoToggle.ENTITIES, ENTITIES),

    entry(InfoToggle.SLIME_CHUNK, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(!MiscUtils.isOverworld(Objects.requireNonNull(level)))
        return;

      if(data.isWorldSeedKnown(level))
      {
        long seed = data.getWorldSeed(level);
        lines.add(line(Formats.SLIME_CHUNK_FORMAT,
          va("result", MiscUtils.canSlimeSpawnAt(pos.getX(), pos.getZ(), seed) ? Formats.SLIME_CHUNK_YES_FORMAT.getStringValue() : Formats.SLIME_CHUNK_NO_FORMAT.getStringValue())));
      }
      else
        lines.add(line(Formats.SLIME_CHUNK_FORMAT, va("result", Formats.SLIME_CHUNK_NO_SEED_FORMAT.getStringValue())));
    }),

    entry(InfoToggle.LOOKING_AT_ENTITY, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.ENTITY)
        return;

      Entity lookedEntity = mc.crosshairPickEntity;
      lines.add(lookedEntity instanceof LivingEntity living ?
        line(Formats.LOOKING_AT_ENTITY_LIVING_FORMAT,
          va("entity", lookedEntity.getName().getString()),
          va("hp", living.getHealth()),
          va("maxhp", living.getMaxHealth())) :
        line(Formats.LOOKING_AT_ENTITY_LIVING_FORMAT, va("entity", Objects.requireNonNull(lookedEntity).getName().getString())));
    }),

    entry(InfoToggle.ENTITY_REG_NAME, (List<String> lines, Set<InfoToggle> addedTypes) ->
    {
      if(mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.ENTITY)
        return;

      Entity lookedEntity = ((EntityHitResult) mc.hitResult).getEntity();
      ResourceLocation resourceLocation = EntityType.getKey(lookedEntity.getType());
      lines.add(line(Formats.ENTITY_REG_NAME_FORMAT, va("name", resourceLocation.toString())));
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

        lines.add(line(Formats.BLOCK_PROPS_HEADING_FORMAT, va("block", resourceLocation.toString())));

        for(Property<?> property : state.getProperties())
        {
          Comparable<?> value = state.getValue(property);
          String value2 = line(
            property instanceof BooleanProperty ?
              value.equals(Boolean.TRUE) ? Formats.BLOCK_PROPS_BOOLEAN_TRUE_FORMAT : Formats.BLOCK_PROPS_BOOLEAN_FALSE_FORMAT :
            property instanceof DirectionProperty ?
              Formats.BLOCK_PROPS_DIRECTION_FORMAT :
            property instanceof IntegerProperty ?
              Formats.BLOCK_PROPS_INT_FORMAT :
              Formats.BLOCK_PROPS_STRING_FORMAT,
              va("value", value.toString()));

          lines.add(line(Formats.BLOCK_PROPS_FORMAT,
            va("prop", property.getName()),
            va("value", value2)));
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
      return as > bs ? 1 : as < bs ? -1 : 0;
    }
  }
}
