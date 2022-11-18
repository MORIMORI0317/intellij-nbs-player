package net.morimori0317.inp;

import com.intellij.openapi.util.IconLoader;
import dev.felnull.fnnbs.instrument.Instrument;
import dev.felnull.fnnbs.instrument.VanillaInstrument;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class INPIcons {
    public static final Icon NBS_FILE = IconLoader.getIcon("/icons/fileTypes/nbs.svg", INPIcons.class);
    public static final Icon NOTE = IconLoader.getIcon("/icons/player/note.svg", INPIcons.class);
    public static final Icon NOTE_0 = IconLoader.getIcon("/icons/player/notes/note_0.svg", INPIcons.class);
    public static final Icon NOTE_1 = IconLoader.getIcon("/icons/player/notes/note_1.svg", INPIcons.class);
    public static final Icon NOTE_2 = IconLoader.getIcon("/icons/player/notes/note_2.svg", INPIcons.class);
    public static final Icon NOTE_3 = IconLoader.getIcon("/icons/player/notes/note_3.svg", INPIcons.class);
    public static final Icon NOTE_4 = IconLoader.getIcon("/icons/player/notes/note_4.svg", INPIcons.class);
    public static final Icon NOTE_5 = IconLoader.getIcon("/icons/player/notes/note_5.svg", INPIcons.class);
    public static final Icon NOTE_6 = IconLoader.getIcon("/icons/player/notes/note_6.svg", INPIcons.class);
    public static final Icon NOTE_7 = IconLoader.getIcon("/icons/player/notes/note_7.svg", INPIcons.class);
    public static final Icon NOTE_8 = IconLoader.getIcon("/icons/player/notes/note_8.svg", INPIcons.class);
    public static final Icon NOTE_9 = IconLoader.getIcon("/icons/player/notes/note_9.svg", INPIcons.class);
    public static final Icon NOTE_10 = IconLoader.getIcon("/icons/player/notes/note_10.svg", INPIcons.class);
    public static final Icon NOTE_11 = IconLoader.getIcon("/icons/player/notes/note_11.svg", INPIcons.class);
    public static final Icon NOTE_12 = IconLoader.getIcon("/icons/player/notes/note_12.svg", INPIcons.class);
    public static final Icon NOTE_13 = IconLoader.getIcon("/icons/player/notes/note_13.svg", INPIcons.class);
    public static final Icon NOTE_14 = IconLoader.getIcon("/icons/player/notes/note_14.svg", INPIcons.class);
    public static final Icon NOTE_15 = IconLoader.getIcon("/icons/player/notes/note_15.svg", INPIcons.class);

    @NotNull
    public static Icon getInstrumentIcon(Instrument instrument) {
        if (instrument instanceof VanillaInstrument) {
            switch ((VanillaInstrument) instrument) {
                case PIANO:
                    return INPIcons.NOTE_0;
                case DOUBLE_BASS:
                    return INPIcons.NOTE_1;
                case BASS_DRUM:
                    return INPIcons.NOTE_2;
                case SNARE_DRUM:
                    return INPIcons.NOTE_3;
                case CLICK:
                    return INPIcons.NOTE_4;
                case GUITAR:
                    return INPIcons.NOTE_5;
                case FLUTE:
                    return INPIcons.NOTE_6;
                case BELL:
                    return INPIcons.NOTE_7;
                case CHIME:
                    return INPIcons.NOTE_8;
                case XYLOPHONE:
                    return INPIcons.NOTE_9;
                case IRON_XYLOPHONE:
                    return INPIcons.NOTE_10;
                case COW_BELL:
                    return INPIcons.NOTE_11;
                case DIDGERIDOO:
                    return INPIcons.NOTE_12;
                case BIT:
                    return INPIcons.NOTE_13;
                case BANJO:
                    return INPIcons.NOTE_14;
                case PLING:
                    return INPIcons.NOTE_15;
            }
        }
        return INPIcons.NOTE;
    }
}
