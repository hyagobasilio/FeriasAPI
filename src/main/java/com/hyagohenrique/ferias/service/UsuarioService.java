package com.hyagohenrique.ferias.service;

import java.util.Optional;

import com.hyagohenrique.ferias.irepository.IUsuarioRepository;
import com.hyagohenrique.ferias.iservice.IUsuarioService;
import com.hyagohenrique.ferias.model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public Usuario save(Usuario u) {
        return this.usuarioRepository.save(u);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return this.usuarioRepository.findByEmail(email);
    }
   
}