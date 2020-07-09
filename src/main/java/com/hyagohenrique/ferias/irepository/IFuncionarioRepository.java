package com.hyagohenrique.ferias.irepository;

import java.util.Date;
import java.util.List;

import com.hyagohenrique.ferias.model.Funcionario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IFuncionarioRepository extends JpaRepository<Funcionario, Long>{
    @Query("SELECT f FROM Funcionario f " 
        + " WHERE (f.dataContratacao BETWEEN :inicio AND :fim AND NOT EXISTS (SELECT fe FROM Ferias fe WHERE fe.funcionario = f )) "
        + " OR ( EXISTS (SELECT fe FROM Ferias fe WHERE fe.fim BETWEEN :inicio AND :fim AND fe.funcionario = f ) ) ")
    List<Funcionario> consultarFuncionariosQueIraoCompletar2AnosSemSolicitarFeriasEmNoMaximoXMeses(@Param("inicio") Date inicio, @Param("fim")  Date fim);
}