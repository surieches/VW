/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

/**
 *
 * @author MAGG
 */
public class Producto {
    private int id;
    private String id_general;    
    private String descripcion;
    private String presentacion;
    private String precio_pag;
    private String precio_caj;
    private boolean pendiente;

    public Producto(int id, String id_general, String descripcion, String presentacion, String precio_pag, String precio_caj, boolean pendiente) {
        this.id = id;
        this.id_general = id_general;
        this.descripcion = descripcion;
        this.presentacion = presentacion;
        this.precio_pag = precio_pag;
        this.precio_caj = precio_caj;
        this.pendiente = pendiente;
    }    

    public Producto(int id, String descripcion, String presentacion, boolean pendiente) {
        this.id = id;
        this.descripcion = descripcion;
        this.presentacion = presentacion;
        this.pendiente = pendiente;
    }

    public String getId_general() {
        return id_general;
    }

    public void setId_general(String id_general) {
        this.id_general = id_general;
    }

    public String getPrecio_pag() {
        return precio_pag;
    }

    public void setPrecio_pag(String precio_pag) {
        this.precio_pag = precio_pag;
    }

    public String getPrecio_caj() {
        return precio_caj;
    }

    public void setPrecio_caj(String precio_caj) {
        this.precio_caj = precio_caj;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public boolean isPendiente() {
        return pendiente;
    }

    public void setPendiente(boolean pendiente) {
        this.pendiente = pendiente;
    }
    
}
