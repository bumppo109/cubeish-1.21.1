package com.bumppo109.cubeish.block.shapes.weathering;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringWallBlock extends WallBlock implements WeatheringCopper {
    public static final MapCodec<WallBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Properties.CODEC.fieldOf("properties").forGetter(block -> {
                        throw new UnsupportedOperationException();
                    }),
                    WeatherState.CODEC.fieldOf("weathering_state")
                            .forGetter(block -> ((WeatheringWallBlock) block).getAge())
            ).apply(instance, WeatheringWallBlock::new)
    );

    private final WeatherState weatherState;

    @Override
    public MapCodec<WallBlock> codec() {
        return CODEC;
    }

    public WeatheringWallBlock(Properties properties, WeatherState weatherState) {
        super(properties);
        this.weatherState = weatherState;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
