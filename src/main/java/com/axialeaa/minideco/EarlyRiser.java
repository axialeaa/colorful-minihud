package com.axialeaa.minideco;

import com.chocohead.mm.api.ClassTinkerers;

// import fi.dy.masa.minihud.gui.GuiConfigs.ConfigGuiTab;

public class EarlyRiser implements Runnable
{
  @Override
  public void run()
  {
    ClassTinkerers.enumBuilder("fi.dy.masa.minihud.gui.GuiConfigs$ConfigGuiTab", String.class)
      .addEnum("FORMATS", "minideco.gui.button.config_gui.formats").build();
  }
}
