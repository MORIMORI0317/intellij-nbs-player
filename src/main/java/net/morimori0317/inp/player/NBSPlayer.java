package net.morimori0317.inp.player;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.project.Project;
import net.morimori0317.inp.nbs.Layer;
import net.morimori0317.inp.nbs.NBS;
import net.morimori0317.inp.nbs.Note;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class NBSPlayer implements Disposable {
    public static final DataKey<NBSPlayer> DATA_KEY = DataKey.create(NBSPlayer.class.getName());
    private final Project project;
    private final NBS nbs;
    private AtomicBoolean playing = new AtomicBoolean();
    private int tick;

    public NBSPlayer(@NotNull Project project, @Nullable NBS nbs) {
        this.project = project;
        this.nbs = nbs;
    }

    public void setPlay(boolean play) {
        playing.set(play);
        if (playing.get())
            playStart();
    }

    private void playStart() {
        var player = NBSPlayerService.getInstance(project);

        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < nbs.getSongLength(); i++) {
                try {
                    long tickSpeed = (long) (1000f / ((float) nbs.getTempo() / 100f));
                    Thread.sleep(tickSpeed);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                playTick(i);
                if (!playing.get())
                    break;
            }
        }, player.getPlayerExecutorService());
    }

    private void playTick(int tick) {
        if (tick < 0 || nbs == null || nbs.getSongLength() <= tick)
            return;

        NBSPlayerService playerService = NBSPlayerService.getInstance(project);

        List<Layer> layers = nbs.getLayers();
        for (Layer layer : layers) {
            Note note = layer.getNote(tick);
            if (note != null)
                playerService.play(nbs, layer, note, false);
        }
    }

    @Override
    public void dispose() {
        setPlay(false);
    }
}
