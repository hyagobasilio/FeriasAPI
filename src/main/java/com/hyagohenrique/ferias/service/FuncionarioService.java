package com.hyagohenrique.ferias.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import com.hyagohenrique.ferias.exception.FeriasNaoDisponivelException;
import com.hyagohenrique.ferias.exception.NotFoundException;
import com.hyagohenrique.ferias.irepository.IFuncionarioRepository;
import com.hyagohenrique.ferias.iservice.IFuncionarioService;
import com.hyagohenrique.ferias.model.Funcionario;
import com.hyagohenrique.ferias.model.UploadFileModel;
import com.hyagohenrique.ferias.service.s3.S3Service;
import com.hyagohenrique.ferias.utils.DateUtils;
import com.hyagohenrique.ferias.utils.QRCodeUtils;

import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Service
public class FuncionarioService implements IFuncionarioService {

    private static final String MSG_FERIAS_NAO_DISPONIVEL = "Funcionário com menos de um ano de contratado.";
    private static final String MSG_MEMBRO_EQUIPE_DE_FERIAS = "Já existe alguém da equipe de ferias!";

    @Autowired
    private IFuncionarioRepository funcionarioRepository;

    @Autowired
    private S3Service s3Service;

    //@Autowired private JavaMailSender mailSender;

    @Override
    public Funcionario salvar(Funcionario funcionario, MultipartFile file) {
        if (file != null) {
            UploadFileModel upload = this.s3Service.upload(file);
            funcionario.setAvatar(upload.getName());
            funcionario.setPathAvatar(upload.getLocation());
        }
        return this.salvar(funcionario);
    }

    @Override
    public Funcionario salvar(Funcionario funcionario) {
        funcionario = this.funcionarioRepository.save(funcionario);
        String matricula = StringUtils.leftPad(funcionario.getId().toString(), 6, '0');
        funcionario.setMatricula(matricula);
        funcionario = this.funcionarioRepository.save(funcionario);
        enviarEmailComQrCode(funcionario);
        return funcionario;
    }
    
    private void enviarEmailComQrCode(Funcionario funcionario) {
        log.info("enviando email");
        /* String qrCodeBase64 = QRCodeUtils.gerarQRCodeAPartirDeFuncionarioDTO(funcionario.converteParaDTO(), 300, 150);
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            
            MimeMessageHelper helper = new MimeMessageHelper( mail );
            helper.setTo( "hyagohba@gmail.com" );
            helper.setSubject( "Teste Envio de e-mail com Spring Boot" );
            helper.setText("<p>Seu QrCode</p> <img src=\""+qrCodeBase64+"\" title=\"QrCode\">", true);
            mailSender.send(mail);

        } catch (Exception e) {
            e.printStackTrace();
        } */
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