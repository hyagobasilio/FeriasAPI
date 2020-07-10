package com.hyagohenrique.ferias.irepository;

import java.util.List;

import com.hyagohenrique.ferias.model.Ferias;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IFeriasRepository extends JpaRepository<Ferias, Long> {
    List<Ferias> findAllByFuncionarioMatricula(String matricula);
}