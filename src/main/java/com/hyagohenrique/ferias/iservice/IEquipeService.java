package com.hyagohenrique.ferias.iservice;

import java.util.List;

import com.hyagohenrique.ferias.model.Equipe;

public interface IEquipeService {
    
    Equipe salvar(Equipe equipe);

    List<Equipe> listarEquipes();
}