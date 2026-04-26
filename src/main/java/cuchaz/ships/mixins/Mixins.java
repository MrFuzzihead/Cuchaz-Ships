package cuchaz.ships.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

/**
 * Early mixin registrations. All game-phase mixins are registered via mixins.cuchaz.ships.json instead.
 * This enum is kept empty to avoid double-registration with the JSON config.
 */
public enum Mixins implements IMixins {

    ;

    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder;
    }

    @Nonnull
    @Override
    public MixinBuilder getBuilder() {
        return builder;
    }
}
