package net.morimori0317.inp.nbs;


import net.morimori0317.inp.nbs.instrument.CustomInstrument;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NBS {
    private final boolean old;
    private final int version;
    private final int vanillaInstrumentCount;
    private final int songLength;
    private final String name;
    private final String author;
    private final String originalAuthor;
    private final String description;
    private final int tempo;
    private final boolean autoSaving;
    private final int autoSavingDuration;
    private final int timeSignature;
    private final int minutesSpent;
    private final int leftClicks;
    private final int rightClicks;
    private final int noteBlocksAdded;
    private final int noteBlocksRemoved;
    private final String importFileName;
    private final boolean loop;
    private final int loopCount;
    private final int loopStart;
    private final List<Layer> layers = new ArrayList<>();
    private final List<CustomInstrument> customInstruments = new ArrayList<>();

    public NBS(InputStream stream) throws IOException {
        int fl = readShort(stream);
        this.old = fl != 0;
        if (old) {
            this.version = 0;
            this.vanillaInstrumentCount = 10;
            this.songLength = fl;
        } else {
            this.version = stream.read();
            this.vanillaInstrumentCount = stream.read();
            this.songLength = readShort(stream);
        }
        if (this.version > 5)
            throw new RuntimeException("Unsupported nbs version");

        int layerCount = readShort(stream);
        for (int i = 0; i < layerCount; i++) {
            layers.add(new Layer());
        }
        this.name = readString(stream);
        this.author = readString(stream);
        this.originalAuthor = readString(stream);
        this.description = readString(stream);
        this.tempo = readShort(stream);
        this.autoSaving = readBoolean(stream);
        this.autoSavingDuration = stream.read();
        this.timeSignature = stream.read();
        this.minutesSpent = readInt(stream);
        this.leftClicks = readInt(stream);
        this.rightClicks = readInt(stream);
        this.noteBlocksAdded = readInt(stream);
        this.noteBlocksRemoved = readInt(stream);
        this.importFileName = readString(stream);
        if (old) {
            this.loop = false;
            this.loopCount = -1;
            this.loopStart = -1;
        } else {
            this.loop = readBoolean(stream);
            this.loopCount = stream.read();
            this.loopStart = readShort(stream);
        }

        int tick = -1;
        int nextJT = readShort(stream);
        while (nextJT != 0) {
            tick += nextJT;
            int layer = -1;
            int nextJL = readShort(stream);
            while (nextJL != 0) {
                layer += nextJL;
                int instrument = stream.read();
                int key = stream.read();
                int velocity = old ? 100 : stream.read();
                int panning = old ? 100 : stream.read();
                int pitch = old ? 0 : readSignedShort(stream);
                Note note = new Note(instrument, key, velocity, panning, pitch);
                layers.get(layer).addNote(tick, note);
                nextJL = readShort(stream);
            }
            nextJT = readShort(stream);
        }

        if (!old) {
            for (Layer layer : layers) {
                layer.setName(readString(stream));
                layer.setLock(readBoolean(stream));
                layer.setVolume(stream.read());
                layer.setStereo(stream.read());
            }
        }

        int cic = stream.read();
        if (cic != -1) {
            for (int i = 0; i < cic; i++) {
                customInstruments.add(new CustomInstrument(readString(stream), readString(stream), stream.read(), readBoolean(stream)));
            }
        }
    }

    public static int readSignedShort(InputStream stream) throws IOException {
        return stream.read() + (stream.read() << 8);
    }

    public static boolean readBoolean(InputStream stream) throws IOException {
        return stream.read() >= 1;
    }

    public static String readString(InputStream stream) throws IOException {
        byte[] dat = new byte[readInt(stream)];
        stream.read(dat);
        return new String(dat, StandardCharsets.UTF_8);
    }

    public static int readInt(InputStream stream) throws IOException {
        return stream.read() + (stream.read() << 8) + (stream.read() << 16) + (stream.read() << 24);
    }

    public static int readShort(InputStream stream) throws IOException {
        return (stream.read()) + (stream.read() << 8);
    }

    public boolean isOld() {
        return old;
    }

    public int getVersion() {
        return version;
    }

    public int getVanillaInstrumentCount() {
        return vanillaInstrumentCount;
    }

    public int getSongLength() {
        return songLength;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getOriginalAuthor() {
        return originalAuthor;
    }

    public String getDescription() {
        return description;
    }

    public int getTempo() {
        return tempo;
    }

    public boolean isAutoSaving() {
        return autoSaving;
    }

    public int getAutoSavingDuration() {
        return autoSavingDuration;
    }

    public int getTimeSignature() {
        return timeSignature;
    }

    public int getMinutesSpent() {
        return minutesSpent;
    }

    public int getLeftClicks() {
        return leftClicks;
    }

    public int getRightClicks() {
        return rightClicks;
    }

    public int getNoteBlocksAdded() {
        return noteBlocksAdded;
    }

    public int getNoteBlocksRemoved() {
        return noteBlocksRemoved;
    }

    public String getImportFileName() {
        return importFileName;
    }

    public boolean isLoop() {
        return loop;
    }

    public int getLoopCount() {
        return loopCount;
    }

    public int getLoopStart() {
        return loopStart;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public List<CustomInstrument> getCustomInstruments() {
        return customInstruments;
    }
}
