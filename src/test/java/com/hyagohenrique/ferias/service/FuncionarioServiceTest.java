package com.hyagohenrique.ferias.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hyagohenrique.ferias.exception.FeriasNaoDisponivelException;
import com.hyagohenrique.ferias.exception.NotFoundException;
import com.hyagohenrique.ferias.irepository.IEquipeRepository;
import com.hyagohenrique.ferias.irepository.IFuncionarioRepository;
import com.hyagohenrique.ferias.iservice.IFuncionarioService;
import com.hyagohenrique.ferias.model.Endereco;
import com.hyagohenrique.ferias.model.Equipe;
import com.hyagohenrique.ferias.model.Funcionario;
import com.hyagohenrique.ferias.utils.DateUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class FuncionarioServiceTest {

    @MockBean
    private IFuncionarioRepository funcionarioRepository;
    @Autowired
    private IFuncionarioService funcionarioService;

    @Autowired
    private IEquipeRepository equipeRepository;

    private static final String NOME = "Fulano da Silva";
    private static final String DATA_NASCIMENTO = "07-07-1992";
    private static final String ENDERECO = "RUA NOVA";
    private static final String DATA_CONTRATACAO = "07-07-2020";
    private static final String MESSAGE_NOT_FOUND = "Recurso não encontrado para o ID: ";
    private static final String MSG_FERIAS_NAO_DISPONIVEL = "Funcionário com menos de um ano de contratado.";
    private static final String MSG_MEMBRO_EQUIPE_DE_FERIAS = "Já existe alguém da equipe de ferias!";

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @Test
    public void testValidaSeFuncionarioTemMenosDeUmAnoDeContratado() throws ParseException {
        exceptionRule.expect(FeriasNaoDisponivelException.class);
        exceptionRule.expectMessage(MSG_FERIAS_NAO_DISPONIVEL);
        
        Funcionario funcionario = getMockFuncionario();
        LocalDate faltaUmDiaParaCompletarUmAno = LocalDate.now().minusYears(1).plusDays(1);
        
        funcionario.setDataContratacao(DateUtils.convertLocalDateToDate(faltaUmDiaParaCompletarUmAno));
        this.funcionarioService.validaSeFuncionarioTemUmAnoDeEmpresa(funcionario);
    }

    /**
     * Valida se ja existe alguem da equipe curtindo as ferias no periodo em que se tenta cadastrar
     * 
     * Resulta esperado: FeriasNaoDisponivelException
     * 
     * @throws ParseException
     */
    @Test
    public void testCadastrarFeriasParaUmFuncionarioQueSeuColegaDeEquipeJaEstejaDeFeriasNoMesmoPeriodo()
            throws ParseException {

        exceptionRule.expect(FeriasNaoDisponivelException.class);
        exceptionRule.expectMessage(MSG_MEMBRO_EQUIPE_DE_FERIAS);


        long longMock = 1L;
        List<Funcionario> lista = new ArrayList<>();
        lista.add(getMockFuncionario());

        BDDMockito.given(this.funcionarioRepository.countByEquipeId(Mockito.anyLong())).willReturn(longMock);
        BDDMockito.given(this.funcionarioRepository.funcionarioQueEstaoDeFeriasNoMesmoTempoDesejado(Mockito.anyLong(), Mockito.any(Date.class))).willReturn(lista);

        this.funcionarioService.validaSeFuncionarioEstaImpedidoDeTirarFeriasPorContaDeOutraPessoaDaMesmaEquipe(longMock, new Date());

    }

    @Test
    public void testProcurarPorIdNaoCadastrado() throws ParseException {
        
        Long id = Long.parseLong("775");

        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage(MESSAGE_NOT_FOUND + id);
        
        this.funcionarioService.buscarPorId(id);
    }

    @Test
    public void testSalvarFuncionario() throws ParseException {
        BDDMockito.given(funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(getMockFuncionario());

        Funcionario funcionario = this.funcionarioService.salvar(getMockFuncionario());

        assertNotNull(funcionario);
        assertEquals(NOME, funcionario.getNome());
        assertEquals(6, funcionario.getMatricula().length());
    }

    @Test
    public void testListarFuncionariosPrestesATirarFerias() throws ParseException {
        List<Funcionario> lista = new ArrayList<>();
        lista.add(getMockFuncionario());
        int quantidadeDeMeses = 2;

        BDDMockito.given(
                this.funcionarioRepository.consultarFuncionariosQueIraoCompletar2AnosSemSolicitarFeriasEmNoMaximoXMeses(
                        Mockito.any(Date.class), Mockito.any(Date.class)))
                .willReturn(lista);

        List<Funcionario> resposta = this.funcionarioService.listarFuncionarioQueDevemTirarFerias(quantidadeDeMeses);

        assertNotNull(resposta);
        assertEquals(1, resposta.size());
        assertEquals(NOME, resposta.get(0).getNome());
    }

    @Test
    public void testListarFuncionarios() throws ParseException {
        List<Funcionario> lista = new ArrayList<>();
        lista.add(getMockFuncionario());

        BDDMockito.given(this.funcionarioService.listarFuncionarios()).willReturn(lista);

        List<Funcionario> resposta = this.funcionarioService.listarFuncionarios();

        assertNotNull(resposta);
        assertEquals(1, resposta.size());
        assertEquals(NOME, resposta.get(0).getNome());
    }

    private Funcionario getMockFuncionario() throws ParseException {
        Equipe equipe = new Equipe();
        equipe.setNome("Equipe 1");
        this.equipeRepository.save(equipe);

        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome(NOME);
        funcionario.setDataNascimento(new SimpleDateFormat("dd-MM-yyyy").parse(DATA_NASCIMENTO));
        funcionario.setDataContratacao(new SimpleDateFormat("dd-MM-yyyy").parse(DATA_CONTRATACAO));
        funcionario.setEndereco(new Endereco(ENDERECO));
        funcionario.setEquipe(equipe);

        return funcionario;
    }
}