package net.morimori0317.inp;

import com.intellij.testFramework.TestDataPath;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import net.morimori0317.inp.util.NBTUtils;

@TestDataPath("$CONTENT_ROOT/testData")
public class NBSTest extends BasePlatformTestCase {

    public void testVersion() {
        var nbsFile = myFixture.configureByFile("test_v5.nbs");
        int v = NBTUtils.getVersion(nbsFile.getVirtualFile());
        assertEquals(5, v);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testData/nbs";
    }
}
