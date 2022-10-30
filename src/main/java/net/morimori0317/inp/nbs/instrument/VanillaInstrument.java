package net.morimori0317.inp.nbs.instrument;

public enum VanillaInstrument implements Instrument {
    PIANO("harp", "block.note_block.harp"),
    DOUBLE_BASS("dbass", "block.note_block.bass"),
    BASS_DRUM("bdrum", "block.note_block.basedrum"),
    SNARE_DRUM("sdrum", "block.note_block.snare"),
    CLICK("click", "block.note_block.hat"),
    GUITAR("guitar", "block.note_block.guitar"),
    FLUTE("flute", "block.note_block.flute"),
    BELL("bell", "block.note_block.bell"),
    CHIME("icechime", "block.note_block.chime"),
    XYLOPHONE("xylobone", "block.note_block.xylophone"),
    IRON_XYLOPHONE("iron_xylophone", "block.note_block.iron_xylophone"),
    COW_BELL("cow_bell", "block.note_block.cow_bell"),
    DIDGERIDOO("didgeridoo", "block.note_block.didgeridoo"),
    BIT("bit", "block.note_block.bit"),
    BANJO("banjo", "block.note_block.banjo"),
    PLING("pling", "block.note_block.pling");
    private final String name;
    private final String registryName;

    VanillaInstrument(String name, String registryName) {
        this.name = name;
        this.registryName = registryName;
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
}