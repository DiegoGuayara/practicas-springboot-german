package com.example.practica1.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.practica1.dto.LoginRequestDTO;
import com.example.practica1.dto.LoginResponseDTO;
import com.example.practica1.jwt.JwtService;
import com.example.practica1.models.Usuario;
import com.example.practica1.repositorios.UsuarioRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario createUsuario(Usuario usuario) {
        String passHasheado = passwordEncoder.encode(usuario.getContraseña());
        usuario.setContraseña(passHasheado);

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> updateUsuario(Integer id, Usuario usuarioDetails) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(usuarioDetails.getNombre());
            usuario.setEmail(usuarioDetails.getEmail());
            return usuarioRepository.save(usuario);
        });
    }

    public boolean deleteUsuario(Integer id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) throws AuthenticationException {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        String token = jwtService.generateToken(userDetails);

        return new LoginResponseDTO(token);
    }
}
