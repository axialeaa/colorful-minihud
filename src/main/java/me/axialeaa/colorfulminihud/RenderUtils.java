package me.axialeaa.colorfulminihud;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
//#if MC >= 12000
//$$ import net.minecraft.client.gui.GuiGraphics;
//#endif
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;

import fi.dy.masa.malilib.config.HudAlignment;
import fi.dy.masa.malilib.util.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class RenderUtils extends fi.dy.masa.malilib.render.RenderUtils
{
  private static final Minecraft mc = Minecraft.getInstance();
  private static final Font font = mc.font;
  private static final Tesselator tesselator = Tesselator.getInstance();

//#if MC >= 12000
//$$ public static int renderComponents(int xOff, int yOff, double scale, int textColor, int bgColor, HudAlignment alignment, boolean useBackground, boolean useShadow, List<Component> lines, GuiGraphics graphics)
//$$ {
//$$   PoseStack poseStack = graphics.pose();
//#else
  public static int renderComponents(int xOff, int yOff, double scale, int textColor, int bgColor, HudAlignment alignment, boolean useBackground, boolean useShadow, List<Component> lines, PoseStack poseStack)
  {
//#endif
    final int scaledWidth = GuiUtils.getScaledWindowWidth();
    final int lineHeight = font.lineHeight + 2;
    final int contentHeight = lines.size() * lineHeight - 2;
    final int bgMargin = 2;
    if(scale < 0.0125)
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
    py = getHudPosY((int)py, yOff, contentHeight, scale, alignment);
    py += getHudOffsetForPotions(alignment, scale, mc.player);

    for(Component line : lines)
    {
      if(line == null)
        //#if MC >= 11900
        //$$ line = Component.literal("NULL");
        //#else
        line = new TextComponent("NULL");
        //#endif

      final int width = font.width(line);
      switch(alignment)
      {
        case TOP_RIGHT:
        case BOTTOM_RIGHT:
          px = scaledWidth / scale - width - xOff - bgMargin;
          break;
        case CENTER:
          px = scaledWidth / scale / 2 - width / 2F - xOff;
          break;
        default:
      }

      final int x = (int)px;
      final int y = (int)py;
      py += lineHeight;

      if(useBackground)
        drawRect(poseStack.last().pose(), x - bgMargin, y - bgMargin, width + bgMargin, bgMargin + font.lineHeight, bgColor);

      //#if MC >= 12000
      //$$ graphics.drawString(font, line, x, y, textColor, useShadow);
      //#else
      if(useShadow)
        font.drawShadow(poseStack, line, x, y, textColor);
      else
        font.draw(poseStack, line, x, y, textColor);
      //#endif
    }

    if(scale != 1d)
      poseStack.popPose();

    return contentHeight + bgMargin * 2;
  }

  public static void drawRect(Matrix4f m, int x, int y, int w, int h, int color)
  {
    setupBlend();
    float a = (color >> 24 & 255)/255f;
    float r = (color >> 16 & 255)/255f;
    float g = (color >> 8 & 255)/255f;
    float b = (color & 255)/255f;
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
    RenderSystem.applyModelViewMatrix();
    BufferBuilder buffer = tesselator.getBuilder();
    buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    buffer.vertex(m, x,     y,     0).color(r, g, b, a).endVertex();
    buffer.vertex(m, x,     y + h, 0).color(r, g, b, a).endVertex();
    buffer.vertex(m, x + w, y + h, 0).color(r, g, b, a).endVertex();
    buffer.vertex(m, x + w, y,     0).color(r, g, b, a).endVertex();
    tesselator.end();
    RenderSystem.disableBlend();
  }
}
