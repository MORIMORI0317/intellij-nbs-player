package net.morimori0317.inp.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import org.jetbrains.annotations.NotNull;

public class NBSEditorLocation implements FileEditorLocation {
    private final NBSFileEditor nbsFileEditor;

    public NBSEditorLocation(@NotNull NBSFileEditor nbsFileEditor) {
        this.nbsFileEditor = nbsFileEditor;
    }

    @Override
    public @NotNull FileEditor getEditor() {
        return this.nbsFileEditor;
    }

    @Override
    public int compareTo(@NotNull FileEditorLocation o) {
        return 1;
    }
}
