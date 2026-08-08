public class Terminal {
    private Boolean reserved;

    Terminal(){
        this.reserved=false;
    }

    synchronized public void Reserve(){

        // Mientras la terminal esté reservada esperamos

       while (reserved==true){
            try{
                wait();
            }catch (InterruptedException e){}
        }

        System.out.println("\u001B[33m Terminal ocupada por el thread "+Thread.currentThread().getName()+"\u001B[0m");
        reserved=true;

    }

    synchronized public void Free(){
        this.reserved=false;
        notifyAll();
    }

}
