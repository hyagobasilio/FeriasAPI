package com.hyagohenrique.ferias.controller;

import com.hyagohenrique.ferias.dto.UsuarioDTO;
import com.hyagohenrique.ferias.exception.NotFoundException;
import com.hyagohenrique.ferias.irepository.IUsuarioRepository;
import com.hyagohenrique.ferias.model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "usuario",  produces = MediaType.APPLICATION_JSON_VALUE  )
public class UsuarioController {
    @Autowired
    IUsuarioRepository usuarioRepository;
    
    @GetMapping("/whoami")
    public UsuarioDTO user() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDetails user = (UserDetails) principal;

        Usuario usuario = this.usuarioRepository.findByEmail(user.getUsername())
        .orElseThrow(() -> new NotFoundException("Recurso não encontrado"));
        
                return usuario.converterParaDTO();

    }
}