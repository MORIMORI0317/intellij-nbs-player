package net.morimori0317.inp;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import net.morimori0317.inp.nbs.Layer;
import net.morimori0317.inp.nbs.NBS;
import net.morimori0317.inp.nbs.Note;
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

    public void play(NBS nbs, Layer layer, Note note, boolean mcRange) {
        Instrument instrument = note.getInstrument(nbs);
        float vol = note.getRawVelocity() * layer.getRawVolume();
        float dfkey = note.getInstrument(nbs).getDefaultPitch() - instrument.getDefaultPitch();
        float pitch = (float) Math.pow(2.0f, (double) (note.getKey() - instrument.getDefaultPitch() + dfkey) / 12.0f);
        float pan = ((float) layer.getStereo() / 100f) - 1f;
        play(instrument, vol, pitch, pan, mcRange);
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
