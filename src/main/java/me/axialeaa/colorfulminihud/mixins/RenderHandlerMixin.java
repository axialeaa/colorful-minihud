package me.axialeaa.colorfulminihud.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.event.RenderHandler;
import me.axialeaa.colorfulminihud.config.Formats;

@Mixin(RenderHandler.class)
public class RenderHandlerMixin
{
  @Shadow(remap = false) private int fps;
  @Shadow(remap = false) private void addLine(String text){}

  private void addLine1(String text, Variable<?>... vars)
  {
    text = text.replace("%", "\0");
    for(Variable<?> var : vars)
    {
      text = text.replace(var.key, var.formatCode);
      text = String.format(text, var.value);
    }
    text = text.replace("\0", "%");
    addLine(text);
  }

  @Redirect(method = "updateLines", at = @At(value = "INVOKE", target = "Lfi/dy/masa/minihud/event/RenderHandler;addLine(Lfi/dy/masa/minihud/config/InfoToggle;)V"), remap = false)
  private void addLine(RenderHandler self, InfoToggle type)
  {
    switch(type)
    {
      case FPS:
        addLine1(Formats.FPS_FORMAT.getStringValue(), new Variable<Integer>("{FPS}", "%", fps));
      case MEMORY_USAGE:
        long memMax = Runtime.getRuntime().maxMemory();
        long memTotal = Runtime.getRuntime().totalMemory();
        long memFree = Runtime.getRuntime().freeMemory();
        long memUsed = memTotal - memFree;
        // addLine1(Formats.MEMORY_USAGE_FORMAT.getStringValue(), new Variable<Integer>("{pused}", "%", memUsed * 100L / memMax));
    }
  }

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
}
