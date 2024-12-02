package monopoly;

//Interfaz de consola para imprimir mensajes y leer datos del usuario.
// Implementada por 'ConsolaNormal'
public interface Consola {

    // Método para imprimir un mensaje al usuario
    void imprimir(String mensaje);

    // Método para leer un dato introducido por el usuario, en respuesta a un mensaje (descripción)
    String leer(String descripcion);
}
