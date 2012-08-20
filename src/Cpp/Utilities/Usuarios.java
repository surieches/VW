package Cpp.Utilities;

/**
 *
 * @author CPP-lap
 */
public class Usuarios{
    private String nombre;
    public Usuarios(){}
    public Usuarios(String nombre, int telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }
    private int telefono;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
}
