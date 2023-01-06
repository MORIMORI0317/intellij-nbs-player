package net.morimori0317.inp;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public final class INPBundle extends DynamicBundle {
    private static final INPBundle INSTANCE = new INPBundle();
    private static final String BUNDLE = "messages.INPBundle";

    private INPBundle() {
        super(BUNDLE);
    }

    public static @Nls @NotNull String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }

    public static @NotNull Supplier<@Nls String> messagePointer(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object @NotNull ... params) {
        return INSTANCE.getLazyMessage(key, params);
    }
}
