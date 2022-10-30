package net.morimori0317.inp.testplayer;


import net.morimori0317.inp.nbs.Layer;
import net.morimori0317.inp.nbs.NBS;
import net.morimori0317.inp.nbs.Note;

import java.util.List;

public class NBSPlayerOld {
    private final NBS nbs;
    private final INBSPlayerImplOld impl;
    private int tick;
    private boolean loop;
    private boolean forcedLoop;
    private int maxLoop = -1;
    private int currentLoop;

    public NBSPlayerOld(NBS nbs, INBSPlayerImplOld impl) {
        this.nbs = nbs;
        this.impl = impl;
    }

    public NBS getNBS() {
        return nbs;
    }

    public boolean tick() {
        if (getNBS().getSongLength() <= tick)
            return false;
        List<Layer> layers = nbs.getLayers();
        for (Layer layer : layers) {
            Note note = layer.getNote(tick);
            if (note != null) {
                float vol = note.getRawVelocity() * layer.getRawVolume();
                float dfkey = note.getInstrument(getNBS()).getDefaultPitch() - 45f;
                float pitch = (float) Math.pow(2.0f, (double) (note.getKey() - 45 + dfkey) / 12.0f);
                float stereo = ((float) layer.getStereo() / 100f) - 1f;
                impl.play(note.getInstrument(getNBS()), vol, pitch, stereo);
            }
        }
        tick++;
        int lc = maxLoop < 0 ? Integer.MAX_VALUE : maxLoop;
        if (getNBS().getSongLength() <= tick && (loop && getNBS().isLoop() || forcedLoop) && currentLoop < lc) {
            tick = getNBS().getLoopStart();
            currentLoop++;
        }
        return true;
    }

    public void setMaxLoopCount(int maxLoop) {
        this.maxLoop = maxLoop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setForcedLoop(boolean forcedLoop) {
        this.forcedLoop = forcedLoop;
    }
}
