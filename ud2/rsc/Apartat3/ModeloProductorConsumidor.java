class ObjetoCompartido {
    int valor;
    boolean disponible = false; // Inicialmente no tenemos valor

    int get() {
        if (this.disponible) {
            this.disponible = false;
            return this.valor;
        } else
            return -1;
    }

    void set(int val) {
        this.disponible = true;
        this.valor = val;
    }
}


class Productor implements Runnable {
    // Referencia a un objeto compartido
    ObjetoCompartido compartido;

    Productor(ObjetoCompartido compartido) {
        this.compartido = compartido;
    }

    @Override
    public void run() {
        for (int y = 0; y < 5; y++) {
            System.out.println("El productor produce: " + y);
            this.compartido.set(y);
            try {
                Thread.currentThread().sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }
}

class Consumidor implements Runnable {
    // Referencia a un objeto compartido
    private ObjetoCompartido compartido;

    Consumidor(ObjetoCompartido compartido) {
        this.compartido = compartido;
    }

    @Override
    public void run() {
        for (int y = 0; y < 5; y++) {
            System.out.println("El consumidor consume: " + this.compartido.get());
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
}


public class ModeloProductorConsumidor {
    public static void main(String[] args) {
        ObjetoCompartido compartido = new ObjetoCompartido();
        Thread p = new Thread(new Productor(compartido));
        Thread c = new Thread(new Consumidor(compartido));
        p.start();
        c.start();

    }
}
