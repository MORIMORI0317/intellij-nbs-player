package net.morimori0317.inp.editor.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import net.morimori0317.inp.player.NBSPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayAction extends AnAction implements DumbAware {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        NBSPlayer nbsPlayer = NBSPlayer.DATA_KEY.getData(e.getDataContext());
        if (nbsPlayer == null) return;

        nbsPlayer.setPlayState(NBSPlayer.PlayState.PLAY);

       /* var tsk = new Task.Backgroundable(e.getProject(), "TEST") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                try {
                    Thread.sleep(1000 * 30);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        var b = new BackgroundableProcessIndicator(tsk);
        b.setIndeterminate(false);

        ProgressManager.getInstance().runProcessWithProgressAsynchronously(tsk, b);*/

        //JBTerminalPanel
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        NBSPlayer nbsPlayer = NBSPlayer.DATA_KEY.getData(e.getDataContext());
        if (nbsPlayer == null) return;

        e.getPresentation().setEnabled(!nbsPlayer.isPlaying());
    }
}
