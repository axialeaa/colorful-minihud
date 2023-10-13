package me.axialeaa.colorfulminihud.mixins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.math.Matrix4f;

import fi.dy.masa.malilib.config.HudAlignment;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.minihud.config.Configs;
import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.event.RenderHandler;
import fi.dy.masa.minihud.util.DataStorage;
import me.axialeaa.colorfulminihud.ColorfulLines;
import me.axialeaa.colorfulminihud.IRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

@Mixin(RenderHandler.class)
public class RenderHandlerMixin implements IRenderHandler
{
  @Shadow(remap = false) private int fps;
  @Shadow(remap = false) @Final private DataStorage data;
  @Shadow(remap = false) private Set<InfoToggle> addedTypes;
  @Shadow(remap = false) private void addLine(String text){}

  @Shadow(remap = false) private LevelChunk getChunk(ChunkPos pos){return null;}
  @Shadow(remap = false) private BlockEntity getTargetedBlockEntity(Level level, Minecraft mc){return null;}
  @Shadow(remap = false) private BlockState getTargetedBlock(Minecraft mc){return null;}

  @Override
  public LevelChunk getChunkPublic(ChunkPos pos)
  {
    return getChunk(pos);
  }

  @Override
  public BlockEntity getTargetedBlockEntityPublic(Level level, Minecraft mc)
  {
    return getTargetedBlockEntity(level, mc);
  }

  @Override
  public BlockState getTargetedBlockPublic(Minecraft mc)
  {
    return getTargetedBlock(mc);
  }

  @Unique private final List<String> linesString = new ArrayList<>();
  @Unique private final List<Component> linesComponent = new ArrayList<>();

  @Redirect(method = "onRenderGameOverlayPost", at = @At(value = "INVOKE", target = "Lfi/dy/masa/malilib/render/RenderUtils;renderText(IIDIILfi/dy/masa/malilib/config/HudAlignment;ZZLjava/util/List;Lcom/mojang/blaze3d/vertex/PoseStack;)I"), remap = false)
  private int renderText(int xOff, int yOff, double scale, int textColor, int bgColor, HudAlignment alignment, boolean useBackground, boolean useShadow, List<String> lines_, PoseStack poseStack)
  {
    Minecraft mc = Minecraft.getInstance();
    Font font = mc.font;
    final int scaledWidth = GuiUtils.getScaledWindowWidth();
    final int lineHeight = font.lineHeight + 2;
    final int contentHeight = linesComponent.size() * lineHeight - 2;
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
    py = RenderUtils.getHudPosY((int)py, yOff, contentHeight, scale, alignment);
    py += RenderUtils.getHudOffsetForPotions(alignment, scale, mc.player);

    for(Component line : linesComponent)
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
          px = scaledWidth / scale / 2 - width / 2F - xOff;
          break;
        default:
      }

      final int x = (int)px;
      final int y = (int)py;
      py += lineHeight;

      if(useBackground)
      {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
          GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        float a = (bgColor >> 24 & 255)/255f;
        float r = (bgColor >> 16 & 255)/255f;
        float g = (bgColor >> 8 & 255)/255f;
        float b = (bgColor & 255)/255f;
        int rx = x - bgMargin;
        int ry = y - bgMargin;
        int w = width + bgMargin;
        int h = bgMargin + font.lineHeight;
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.applyModelViewMatrix();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f m = poseStack.last().pose();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(m, rx,     ry,     0).color(r, g, b, a).endVertex();
        buffer.vertex(m, rx,     ry + h, 0).color(r, g, b, a).endVertex();
        buffer.vertex(m, rx + w, ry + h, 0).color(r, g, b, a).endVertex();
        buffer.vertex(m, rx + w, ry,     0).color(r, g, b, a).endVertex();
        tesselator.end();
        RenderSystem.disableBlend();
      }
        // RenderUtils.drawRect(x - bgMargin, y - bgMargin, width + bgMargin, bgMargin + font.lineHeight, bgColor);

      if(useShadow)
        font.drawShadow(poseStack, line, x, y, textColor);

      else
        font.draw(poseStack, line, x, y, textColor);
    }

    if(scale != 1d)
      poseStack.popPose();

    return contentHeight + bgMargin * 2;
  }

  @Inject(method = "updateLines", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;)V", ordinal = 0), remap = false)
  private void preUpdateLines(CallbackInfo ci)
  {
    ColorfulLines.setup(fps, data);
    linesString.clear();
  }

  @Redirect(method = "updateLines", at = @At(value = "INVOKE", target = "Lfi/dy/masa/minihud/event/RenderHandler;addLine(Lfi/dy/masa/minihud/config/InfoToggle;)V"), remap = false)
  private void addLine(RenderHandler self, InfoToggle type)
  {
    ColorfulLines.addLine(type, linesString, addedTypes);
  }

  @Inject(method = "updateLines", at = @At(value = "INVOKE", target = "Lfi/dy/masa/malilib/config/options/ConfigBoolean;getBooleanValue()Z", ordinal = 0), cancellable = true, remap = false)
  private void postUpdateLines(CallbackInfo ci)
  {
    linesComponent.clear();
    for(String text : linesString)
      try
      {
        linesComponent.add(Component.Serializer.fromJsonLenient((String)text));
      }
      catch(Exception e)
      {
        linesComponent.add(new TextComponent("Formatting failed - Invalid JSON"));
      }

    if(Configs.Generic.SORT_LINES_BY_LENGTH.getBooleanValue())
    {
      Collections.sort(linesComponent, new ColorfulLines.StringHolder());
      if(Configs.Generic.SORT_LINES_REVERSED.getBooleanValue())
        Collections.reverse(linesComponent);
    }
    ci.cancel();
  }
}
