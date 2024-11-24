package monopoly;
import java.util.Scanner;

public class ConsolaNormal implements Consola {
    // Instancia de Scanner para leer datos del usuario
    private static final Scanner scanner = new Scanner(System.in);

    // Implementación del método imprimir
    @Override
    public void imprimir(String mensaje) {
        System.out.println(mensaje);
    }

    // Implementación del método leer
    @Override
    public String leer(String descripcion) { // Se imprime la descripción y se lee la respuesta
        System.out.print(descripcion);
        return scanner.nextLine();    
}}