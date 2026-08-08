import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;


class generadorEco implements Runnable{
    private Socket MySocket;

    generadorEco(Socket socket){
        MySocket=socket;
        System.out.println(MySocket.toString());
    }

    @Override
    public void run() {
        try{

            // Ara hem de llegir què ens envíen
            InputStream is=MySocket.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader bf=new BufferedReader(isr);
            String linia=bf.readLine();

            String resposta;
            
            System.out.println("<log> Received... "+linia);
            // Analitzem la línia, i en funció d'aquesta retornem un resultat
            switch (linia){
                case "Hola don Pepito":
                    resposta="Hola don José";
                    break;

                case "Pasó usted por mi casa?":
                    resposta="Por su casa yo pasé";
                    break;
                
                case "Y vio usted a mi abuela?":
                    resposta="A su abuela yo la vi";
                    break;

                case "Adiós don Pepito":
                    resposta="Adiós don José";
                    break;
                
                default:
                    resposta="No es reconeix el misstge...";
                    break;
                
            }
            
            // Escrivim el resultat a l'stream d'eixida
            OutputStream os=MySocket.getOutputStream();
            PrintWriter pw=new PrintWriter(os);
            pw.write(resposta+"\n");
            pw.flush();

            // Pausa abans de tancar la connexió
            try{
                System.out.println("Esperant 10 segons...");
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException ie){
                System.out.println("S'ha inerromput la pausa...");
            };

            pw.close();
            os.close();
            this.MySocket.close();
            
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

}