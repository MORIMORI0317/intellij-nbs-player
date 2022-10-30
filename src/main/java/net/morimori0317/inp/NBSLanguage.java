package net.morimori0317.inp;

import com.intellij.lang.Language;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NotNull;

public class NBSLanguage extends Language {
    public static final NBSLanguage INSTANCE = new NBSLanguage();

    private NBSLanguage() {
        super("NBS");
    }

    @Override
    public @NotNull @NlsSafe String getDisplayName() {
        return "Note block song";
    }
}
