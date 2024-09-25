package Entrega2; //semana 5

//Brian Steven Zambrano Hurtado
//Oscar Javier Romero Beltran

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainClass {

    // Método para leer el archivo de productos y cargar los precios
    public static Map<String, Double> leerProductos(String filePath) {
        Map<String, Double> productos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    String idProducto = partes[0].trim();
                    String precioTexto = partes[2].trim();
                    try {
                        // Reemplazamos las comas por puntos antes de convertir
                        String precioConFormatoCorrecto = precioTexto.replace(",", ".");
                        double precioProducto = Double.parseDouble(precioConFormatoCorrecto);
                        productos.put(idProducto, precioProducto);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: No se pudo convertir el precio del producto con ID " + idProducto + ". Valor: " + precioTexto);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de productos: " + e.getMessage());
        }
        return productos;
    }

    // Método para calcular las ventas por vendedor
    public static Map<String, Double> calcularVentasPorVendedor(String folderPath, Map<String, Double> preciosProductos) {
        Map<String, Double> ventasPorVendedor = new HashMap<>();
        File folder = new File(folderPath);

        // Verificar si el folder es válido y contiene archivos
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("Error: La carpeta de ventas no existe o está vacía.");
            return ventasPorVendedor;
        }

        for (File file : files) {
            // Procesar solo archivos que terminen con "_ventas.txt"
            if (file.isFile() && file.getName().endsWith("_ventas.txt")) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String primeraLinea = br.readLine();  // Leer la primera línea que contiene la info del vendedor
                    if (primeraLinea != null) {
                        String[] partesVendedor = primeraLinea.split(";");
                        if (partesVendedor.length >= 2) {
                            String idVendedor = partesVendedor[0].split(":")[1].trim(); 
                            double totalVentas = 0.0;

                            String linea;
                            while ((linea = br.readLine()) != null) {
                                String[] partesVenta = linea.split(";");
                                if (partesVenta.length >= 2) {
                                    String idProducto = partesVenta[0].split(":")[1].trim(); // Extraer ID de producto
                                    int cantidadVendida = Integer.parseInt(partesVenta[1].split(":")[1].trim()); // Extraer cantidad vendida
                                    Double precioProducto = preciosProductos.get(idProducto);
                                    if (precioProducto != null) {
                                        totalVentas += precioProducto * cantidadVendida;
                                    } else {
                                        System.out.println("Advertencia: Producto con ID " + idProducto + " no encontrado en el archivo de productos.");
                                    }
                                } else {
                                    System.out.println("Advertencia: Línea de venta mal formada en el archivo: " + file.getName());
                                }
                            }

                            // Actualizar total de ventas del vendedor
                            ventasPorVendedor.put(idVendedor, totalVentas);
                        } else {
                            System.out.println("Advertencia: Línea de vendedor mal formada en el archivo: " + file.getName());
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error al leer el archivo de ventas: " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: Formato inesperado en el archivo: " + file.getName());
                } catch (NumberFormatException e) {
                    System.out.println("Error: No se pudo convertir la cantidad vendida en el archivo: " + file.getName());
                }
            }
        }

        return ventasPorVendedor;
    }

    // Método para generar el reporte de vendedores
    public static void generarReporteVendedores(String outputPath, Map<String, Double> ventasPorVendedor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            bw.write("ID Vendedor; Total Ventas\n");
            for (Map.Entry<String, Double> entry : ventasPorVendedor.entrySet()) {
                bw.write(entry.getKey() + "; " + entry.getValue() + "\n");
            }
            System.out.println("Reporte de vendedores generado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al generar el reporte de vendedores: " + e.getMessage());
        }
    }

    // Método para generar el reporte de productos
    public static void generarReporteProductos(String outputPath, Map<String, Double> preciosProductos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            bw.write("ID Producto; Precio\n");
            for (Map.Entry<String, Double> entry : preciosProductos.entrySet()) {
                bw.write(entry.getKey() + "; " + entry.getValue() + "\n");
            }
            System.out.println("Reporte de productos generado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al generar el reporte de productos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String productosFilePath = "productos.txt";  // Ruta del archivo de productos
        String ventasFolderPath = "C:/Users/ORomero/eclipse-workspace/Proyecto_Vendedores"; // Carpeta que contiene los archivos de ventas
        String reporteVendedoresPath = "reporte_vendedores.txt"; // Ruta del reporte de vendedores
        String reporteProductosPath = "reporte_productos.txt";   // Ruta del reporte de productos

        // Leer productos
        Map<String, Double> preciosProductos = leerProductos(productosFilePath);
        
        // Calcular ventas por vendedor
        Map<String, Double> ventasPorVendedor = calcularVentasPorVendedor(ventasFolderPath, preciosProductos);
        
        // Generar reportes
        generarReporteVendedores(reporteVendedoresPath, ventasPorVendedor);
        generarReporteProductos(reporteProductosPath, preciosProductos);
    }
}
