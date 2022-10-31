package net.morimori0317.inp.nbs;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class NBSUtils {
    public static int getVersion(InputStream stream) throws IOException {
        if (NBS.readShort(stream) != 0)
            return 0;
        return stream.read();
    }

    @NotNull
    public static NBSLoadResult load(VirtualFile virtualFile) {
        try {
            try (InputStream stream = new BufferedInputStream(virtualFile.getInputStream())) {
                int v = getVersion(stream);
                if (v > 5 || v < 0)
                    return new NBSLoadResult(null, "Unsupported NBS version");
            }
            NBS nbs;
            try (InputStream stream = new BufferedInputStream(virtualFile.getInputStream())) {
                nbs = new NBS(stream);
            }
            return new NBSLoadResult(nbs, null);
        } catch (IOException e) {
            return new NBSLoadResult(null, "Failed to load NBS file");
        }
    }
}
