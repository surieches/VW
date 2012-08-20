package Beans;

/**
 *
 * @author MAGG
 */
public class FarmaciaBean {
    private String Farmacia;
    private String Colonia;
    private String TipoCadena;
    private String Tipo2;
    private String CP;
    private String Ageb;
    private String Ciudad;
    private String Municipio;
    private String Direccion;
    private String Telefono;
    private boolean Pendiente;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }    
    
    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }
    
    public String getMunicipio() {
        return Municipio;
    }

    public void setMunicipio(String Municipio) {
        this.Municipio = Municipio;
    }
    
    public boolean isPendiente() {
        return Pendiente;
    }

    public void setPendiente(boolean Pendiente) {
        this.Pendiente = Pendiente;
    }
    
    public String getAgeb() {
        return Ageb;
    }

    public void setAgeb(String Ageb) {
        this.Ageb = Ageb;
    }

    public String getCP() {
        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String Ciudad) {
        this.Ciudad = Ciudad;
    }

    public String getColonia() {
        return Colonia;
    }

    public void setColonia(String Colonia) {
        this.Colonia = Colonia;
    }

    public String getFarmacia() {
        return Farmacia;
    }

    public void setFarmacia(String Farmacia) {
        this.Farmacia = Farmacia;
    }

    public String getTipo2() {
        return Tipo2;
    }

    public void setTipo2(String Tipo2) {
        this.Tipo2 = Tipo2;
    }

    public String getTipoCadena() {
        return TipoCadena;
    }

    public void setTipoCadena(String TipoCadena) {
        this.TipoCadena = TipoCadena;
    }

}
