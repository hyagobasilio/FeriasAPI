package com.hyagohenrique.ferias.service;

import java.util.List;

import com.hyagohenrique.ferias.exception.NotFoundException;
import com.hyagohenrique.ferias.irepository.IFeriasRepository;
import com.hyagohenrique.ferias.iservice.IFeriasService;
import com.hyagohenrique.ferias.iservice.IFuncionarioService;
import com.hyagohenrique.ferias.model.Ferias;
import com.hyagohenrique.ferias.model.Funcionario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeriasService implements IFeriasService {


    @Autowired
    private IFeriasRepository feriasRepository;

    @Autowired
    private IFuncionarioService funcionarioService;


    @Override
    public Ferias salvar(Ferias ferias) {
    
        Funcionario funcionario = this.funcionarioService.buscarPorId(ferias.getFuncionario().getId());
        this.funcionarioService.validaSeFuncionarioTemUmAnoDeEmpresa(funcionario);
        this.funcionarioService.validaSeFuncionarioEstaImpedidoDeTirarFeriasPorContaDeOutraPessoaDaMesmaEquipe(funcionario.getEquipe().getId(), ferias.getInicio());
        
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
    public boolean deletar(Long id) {
        try {
            this.feriasRepository.deleteById(id);
            return true;
        }catch(Exception e) {
            throw new NotFoundException("Ferias não encontrada com este id: " + id);
        }
    }
    @Override
    public List<Ferias> listar() {
        return this.feriasRepository.findAll();
    }

    @Override
    public List<Ferias> buscarFeriasPorMatriculaDoFuncionario(String matricula) {
        return this.feriasRepository.findAllByFuncionarioMatricula(matricula);
    }
    
}