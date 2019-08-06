package pacman.threads;

import pacman.PacmanGame;
import pacman.entity.BaseCreatura;

public class PacmanThread extends BaseThread {
    public PacmanThread(BaseCreatura creatura, PacmanGame dati) {
        super(creatura, dati);
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            try {
                creatura.onTick();
                sleep(1000/TPS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
