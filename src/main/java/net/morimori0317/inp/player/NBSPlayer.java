package net.morimori0317.inp.player;


import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.project.Project;
import dev.felnull.fnnbs.Layer;
import dev.felnull.fnnbs.NBS;
import dev.felnull.fnnbs.Note;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NBSPlayer implements Disposable {
    public static final DataKey<NBSPlayer> DATA_KEY = DataKey.create(NBSPlayer.class.getName());
    private final Project project;
    private final NBS nbs;
    private final AtomicBoolean playing = new AtomicBoolean();
    private final AtomicInteger tick = new AtomicInteger();
    private final AtomicBoolean loop = new AtomicBoolean();
    private final AtomicLong updateTickTime = new AtomicLong(-1);
    private final int fps = 50;
    private Timer ringTimer;
    private Timer deltaTimer;
    private FloatConsumer progressListener;
    private BooleanConsumer playingListener;

    public NBSPlayer(@NotNull Project project, @Nullable NBS nbs) {
        this.project = project;
        this.nbs = nbs;
    }

    private void startRingTimer() {
        stopRingTimer();
        ringTimer = new Timer((int) (1000f / ((float) nbs.getTempo() / 100f)), new RingTimer());
        ringTimer.start();

        deltaTimer = new Timer(1000 / fps, new DeltaTimer());
        deltaTimer.start();
    }

    private void stopRingTimer() {
        if (ringTimer != null) {
            ringTimer.stop();
            ringTimer = null;
        }

        if (deltaTimer != null) {
            deltaTimer.stop();
            deltaTimer = null;
        }
    }

    private class DeltaTimer implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (nbs == null || !isPlaying())
                return;

            long time = updateTickTime.get();
            if (time < 0)
                return;

            int tmp = (int) (1000f / ((float) nbs.getTempo() / 100f));
            long eq = System.currentTimeMillis() - time;
            float delta = ((float) eq / (float) tmp);
            if (progressListener != null)
                progressListener.accept((float) tick.get() + Math.min(delta, tmp));
        }
    }

    private class RingTimer implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (nbs == null)
                return;

            if (!playing.get()) {
                stopRingTimer();
                return;
            }

            if (nbs.getSongLength() < tick.get() + 1) {
                if (loop.get()) {
                    tick.set(loopStartTick());
                } else {
                    tick.set(0);
                    playing.set(false);
                    return;
                }
            }

            int tk = tick.getAndIncrement();
            playTick(tk);
            updateTickTime.set(System.currentTimeMillis());
        }
    }

    public void setPlayingListener(BooleanConsumer playingListener) {
        this.playingListener = playingListener;
    }

    public void setProgressListener(FloatConsumer progressListener) {
        this.progressListener = progressListener;
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

    public int getTick() {
        return tick.get();
    }

    public void setTick(int tick) {
        this.tick.set(tick);

        if (progressListener != null && isPlaying())
            progressListener.accept(tick);
        updateTickTime.set(-1);
    }

    public void setPlay(boolean play) {
        boolean pre = playing.get();
        playing.set(play);


        if (play != pre) {
            playingListener.accept(play);
            updateTickTime.set(play ? System.currentTimeMillis() : -1);
        }

        if (!pre && playing.get())
            playStart();
    }

    private void playStart() {
        if (nbs == null)
            return;

        if (playing.get())
            startRingTimer();
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
        stopRingTimer();
    }
}
