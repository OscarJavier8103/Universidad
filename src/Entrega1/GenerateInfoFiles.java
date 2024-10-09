package Entrega1; //Entrega final

//Brian Steven Zambrano Hurtado
//Oscar Javier Romero Beltran
//Yennifer Tatiana Villa Zapata
//Yuli Estefanny Florez Velasquez
//Omar Enrique Leguizamón Rodriguez

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateInfoFiles {

    public List<String> leerProductos(String filePath) {
        List<String> productos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 1) {
                    String idProducto = partes[0].trim();
                    productos.add(idProducto);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de productos: " + e.getMessage());
        }
        return productos;
    }

    public void createSalesFile(int cantidadVentasAleatorias, String nombreVendedor, long idVendedor, List<String> productosExistentes) {
        Random aleatorio = new Random();

        if (productosExistentes.isEmpty()) {
            System.out.println("No hay productos disponibles para generar ventas.");
            return;
        }

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nombreVendedor + "_" + idVendedor + "_ventas.txt"))) {
            escritor.write("CC: " + idVendedor + ";Nombre: " + nombreVendedor + "\n");

            for (int i = 0; i < cantidadVentasAleatorias; i++) {
                String idProducto = productosExistentes.get(aleatorio.nextInt(productosExistentes.size()));
                int cantidadVendida = aleatorio.nextInt(20) + 1;
                escritor.write("Producto:" + idProducto + ";Cantidad:" + cantidadVendida + "\n");
            }
            System.out.println("Archivo de ventas generado para " + nombreVendedor);
        } catch (IOException e) {
            System.out.println("Error al crear archivo de ventas: " + e.getMessage());
        }
    }

    public void createProductsFile(int cantidadProductos) {
        String[] productos = {"Tesseract", "Cetro de Loki", "Traje de Ant-Man", "Armadura de Iron Man", "Guante del infinito", "Gema del tiempo", "Gema del espacio", "Gema del poder", "Gema del alma"};
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("productos.txt"))) {
            Random aleatorio = new Random();
            for (int i = 0; i < cantidadProductos; i++) {
                int idProducto = aleatorio.nextInt(900000) + 100000;
                String idProductoConFormato = String.format("%06d", idProducto);
                String nombreProducto = productos[aleatorio.nextInt(productos.length)];
                double precioProducto = 10 + (100000 - 10) * aleatorio.nextDouble();
                escritor.write(idProductoConFormato + ";" + nombreProducto + ";" + String.format("%.2f", precioProducto).replace(".", ",") + "\n");
            }
            System.out.println("Archivo de productos generado.");
        } catch (IOException e) {
            System.out.println("Error al crear archivo de productos: " + e.getMessage());
        }
    }

    public void createVendorInfoFile(int cantidadVendedores) {
        String[] nombres = {"Peter", "Tony", "Wanda", "Steve", "Thor", "Bruce", "Clint", "Natasha", "Stephen"};
        String[] apellidos = {"Parker", "Stark", "Maximoff", "Rogers", "Odinson", "Banner", "Barton", "Romanoff", "Strange"};
        Random aleatorio = new Random();

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("vendedores.txt"))) {
            for (int i = 0; i < cantidadVendedores; i++) {
                String tipoDocumento = "CC";
                long numeroDocumento = 10000000 + aleatorio.nextInt(90000000);
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
        generador.createProductsFile(10);
        List<String> productosExistentes = generador.leerProductos("productos.txt");
        generador.createSalesFile(5, "Peter", 12345678, productosExistentes);
        generador.createVendorInfoFile(5);
    }
}
