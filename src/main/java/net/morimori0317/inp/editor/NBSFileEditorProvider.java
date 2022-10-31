package net.morimori0317.inp.editor;

import com.intellij.openapi.fileEditor.AsyncFileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import net.morimori0317.inp.NBSFileType;
import net.morimori0317.inp.nbs.NBSLoadResult;
import net.morimori0317.inp.nbs.NBSUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class NBSFileEditorProvider implements AsyncFileEditorProvider, DumbAware {
    private static final String EDITOR_TYPE_ID = "nbs-editor";

    @Override
    public @NotNull Builder createEditorAsync(@NotNull Project project, @NotNull VirtualFile file) {
        NBSLoadResult nbsLoadResult = NBSUtils.load(file);
        return new Builder() {
            @Override
            public FileEditor build() {
                return new NBSFileEditor(project, file, nbsLoadResult);
            }
        };
    }

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return FileTypeRegistry.getInstance().isFileOfType(file, NBSFileType.INSTANCE);
    }

    @Override
    public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        return createEditorAsync(project, file).build();
    }

    @Override
    public @NotNull @NonNls String getEditorTypeId() {
        return EDITOR_TYPE_ID;
    }

    @Override
    public @NotNull FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }
}
