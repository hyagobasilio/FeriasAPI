package com.hyagohenrique.ferias.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.hyagohenrique.ferias.exception.FeriasNaoDisponivelException;
import com.hyagohenrique.ferias.exception.NotFoundException;
import com.hyagohenrique.ferias.irepository.IFuncionarioRepository;
import com.hyagohenrique.ferias.iservice.IFuncionarioService;
import com.hyagohenrique.ferias.model.Funcionario;
import com.hyagohenrique.ferias.utils.DateUtils;

import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Service
public class FuncionarioService implements IFuncionarioService {

    private static final String MSG_FERIAS_NAO_DISPONIVEL = "Funcionário com menos de um ano de contratado.";
    private static final String MSG_MEMBRO_EQUIPE_DE_FERIAS = "Já existe alguém da equipe de ferias!";

    @Autowired
    private IFuncionarioRepository funcionarioRepository;

    @Override
    public Funcionario salvar(Funcionario funcionario) {
        funcionario = this.funcionarioRepository.save(funcionario);
        String matricula = StringUtils.leftPad(funcionario.getId().toString(), 6, '0');
        funcionario.setMatricula(matricula);
        funcionario = this.funcionarioRepository.save(funcionario);

        return funcionario;
    }

    @Override
    public List<Funcionario> listarFuncionarios() {
        return this.funcionarioRepository.findAll();
    }

    @Override
    public Funcionario buscarPorId(Long id) {
        log.info("buscano funcionario por id: " + id );
        return this.funcionarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Recurso não encontrado para o ID: " + id));
    }

    @Override
    public List<Funcionario> listarFuncionarioQueDevemTirarFerias(int numeroDeMeses) {
        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.minusYears(2);
        LocalDate fim  = inicio.minusMonths(numeroDeMeses);
        
        Date inicioDate = DateUtils.convertLocalDateToDate(inicio);
        Date fimDate = DateUtils.convertLocalDateToDate(fim);
        return  this.funcionarioRepository.consultarFuncionariosQueIraoCompletar2AnosSemSolicitarFeriasEmNoMaximoXMeses(inicioDate, fimDate);
    }
    @Override
    public void validaSeFuncionarioTemUmAnoDeEmpresa(Funcionario funcionario) {
        LocalDate umAnoAtras = LocalDate.now().minusYears(1);
        Date umAnoAtrasDate = DateUtils.convertLocalDateToDate(umAnoAtras);
        
        if(umAnoAtrasDate.compareTo(funcionario.getDataContratacao()) < 0 ) {
            throw new FeriasNaoDisponivelException(MSG_FERIAS_NAO_DISPONIVEL);
        }
        
    }
    
    @Override
    public void validaSeFuncionarioEstaImpedidoDeTirarFeriasPorContaDeOutraPessoaDaMesmaEquipe(Long idEquipe, Date inicio) {
        Long quantidadeDeMenbrosDaEquipe = this.funcionarioRepository.countByEquipeId(idEquipe);
        List<Funcionario> lista = this.funcionarioRepository.funcionarioQueEstaoDeFeriasNoMesmoTempoDesejado(idEquipe, inicio);
        
        // se a equipe for de ate 4 membros e 
        // se tem alguem de ferias no mesmo periodo.  Lanca uma excecao
        if (quantidadeDeMenbrosDaEquipe < 5 && !lista.isEmpty() ) {
            throw new FeriasNaoDisponivelException(MSG_MEMBRO_EQUIPE_DE_FERIAS);
        }   
    }
}