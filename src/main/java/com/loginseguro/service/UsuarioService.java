package com.loginseguro.service;

import com.loginseguro.entity.Perfil;
import com.loginseguro.entity.Usuario;
import com.loginseguro.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario cadastrar(Usuario usuario) {
        return salvarNovo(usuario, Perfil.USUARIO);
    }

    public Usuario salvarNovo(Usuario usuario, Perfil perfil) {
        usuario.setId(null);
        usuario.setEmail(usuario.getEmail().trim().toLowerCase());
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setPerfil(perfil);
        return usuarioRepository.save(usuario);
    }

    public boolean emailJaCadastrado(String email) {
        return usuarioRepository.existsByEmail(email.trim().toLowerCase());
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public void alterarPerfil(String id, Perfil perfil) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setPerfil(perfil);
        usuarioRepository.save(usuario);
    }

    public void excluir(String id) {
        usuarioRepository.deleteById(id);
    }
}
