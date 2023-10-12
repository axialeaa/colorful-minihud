package me.axialeaa.colorfulminihud;

import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.WorldUtils;
import fi.dy.masa.minihud.config.Configs;
import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.event.RenderHandler;
import fi.dy.masa.minihud.mixin.IMixinWorldRenderer;
import fi.dy.masa.minihud.util.DataStorage;
import fi.dy.masa.minihud.util.MiscUtils;
import me.axialeaa.colorfulminihud.config.Formats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class ColorfulLines
{
  private static final IRenderHandler accessor = (IRenderHandler)RenderHandler.getInstance();

  private static class Variable<T>
  {
    private final String key;
    private final String defaultFormat;
    public final T value;

    public Variable(String key, String defaultFormat, T value)
    {
      this.key = key;
      this.defaultFormat = defaultFormat;
      this.value = value;
    }

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
    return new Variable<T>(key, defaultFormat, value);
  }

  private static Variable<String> va(String key, String value)
  {
    return new Variable<String>(key, "s", value);
  }

  private static Variable<Integer> va(String key, int value)
  {
    return new Variable<Integer>(key, "d", value);
  }

  private static Variable<Long> va(String key, long value)
  {
    return new Variable<Long>(key, "d", value);
  }

  private static Variable<Float> va(String key, float value)
  {
    return new Variable<Float>(key, "f", value);
  }

  private static Variable<Double> va(String key, double value)
  {
    return new Variable<Double>(key, "f", value);
  }

  @Nullable
  public static String getLine(InfoToggle type, int fps, DataStorage data, Set<InfoToggle> addedTypes)
  {
    Minecraft mc = Minecraft.getInstance();
    Level level = mc.level;
    LocalPlayer player = mc.player;
    double x = player.getX(), y = player.getY(), z = player.getZ();
    BlockPos pos = new BlockPos(x, y, z);
    ChunkPos chunkPos = new ChunkPos(pos);

    switch(type)
    {
      case FPS:
        return line(Formats.FPS_FORMAT, va("fps", fps));

      case MEMORY_USAGE:
        long memMax = Runtime.getRuntime().maxMemory();
        long memTotal = Runtime.getRuntime().totalMemory();
        long memFree = Runtime.getRuntime().freeMemory();
        long memUsed = memTotal - memFree;
        return line(Formats.MEMORY_USAGE_FORMAT,
          va("pused", "2d", memUsed * 100L / memMax),
          va("pallocated", "2d", memTotal * 100L / memMax),
          va("used", "3d", MiscUtils.bytesToMb(memUsed)),
          va("max", "3d", MiscUtils.bytesToMb(memMax)),
          va("total", "3d", MiscUtils.bytesToMb(memTotal)));

      case TIME_REAL:
        Date date = new Date(System.currentTimeMillis());
        return line(Formats.TIME_REAL_FORMAT, va("time", "tk", date));

      case TIME_WORLD:
        return line(Formats.TIME_WORLD_FORMAT,
          va("time", "5d", level.getDayTime()),
          va("total", level.getGameTime()));

      case TIME_WORLD_FORMATTED:
        long timeDay = level.getDayTime();
        long day = timeDay / 24000;
        int dayTicks = (int)(timeDay % 24000);
        return line(Formats.TIME_WORLD_FORMATTED_FORMAT,
          va("day", day),
          va("day_1", day + 1),
          va("hour", "02d", (dayTicks / 1000 + 6) % 24),
          va("min", "02d", (int)(dayTicks * 0.06) % 60),
          va("sec", "02d", (int)(dayTicks * 3.6) % 60));

      case TIME_DAY_MODULO:
        int mod = Configs.Generic.TIME_DAY_DIVISOR.getIntegerValue();
        return line(Formats.TIME_DAY_MODULO_FORMAT,
          va("mod", mod),
          va("time", "5d", level.getDayTime() % mod));

      case TIME_TOTAL_MODULO:
        int mod1 = Configs.Generic.TIME_TOTAL_DIVISOR.getIntegerValue();
        return line(Formats.TIME_TOTAL_MODULO_FORMAT,
          va("mod", mod1),
          va("time", "5d", level.getGameTime() % mod1));

      case SERVER_TPS:
        if(mc.hasSingleplayerServer() && mc.getSingleplayerServer().getTickCount() % 10 == 0)
          data.updateIntegratedServerTPS();

        if(!data.hasTPSData())
          return line(Formats.SERVER_TPS_NULL_FORMAT);

        double tps = data.getServerTPS();
        double mspt = data.getServerMSPT();
        String preTps = tps >= 20d ? GuiBase.TXT_GREEN : GuiBase.TXT_RED;

        if(data.isCarpetServer() || mc.isLocalServer())
        {
          String preMspt = mspt <= 40 ? GuiBase.TXT_GREEN :
            mspt <= 45 ? GuiBase.TXT_YELLOW :
            mspt <= 50 ? GuiBase.TXT_GOLD :
            GuiBase.TXT_RED;
          return line(Formats.SERVER_TPS_CARPET_FORMAT,
            va("preTps", preTps),
            va("tps", ".1f", tps),
            va("rst", GuiBase.TXT_RST),
            va("preMspt", preMspt),
            va("mspt", ".1f", mspt));
        }

        String preMspt = mspt <= 51 ? GuiBase.TXT_GREEN : GuiBase.TXT_RED;
        return line(Formats.SERVER_TPS_VANILLA_FORMAT,
          va("preTps", preTps),
          va("tps", ".1f", tps),
          va("rst", GuiBase.TXT_RST),
          va("preMspt", preMspt),
          va("mspt", ".1f", mspt));

      case PING:
        PlayerInfo info = player.connection.getPlayerInfo(player.getUUID());
        return info == null ? null : line(Formats.PING_FORMAT, va("ping", info.getLatency()));

      case COORDINATES:
      case DIMENSION:
        if(addedTypes.contains(InfoToggle.COORDINATES) || addedTypes.contains(InfoToggle.DIMENSION))
          return null;

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
          str.append(line(Formats.DIMENSION_FORMAT, va("dim", level.dimension().location().toString())));
          addedTypes.add(InfoToggle.DIMENSION);
        }
        return str.append("]").toString();

      case BLOCK_POS:
      case CHUNK_POS:
      case REGION_FILE:
        if(addedTypes.contains(InfoToggle.BLOCK_POS) || addedTypes.contains(InfoToggle.CHUNK_POS) || addedTypes.contains(InfoToggle.REGION_FILE))
          return null;

        StringBuilder str1 = new StringBuilder(256);
        str1.append("[");
        boolean hasOther = InfoToggle.BLOCK_POS.getBooleanValue();
        if(hasOther)
        {
          str1.append(line(Formats.BLOCK_POS_FORMAT,
            va("x", pos.getX()),
            va("y", pos.getY()),
            va("z", pos.getZ())));
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
        return str1.append("]").toString();

      case BLOCK_IN_CHUNK:
        return line(Formats.BLOCK_IN_CHUNK_FORMAT,
          va("x", pos.getX() & 0xf),
          va("y", pos.getY() & 0xf),
          va("z", pos.getZ() & 0xf),
          va("cx", chunkPos.x),
          va("cy", pos.getY() >> 4),
          va("cz", chunkPos.z));

      case BLOCK_BREAK_SPEED:
        return line(Formats.BLOCK_BREAK_SPEED_FORMAT, va("bbs", ".2f", data.getBlockBreakingSpeed()));

      case DISTANCE:
        Vec3 ref = data.getDistanceReferencePoint();
        return line(Formats.DISTANCE_FORMAT,
          va("d", ".2f", Math.sqrt(ref.distanceToSqr(x, y, z))),
          va("dx", ".2f", x - ref.x),
          va("dy", ".2f", y - ref.y),
          va("dz", ".2f", z - ref.z),
          va("rx", ".2f", ref.x),
          va("ry", ".2f", ref.y),
          va("rz", ".2f", ref.z));

      case FACING:
        Direction facing = player.getDirection();
        String str2 = switch(facing)
        {
          case NORTH -> Formats.FACING_PZ_FORMAT.getStringValue();
          case SOUTH -> Formats.FACING_NZ_FORMAT.getStringValue();
          case EAST -> Formats.FACING_PX_FORMAT.getStringValue();
          case WEST -> Formats.FACING_NX_FORMAT.getStringValue();
          default -> "Invalid direction";
        };
        return line(Formats.FACING_FORMAT, va("dir", str2));

      case LIGHT_LEVEL:
        if(accessor.getChunkPublic(chunkPos).isEmpty())
          return null;

        LevelLightEngine lightEngine = level.getChunkSource().getLightEngine();
        return line(Formats.LIGHT_LEVEL_CLIENT_FORMAT,
          va("light", lightEngine.getRawBrightness(pos, 0)),
          va("block", lightEngine.getLayerListener(LightLayer.BLOCK).getLightValue(pos)),
          va("sky", lightEngine.getLayerListener(LightLayer.SKY).getLightValue(pos)));

      case BEE_COUNT:
        Level bestLevel = WorldUtils.getBestWorld(mc);
        BlockEntity be = accessor.getTargetedBlockEntityPublic(bestLevel, mc);
        return be instanceof BeehiveBlockEntity bbe ?
          line(Formats.BEE_COUNT_FORMAT, va("bees", bbe.getOccupantCount())) : null;

      case HONEY_LEVEL:
        BlockState state = accessor.getTargetedBlockPublic(mc);
        return state != null && state.getBlock() instanceof BeehiveBlock ?
          line(Formats.HONEY_LEVEL_FORMAT, va("honey", BeehiveBlockEntity.getHoneyLevel(state))) : null;

      case ROTATION_YAW:
      case ROTATION_PITCH:
      case SPEED:
        if(addedTypes.contains(InfoToggle.ROTATION_YAW) || addedTypes.contains(InfoToggle.ROTATION_PITCH) || addedTypes.contains(InfoToggle.SPEED))
          return null;

        StringBuilder str3 = new StringBuilder(128);
        str3.append("[");
        boolean hasOther1 = InfoToggle.ROTATION_YAW.getBooleanValue();
        if(hasOther1)
        {
          str3.append(line(Formats.ROTATION_YAW_FORMAT, va("yaw", Mth.wrapDegrees(player.getYRot()))));
          addedTypes.add(InfoToggle.ROTATION_YAW);
        }
        if(InfoToggle.ROTATION_PITCH.getBooleanValue())
        {
          if(hasOther1)
            str3.append(",");
          hasOther1 = true;
          str3.append(line(Formats.ROTATION_PITCH_FORMAT, va("pitch", Mth.wrapDegrees(player.getXRot()))));
          addedTypes.add(InfoToggle.ROTATION_PITCH);
        }
        if(InfoToggle.SPEED.getBooleanValue())
        {
          if(hasOther1)
            str3.append(",");
          double dx = x - player.xOld, dy = y - player.yOld, dz = z - player.zOld;
          double dist1 = Math.sqrt(dx*dx + dy*dy + dz*dz);
          str3.append(line(Formats.SPEED_FORMAT, va("speed", dist1 * 20)));
          addedTypes.add(InfoToggle.SPEED);
        }
        return str3.append("]").toString();

      case SPEED_AXIS:
        return line(Formats.SPEED_AXIS_FORMAT,
          va("x", (x - player.xOld) * 20),
          va("y", (y - player.yOld) * 20),
          va("z", (z - player.zOld) * 20));

      case CHUNK_SECTIONS:
        return line(Formats.CHUNK_SECTIONS_FORMAT, va("c", ((IMixinWorldRenderer)mc.levelRenderer).getRenderedChunksInvoker()));

      case CHUNK_SECTIONS_FULL:
        return line(Formats.CHUNK_SECTIONS_FULL_FORMAT, va("c", mc.levelRenderer.getChunkStatistics()));

      case CHUNK_UPDATES:
        return "TODO"; // minihud parity

      case LOADED_CHUNKS_COUNT:
        String chunksClient = level.gatherChunkSourceStats();
        Level levelServer = WorldUtils.getBestWorld(mc);
        if(levelServer == null || levelServer != level)
          return line(Formats.LOADED_CHUNKS_COUNT_CLIENT_FORMAT, va("client", chunksClient));

        ServerChunkCache cache = (ServerChunkCache)levelServer.getChunkSource();
        return line(Formats.LOADED_CHUNKS_COUNT_SERVER_FORMAT,
          va("chunks", cache.getLoadedChunksCount()),
          va("total", cache.getTickingGenerated()),
          va("client", chunksClient));

      case PARTICLE_COUNT:
        return line(Formats.PARTICLE_COUNT_FORMAT, va("p", mc.particleEngine.countParticles()));

      case DIFFICULTY:
        ChunkAccess serverChunk = Objects.requireNonNull(level).getChunk(chunkPos.getWorldPosition());

        float moonPhaseFactor = level.getMoonBrightness();
        long chunkInhabitedTime = serverChunk.getInhabitedTime();
        DifficultyInstance diff = new DifficultyInstance(level.getDifficulty(), level.getDayTime(), chunkInhabitedTime, moonPhaseFactor);
        return line(Formats.DIFFICULTY_FORMAT,
          va("local", ".2f", diff.getEffectiveDifficulty()),
          va("clamped", ".2f", diff.getSpecialMultiplier()),
          va("day", level.getDayTime() / 24000L)
        );

      // case BIOME:
      // case BIOME_REG_NAME:
      // case TILE_ENTITIES:
      // case ENTITIES_CLIENT:
      // case ENTITIES_SERVER:

      case SLIME_CHUNK:
        if (!MiscUtils.isOverworld(Objects.requireNonNull(level)))
          return null;
        if (data.isWorldSeedKnown(level))
        {
          long seed = data.getWorldSeed(level);
          return line(Formats.SLIME_CHUNK_FORMAT,
            va("result", MiscUtils.canSlimeSpawnAt(pos.getX(), pos.getZ(), seed) ?
              Formats.SLIME_CHUNK_YES_FORMAT.getStringValue() :
              Formats.SLIME_CHUNK_NO_FORMAT.getStringValue()
            )
          );
        }
        else
        {
          return line(Formats.SLIME_CHUNK_FORMAT, va("result", Formats.SLIME_CHUNK_NO_SEED_FORMAT.getStringValue()));
        }

      // case LOOKING_AT_ENTITY:
      // case ENTITY_REG_NAME:
      // case LOOKING_AT_BLOCK:
      // case LOOKING_AT_BLOCK_CHUNK:
      // case BLOCK_PROPS:

      default:
        return null;
    }
  }
}
