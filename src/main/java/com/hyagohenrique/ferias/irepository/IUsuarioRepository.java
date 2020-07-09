package com.hyagohenrique.ferias.irepository;

import java.util.Optional;

import com.hyagohenrique.ferias.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    Usuario save(Usuario usuario);
}