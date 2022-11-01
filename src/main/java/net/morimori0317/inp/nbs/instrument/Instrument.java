package net.morimori0317.inp.nbs.instrument;

import javax.swing.*;

public interface Instrument {

    String getSoundName();

    boolean isVanillaNote();

    float getDefaultPitch();

    Icon getIcon();
}
