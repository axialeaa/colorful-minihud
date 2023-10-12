package me.axialeaa.colorfulminihud;

import java.util.Date;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.minihud.config.Configs;
import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.util.DataStorage;
import fi.dy.masa.minihud.util.MiscUtils;
import me.axialeaa.colorfulminihud.config.Formats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ColorfulLines
{
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
            va("x", pos.getX() >> 4),
            va("y", pos.getY() >> 4),
            va("z", pos.getZ() >> 4)));
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
          va("cx", pos.getX() >> 4),
          va("cy", pos.getY() >> 4),
          va("cz", pos.getZ() >> 4));

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

      default:
        return null;
    }
  }
}
