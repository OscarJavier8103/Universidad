package Entrega1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateInfoFiles {

    // Método para leer el archivo de productos y devolver una lista de los IDs de productos existentes
    public List<String> leerProductos(String filePath) {
        List<String> productos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 1) {
                    String idProducto = partes[0].trim(); // El ID del producto está en la primera posición
                    productos.add(idProducto); // Añadir el ID del producto a la lista
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de productos: " + e.getMessage());
        }
        return productos;
    }

    // Método para generar un archivo de ventas solo con productos existentes
    public void createSalesFile(int cantidadVentasAleatorias, String nombreVendedor, long idVendedor, List<String> productosExistentes) {
        Random aleatorio = new Random();

        // Si no hay productos existentes, no puede generar ventas
        if (productosExistentes.isEmpty()) {
            System.out.println("No hay productos disponibles para generar ventas.");
            return;
        }

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreVendedor + "_" + idVendedor + "_ventas.txt"))) {
            escritor.write("CC: " + idVendedor + ";Nombre: "+ nombreVendedor +"\n"); // Datos del vendedor
            for (int i = 0; i < cantidadVentasAleatorias; i++) {
                // Seleccionar un producto existente aleatoriamente
                String idProducto = productosExistentes.get(aleatorio.nextInt(productosExistentes.size()));
                int cantidadVendida = aleatorio.nextInt(20) + 1; // Generar una cantidad vendida de forma aleatoria
                escritor.write("Producto:" + idProducto + ";" +"Cantidad:" + cantidadVendida + "\n");
            }
            System.out.println("Archivo de ventas generado para " + nombreVendedor);
        } catch (IOException e) {
            System.out.println("Error al crear archivo de ventas: " + e.getMessage());
        }
    }

    // Método para generar el archivo de productos
    public void createProductsFile(int cantidadProductos) {
        String[] productos = {"Tesseract", "Cetro de Loki", "Traje de Ant-Man", "Armadura de Iron Man", "Guante del infinito", "Gema del tiempo", "Gema del espacio", "Gema del poder", "Gema del alma"};
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("productos.txt"))) {
            Random aleatorio = new Random();
            for (int i = 0; i < cantidadProductos; i++) {
                int idProducto = aleatorio.nextInt(900000) + 100000; // Genera un número aleatorio entre 100000 y 999999
                String idProductoConFormato = String.format("%06d", idProducto); // Formatea el número a 6 dígitos
                String nombreProducto = productos[aleatorio.nextInt(productos.length)];
                double precioProducto = 10 + (100000 - 10) * aleatorio.nextDouble(); // Precio aleatorio entre 10 y 100000
                escritor.write(idProductoConFormato + ";" + nombreProducto + ";" + String.format("%.2f", precioProducto).replace(".", ",") + "\n");
            }
            System.out.println("Archivo de productos generado.");
        } catch (IOException e) {
            System.out.println("Error al crear archivo de productos: " + e.getMessage());
        }
    }

    // Método para generar un archivo de vendedores
    public void createVendorInfoFile(int cantidadVendedores) {
        String[] nombres = {"Peter", "Tony", "Wanda", "Steve", "Thor", "Bruce", "Clint", "Natasha", "Stephen"};
        String[] apellidos = {"Parker", "Stark", "Maximoff", "Rogers", "Odinson", "Banner", "Barton", "Romanoff", "Strange"};
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

        // 1. Generar el archivo de productos
        generador.createProductsFile(10);

        // 2. Leer los productos generados para usarlos en las ventas
        List<String> productosExistentes = generador.leerProductos("productos.txt");

        // 3. Generar el archivo de ventas usando solo productos existentes
        generador.createSalesFile(5, "Peter", 12345678, productosExistentes);

        // 4. Generar el archivo de vendedores (opcional, pero como en la primera entrega)
        generador.createVendorInfoFile(5);
    }
}
