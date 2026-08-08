
class ObjetoCompartido {
    int valor;
    boolean disponible = false; // Inicialmente no tenemos valor

    public int get() {

        // Mientras no tengamos datos disponibles esperamos
        while (disponible == false) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        // Cuando se despierte, volvemos a establecer la
        // disponibilidad a falso, notificamos a todos
        // los productores de la disponibilidad y
        // devolvemos el valor.

        this.disponible = false;
        notifyAll();
        return this.valor;

    }

    public void set(int val) {

        // Mientras quedan datos nos esperamos
        while (disponible == true) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        // Cuando se despierte, volvemos a establecer la
        // disponibilidad a cierto, establecemos el valor
        // generado por el productor, y notificamos a todos
        // los consumidores de la disponibilidad.

        this.valor = val;
        this.disponible = true;
        notifyAll();
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

public class ModeloProductorConsumidor2 {
    public static void main(String[] args) {
        ObjetoCompartido compartido = new ObjetoCompartido();
        Thread p = new Thread(new Productor(compartido));
        Thread c = new Thread(new Consumidor(compartido));
        p.start();
        c.start();

    }
}
