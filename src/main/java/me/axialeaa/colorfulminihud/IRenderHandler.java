package me.axialeaa.colorfulminihud;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public interface IRenderHandler
{
  public LevelChunk getChunkPublic(ChunkPos pos);
  public BlockEntity getTargetedBlockEntityPublic(Level level, Minecraft mc);
  public BlockState getTargetedBlockPublic(Minecraft mc);
}
