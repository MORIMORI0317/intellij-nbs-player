package net.morimori0317.inp.editor.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.DumbAware;
import net.morimori0317.inp.player.NBSPlayer;
import org.jetbrains.annotations.NotNull;

public class ToggleLoopAction extends ToggleAction implements DumbAware {
    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        NBSPlayer nbsPlayer = NBSPlayer.DATA_KEY.getData(e.getDataContext());
        if (nbsPlayer == null) return false;

        return nbsPlayer.isLoop();
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        NBSPlayer nbsPlayer = NBSPlayer.DATA_KEY.getData(e.getDataContext());
        if (nbsPlayer == null) return;

        nbsPlayer.setLoop(state);
    }
}
