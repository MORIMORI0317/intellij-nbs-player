package net.morimori0317.inp.player;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.project.Project;
import dev.felnull.fnnbs.Layer;
import dev.felnull.fnnbs.NBS;
import dev.felnull.fnnbs.Note;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class NBSPlayer implements Disposable {
    public static final DataKey<NBSPlayer> DATA_KEY = DataKey.create(NBSPlayer.class.getName());
    private final Project project;
    private final NBS nbs;
    private final AtomicBoolean playing = new AtomicBoolean();
    private final AtomicInteger tick = new AtomicInteger();
    private final AtomicBoolean loop = new AtomicBoolean();

    public NBSPlayer(@NotNull Project project, @Nullable NBS nbs) {
        this.project = project;
        this.nbs = nbs;
    }

    public boolean isLoop() {
        return loop.get();
    }

    public void setLoop(boolean loop) {
        this.loop.set(loop);
    }

    public boolean isPlaying() {
        return playing.get();
    }

    public void setTick(int tick) {
        this.tick.set(tick);
    }

    public void setPlay(boolean play) {
        boolean pre = playing.get();
        playing.set(play);
        if (!pre && playing.get())
            playStart();
    }

    private void playStart() {
        var player = NBSPlayerService.getInstance(project);
        if (playing.get())
            CompletableFuture.runAsync(this::playAsync, player.getPlayerExecutorService());
    }

    private void playAsync() {
        if (nbs == null)
            return;

        while (nbs.getSongLength() > tick.get()) {
            try {
                long tickSpeed = (long) (1000f / ((float) nbs.getTempo() / 100f));
                Thread.sleep(tickSpeed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            playTick(tick.getAndIncrement());

            if (!playing.get())
                return;
        }


        if (!loop.get()) {
            tick.set(0);
            playing.set(false);
        } else {
            tick.set(loopStartTick());
            playStart();
        }
    }

    private int loopStartTick() {
        if (nbs != null && nbs.isLoop())
            return nbs.getLoopStart();
        return 0;
    }

    private void playTick(int tick) {
        if (tick < 0 || nbs == null || nbs.getSongLength() <= tick)
            return;

        NBSPlayerService playerService = NBSPlayerService.getInstance(project);

        int layerct = nbs.getActualLayerCount();
        for (int i = 0; i < layerct; i++) {
            Layer layer = nbs.getLayer(i);
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
