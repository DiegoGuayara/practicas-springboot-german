package com.example.practica1.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import com.example.practica1.dto.LoginRequestDTO;
import com.example.practica1.dto.LoginResponseDTO;
import com.example.practica1.models.Usuario;
import com.example.practica1.servicios.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.createUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED); // 201
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = usuarioService.login(loginRequest);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Credenciales inválidas");
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) {
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return usuarioService.updateUsuario(id, usuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        if (usuarioService.deleteUsuario(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
