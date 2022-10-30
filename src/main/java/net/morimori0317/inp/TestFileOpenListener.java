package net.morimori0317.inp;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.vfs.VirtualFile;
import net.morimori0317.inp.nbs.NBS;
import net.morimori0317.inp.testplayer.AsyncNBSPlayerOld;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestFileOpenListener implements FileEditorManagerListener {
    @Override
    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        if (!FileTypeRegistry.getInstance().isFileOfType(file, NBSFileType.INSTANCE))
            return;

        var player = NBSPlayerService.getInstance(source.getProject());
        NBS nbs;

        try (InputStream stream = new BufferedInputStream(file.getInputStream())){
            nbs = new NBS(stream);
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        AsyncNBSPlayerOld pl = new AsyncNBSPlayerOld(nbs, (instrument, volume, pitch, stereo) -> {
            player.play(instrument, volume, pitch, stereo, false);
        });
        pl.playStart();
    }
}
