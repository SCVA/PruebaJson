package moviles.pruebajson.data;

public class Postre {
    private String id;
    private String nombre;
    private String cantidad;
    private String precio;
    private String url;
    private String img;
    private String fecha;

    public Postre(String id, String nombre, String cantidad, String precio, String url, String img, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.url = url;
        this.img = img;
        this.fecha = fecha;
    }
}
