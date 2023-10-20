package me.axialeaa.colorfulminihud.mixins;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fi.dy.masa.malilib.gui.GuiConfigsBase.ConfigOptionWrapper;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.minihud.gui.GuiConfigs;
import me.axialeaa.colorfulminihud.ColorfulMinihudMod;
import me.axialeaa.colorfulminihud.config.Formats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiConfigs.class)
public class GuiConfigsMixin
{
  @Inject(method = "getConfigWidth", at = @At(value = "RETURN", ordinal = 4), cancellable = true, remap = false)
  private void getConfigWidth(CallbackInfoReturnable<Integer> cir)
  {
    if(GuiConfigs.tab == ColorfulMinihudMod.FORMATS)
      cir.setReturnValue(GuiUtils.getScaledWindowWidth() - 300);
  }

  @ModifyReturnValue(method = "getConfigs", at = @At(value = "RETURN", ordinal = 0), remap = false)
  private List<ConfigOptionWrapper> getGenericConfigs(List<ConfigOptionWrapper> original)
  {
    ImmutableList.Builder<ConfigOptionWrapper> list = ImmutableList.builder();
    for(ConfigOptionWrapper wrapper : original)
    {
      String name = wrapper.getConfig().getName();
      if(!name.equals("coordinateFormat") &&
        !name.equals("dateFormatReal") &&
        !name.equals("dateFormatMinecraft"))
        list.add(wrapper);
    }
    return list.build();
  }

  @Inject(method = "getConfigs", at = @At(value = "RETURN", ordinal = 5), cancellable = true, remap = false)
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
