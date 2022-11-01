package net.morimori0317.inp.nbs.instrument;

import net.morimori0317.inp.INPIcons;

import javax.swing.*;

public enum VanillaInstrument implements Instrument {
    PIANO("harp", "block.note_block.harp", INPIcons.NOTE_0),
    DOUBLE_BASS("dbass", "block.note_block.bass", INPIcons.NOTE_1),
    BASS_DRUM("bdrum", "block.note_block.basedrum", INPIcons.NOTE_2),
    SNARE_DRUM("sdrum", "block.note_block.snare", INPIcons.NOTE_3),
    CLICK("click", "block.note_block.hat", INPIcons.NOTE_4),
    GUITAR("guitar", "block.note_block.guitar", INPIcons.NOTE_5),
    FLUTE("flute", "block.note_block.flute", INPIcons.NOTE_6),
    BELL("bell", "block.note_block.bell", INPIcons.NOTE_7),
    CHIME("icechime", "block.note_block.chime", INPIcons.NOTE_8),
    XYLOPHONE("xylobone", "block.note_block.xylophone", INPIcons.NOTE_9),
    IRON_XYLOPHONE("iron_xylophone", "block.note_block.iron_xylophone", INPIcons.NOTE_10),
    COW_BELL("cow_bell", "block.note_block.cow_bell", INPIcons.NOTE_11),
    DIDGERIDOO("didgeridoo", "block.note_block.didgeridoo", INPIcons.NOTE_12),
    BIT("bit", "block.note_block.bit", INPIcons.NOTE_13),
    BANJO("banjo", "block.note_block.banjo", INPIcons.NOTE_14),
    PLING("pling", "block.note_block.pling", INPIcons.NOTE_15);
    private final String name;
    private final String registryName;
    private final Icon icon;

    VanillaInstrument(String name, String registryName, Icon icon) {
        this.name = name;
        this.registryName = registryName;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getRegistryName() {
        return registryName;
    }

    @Override
    public String getSoundName() {
        return registryName;
    }

    @Override
    public boolean isVanillaNote() {
        return true;
    }

    @Override
    public float getDefaultPitch() {
        return 45;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }
}