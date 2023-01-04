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
import dev.felnull.fnnbs.NBS;
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

            nbsPlayer.setProgressListener(prgrs -> {
                linePanel.onNBSPlayProgress(prgrs);

                playTimePane.updateTime(prgrs);
            });

            nbsPlayer.setPlayStateListener(playState -> {
                linePanel.onNBSPlayChange(playState);

                if (playState == NBSPlayer.PlayState.STOP)
                    playTimePane.updateTime(0);
            });
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

    private class PlayTimePane extends JPanel {
        private final JBLabel timerLabel = new JBLabel();

        private PlayTimePane() {
            super(new GridLayout(1, 2));

            timerLabel.setBorder(new CompoundBorder(IdeBorderFactory.createRoundedBorder(), IdeBorderFactory.createEmptyBorder(JBUI.insets(2))));
            updateTime(0);
            add(timerLabel);

            setBorder(IdeBorderFactory.createEmptyBorder(JBUI.insets(2)));
        }

        private void updateTime(float progress) {
            NBS nbs = nbsLoadResult.getNBS();
            float totalSec = 0f;
            float compSec = 0f;

            if (nbs != null) {
                float tickSec = 1000f / ((float) nbs.getTempo() / 100f);
                totalSec = tickSec * nbs.getSongLength();
                compSec = tickSec * progress;
            }

            timerLabel.setText(String.format("%s / %s", timeStr(compSec), timeStr(totalSec)));
        }

        private String timeStr(float sec) {
            long hourTime = (long) sec / 3600000;
            long minTime = ((long) sec - hourTime * 3600000) / 60000;
            long secTime = ((long) sec - hourTime * 3600000 - minTime * 60000) / 1000;
            float milliSec = sec / 1000f;
            long milliTime = (long) ((milliSec - (long) milliSec) * 1000f);

            return String.format("%02d:%02d:%02d;%03d", hourTime, minTime, secTime, milliTime);
        }
    }
}
