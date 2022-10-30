package net.morimori0317.inp.testplayer;


import net.morimori0317.inp.nbs.NBS;

public class AsyncNBSPlayerOld extends NBSPlayerOld {
    private PlayerThread playerThread;
    private String playThreadName;

    public AsyncNBSPlayerOld(NBS nbs, INBSPlayerImplOld impl) {
        super(nbs, impl);
    }

    public void setPlayThreadName(String playThreadName) {
        this.playThreadName = playThreadName;
    }

    public void playStart() {
        if (isPlaying()) return;
        this.playerThread = new PlayerThread();
        if (playThreadName != null)
            this.playerThread.setName(playThreadName);
        this.playerThread.start();
    }

    public void playStop() {
        if (!isPlaying()) return;
        this.playerThread.interrupt();
        this.playerThread = null;
    }

    public boolean isPlaying() {
        return this.playerThread != null && this.playerThread.isAlive();
    }

    private class PlayerThread extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                if (!tick())
                    break;
                long tickSpeed = (long) (1000f / ((float) getNBS().getTempo() / 100f));
                try {
                    Thread.sleep(tickSpeed);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
