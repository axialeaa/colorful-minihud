package me.axialeaa.colorfulminihud.mixins;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import fi.dy.masa.malilib.config.HudAlignment;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.event.RenderHandler;
import fi.dy.masa.minihud.util.DataStorage;
import me.axialeaa.colorfulminihud.ColorfulLines;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

@Mixin(RenderHandler.class)
public class RenderHandlerMixin
{
  @Shadow(remap = false) private int fps;
  @Shadow(remap = false) @Final private DataStorage data;
  @Shadow(remap = false) private Set<InfoToggle> addedTypes;
  @Shadow(remap = false) private void addLine(String text){}

  private final List<Component> lines = new ArrayList<>();

  @Redirect(method = "onRenderGameOverlayPost", at = @At(value = "INVOKE", target = "Lfi/dy/masa/malilib/render/RenderUtils;renderText(IIDIILfi/dy/masa/malilib/config/HudAlignment;ZZLjava/util/List;Lcom/mojang/blaze3d/vertex/PoseStack;)I"), remap = false)
  private int renderText(int xOff, int yOff, double scale, int textColor, int bgColor, HudAlignment alignment, boolean useBackground, boolean useShadow, List<String> lines_, PoseStack poseStack)
  {
    Minecraft mc = Minecraft.getInstance();
    Font font = mc.font;
    final int scaledWidth = GuiUtils.getScaledWindowWidth();
    final int lineHeight = font.lineHeight + 2;
    final int contentHeight = lines.size() * lineHeight - 2;
    final int bgMargin = 2;
    if(scale == 0d)
      return 0;

    if(scale != 1d)
    {
      xOff *= scale;
      yOff *= scale;
      poseStack.pushPose();
      poseStack.scale((float)scale, (float)scale, 0f);
    }

    double px = xOff + bgMargin;
    double py = yOff + bgMargin;
    py = RenderUtils.getHudPosY((int)py, yOff, contentHeight, scale, alignment);
    py += RenderUtils.getHudOffsetForPotions(alignment, scale, mc.player);

    for(Component line : lines)
    {
      if(line == null)
        line = new TextComponent("NULL");

      final int width = font.width(line);
      switch(alignment)
      {
        case TOP_RIGHT:
        case BOTTOM_RIGHT:
          px = scaledWidth / scale - width - xOff - bgMargin;
          break;
        case CENTER:
          px = scaledWidth / scale / 2 - width / 2 - xOff;
          break;
        default:
      }

      final int x = (int)px;
      final int y = (int)py;
      py += lineHeight;

      if(useBackground)
        RenderUtils.drawRect(x - bgMargin, y - bgMargin, width + bgMargin, bgMargin + font.lineHeight, bgColor);

      if(useShadow)
        font.drawShadow(poseStack, line, x, y, textColor);

      else
        font.draw(poseStack, line, x, y, textColor);
    }

    if(scale != 1d)
      poseStack.popPose();

    return contentHeight + bgMargin * 2;
  }

  @Redirect(method = "updateLines", at = @At(value = "INVOKE", target = "Lfi/dy/masa/minihud/event/RenderHandler;addLine(Lfi/dy/masa/minihud/config/InfoToggle;)V"), remap = false)
  private void addLine(RenderHandler self, InfoToggle type)
  {
    String line = ColorfulLines.getLine(type, fps, data, addedTypes);
    if(line != null)
      addLine(line);
  }

  @Inject(method = "updateLines", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", ordinal = 1), remap = false)
  private void clearLines(CallbackInfo ci)
  {
    lines.clear();
  }

  @Redirect(method = "updateLines", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1), remap = false)
  private boolean collectLines(List<String> self, Object text)
  {
    try
    {
      return lines.add(Component.Serializer.fromJsonLenient((String)text));
    }
    catch(Exception e)
    {
      return lines.add(new TextComponent("Formatting failed - Invalid JSON"));
    }
  }
}
