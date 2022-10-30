package net.morimori0317.inp.nbs;

import java.io.IOException;
import java.io.InputStream;

public class NBSUtils {
    public static int getVersion(InputStream stream) throws IOException {
        if (NBS.readShort(stream) != 0)
            return 0;
        return stream.read();
    }
}
