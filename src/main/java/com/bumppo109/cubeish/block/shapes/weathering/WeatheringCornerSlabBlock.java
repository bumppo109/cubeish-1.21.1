package com.bumppo109.cubeish.block.shapes.weathering;

import com.bumppo109.cubeish.block.shapes.base.CornerSlabBlock;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class WeatheringCornerSlabBlock extends CornerSlabBlock implements WeatheringCopper {
    public static final MapCodec<WeatheringCornerSlabBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Properties.CODEC.fieldOf("properties").forGetter(block -> {
                        throw new UnsupportedOperationException();
                    }), WeatherState.CODEC.fieldOf("weathering_state").forGetter(WeatheringCopper::getAge)
            ).apply(instance, WeatheringCornerSlabBlock::new)
    );

    private final WeatherState weatherState;

    @Override
    public MapCodec<WeatheringCornerSlabBlock> codec() {
        return CODEC;
    }

    public WeatheringCornerSlabBlock(Properties properties, WeatherState weatherState) {
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
