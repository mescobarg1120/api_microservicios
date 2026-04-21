package com.example.gestion_usuarios.controller;
import com.example.gestion_usuarios.entity.Tienda;
import com.example.gestion_usuarios.service.TiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tiendas")
public class TiendaController {

    private final TiendaService tiendaService;

    @Autowired
    public TiendaController(TiendaService tiendaService) {
        this.tiendaService = tiendaService;
    }

    @GetMapping
    public ResponseEntity<List<Tienda>> obtenerTodasLasTiendas() {
        List<Tienda> tiendas = tiendaService.obtenerTodasLasTiendas();
        return new ResponseEntity<>(tiendas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tienda> obtenerTiendaPorId(@PathVariable Long id) {
        Tienda tienda = tiendaService.obtenerTiendaPorId(id);
        return new ResponseEntity<>(tienda, HttpStatus.OK);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Tienda> obtenerTiendaPorNombre(@PathVariable String nombre) {
        Tienda tienda = tiendaService.obtenerTiendaPorNombre(nombre);
        return new ResponseEntity<>(tienda, HttpStatus.OK);
    }

    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<List<Tienda>> obtenerTiendasPorCiudad(@PathVariable String ciudad) {
        List<Tienda> tiendas = tiendaService.obtenerTiendasPorCiudad(ciudad);
        return new ResponseEntity<>(tiendas, HttpStatus.OK);
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<Tienda>> obtenerTiendasPorRegion(@PathVariable String region) {
        List<Tienda> tiendas = tiendaService.obtenerTiendasPorRegion(region);
        return new ResponseEntity<>(tiendas, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tienda> crearTienda( @RequestBody Tienda tienda) {
        Tienda nuevaTienda = tiendaService.crearTienda(tienda);
        return new ResponseEntity<>(nuevaTienda, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tienda> actualizarTienda(@PathVariable Long id,  @RequestBody Tienda tienda) {
        Tienda tiendaActualizada = tiendaService.actualizarTienda(id, tienda);
        return new ResponseEntity<>(tiendaActualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarTienda(@PathVariable Long id) {
        tiendaService.eliminarTienda(id);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Tienda eliminada correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
