package me.axialeaa.colorfulminihud.mixins;

//#if MC >= 12000
//$$ import net.minecraft.client.gui.GuiGraphics;
//#else
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
import fi.dy.masa.malilib.config.HudAlignment;
import fi.dy.masa.minihud.config.Configs;
import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.event.RenderHandler;
import fi.dy.masa.minihud.util.DataStorage;
import me.axialeaa.colorfulminihud.ColorfulLines;
import me.axialeaa.colorfulminihud.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mixin(RenderHandler.class)
public class RenderHandlerMixin
{
  //#if MC < 11903
  @Shadow private int fps;
  //#endif
  @Shadow @Final private DataStorage data;
  @Shadow private Set<InfoToggle> addedTypes;

  @Shadow private LevelChunk getClientChunk(ChunkPos pos){return null;}
  @Shadow private LevelChunk getChunk(ChunkPos pos){return null;}
  @Shadow private BlockEntity getTargetedBlockEntity(Level level, Minecraft mc){return null;}
  @Shadow private BlockState getTargetedBlock(Minecraft mc){return null;}

  @Unique private final List<String> linesString = new ArrayList<>();
  @Unique private final List<Component> linesComponent = new ArrayList<>();

  //#if MC >= 12000
  //$$ @Redirect(method = "onRenderGameOverlayPost", at = @At(value = "INVOKE", target = "Lfi/dy/masa/malilib/render/RenderUtils;renderText(IIDIILfi/dy/masa/malilib/config/HudAlignment;ZZLjava/util/List;Lnet/minecraft/client/gui/GuiGraphics;)I"))
  //$$ private int renderText(int xOff, int yOff, double scale, int textColor, int bgColor, HudAlignment alignment, boolean useBackground, boolean useShadow, List<String> lines, GuiGraphics graphics)
  //$$ {
  //$$   return RenderUtils.renderComponents(xOff, yOff, scale, textColor, bgColor, alignment, useBackground, useShadow, linesComponent, graphics);
  //$$ }
  //#else
  @Redirect(method = "onRenderGameOverlayPost", at = @At(value = "INVOKE", target = "Lfi/dy/masa/malilib/render/RenderUtils;renderText(IIDIILfi/dy/masa/malilib/config/HudAlignment;ZZLjava/util/List;Lcom/mojang/blaze3d/vertex/PoseStack;)I"))
  private int renderText(int xOff, int yOff, double scale, int textColor, int bgColor, HudAlignment alignment, boolean useBackground, boolean useShadow, List<String> lines, PoseStack poseStack)
  {
    return RenderUtils.renderComponents(xOff, yOff, scale, textColor, bgColor, alignment, useBackground, useShadow, linesComponent, poseStack);
  }
  //#endif

  @Inject(method = "updateLines", at = @At(value = "INVOKE", target = "Ljava/util/Collections;sort(Ljava/util/List;)V", ordinal = 0), remap = false)
  private void preUpdateLines(CallbackInfo ci)
  {
    Minecraft mc = Minecraft.getInstance();
    ClientLevel level = mc.level;
    LocalPlayer player = mc.player;
    double x = Objects.requireNonNull(player).getX(), y = player.getY(), z = player.getZ();
    //#if MC >= 11904
    //$$ BlockPos pos = BlockPos.containing(x, y, z);
    //#else
    BlockPos pos = new BlockPos(x, y, z);
    //#endif
    ChunkPos chunkPos = new ChunkPos(pos);

    //#if MC >= 11903
    //$$ ColorfulLines.setup(mc.getFps(), data, level, player, x, y, z, pos, chunkPos, getClientChunk(chunkPos), getChunk(chunkPos), getTargetedBlock(mc), getTargetedBlockEntity(level, mc));
    //#else
    ColorfulLines.setup(fps, data, level, player, x, y, z, pos, chunkPos, getClientChunk(chunkPos), getChunk(chunkPos), getTargetedBlock(mc), getTargetedBlockEntity(level, mc));
    //#endif
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
        linesComponent.add(Component.Serializer.fromJsonLenient(text));
      }
      catch(Exception e)
      {
        //#if MC >= 11900
        //$$ linesComponent.add(Component.literal("Formatting failed - Invalid JSON"));
        //#else
        linesComponent.add(new TextComponent("Formatting failed - Invalid JSON"));
        //#endif
      }

    if(Configs.Generic.SORT_LINES_BY_LENGTH.getBooleanValue())
    {
      linesComponent.sort(new ColorfulLines.StringHolder());
      if(Configs.Generic.SORT_LINES_REVERSED.getBooleanValue())
        Collections.reverse(linesComponent);
    }
    ci.cancel();
  }
}
