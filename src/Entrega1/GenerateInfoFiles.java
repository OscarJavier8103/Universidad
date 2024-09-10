package Entrega1;

//Estudiante: Oscar Javier Romero Beltran
//Estudiante: Yuli Estefanny Florez Velazquex
//Estudiante: Omar Enrique Leguizamon Rodriguez
//Estudiante: 
//Estudiante:


//Los imports utilizados, permiten trabajar con archivos y generar datos aleatorios

//- BufferedWriter y FileWriter se utilizan para escribir datos en archivos de texto.
import java.io.BufferedWriter;
import java.io.FileWriter;

//- IOException maneja posibles errores al escribir archivos.
import java.io.IOException;

//- Random se utiliza para generar datos aleatorios, como productos y ventas.
import java.util.Random;



//Generación de la clase GenerateInfoFiles
public class GenerateInfoFiles {

    // Metodo 1: Este apartado de codigo se refiere al metodo que genera un archivo de ventas para un vendedor
	public void crearArchivoVentas(int cantidadVentasAleatorias, String nombreVendedor, long idVendedor) {
	    try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreVendedor + "_" + idVendedor + "_ventas.txt"))) {
	        Random aleatorio = new Random();
	        escritor.write("CC: " + idVendedor + " Nombre: " + nombreVendedor + "\n"); // Datos del vendedor
	        for (int i = 0; i < cantidadVentasAleatorias; i++) {
	            int idProducto = aleatorio.nextInt(900000) + 100000; // Numero aleatorio para crear el codigo de un producto
	            String idProductoConFormato = String.format("%06d", idProducto); // Convertir el ID del producto con 6 dígitos
	            int cantidadVendida = aleatorio.nextInt(20) + 1; // Generar una cantidad vendida de forma aleatoria
	            escritor.write("ID Producto: " + idProductoConFormato + " - " + "Cantidad Vendida: " + cantidadVendida + ";\n");
	        }
	        System.out.println("Archivo de ventas generado para " + nombreVendedor);
	    } catch (IOException e) {
	        System.out.println("Error al crear archivo de ventas: " + e.getMessage());
	    }
	}

    // Metodo 2: Este metodo genera un archivo con información de productos
    public void crearArchivoProductos(int cantidadProductos) {
    	String[] productos = {"Tesseract", "Cetro de Loki", "Traje de Ant-Man", "Armadura de Iron Man", "Guante del infinito","Gema del tiempo","Gema del espacio","Gema del poder","Gema del alma"};
    	try (BufferedWriter escritor = new BufferedWriter(new FileWriter("productos.txt"))) {
            Random aleatorio = new Random();
            for (int i = 0; i < cantidadProductos; i++) {
                int idProducto = aleatorio.nextInt(900000) + 100000; // Genera un número aleatorio entre 100000 y 999999
                String idProductoConFormato = String.format("%06d", idProducto); // Formatea el número a 6 dígitos
                //String nombreProducto = "Producto " + idProductoConFormato;
                String nombreProducto = productos[aleatorio.nextInt(productos.length)];
                double precioProducto = 10 + (100000 - 10) * aleatorio.nextDouble(); // Precio aleatorio entre 10 y 100000
                escritor.write("ID:" + idProductoConFormato + " - Nombre Producto: " + nombreProducto + " " + "- Precio: " + String.format("%.2f", precioProducto) + "\n");
            }
            System.out.println("Archivo de productos generado.");
        } catch (IOException e) {
            System.out.println("Error al crear archivo de productos: " + e.getMessage());
        }
    }

    // Metodo 3: se genera un archivo con información de vendedores
    public void crearArchivoInformacionVendedores(int cantidadVendedores) {
        String[] nombres = {"Peter", "Tony", "Wanda", "Steve", "Thor","Bruce","Clint","Natasha","Stephen"};
        String[] apellidos = {"Parker", "Stark", "Maximoff", "Rogers", "Odinson","Banner","Barton","Romanoff","Strange"};
        Random aleatorio = new Random();

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("vendedores.txt"))) {
            for (int i = 0; i < cantidadVendedores; i++) {
                String tipoDocumento = "CC";
                long numeroDocumento = 10000000 + aleatorio.nextInt(90000000); // Documento aleatorio
                String nombre = nombres[aleatorio.nextInt(nombres.length)];
                String apellido = apellidos[aleatorio.nextInt(apellidos.length)];
                escritor.write(tipoDocumento + ": " + numeroDocumento + " Nombre: " + nombre + " " + apellido + "\n");
            }
            System.out.println("Archivo de información de vendedores generado.");
        } catch (IOException e) {
            System.out.println("Error al crear archivo de información de vendedores: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        GenerateInfoFiles generador = new GenerateInfoFiles();
        
        // Finalmente generamos los archivos de prueba
        generador.crearArchivoVentas(5, "Peter", 12345678);
        generador.crearArchivoProductos(10);
        generador.crearArchivoInformacionVendedores(5);
    }
}

