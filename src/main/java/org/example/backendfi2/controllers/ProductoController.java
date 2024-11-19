package org.example.backendfi2.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backendfi2.dto.ApiResponse;
import org.example.backendfi2.dto.ProductoDTO;
import org.example.backendfi2.model.Producto;
import org.example.backendfi2.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Crear un nuevo producto con imágenes
    @PostMapping(value = "/crear", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> crearProducto(
            @RequestPart("producto") String productoJson,  // Recibe el JSON como String
            @RequestParam Long categoriaId,
            @RequestParam Long comunidadId,
            @RequestPart("archivos") List<MultipartFile> archivos) {
        try {
            // Deserializar el JSON del producto
            ObjectMapper objectMapper = new ObjectMapper();
            Producto producto = objectMapper.readValue(productoJson, Producto.class);

            Producto productoGuardado = productoService.crearProducto(producto, categoriaId, comunidadId, archivos);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse(true, "Producto creado con éxito", productoGuardado)
            );
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse(false, "Error al guardar los archivos", null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse(false, "Error al crear el producto: " + e.getMessage(), null)
            );
        }
    }





    // Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> obtenerProducto(@PathVariable Long id) {
        try {
            Optional<ProductoDTO> producto = productoService.obtenerProductoPorId(id);
            if (producto.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(true, "Producto encontrado", producto.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Producto no encontrado", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Error al obtener el producto", null));
        }
    }

    // Listar todos los productos
    @GetMapping("/listar")
    public ResponseEntity<ApiResponse> listarProductos(@RequestParam(required = false) String createdBy) {
        try {
            List<ProductoDTO> productos;

            // Si el parámetro createdBy no es nulo, filtramos por createdBy
           // if (createdBy != null && !createdBy.isEmpty()) {
                productos = productoService.listarProductos(createdBy);
            //}

            return ResponseEntity.ok(new ApiResponse(true, "Listado de productos", productos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Error al listar productos", null));
        }
    }
    @GetMapping("/listarAll")
    public ResponseEntity<ApiResponse> listarProductos() {
        try {
            List<ProductoDTO> productos;

            // Si el parámetro createdBy no es nulo, filtramos por createdBy
            // if (createdBy != null && !createdBy.isEmpty()) {
            productos = productoService.listarProductossn();
            //}

            return ResponseEntity.ok(new ApiResponse(true, "Listado de productos", productos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Error al listar productos", null));
        }
    }
    // Eliminar un producto
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponse> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok(new ApiResponse(true, "Producto eliminado con éxito", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Error al eliminar el producto", null));
        }
    }
}