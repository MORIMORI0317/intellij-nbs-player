package net.morimori0317.inp.nbs.instrument;

import net.morimori0317.inp.INPIcons;

import javax.swing.*;
import java.util.Objects;

public class CustomInstrument implements Instrument {
    private final String name;
    private final String fileName;
    private final int pitch;
    private final boolean pressKeys;

    public CustomInstrument(String name, String fileName, int pitch, boolean autoPressKeys) {
        this.name = name;
        this.fileName = fileName;
        this.pitch = pitch;
        this.pressKeys = autoPressKeys;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isPressKeys() {
        return pressKeys;
    }

    @Override
    public String getSoundName() {
        return name;
    }

    @Override
    public boolean isVanillaNote() {
        return false;
    }

    @Override
    public float getDefaultPitch() {
        return pitch;
    }

    @Override
    public String toString() {
        return "CustomInstrument{" +
                "name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", pitch=" + pitch +
                ", pressKeys=" + pressKeys +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomInstrument that = (CustomInstrument) o;
        return pitch == that.pitch && pressKeys == that.pressKeys && Objects.equals(name, that.name) && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fileName, pitch, pressKeys);
    }

    @Override
    public Icon getIcon() {
        return INPIcons.NOTE;
    }
}
