package com.ezbar.security;

import ClasesBD.Usuario;
import Repositorios.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio personalizado para cargar detalles de usuario desde la base de datos.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("[DEBUG-AUTH] Intentando autenticar usuario: " + username);
        
        Usuario usuario = usuarioRepository.findByNombre(username)
                .orElseGet(() -> {
                    System.out.println("[DEBUG-AUTH] Usuario NO encontrado en la base de datos: " + username);
                    return null;
                });

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        System.out.println("[DEBUG-AUTH] Usuario encontrado. Rol: " + usuario.getRol() + ", Activo: " + usuario.isActivo());

        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException("El usuario está inactivo: " + username);
        }

        // Convertimos nuestra entidad Usuario a UserDetails de Spring Security
        return User.withUsername(usuario.getNombre())
                .password(usuario.getPassword())
                .roles(usuario.getRol())
                .build();
    }
}
