package net.morimori0317.inp.nbs;

import dev.felnull.fnnbs.NBS;
import org.jetbrains.annotations.Nullable;

public class NBSLoadResult {
    private final NBS nbs;
    private final String error;

    public NBSLoadResult(NBS nbs, String error) {
        this.nbs = nbs;
        this.error = error;
    }

    @Nullable
    public NBS getNBS() {
        return nbs;
    }

    @Nullable
    public String getError() {
        return error;
    }
}
