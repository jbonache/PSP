package com.ieseljust.psp.myshell;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyShell {

    // Colores
    static final String ROJO = "\u001B[31m";
    static final String VERDE = "\u001B[32m";
    static final String RST = "\u001B[0m";

    static void RedirigirSalida(Process p) throws IOException {

        // Mostrar la salida: Leemos la salida del proceso
        // con getInputStream y la mostramos por pantalla en verde
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        p.getInputStream()));
        String line;

        while ((line = br.readLine()) != null) {
            System.out.println(VERDE + line + RST);
        }

        /*
         * // Mostrar la salida de error: Leemos la salida de
         * // error del proceso y la mostramos por pantalla en rojo
         * BufferedReader brerr = new BufferedReader(
         * new InputStreamReader(
         * p.getErrorStream()));
         * String errline;
         * while ((errline = brerr.readLine()) != null) {
         * System.out.println(ROJO + errline + RST);
         * }
         */
    }

    public static void main(String[] args) {

        // Definimos un Scanner para obtener la entrada del usuario
        Scanner keyboard = null;

        try {
            // Asignamos el Scanner a la entrada estándar
            keyboard = new Scanner(System.in);

            String orden; // String para la orden introducida

            // Definimos un ArrayList de ProcessBuilder para ejecutar las órdenes
            List<ProcessBuilder> builders;

            do {
                // Mostramos el shell y capturamos la entrada
                System.out.print("# MyShell> ");
                orden = keyboard.nextLine();

                // Si la orden es quit cerramos la aplicación
                if (orden.equals("quit")) {
                    System.exit(0);
                }

                try {
                    // Obtenemos un array con las diferentes órdenes
                    // separadas por tuberías
                    String[] array_ordenes = orden.split("\\|");

                    // Inicializamos los builders
                    builders = new ArrayList<ProcessBuilder>();
                    
                    // Y creamos la lista de ProcessBuilders
                    for (String orden_interna : array_ordenes) {

                        // Obtenemos un array con los diferentes
                        // items de la orden.

                        String[] array_orden = orden_interna.trim().split(" ");
                        // System.out.println("Adding... "+orden_interna.trim());
                        builders.add(new ProcessBuilder(array_orden));
                    }

                    // Invocamos a startPipeline para lanzar las diferentes órdenes
                    List<Process> listaProcesos = ProcessBuilder.startPipeline(builders);

                    Process ultimo = listaProcesos.get(listaProcesos.size() - 1);
                    // System.out.println(ultimo.info().command());
                    RedirigirSalida(ultimo);
                    
                } catch (Exception e) {
                    // Si se produce cualquier excepción lo reportamos:
                    System.out.println(ROJO + "Error: " + e.getMessage() + RST);
                }

            } while (true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            if (keyboard != null)
                keyboard.close();
        }

    }
}
