package com.hyagohenrique.ferias.irepository;

import java.util.Date;
import java.util.List;

import com.hyagohenrique.ferias.model.Ferias;
import com.hyagohenrique.ferias.model.Funcionario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IFeriasRepository extends JpaRepository<Ferias, Long> {
    List<Ferias> findAllByFuncionarioMatricula(String matricula);
}