package pacman.threads;

import pacman.PacmanGame;
import pacman.entity.BaseCreatura;
import pacman.entity.CreatureManager;
import pacman.entity.Fantasma;
import pacman.entity.Pacman;

public class chkMangiatoThread extends BaseThread {
    public chkMangiatoThread(BaseCreatura creatura, PacmanGame dati) {
        super(creatura, dati);
        if (!(creatura instanceof Pacman))
            throw new IllegalArgumentException("creature must be a Pacman!");
        setName("Thread chkMangiato");
    }

    @Override
    public void run() {
        final CreatureManager creature = dati.getCreature();
        while (!isInterrupted()) {
            try {
                dati.getSemMuovi().acquire(NUM_FANTASMI + 1);
                for (Fantasma f : creature.getFantasmi()) {
                    if (Math.abs(f.getMiddleX() - creatura.getMiddleX()) < creatura.getWidth() / 2 && Math.abs(f.getMiddleY() - creatura.getMiddleY()) < creatura.getHeight() / 2) {
                        System.out.println("MANGIATO!!");
                        if (f.isVulnerable() && !f.isAppenaMorto()) {
                            f.muori();
                            System.out.println("fantasma " + f.getColor() + " morto");
                        } else if (!creatura.isAppenaMorto()) {
                            dati.gameOver();
                            creatura.setAppenaMorto();
                            if (dati.getVite() <= 0)
                                return;
                            try {
                                sleep(3000);    //ferma per 3 secondi il gioco (i thread sono sincronizzati con i semafori)
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                }
                dati.getSemControlla().release(NUM_FANTASMI + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        System.out.println("Fine");

    }
}
