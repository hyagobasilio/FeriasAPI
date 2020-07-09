package com.hyagohenrique.ferias.iservice;

import java.util.List;

import com.hyagohenrique.ferias.model.Ferias;

public interface IFeriasService {
    

    Ferias salvar(Ferias ferias);

    boolean removerFerias(Ferias ferias);

    List<Ferias> buscarFeriasPorMatriculaDoFuncionario(String matricula);
}