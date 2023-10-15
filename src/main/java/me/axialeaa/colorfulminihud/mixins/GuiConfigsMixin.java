package me.axialeaa.colorfulminihud.mixins;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fi.dy.masa.malilib.gui.GuiConfigsBase.ConfigOptionWrapper;
import fi.dy.masa.minihud.gui.GuiConfigs;
import me.axialeaa.colorfulminihud.config.Formats;
import me.axialeaa.colorfulminihud.ColorfulMinihudMod;

@Mixin(GuiConfigs.class)
public class GuiConfigsMixin
{
  @Inject(method = "getConfigs", at = @At("RETURN"), cancellable = true, remap = false)
  private void getFormatConfig(CallbackInfoReturnable<List<ConfigOptionWrapper>> cir)
  {
    if(GuiConfigs.tab == ColorfulMinihudMod.FORMATS)
      cir.setReturnValue(ConfigOptionWrapper.createFor(Formats.OPTIONS));
  }

  @ModifyReturnValue(method = "getConfigs", at = @At(value = "RETURN", ordinal = 1), remap = false)
  public List<ConfigOptionWrapper> addToColors(List<ConfigOptionWrapper> original)
  {
    ImmutableList.Builder<ConfigOptionWrapper> list = ImmutableList.builder();
    list.addAll(original);
    list.addAll(ConfigOptionWrapper.createFor(Formats.COLORS));
    return list.build();
  }
}
