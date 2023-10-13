package me.axialeaa.colorfulminihud;

import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.WorldUtils;
import fi.dy.masa.minihud.config.Configs;
import fi.dy.masa.minihud.config.InfoToggle;
import fi.dy.masa.minihud.event.RenderHandler;
import fi.dy.masa.minihud.mixin.IMixinServerWorld;
import fi.dy.masa.minihud.mixin.IMixinWorldRenderer;
import fi.dy.masa.minihud.util.DataStorage;
import fi.dy.masa.minihud.util.IServerEntityManager;
import fi.dy.masa.minihud.util.MiscUtils;
import me.axialeaa.colorfulminihud.config.Formats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class ColorfulLines
{
  private static final IRenderHandler accessor = (IRenderHandler)RenderHandler.getInstance();

  private record Variable<T>(String key, String defaultFormat, T value)
  {
    public String apply(String text, int i)
    {
      text = text.replace("%" + key + "$", "%" + i + "$");
      return text.replace("%" + key, "%" + i + "$" + defaultFormat);
    }
  }

  private static String line(ConfigString config, Variable<?>... vars)
  {
    String text = config.getStringValue();
    Object[] values = new Object[vars.length];
    int i = 0;
    for(Variable<?> var : vars)
    {
      values[i] = var.value;
      text = var.apply(text, ++i);
    }

    text = text.replace("%colorFG", Formats.COLORFG.getStringValue())
      .replace("%colorBG", Formats.COLORBG.getStringValue())
      .replace("%color0", Formats.COLOR0.getStringValue())
      .replace("%color1", Formats.COLOR1.getStringValue())
      .replace("%color2", Formats.COLOR2.getStringValue())
      .replace("%color3", Formats.COLOR3.getStringValue())
      .replace("%color4", Formats.COLOR4.getStringValue())
      .replace("%color5", Formats.COLOR5.getStringValue())
      .replace("%color6", Formats.COLOR6.getStringValue())
      .replace("%color7", Formats.COLOR7.getStringValue())
      .replace("%color8", Formats.COLOR8.getStringValue())
      .replace("%color9", Formats.COLOR9.getStringValue())
      .replace("%color10", Formats.COLOR10.getStringValue())
      .replace("%color11", Formats.COLOR11.getStringValue())
      .replace("%color12", Formats.COLOR12.getStringValue())
      .replace("%color13", Formats.COLOR13.getStringValue())
      .replace("%color14", Formats.COLOR14.getStringValue())
      .replace("%color15", Formats.COLOR15.getStringValue());

    return String.format(text, values);
  }

  private static <T> Variable<T> va(String key, String defaultFormat, T value)
  {
    return new Variable<>(key, defaultFormat, value);
  }

  private static Variable<String> va(String key, String value)
  {
    return new Variable<>(key, "s", value);
  }

  private static Variable<Integer> va(String key, int value)
  {
    return new Variable<>(key, "d", value);
  }

  private static Variable<Long> va(String key, long value)
  {
    return new Variable<>(key, "d", value);
  }

  private static Variable<Float> va(String key, float value)
  {
    return new Variable<>(key, "f", value);
  }

  private static Variable<Double> va(String key, double value)
  {
    return new Variable<>(key, "f", value);
  }

  @Nullable
  public static String getLine(InfoToggle type, int fps, DataStorage data, Set<InfoToggle> addedTypes)
  {
    Minecraft mc = Minecraft.getInstance();
    Level level = mc.level;
    LocalPlayer player = mc.player;
    double x = Objects.requireNonNull(player).getX(), y = player.getY(), z = player.getZ();
    double dx = x - player.xOld, dy = y - player.yOld, dz = z - player.zOld;
    BlockPos pos = new BlockPos(x, y, z);
    ChunkPos chunkPos = new ChunkPos(pos);

    return switch(type)
    {
      case FPS -> line(Formats.FPS_FORMAT, va("fps", fps));

      case MEMORY_USAGE ->
      {
        long memMax = Runtime.getRuntime().maxMemory();
        long memTotal = Runtime.getRuntime().totalMemory();
        long memFree = Runtime.getRuntime().freeMemory();
        long memUsed = memTotal - memFree;
        yield line(Formats.MEMORY_USAGE_FORMAT,
          va("pused", "2d", memUsed * 100L / memMax),
          va("pallocated", "2d", memTotal * 100L / memMax),
          va("used", "3d", MiscUtils.bytesToMb(memUsed)),
          va("max", "3d", MiscUtils.bytesToMb(memMax)),
          va("total", "3d", MiscUtils.bytesToMb(memTotal)));
      }

      case TIME_REAL -> line(Formats.TIME_REAL_FORMAT, va("time", "tk", new Date(System.currentTimeMillis())));

      case TIME_WORLD -> line(Formats.TIME_WORLD_FORMAT,
        va("time", "5d", Objects.requireNonNull(level).getDayTime()),
        va("total", level.getGameTime()));

      case TIME_WORLD_FORMATTED ->
      {
        long timeDay = Objects.requireNonNull(level).getDayTime();
        long day = timeDay / 24000;
        int dayTicks = (int)(timeDay % 24000);
        yield line(Formats.TIME_WORLD_FORMATTED_FORMAT,
          va("day", day),
          va("day_1", day + 1),
          va("hour", "02d", (dayTicks / 1000 + 6) % 24),
          va("min", "02d", (int)(dayTicks * 0.06) % 60),
          va("sec", "02d", (int)(dayTicks * 3.6) % 60));
      }

      case TIME_DAY_MODULO ->
      {
        int mod = Configs.Generic.TIME_DAY_DIVISOR.getIntegerValue();
        yield line(Formats.TIME_DAY_MODULO_FORMAT,
          va("mod", mod),
          va("time", "5d", Objects.requireNonNull(level).getDayTime() % mod));
      }

      case TIME_TOTAL_MODULO ->
      {
        int mod1 = Configs.Generic.TIME_TOTAL_DIVISOR.getIntegerValue();
        yield line(Formats.TIME_TOTAL_MODULO_FORMAT,
          va("mod", mod1),
          va("time", "5d", Objects.requireNonNull(level).getGameTime() % mod1));
      }

      case SERVER_TPS ->
      {
        if(mc.hasSingleplayerServer() && Objects.requireNonNull(mc.getSingleplayerServer()).getTickCount() % 10 == 0)
          data.updateIntegratedServerTPS();

        if(!data.hasTPSData())
          yield line(Formats.SERVER_TPS_NULL_FORMAT);

        double tps = data.getServerTPS();
        double mspt = data.getServerMSPT();
        String preTps = tps >= 20d ? GuiBase.TXT_GREEN : GuiBase.TXT_RED;

        if(data.isCarpetServer() || mc.isLocalServer())
        {
          String preMspt = mspt <= 40 ? GuiBase.TXT_GREEN :
            mspt <= 45 ? GuiBase.TXT_YELLOW :
            mspt <= 50 ? GuiBase.TXT_GOLD :
            GuiBase.TXT_RED;
          yield line(Formats.SERVER_TPS_CARPET_FORMAT,
            va("preTps", preTps),
            va("tps", ".1f", tps),
            va("rst", GuiBase.TXT_RST),
            va("preMspt", preMspt),
            va("mspt", ".1f", mspt));
        }

        String preMspt = mspt <= 51 ? GuiBase.TXT_GREEN : GuiBase.TXT_RED;
        yield line(Formats.SERVER_TPS_VANILLA_FORMAT,
          va("preTps", preTps),
          va("tps", ".1f", tps),
          va("rst", GuiBase.TXT_RST),
          va("preMspt", preMspt),
          va("mspt", ".1f", mspt));
      }

      case PING ->
      {
        PlayerInfo info = player.connection.getPlayerInfo(player.getUUID());
        yield info == null ? null : line(Formats.PING_FORMAT, va("ping", info.getLatency()));
      }

      case COORDINATES_SCALED -> "TODO - will be merged with the below case";

      case COORDINATES, DIMENSION ->
      {
        if(addedTypes.contains(InfoToggle.COORDINATES) || addedTypes.contains(InfoToggle.DIMENSION))
          yield null;

        StringBuilder str = new StringBuilder(128);
        str.append("[");
        if(InfoToggle.COORDINATES.getBooleanValue())
        {
          str.append(line(Formats.COORDINATES_FORMAT,
            va("x", ".2f", x),
            va("y", ".4f", y),
            va("z", ".2f", z)));
          addedTypes.add(InfoToggle.COORDINATES);
        }
        if(InfoToggle.DIMENSION.getBooleanValue())
        {
          if(InfoToggle.COORDINATES.getBooleanValue())
            str.append(",");
          str.append(line(Formats.DIMENSION_FORMAT, va("dim", Objects.requireNonNull(level).dimension().location().toString())));
          addedTypes.add(InfoToggle.DIMENSION);
        }
        yield str.append("]").toString();
      }

      case BLOCK_POS, CHUNK_POS, REGION_FILE ->
      {
        if(addedTypes.contains(InfoToggle.BLOCK_POS) || addedTypes.contains(InfoToggle.CHUNK_POS) || addedTypes.contains(InfoToggle.REGION_FILE))
          yield null;

        StringBuilder str1 = new StringBuilder(256);
        str1.append("[");
        boolean hasOther = InfoToggle.BLOCK_POS.getBooleanValue();
        if(hasOther)
        {
          str1.append(line(Formats.BLOCK_POS_FORMAT,
            va("x", pos.getX()),
            va("y", pos.getY()),
            va("z", pos.getZ())));
          addedTypes.add(InfoToggle.BLOCK_POS);
        }
        if(InfoToggle.CHUNK_POS.getBooleanValue())
        {
          if(hasOther)
            str1.append(",");
          hasOther = true;

          str1.append(line(Formats.CHUNK_POS_FORMAT,
            va("x", chunkPos.x),
            va("y", pos.getY() >> 4),
            va("z", chunkPos.z)));
          addedTypes.add(InfoToggle.CHUNK_POS);
        }
        if(InfoToggle.REGION_FILE.getBooleanValue())
        {
          if(hasOther)
            str1.append(",");
          str1.append(line(Formats.REGION_FILE_FORMAT,
            va("x", pos.getX() >> 9),
            va("z", pos.getZ() >> 9)));
          addedTypes.add(InfoToggle.REGION_FILE);
        }
        yield str1.append("]").toString();
      }

      case BLOCK_IN_CHUNK -> line(Formats.BLOCK_IN_CHUNK_FORMAT,
        va("x", pos.getX() & 0xf),
        va("y", pos.getY() & 0xf),
        va("z", pos.getZ() & 0xf),
        va("cx", chunkPos.x),
        va("cy", pos.getY() >> 4),
        va("cz", chunkPos.z));

      case BLOCK_BREAK_SPEED -> line(Formats.BLOCK_BREAK_SPEED_FORMAT, va("bbs", ".2f", data.getBlockBreakingSpeed()));

      case DISTANCE ->
      {
        Vec3 ref = data.getDistanceReferencePoint();
        yield line(Formats.DISTANCE_FORMAT,
          va("d", ".2f", Math.sqrt(ref.distanceToSqr(x, y, z))),
          va("dx", ".2f", x - ref.x),
          va("dy", ".2f", y - ref.y),
          va("dz", ".2f", z - ref.z),
          va("rx", ".2f", ref.x),
          va("ry", ".2f", ref.y),
          va("rz", ".2f", ref.z));
      }

      case FACING ->
      {
        Direction facing = player.getDirection();
        String str2 = switch(facing)
        {
          case NORTH -> Formats.FACING_PZ_FORMAT.getStringValue();
          case SOUTH -> Formats.FACING_NZ_FORMAT.getStringValue();
          case EAST -> Formats.FACING_PX_FORMAT.getStringValue();
          case WEST -> Formats.FACING_NX_FORMAT.getStringValue();
          default -> "Invalid direction";
        };
        yield line(Formats.FACING_FORMAT,
          va("dir", facing.toString()),
          va("coord", str2)
        );
      }

      case LIGHT_LEVEL ->
      {
        if(accessor.getChunkPublic(chunkPos).isEmpty())
          yield null;

        LevelLightEngine lightEngine = Objects.requireNonNull(level).getChunkSource().getLightEngine();
        yield line(Formats.LIGHT_LEVEL_CLIENT_FORMAT,
          va("light", lightEngine.getRawBrightness(pos, 0)),
          va("block", lightEngine.getLayerListener(LightLayer.BLOCK).getLightValue(pos)),
          va("sky", lightEngine.getLayerListener(LightLayer.SKY).getLightValue(pos)));
      }

      case BEE_COUNT ->
      {
        Level bestLevel = WorldUtils.getBestWorld(mc);
        BlockEntity be = accessor.getTargetedBlockEntityPublic(bestLevel, mc);
        yield be instanceof BeehiveBlockEntity bbe ?
          line(Formats.BEE_COUNT_FORMAT, va("bees", bbe.getOccupantCount())) : null;
      }

      case FURNACE_XP ->
      {
        Level bestLevel = WorldUtils.getBestWorld(mc);
        BlockEntity be = accessor.getTargetedBlockEntityPublic(bestLevel, mc);
        if (be instanceof AbstractFurnaceBlockEntity furnace)
          yield line(Formats.FURNACE_XP_FORMAT, va("xp", MiscUtils.getFurnaceXpAmount(furnace)));
        else
          yield null;
      }

      case HONEY_LEVEL ->
      {
        BlockState state = accessor.getTargetedBlockPublic(mc);
        yield state != null && state.getBlock() instanceof BeehiveBlock ?
          line(Formats.HONEY_LEVEL_FORMAT, va("honey", BeehiveBlockEntity.getHoneyLevel(state))) : null;
      }

      case ROTATION_YAW, ROTATION_PITCH, SPEED ->
      {
        if(addedTypes.contains(InfoToggle.ROTATION_YAW) || addedTypes.contains(InfoToggle.ROTATION_PITCH) || addedTypes.contains(InfoToggle.SPEED))
          yield null;

        StringBuilder str3 = new StringBuilder(128);
        str3.append("[");
        boolean hasOther1 = InfoToggle.ROTATION_YAW.getBooleanValue();
        if(hasOther1)
        {
          str3.append(line(Formats.ROTATION_YAW_FORMAT, va("yaw", Mth.wrapDegrees(player.getYRot()))));
          addedTypes.add(InfoToggle.ROTATION_YAW);
        }
        if(InfoToggle.ROTATION_PITCH.getBooleanValue())
        {
          if(hasOther1)
            str3.append(",");
          hasOther1 = true;
          str3.append(line(Formats.ROTATION_PITCH_FORMAT, va("pitch", Mth.wrapDegrees(player.getXRot()))));
          addedTypes.add(InfoToggle.ROTATION_PITCH);
        }
        if(InfoToggle.SPEED.getBooleanValue())
        {
          if(hasOther1)
            str3.append(",");
          double dist1 = Math.sqrt(dx*dx + dy*dy + dz*dz);
          str3.append(line(Formats.SPEED_FORMAT, va("speed", dist1 * 20)));
          addedTypes.add(InfoToggle.SPEED);
        }
        yield str3.append("]").toString();
      }

      case SPEED_HV ->
        line(Formats.SPEED_HV_FORMAT,
          va("xz", ".3f", Math.sqrt(dx*dx + dz*dz) * 20.0),
          va("y", ".3f", dy * 20.0));

      case SPEED_AXIS -> line(Formats.SPEED_AXIS_FORMAT,
        va("x", dx * 20),
        va("y", dy * 20),
        va("z", dz * 20));

      case HORSE_SPEED, HORSE_JUMP -> {
        if (addedTypes.contains(InfoToggle.HORSE_SPEED) || addedTypes.contains(InfoToggle.HORSE_JUMP))
          yield null;

        Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof AbstractHorse horse))
          yield null;

        if (horse.isSaddled())
        {
          if (InfoToggle.HORSE_SPEED.getBooleanValue())
          {
            float horseSpeed = horse.getSpeed();
            horseSpeed *= 42.163F;
            line(Formats.HORSE_SPEED_FORMAT, va("speed", ".3f", horseSpeed)); // TODO - requires addLine
          }

          if (InfoToggle.HORSE_JUMP.getBooleanValue())
          {
            double jump = horse.getCustomJump();
            double calculatedJumpHeight = -0.1817584952 * jump*jump*jump + 3.689713992 * jump*jump + 2.128599134 * jump - 0.343930367;
            line(Formats.HORSE_JUMP_FORMAT, va("speed", ".3f", calculatedJumpHeight)); // TODO - requires addLine
          }

          addedTypes.add(InfoToggle.HORSE_SPEED);
          addedTypes.add(InfoToggle.HORSE_JUMP);
        }
        yield null;
      }

      case CHUNK_SECTIONS -> line(Formats.CHUNK_SECTIONS_FORMAT, va("c", ((IMixinWorldRenderer)mc.levelRenderer).getRenderedChunksInvoker()));

      case CHUNK_SECTIONS_FULL -> line(Formats.CHUNK_SECTIONS_FULL_FORMAT, va("c", mc.levelRenderer.getChunkStatistics()));

      case CHUNK_UPDATES -> "TODO"; // minihud parity

      case LOADED_CHUNKS_COUNT ->
      {
        String chunksClient = Objects.requireNonNull(level).gatherChunkSourceStats();
        Level levelServer = WorldUtils.getBestWorld(mc);
        if(levelServer == null || levelServer != level)
          yield line(Formats.LOADED_CHUNKS_COUNT_CLIENT_FORMAT, va("client", chunksClient));

        ServerChunkCache cache = (ServerChunkCache)levelServer.getChunkSource();
        yield line(Formats.LOADED_CHUNKS_COUNT_SERVER_FORMAT,
                va("chunks", cache.getLoadedChunksCount()),
                va("total", cache.getTickingGenerated()),
                va("client", chunksClient));
      }

      case PARTICLE_COUNT -> line(Formats.PARTICLE_COUNT_FORMAT, va("p", mc.particleEngine.countParticles()));

      case DIFFICULTY ->
      {
        ChunkAccess serverChunk = Objects.requireNonNull(level).getChunk(chunkPos.getWorldPosition());

        float moonPhaseFactor = level.getMoonBrightness();
        long chunkInhabitedTime = serverChunk.getInhabitedTime();
        DifficultyInstance diff = new DifficultyInstance(level.getDifficulty(), level.getDayTime(), chunkInhabitedTime, moonPhaseFactor);
        yield line(Formats.DIFFICULTY_FORMAT,
          va("local", ".2f", diff.getEffectiveDifficulty()),
          va("clamped", ".2f", diff.getSpecialMultiplier()),
          va("day", level.getDayTime() / 24000L)
        );
      }

      case BIOME ->
      {
        if (accessor.getChunkPublic(chunkPos).isEmpty())
          yield null;

        Biome biome = Objects.requireNonNull(level).getBiome(pos);
        ResourceLocation resourceLocation = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);
        yield line(Formats.BIOME_FORMAT, va("biome", StringUtils.translate("biome." + Objects.requireNonNull(resourceLocation).toString().replace(":", "."))));
      }

      case BIOME_REG_NAME ->
      {
        if (accessor.getChunkPublic(chunkPos).isEmpty())
          yield null;

        Biome biome = Objects.requireNonNull(level).getBiome(pos);
        ResourceLocation resourceLocation = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(biome);
        yield line(Formats.BIOME_REG_NAME_FORMAT, va("name", resourceLocation != null ? resourceLocation.toString() : "?"));
      }

      case TILE_ENTITIES -> "TODO";

      case ENTITIES_CLIENT_WORLD, ENTITIES ->
      {
        if (addedTypes.contains(InfoToggle.ENTITIES_CLIENT_WORLD) || addedTypes.contains(InfoToggle.ENTITIES))
          yield null;

        StringBuilder str4 = new StringBuilder(128);
        str4.append("[");
        boolean hasOther2 = InfoToggle.ENTITIES_CLIENT_WORLD.getBooleanValue();
        if (hasOther2)
        {
          str4.append(line(Formats.ENTITIES_CLIENT_FORMAT, va("e", Objects.requireNonNull(mc.level).getEntityCount())));
          addedTypes.add(InfoToggle.ENTITIES_CLIENT_WORLD);
        }
        if (InfoToggle.ENTITIES.getBooleanValue() && mc.hasSingleplayerServer())
        {
          Level world = WorldUtils.getBestWorld(mc);
          if (world instanceof ServerLevel)
          {
            if (hasOther2)
              str4.append(",");
            str4.append(line(Formats.ENTITIES_SERVER_FORMAT, va("e", ((IServerEntityManager) ((IMixinServerWorld) world).minihud_getEntityManager()).getIndexSize())));
            addedTypes.add(InfoToggle.ENTITIES);
            hasOther2 = true;
          }
        }
        if (hasOther2)
          yield str4.append("]").toString();
        else
          yield null;
      }

      case SLIME_CHUNK ->
      {
        if (!MiscUtils.isOverworld(Objects.requireNonNull(level)))
          yield null;

        if (data.isWorldSeedKnown(level))
        {
          long seed = data.getWorldSeed(level);
          yield line(Formats.SLIME_CHUNK_FORMAT,
            va("result", MiscUtils.canSlimeSpawnAt(pos.getX(), pos.getZ(), seed) ? Formats.SLIME_CHUNK_YES_FORMAT.getStringValue() : Formats.SLIME_CHUNK_NO_FORMAT.getStringValue())
          );
        }
        else
          yield line(Formats.SLIME_CHUNK_FORMAT, va("result", Formats.SLIME_CHUNK_NO_SEED_FORMAT.getStringValue()));
      }

      case LOOKING_AT_ENTITY ->
      {
        if (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.ENTITY)
          yield null;

        Entity lookedEntity = mc.crosshairPickEntity;
        if (lookedEntity instanceof LivingEntity living)
          yield line(Formats.LOOKING_AT_ENTITY_LIVING_FORMAT,
            va("entity", lookedEntity.getName().getString()),
            va("hp", living.getHealth()),
            va("maxhp", living.getMaxHealth())
          );
        else
          yield line(Formats.LOOKING_AT_ENTITY_LIVING_FORMAT, va("entity", Objects.requireNonNull(lookedEntity).getName().getString()));
      }

      case ENTITY_REG_NAME ->
      {
        if (mc.hitResult == null || mc.hitResult.getType() != HitResult.Type.ENTITY)
          yield null;

        Entity lookedEntity = ((EntityHitResult) mc.hitResult).getEntity();
        ResourceLocation resourceLocation = EntityType.getKey(lookedEntity.getType());
        yield line(Formats.ENTITY_REG_NAME_FORMAT, va("name", resourceLocation.toString()));
      }

      case LOOKING_AT_BLOCK, LOOKING_AT_BLOCK_CHUNK ->
      {
        if (addedTypes.contains(InfoToggle.LOOKING_AT_BLOCK) || addedTypes.contains(InfoToggle.LOOKING_AT_BLOCK_CHUNK))
          yield null;

        if (mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK)
        {
          BlockPos lookPos = ((BlockHitResult)mc.hitResult).getBlockPos();
          StringBuilder str5 = new StringBuilder(128);
          str5.append("[");
          boolean hasOther3 = InfoToggle.LOOKING_AT_BLOCK.getBooleanValue();
          if (hasOther3)
          {
            str5.append(line(Formats.LOOKING_AT_BLOCK_FORMAT,
              va("x", lookPos.getX()),
              va("y", lookPos.getY()),
              va("z", lookPos.getZ())
            ));
            addedTypes.add(InfoToggle.LOOKING_AT_BLOCK);
          }

          if (InfoToggle.LOOKING_AT_BLOCK_CHUNK.getBooleanValue())
          {
            if (hasOther3) str5.append(",");
            str5.append(line(Formats.LOOKING_AT_BLOCK_CHUNK_FORMAT,
              va("x", lookPos.getX()),
              va("y", lookPos.getY()),
              va("z", lookPos.getZ()),
              va("cx", lookPos.getX() >> 4),
              va("cy", lookPos.getY() >> 4),
              va("cz", lookPos.getZ() >> 4)
            ));
            addedTypes.add(InfoToggle.LOOKING_AT_BLOCK_CHUNK);
          }
          yield str5.append("]").toString();
        }
        else
          yield null;
      }

      case BLOCK_PROPS -> "TODO";
    };
  }

  private static void getBlockProperties(Minecraft mc)
  {
    if (mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK)
    {
      BlockPos posLooking = ((BlockHitResult)mc.hitResult).getBlockPos();
      BlockState state = Objects.requireNonNull(mc.level).getBlockState(posLooking);
      ResourceLocation resourceLocation = Registry.BLOCK.getKey(state.getBlock());

      line(Formats.BLOCK_PROPS_HEADING_FORMAT, va("name", resourceLocation.toString())); // TODO - requires addLine
      String separator = line(Formats.BLOCK_PROPS_SEPARATOR_FORMAT);

      Collection<net.minecraft.world.level.block.state.properties.Property<?>> properties = state.getProperties();
      if (properties.size() > 0)
        for (Property<?> property : properties)
        {
          Comparable<?> value = state.getValue(property);
          if (property instanceof BooleanProperty)
            line(value.equals(Boolean.TRUE) ? Formats.BLOCK_PROPS_BOOLEAN_TRUE_FORMAT : Formats.BLOCK_PROPS_BOOLEAN_FALSE_FORMAT,
              va("prop", property.getName()),
              va("separator", separator)
            ); // TODO - requires addLine
          else if (property instanceof DirectionProperty)
            line(Formats.BLOCK_PROPS_DIRECTION_FORMAT,
              va("prop", property.getName()),
              va("separator", separator),
              va("value", value.toString())
            ); // TODO - requires addLine
          else if (property instanceof IntegerProperty)
            line(Formats.BLOCK_PROPS_INT_FORMAT,
              va("prop", property.getName()),
              va("separator", separator),
              va("value", value.toString())
            ); // TODO - requires addLine
          else
            line(Formats.BLOCK_PROPS_STRING_FORMAT,
              va("prop", property.getName()),
              va("separator", separator),
              va("value", value.toString())
            ); // TODO - requires addLine
        }
    }
  }
  
}
