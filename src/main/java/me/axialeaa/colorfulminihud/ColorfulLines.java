package me.axialeaa.colorfulminihud;

import java.util.Date;

import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.minihud.config.Configs;
import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.util.MiscUtils;
import me.axialeaa.colorfulminihud.config.Formats;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

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

  public static String getLine(InfoToggle type, int fps)
  {
    Minecraft mc = Minecraft.getInstance();
    Level level = mc.level;

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
          va("pused", memUsed * 100L / memMax),
          va("pallocated", memTotal * 100L / memMax),
          va("used", MiscUtils.bytesToMb(memUsed)),
          va("max", MiscUtils.bytesToMb(memMax)),
          va("total", MiscUtils.bytesToMb(memTotal)));

      case TIME_REAL:
        Date date = new Date(System.currentTimeMillis());
        return line(Formats.TIME_REAL_FORMAT, va("time", "tS", date));

      case TIME_WORLD:
        return line(Formats.TIME_WORLD_FORMAT,
          va("time", level.getDayTime()),
          va("total", level.getGameTime()));

      case TIME_WORLD_FORMATTED:
        long timeDay = level.getDayTime();
        long day = timeDay / 24000;
        int dayTicks = (int)(timeDay % 24000);
        return line(Formats.TIME_WORLD_FORMATTED_FORMAT,
          va("day", day),
          va("day_1", day + 1),
          va("hour", (dayTicks / 1000 + 6) % 24),
          va("min", (int)(dayTicks * 0.06) % 60),
          va("sec", (int)(dayTicks * 3.6) % 60));

      case TIME_DAY_MODULO:
        int mod = Configs.Generic.TIME_DAY_DIVISOR.getIntegerValue();
        return line(Formats.TIME_DAY_MODULO_FORMAT,
          va("mod", mod),
          va("time", level.getDayTime() % mod));

      case TIME_TOTAL_MODULO:
        int mod1 = Configs.Generic.TIME_TOTAL_DIVISOR.getIntegerValue();
        return line(Formats.TIME_TOTAL_MODULO_FORMAT,
          va("mod", mod1),
          va("time", level.getGameTime() % mod1));

      case SERVER_TPS:
        return "";

      default:
        return "";
    }
  }
}
