package net.morimori0317.inp;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import net.morimori0317.inp.nbs.instrument.Instrument;
import net.morimori0317.inp.nbs.player.NBSPlayer;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NBSPlayerService implements Disposable {
    private final ExecutorService executorService = Executors.newCachedThreadPool(new BasicThreadFactory.Builder().namingPattern("nbs-player-%d").daemon(true).build());

    public static NBSPlayerService getInstance(Project project) {
        return project.getService(NBSPlayerService.class);
    }

    public void play(Instrument instrument, float volume, float pitch, float pan, boolean mcRange) {
        if (!executorService.isShutdown()) {
            CompletableFuture.runAsync(() -> {
                try {
                    NBSPlayer.play(instrument, volume, pitch, pan, mcRange);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }, executorService);
        }
    }

    @Override
    public void dispose() {
        executorService.shutdown();
    }
}
