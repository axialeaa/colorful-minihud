package me.axialeaa.colorfulminihud;

import java.util.Date;

import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.util.MiscUtils;
import me.axialeaa.colorfulminihud.config.Formats;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class ColorfulLines
{
  private static class Variable<T>
  {
    public final String key;
    public final String formatCode;
    public final T value;

    public Variable(String key, String formatCode, T value)
    {
      this.key = key;
      this.formatCode = formatCode;
      this.value = value;
    }
  }

  private static String line(String text, Variable<?>... vars)
  {
    text = text.replace("%", "\0");
    for(Variable<?> var : vars)
    {
      text = text.replace(var.key, var.formatCode);
      text = String.format(text, var.value);
    }
    text = text.replace("\0", "%");
    return text;
  }

  public static Component interpret(String text)
  {
    try
    {
      return Component.Serializer.fromJsonLenient(text);
    }
    catch(Exception e)
    {
      return new TextComponent("Formatting failed - Invalid JSON");
    }
  }

  public static String getLine(InfoToggle type, int fps)
  {
    switch(type)
    {
      case FPS:
        return line(Formats.FPS_FORMAT.getStringValue(), new Variable<Integer>("{FPS}", "%", fps));

      case MEMORY_USAGE:
        long memMax = Runtime.getRuntime().maxMemory();
        long memTotal = Runtime.getRuntime().totalMemory();
        long memFree = Runtime.getRuntime().freeMemory();
        long memUsed = memTotal - memFree;
        return line(Formats.MEMORY_USAGE_FORMAT.getStringValue(),
          new Variable<Long>("{pused}", "%", memUsed * 100L / memMax),
          new Variable<Long>("{pallocated}", "%", memTotal * 100L / memMax),
          new Variable<Long>("{used}", "%", MiscUtils.bytesToMb(memUsed)),
          new Variable<Long>("{max}", "%", MiscUtils.bytesToMb(memMax)),
          new Variable<Long>("{total}", "%", MiscUtils.bytesToMb(memTotal)));

      case TIME_REAL:
        Date date = new Date(System.currentTimeMillis());
        return line(Formats.TIME_REAL_FORMAT.getStringValue(), new Variable<Date>("{time}", "%T", date));
      default:
        return "";
    }
  }
}
