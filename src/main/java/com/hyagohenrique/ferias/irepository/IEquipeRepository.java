package com.hyagohenrique.ferias.irepository;

import com.hyagohenrique.ferias.model.Equipe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IEquipeRepository extends JpaRepository<Equipe, Long> {
    
}