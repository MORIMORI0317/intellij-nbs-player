package net.morimori0317.inp.editor.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.panels.NonOpaquePanel;
import net.morimori0317.inp.editor.actions.NBSEditorActions;
import net.morimori0317.inp.nbs.NBSLoadResult;
import net.morimori0317.inp.player.NBSPlayer;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class NBSEditorUI extends JPanel implements Disposable, DataProvider {
    private final Project project;
    private final VirtualFile virtualFile;
    private final NBSLoadResult nbsLoadResult;
    private final NBSPlayer nbsPlayer;
    private NBSLinePanel linePanel;

    public NBSEditorUI(@NotNull Project project, @NotNull VirtualFile virtualFile, @NotNull NBSLoadResult nbsLoadResult, @NotNull NBSPlayer nbsPlayer) {
        this.project = project;
        this.virtualFile = virtualFile;
        this.nbsLoadResult = nbsLoadResult;
        this.nbsPlayer = nbsPlayer;

        setLayout(new BorderLayout());

        ActionManager actionManager = ActionManager.getInstance();
        ActionGroup toolbarActions = (ActionGroup) actionManager.getAction(NBSEditorActions.GROUP_TOOLBAR);
        ActionToolbar actionToolbar = actionManager.createActionToolbar(NBSEditorActions.ACTION_PLACE, toolbarActions, true);
        actionToolbar.setTargetComponent(this);

        JComponent toolbarPanel = actionToolbar.getComponent();

        JPanel topPanel = new NonOpaquePanel(new BorderLayout());
        topPanel.add(toolbarPanel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);

        if (nbsLoadResult.getNBS() != null) {
            this.linePanel = new NBSLinePanel(project, nbsLoadResult.getNBS());
            add(linePanel, BorderLayout.CENTER);
        }
    }

    @Override
    public void dispose() {
        if (linePanel != null)
            Disposer.dispose(linePanel);
    }

    @Override
    public @Nullable Object getData(@NotNull @NonNls String dataId) {
        if (CommonDataKeys.PROJECT.is(dataId))
            return project;

        if (CommonDataKeys.VIRTUAL_FILE.is(dataId))
            return virtualFile;

        if (CommonDataKeys.VIRTUAL_FILE_ARRAY.is(dataId))
            return new VirtualFile[]{virtualFile};

        return NBSPlayer.DATA_KEY.is(dataId) ? nbsPlayer : null;
    }
}
