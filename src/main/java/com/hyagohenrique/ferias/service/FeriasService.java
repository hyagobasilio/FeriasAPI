package com.hyagohenrique.ferias.service;

import java.util.List;

import com.hyagohenrique.ferias.irepository.IFeriasRepository;
import com.hyagohenrique.ferias.iservice.IFeriasService;
import com.hyagohenrique.ferias.model.Ferias;

import org.springframework.beans.factory.annotation.Autowired;

public class FeriasService implements IFeriasService {


    @Autowired
    private IFeriasRepository feriasRepository;

    @Override
    public Ferias salvar(Ferias ferias) {
        return this.feriasRepository.save(ferias);
    }

    @Override
    public boolean removerFerias(Ferias ferias) {
        try {
            this.feriasRepository.delete(ferias);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    @Override
    public List<Ferias> buscarFeriasPorMatriculaDoFuncionario(String matricula) {
        return this.feriasRepository.findAllByFuncionarioMatricula(matricula);
    }
    
}