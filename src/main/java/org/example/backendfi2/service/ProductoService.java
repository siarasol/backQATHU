package org.example.backendfi2.service;

import org.example.backendfi2.service.CloudinaryService;
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

    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductoService(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }




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
                // Subir el archivo a Cloudinary y obtener la URL pública
                String imageUrl = cloudinaryService.uploadImage(archivo); // Asegúrate de tener este método en CloudinaryService

                // Crear un nuevo objeto Archivos y setear la URL pública en lugar de la ruta física
                Archivos archivoGuardado = new Archivos();
                archivoGuardado.setActive(true);
                archivoGuardado.setCreatedBy(producto.getCreatedBy());
                archivoGuardado.setCreatedAt(LocalDateTime.now());
                archivoGuardado.setNombre(archivo.getOriginalFilename());
                archivoGuardado.setPath(imageUrl); // Guardar la URL pública en el campo `path`
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
            throw new IOException("Error al subir el archivo a Cloudinary", e);
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

                // Convertir las imágenes asociadas en ArchivoProductoDTO usando la URL de Cloudinary almacenada en la base de datos
                List<ArchivoProductoDTO> archivosDTO = producto.getArchivos().stream()
                        .map(archivo -> {
                            try {
                                // Usar la URL de Cloudinary almacenada en la base de datos
                                String url = archivo.getArchivos().getPath(); // Asegúrate de que 'path' contiene la URL de Cloudinary
                                return new ArchivoProductoDTO(archivo.getId(), url);
                            } catch (Exception e) {
                                // Manejar la excepción si ocurre un error
                                throw new RuntimeException("Error al obtener la URL de la imagen desde Cloudinary", e);
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
                                        // Usar directamente la URL de Cloudinary almacenada en el campo `path`
                                        String url = archivo.getArchivos().getPath(); // URL completa de Cloudinary
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
                                        // Utilizar la URL completa de Cloudinary almacenada en `archivo.getArchivos().getPath`
                                        String url = archivo.getArchivos().getPath(); // URL completa de Cloudinary
                                        return new ArchivoProductoDTO(archivo.getId(), url);
                                    })
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al listar productos", e);
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
