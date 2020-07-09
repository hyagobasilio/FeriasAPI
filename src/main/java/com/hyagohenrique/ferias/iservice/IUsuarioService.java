package com.hyagohenrique.ferias.iservice;

import java.util.Optional;

import com.hyagohenrique.ferias.model.Usuario;

public interface IUsuarioService {
    Usuario save(Usuario u);
	
	Optional<Usuario> findByEmail(String email);
}