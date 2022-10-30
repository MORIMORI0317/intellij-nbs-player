package net.morimori0317.inp;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class NBSFileType extends LanguageFileType {
    public static final NBSFileType INSTANCE = new NBSFileType();

    private NBSFileType() {
        super(NBSLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "NBS";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return "NBS description";
    }

    @Override
    public @NlsSafe @NotNull String getDefaultExtension() {
        return "nbs";
    }

    @Override
    public @Nullable Icon getIcon() {
        return INPIcons.NBS_FILE;
    }
}
