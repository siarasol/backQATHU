package org.example.backendfi2.service;


import org.example.backendfi2.model.Archivos;
import org.example.backendfi2.model.ArchivoProducto;
import org.example.backendfi2.model.Categoria;
import org.example.backendfi2.model.Producto;
import org.example.backendfi2.repository.ArchivoProductoRepository;
import org.example.backendfi2.repository.ArchivoRepository;
import org.example.backendfi2.repository.CategoriaRepository;

import org.example.backendfi2.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.example.backendfi2.dto.ProductoDTO;
import org.example.backendfi2.dto.ArchivoProductoDTO;

@Service
public class ProductoService {

    // Ruta base para almacenar las imágenes
    private static final String STORAGE_PATH = "/u03/jboss/data/Qhatu/imagenes/";

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ArchivoRepository archivoRepository;

    @Autowired
    private ArchivoProductoRepository archivoProductoRepository;






    public Producto crearProducto(Producto producto, Long categoriaId, List<MultipartFile> archivos) throws IOException {
        try {
            // Buscar la categoría por ID
            Optional<Categoria> categoria = categoriaRepository.findById(categoriaId);
            if (!categoria.isPresent()) {
                throw new IllegalArgumentException("Categoría no encontrada");
            }

            // Asignar la categoría al producto
            producto.setCategoria(categoria.get());
            producto.setCreatedAt(LocalDateTime.now());

            // Guardar el producto

            Producto productoGuardado = productoRepository.save(producto);

            // Formato de fecha y hora para el nombre del archivo
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

            // Guardar las imágenes asociadas al producto
            for (MultipartFile archivo : archivos) {
                // Obtener la extensión del archivo
                String fileExtension = archivo.getOriginalFilename() != null ?
                        archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf(".")) : "";

                // Generar el nuevo nombre del archivo con fecha y hora
                String fileName = System.currentTimeMillis() + "_" + dateTimeFormatter.format(LocalDateTime.now()) + fileExtension;

                // Generar la ruta completa del archivo
                String filePath = STORAGE_PATH + fileName;
                Path path = Paths.get(filePath);

                // Guardar el archivo físicamente
                Files.write(path, archivo.getBytes());

                // Crear un nuevo objeto Archivo y setear la ruta
                Archivos archivoGuardado = new Archivos();
                archivoGuardado.setActive(true);
                archivoGuardado.setCreatedBy(producto.getCreatedBy());
                archivoGuardado.setCreatedAt(LocalDateTime.now());
                archivoGuardado.setNombre(fileName);
                archivoGuardado.setPath(filePath);
                archivoGuardado.setTipoArchivo(archivo.getContentType());
                archivoRepository.save(archivoGuardado);

                // Relacionar el archivo con el producto
                ArchivoProducto archivoProducto = new ArchivoProducto();
                archivoProducto.setCreatedAt(LocalDateTime.now());
                archivoProducto.setCreatedBy(producto.getCreatedBy());
                archivoProducto.setArchivos(archivoGuardado);
                archivoProducto.setProducto(productoGuardado);
                archivoProductoRepository.save(archivoProducto);
            }

            return productoGuardado;

        } catch (IOException e) {
            throw new IOException("Error al guardar el archivo", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el producto", e);
        }
    }

    // Obtener un producto por ID
    public Optional<ProductoDTO> obtenerProductoPorId(Long id) {
        try {
            // Buscar el producto por ID
            Optional<Producto> productoOpt = productoRepository.findById(id);

            if (productoOpt.isPresent()) {
                Producto producto = productoOpt.get();

                // Convertir las imágenes asociadas en ArchivoProductoDTO con URL accesible
                List<ArchivoProductoDTO> archivosDTO = producto.getArchivos().stream()
                        .map(archivo -> {
                            try {
                                // Convertir la ruta local en una URL accesible
                                String[] pathParts = archivo.getArchivos().getPath().split("/");
                                String filename = pathParts[pathParts.length - 1];
                                String url = "http://localhost:8080/imagenes/" + filename;
                                return new ArchivoProductoDTO(archivo.getId(), url);
                            } catch (Exception e) {
                                // Manejar la excepción si ocurre un error al construir la URL
                                throw new RuntimeException("Error al generar la URL de la imagen", e);
                            }
                        })
                        .collect(Collectors.toList());

                // Crear el ProductoDTO con las imágenes convertidas a ArchivoProductoDTO
                ProductoDTO productoDTO = new ProductoDTO(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getStock(),
                        producto.getDescripcion(),
                        producto.getPrecio(),
                        archivosDTO
                );

                return Optional.of(productoDTO);
            }
            return Optional.empty();
        } catch (Exception e) {
            // Manejo de excepción general
            throw new RuntimeException("Error al obtener el producto con ID " + id, e);
        }
    }

    public List<ProductoDTO> listarProductos(String createdBy) {
        try {
            List<Producto> productos = productoRepository.findByCreatedBy(createdBy);

            return productos.stream()
                    .map(producto -> new ProductoDTO(
                            producto.getId(),
                            producto.getNombre(),
                            producto.getStock(),
                            producto.getDescripcion(),
                            producto.getPrecio(),
                            producto.getArchivos().stream()
                                    .map(archivo -> {
                                        // Convertir la ruta local en una URL accesible
                                        String filename = archivo.getArchivos().getPath().split("/")[archivo.getArchivos().getPath().split("/").length - 1];
                                        String url = "http://localhost:8080/imagenes/" + filename;
                                        return new ArchivoProductoDTO(archivo.getId(), url);
                                    })
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al listar productos filtrados por 'createdBy'", e);
        }
    }

    public List<ProductoDTO> listarProductossn() {
        try {
            List<Producto> productos = productoRepository.findAll();

            return productos.stream()
                    .map(producto -> new ProductoDTO(
                            producto.getId(),
                            producto.getNombre(),
                            producto.getStock(),
                            producto.getDescripcion(),
                            producto.getPrecio(),
                            producto.getArchivos().stream()
                                    .map(archivo -> {
                                        // Convertir la ruta local en una URL accesible
                                        String filename = archivo.getArchivos().getPath().split("/")[archivo.getArchivos().getPath().split("/").length - 1];
                                        String url = "http://localhost:8080/imagenes/" + filename;
                                        return new ArchivoProductoDTO(archivo.getId(), url);
                                    })
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al listar productos filtrados por 'createdBy'", e);
        }
    }


    // Eliminar un producto
    public void eliminarProducto(Long id) {
        try {
            productoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el producto", e);
        }
    }
}
