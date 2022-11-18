package net.morimori0317.inp.player;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import dev.felnull.fnnbs.Layer;
import dev.felnull.fnnbs.NBS;
import dev.felnull.fnnbs.Note;
import dev.felnull.fnnbs.instrument.CustomInstrument;
import dev.felnull.fnnbs.instrument.Instrument;
import dev.felnull.fnnbs.instrument.VanillaInstrument;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NBSPlayerService implements Disposable {
    private final ExecutorService playerExecutorService = Executors.newCachedThreadPool(new BasicThreadFactory.Builder().namingPattern("nbs-player-%d").daemon(true).build());

    public static NBSPlayerService getInstance(Project project) {
        return project.getService(NBSPlayerService.class);
    }

    public ExecutorService getPlayerExecutorService() {
        return playerExecutorService;
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
        if (!playerExecutorService.isShutdown()) {
            CompletableFuture.runAsync(() -> {
                try {
                    playInternal(instrument, volume, pitch, pan, mcRange);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }, playerExecutorService);
        }
    }

    @Override
    public void dispose() {
        playerExecutorService.shutdown();
    }

    private void playInternal(Instrument instrument, float volume, float pitch, float pan, boolean mcRange) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        try (InputStream stream = getSound(instrument)) {
            if (stream == null)
                return;

            if (mcRange)
                pitch = Math.max(0.5F, Math.min(pitch, 2.0F));

            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream)) {
                AudioFormat af = audioInputStream.getFormat();
                af = new AudioFormat(af.getEncoding(), Math.min(af.getSampleRate() * pitch, 200000), af.getSampleSizeInBits(), af.getChannels(), af.getFrameSize(), af.getFrameRate(), af.isBigEndian(), af.properties());

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, af, AudioSystem.NOT_SPECIFIED);
                try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info)) {
                    line.open(af, AudioSystem.NOT_SPECIFIED);

                    FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                    float gain = Math.min(Math.max((float) (20d * Math.log10(volume)), gainControl.getMinimum()), gainControl.getMaximum());
                    gainControl.setValue(gain);

                    line.start();
                    int buffer_size = 128000;
                    int bytes_read = 0;
                    byte[] ab_data = new byte[buffer_size];
                    while (bytes_read != -1) {
                        bytes_read = audioInputStream.read(ab_data, 0, ab_data.length);
                        if (bytes_read >= 0) {
                            line.write(ab_data, 0, bytes_read);
                        }
                    }
                    line.drain();
                }
            }
        }
    }

    private InputStream getSound(Instrument instrument) {
        if (instrument instanceof VanillaInstrument)
            return resourceExtractor(NBSPlayer.class, String.format("/sounds/%s.wav", ((VanillaInstrument) instrument).getName()));

        if (instrument instanceof CustomInstrument) {
            String name = ((CustomInstrument) instrument).getFileName();
            String[] ps = name.split("/");
            name = ps[ps.length - 1];

            int ex = name.lastIndexOf(".");
            if (ex >= 0)
                name = name.substring(0, ex);

            return resourceExtractor(NBSPlayer.class, String.format("/sounds/custom/%s.wav", name));
        }
        return null;
    }

    private InputStream resourceExtractor(Class<?> clazz, String path) {
        if (path.startsWith("/")) path = path.substring(1);

        InputStream stream = clazz.getResourceAsStream("/" + path);
        if (stream == null) stream = ClassLoader.getSystemResourceAsStream(path);
        return stream != null ? new BufferedInputStream(stream) : null;
    }
}
