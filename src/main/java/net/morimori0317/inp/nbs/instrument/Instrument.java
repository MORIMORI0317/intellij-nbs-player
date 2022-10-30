package net.morimori0317.inp.nbs.instrument;

public interface Instrument {

    String getSoundName();

    boolean isVanillaNote();

    float getDefaultPitch();
}
