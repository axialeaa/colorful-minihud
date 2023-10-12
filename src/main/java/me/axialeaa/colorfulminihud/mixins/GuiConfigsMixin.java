package me.axialeaa.colorfulminihud.mixins;

import java.util.List;

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
  private void getConfigs(CallbackInfoReturnable<List<ConfigOptionWrapper>> cir)
  {
    if(GuiConfigs.tab == ColorfulMinihudMod.FORMATS)
      cir.setReturnValue(ConfigOptionWrapper.createFor(Formats.OPTIONS));
  }
}
