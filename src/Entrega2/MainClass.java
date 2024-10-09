package Entrega2; //Entrega Final

//Brian Steven Zambrano Hurtado
//Oscar Javier Romero Beltran
//Yennifer Tatiana Villa Zapata
//Yuli Estefanny Florez Velasquez
//Omar Enrique Leguizamón Rodriguez


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainClass {

	public static Map<String, String[]> leerProductos(String filePath) {
	   
	    Map<String, String[]> productos = new HashMap<>();
	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String linea;
	        while ((linea = br.readLine()) != null) {
	            String[] partes = linea.split(";");
	            if (partes.length >= 3) {
	                String idProducto = partes[0].trim();
	                String nombreProducto = partes[1].trim();
	                String precioTexto = partes[2].trim();
	                try {
	                   
	                    String precioConFormatoCorrecto = precioTexto.replace(",", ".");
	                    productos.put(idProducto, new String[] {nombreProducto, precioConFormatoCorrecto});
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


    public static Map<String, Double> calcularVentasPorVendedor(String folderPath, Map<String, Double> preciosProductos) {
        Map<String, Double> ventasPorVendedor = new HashMap<>();

        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("Error: La carpeta de ventas no existe o está vacía.");
            return ventasPorVendedor;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith("_ventas.txt")) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String primeraLinea = br.readLine();
                    if (primeraLinea != null) {
                        String[] partesVendedor = primeraLinea.split(";");
                        if (partesVendedor.length >= 2) {
                            String idVendedor = partesVendedor[0].split(":")[1].trim();
                            double totalVentas = 0.0;

                            String linea;
                            while ((linea = br.readLine()) != null) {
                                String[] partesVenta = linea.split(";");
                                if (partesVenta.length >= 2) {
                                    String idProducto = partesVenta[0].split(":")[1].trim();
                                    int cantidadVendida = Integer.parseInt(partesVenta[1].split(":")[1].trim());

                                    Double precioProducto = preciosProductos.get(idProducto);
                                    if (precioProducto != null) {
                                        if (cantidadVendida < 0 || precioProducto < 0) {
                                            System.out.println("Advertencia: Cantidad o precio negativo para el producto con ID " + idProducto);
                                        } else {
                                            totalVentas += precioProducto * cantidadVendida;
                                        }
                                    } else {
                                        System.out.println("Advertencia: Producto con ID " + idProducto + " no encontrado en el archivo de productos.");
                                    }
                                }
                            }
                            ventasPorVendedor.put(idVendedor, totalVentas);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error al leer el archivo de ventas: " + e.getMessage());
                }
            }
        }
        return ventasPorVendedor;
    }

    public static Map<String, Integer> calcularCantidadVendidaPorProducto(String folderPath) {
        Map<String, Integer> productosVendidos = new HashMap<>();

        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("Error: La carpeta de ventas no existe o está vacía.");
            return productosVendidos;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith("_ventas.txt")) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    br.readLine(); 

                    String linea;
                    while ((linea = br.readLine()) != null) {
                        String[] partesVenta = linea.split(";");
                        if (partesVenta.length >= 2) {
                            String idProducto = partesVenta[0].split(":")[1].trim();
                            int cantidadVendida = Integer.parseInt(partesVenta[1].split(":")[1].trim());

                            productosVendidos.put(idProducto, productosVendidos.getOrDefault(idProducto, 0) + cantidadVendida);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error al leer archivo de ventas: " + e.getMessage());
                }
            }
        }
        return productosVendidos;
    }

    public static void generarReporteVendedores(String outputPath, Map<String, Double> ventasPorVendedor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            bw.write("ID Vendedor; Total Ventas\n");

            ventasPorVendedor.entrySet()
                .stream()
                .sorted((v1, v2) -> Double.compare(v2.getValue(), v1.getValue())) 
                .forEach(entry -> {
                    try {
                        bw.write(entry.getKey() + "; " + entry.getValue() + "\n");
                    } catch (IOException e) {
                        System.out.println("Error al escribir en el reporte de vendedores: " + e.getMessage());
                    }
                });

            System.out.println("Reporte de vendedores generado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al generar el reporte de vendedores: " + e.getMessage());
        }
    }

    public static void generarReporteProductosVendidos(String outputPath, Map<String, Integer> productosVendidos, Map<String, String[]> productos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            bw.write("ID Producto; Nombre Producto; Cantidad Vendida; Precio Unitario\n");

            productosVendidos.entrySet()
                .stream()
                .sorted((p1, p2) -> Integer.compare(p2.getValue(), p1.getValue()))
                .forEach(entry -> {
                    try {
                        String idProducto = entry.getKey();
                        int cantidad = entry.getValue();
                        String[] datosProducto = productos.get(idProducto);
                        if (datosProducto != null) {
                            String nombreProducto = datosProducto[0];
                            String precioProducto = datosProducto[1];
                            bw.write(idProducto + "; " + nombreProducto + "; " + cantidad + "; " + precioProducto + "\n");
                        } else {
                            System.out.println("Producto con ID " + idProducto + " no encontrado.");
                        }
                    } catch (IOException e) {
                        System.out.println("Error al escribir en el reporte de productos vendidos: " + e.getMessage());
                    }
                });

            System.out.println("Reporte de productos vendidos generado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al generar el reporte de productos vendidos: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        String productosFilePath = "productos.txt";
        String ventasFolderPath = "C:/Users/ORomero/eclipse-workspace/Proyecto_Vendedores";
        String reporteVendedoresPath = "reporte_vendedores.txt";
        String reporteProductosPath = "reporte_productos_vendidos.txt";

        
        Map<String, String[]> productos = leerProductos(productosFilePath);
        Map<String, Double> ventasPorVendedor = calcularVentasPorVendedor(ventasFolderPath, getPreciosFromProductos(productos));
        Map<String, Integer> productosVendidos = calcularCantidadVendidaPorProducto(ventasFolderPath);

        generarReporteVendedores(reporteVendedoresPath, ventasPorVendedor);
        generarReporteProductosVendidos(reporteProductosPath, productosVendidos, productos);
    }

    
    public static Map<String, Double> getPreciosFromProductos(Map<String, String[]> productos) {
        Map<String, Double> precios = new HashMap<>();
        for (Map.Entry<String, String[]> entry : productos.entrySet()) {
            precios.put(entry.getKey(), Double.parseDouble(entry.getValue()[1]));
        }
        return precios;
    }

}

