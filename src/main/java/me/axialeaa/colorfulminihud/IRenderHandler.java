package me.axialeaa.colorfulminihud;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public interface IRenderHandler
{
  LevelChunk getChunkPublic(ChunkPos pos);
  BlockEntity getTargetedBlockEntityPublic(Level level, Minecraft mc);
  BlockState getTargetedBlockPublic(Minecraft mc);
}
