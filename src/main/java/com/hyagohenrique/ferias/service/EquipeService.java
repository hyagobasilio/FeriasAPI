package com.hyagohenrique.ferias.service;

import java.util.List;

import com.hyagohenrique.ferias.irepository.IEquipeRepository;
import com.hyagohenrique.ferias.iservice.IEquipeService;
import com.hyagohenrique.ferias.model.Equipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipeService implements IEquipeService {
    @Autowired
    private IEquipeRepository equipeRepository;

    @Override
    public Equipe salvar(Equipe equipe) {
        return this.equipeRepository.save(equipe);
    }

    @Override
    public List<Equipe> listarEquipes() {
        return this.equipeRepository.findAll();
    }
    
}