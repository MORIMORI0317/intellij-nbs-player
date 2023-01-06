package net.morimori0317.inp.editor.actions;

import com.intellij.DynamicBundle;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import net.morimori0317.inp.player.NBSPlayer;
import org.jetbrains.annotations.NotNull;

public class StopAction extends AnAction implements DumbAware {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        NBSPlayer nbsPlayer = NBSPlayer.DATA_KEY.getData(e.getDataContext());
        if (nbsPlayer == null) return;

        nbsPlayer.setPlayState(NBSPlayer.PlayState.STOP);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        NBSPlayer nbsPlayer = NBSPlayer.DATA_KEY.getData(e.getDataContext());
        if (nbsPlayer == null) return;

        e.getPresentation().setEnabled(nbsPlayer.isPlaying() || nbsPlayer.getTick() > 0);
    }
}
