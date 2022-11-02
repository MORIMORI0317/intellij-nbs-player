package net.morimori0317.inp.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import net.morimori0317.inp.editor.ui.NBSEditorUI;
import net.morimori0317.inp.nbs.NBSLoadResult;
import net.morimori0317.inp.player.NBSPlayer;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

public class NBSFileEditor extends UserDataHolderBase implements FileEditor {
    public static final String EDITOR_NAME = "NBS Editor";
    private final Project myProject;
    private final VirtualFile myFile;
    private final NBSLoadResult nbsLoadResult;
    private final FileEditorLocation editorLocation = new NBSEditorLocation(this);
    private final NBSPlayer nbsPlayer;
    private final NBSEditorUI myPanel;

    public NBSFileEditor(@NotNull Project project, @NotNull VirtualFile virtualFile, @NotNull NBSLoadResult nbsLoadResult) {
        this.myProject = project;
        this.myFile = virtualFile;
        this.nbsLoadResult = nbsLoadResult;
        this.nbsPlayer = new NBSPlayer(project, nbsLoadResult.getNBS());
        this.myPanel = new NBSEditorUI(project, virtualFile, nbsLoadResult, nbsPlayer);
    }

    @Override
    public @NotNull JComponent getComponent() {
        return myPanel;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return myPanel;
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) @NotNull String getName() {
        return EDITOR_NAME;
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Override
    public @Nullable FileEditorLocation getCurrentLocation() {
        return editorLocation;
    }

    @Override
    public void dispose() {
        Disposer.dispose(myPanel);
        Disposer.dispose(nbsPlayer);
    }

    @Override
    public @Nullable VirtualFile getFile() {
        return myFile;
    }
}
