package net.morimori0317.inp;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.Nls;
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
    public @Nls @NotNull String getDisplayName() {
        return INPBundle.message("filetype.nbs.name");
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return INPBundle.message("filetype.nbs.description");
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
