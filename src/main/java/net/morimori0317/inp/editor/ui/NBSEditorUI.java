package net.morimori0317.inp.editor.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.SideBorder;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.util.ui.JBUI;
import net.morimori0317.inp.editor.actions.NBSEditorActions;
import net.morimori0317.inp.nbs.NBSLoadResult;
import net.morimori0317.inp.player.NBSPlayer;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
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

        JComponent toolbarComp = actionToolbar.getComponent();

        PlayTimePane playTimePane = new PlayTimePane();

        JPanel topPanel = new NonOpaquePanel(new BorderLayout());
        topPanel.add(toolbarComp, BorderLayout.WEST);
        topPanel.add(playTimePane, BorderLayout.EAST);
        topPanel.setBorder(IdeBorderFactory.createBorder(SideBorder.BOTTOM));

        add(topPanel, BorderLayout.NORTH);

        if (nbsLoadResult.getNBS() != null) {
            this.linePanel = new NBSLinePanel(project, nbsLoadResult.getNBS(), nbsPlayer);
            add(linePanel, BorderLayout.CENTER);
        }

        nbsPlayer.setProgressListener(prgrs -> {
            if (linePanel != null)
                linePanel.onNBSPlayProgress(prgrs);
        });

        nbsPlayer.setPlayStateListener(playState -> {
            if (linePanel != null)
                linePanel.onNBSPlayChange(playState);
        });
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

    private class PlayTimePane extends JPanel {
        private PlayTimePane() {
            super(new GridLayout(1, 2));

            JBLabel timerLabel = new JBLabel("00:00:00;000 / 00:00:00;000");
            timerLabel.setBorder(new CompoundBorder(IdeBorderFactory.createRoundedBorder(), IdeBorderFactory.createEmptyBorder(JBUI.insets(2))));
            add(timerLabel);

            setBorder(IdeBorderFactory.createEmptyBorder(JBUI.insets(2)));
        }
    }
}
