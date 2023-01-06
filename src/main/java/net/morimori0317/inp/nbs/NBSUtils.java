package net.morimori0317.inp.nbs;

import com.intellij.openapi.vfs.VirtualFile;
import dev.felnull.fnnbs.NBS;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NBSUtils {
    public static int getVersion(InputStream stream) throws IOException {
        if (readShort(stream) != 0)
            return 0;
        return stream.read();
    }

    private static int readShort(InputStream stream) throws IOException {
        return (stream.read()) + (stream.read() << 8);
    }

    @NotNull
    public static NBSLoadResult load(VirtualFile virtualFile) {
        try {
            try (InputStream stream = new BufferedInputStream(virtualFile.getInputStream())) {
                int v = getVersion(stream);
                if (v > 5 || v < 0)
                    return new NBSLoadResult(null, "Unsupported NBS version: " + v);
            }
            NBS nbs;
            try (InputStream stream = new BufferedInputStream(virtualFile.getInputStream())) {
                nbs = NBS.load(stream);
            }
            return new NBSLoadResult(nbs, null);
        } catch (Exception e) {
            return new NBSLoadResult(null, "Failed to load NBS file: " + e.getClass() + " (" + e.getLocalizedMessage() + ")");
        }
    }
}
