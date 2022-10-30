package net.morimori0317.inp.nbs;


import net.morimori0317.inp.nbs.instrument.Instrument;
import net.morimori0317.inp.nbs.instrument.VanillaInstrument;

import java.util.Objects;

public class Note {
    private final int instrument;
    private final int key;
    private final int velocity;
    private final int panning;
    private final int pitch;

    public Note(int instrument, int key, int velocity, int panning, int pitch) {
        this.instrument = instrument;
        this.key = key;
        this.velocity = velocity;
        this.panning = panning;
        this.pitch = pitch;
    }

    public int getInstrument() {
        return instrument;
    }

    public int getKey() {
        return key;
    }

    public int getPanning() {
        return panning;
    }

    public int getPitch() {
        return pitch;
    }

    public int getVelocity() {
        return velocity;
    }

    public float getRawVelocity() {
        return (float) velocity / 100f;
    }

    public Instrument getInstrument(NBS nbs) {
        if (nbs.getVanillaInstrumentCount() > instrument && VanillaInstrument.values().length > instrument)
            return VanillaInstrument.values()[instrument];
        int cc = instrument - nbs.getVanillaInstrumentCount();
        return nbs.getCustomInstruments().get(cc);
    }

    @Override
    public String toString() {
        return "Note{" +
                "instrument=" + instrument +
                ", key=" + key +
                ", velocity=" + velocity +
                ", panning=" + panning +
                ", pitch=" + pitch +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return instrument == note.instrument && key == note.key && velocity == note.velocity && panning == note.panning && pitch == note.pitch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrument, key, velocity, panning, pitch);
    }
}
