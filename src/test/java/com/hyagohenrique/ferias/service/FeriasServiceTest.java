package com.hyagohenrique.ferias.service;

import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import com.hyagohenrique.ferias.exception.FeriasNaoDisponivelException;
import com.hyagohenrique.ferias.irepository.IFeriasRepository;
import com.hyagohenrique.ferias.irepository.IFuncionarioRepository;
import com.hyagohenrique.ferias.iservice.IFeriasService;
import com.hyagohenrique.ferias.model.Endereco;
import com.hyagohenrique.ferias.model.Equipe;
import com.hyagohenrique.ferias.model.Ferias;
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
public class FeriasServiceTest {

    @MockBean
    private IFeriasRepository feriasRepository;

    @MockBean
    private IFuncionarioRepository funcionarioRepository;

    @Autowired
    private IFeriasService feriasService;

    private static final Long ID = 1L;
    private static final String MSG_FERIAS_NAO_DISPONIVEL = "Funcionário com menos de um ano de contratado.";

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testSalvarFerias() throws ParseException {

        Optional<Funcionario> funcionarioMock = Optional.of(getMockFuncionario());

        BDDMockito.given(this.funcionarioRepository.findById(Mockito.anyLong()))
            .willReturn(funcionarioMock);
        BDDMockito.given(this.feriasRepository.save(Mockito.any(Ferias.class)))
            .willReturn(getMockFerias());
        

        Ferias ferias = getMockFerias();
        
        Ferias fe = this.feriasService.salvar(ferias);

        assertNotNull(fe);

    }


    @Test
    public void testCadastrarFeriasParaFuncionarioComMenosDeDoisAnosDeContratado() throws ParseException {
        Funcionario f = getMockFuncionario();
        // seta a data de hoje para ser a data de contratacao do funcionario
        f.setDataContratacao(new Date(System.currentTimeMillis()));

        Optional<Funcionario> funcionarioMock = Optional.of(f);

        BDDMockito.given(this.funcionarioRepository.findById(Mockito.anyLong()))
            .willReturn(funcionarioMock);
        BDDMockito.given(this.feriasRepository.save(Mockito.any(Ferias.class))).willReturn(getMockFerias());
        
        Ferias ferias = getMockFerias();
        ferias.setFuncionario(f);

        exceptionRule.expect(FeriasNaoDisponivelException.class);
        exceptionRule.expectMessage(MSG_FERIAS_NAO_DISPONIVEL);
        
        this.feriasService.salvar(ferias);
    }
    

    public Ferias getMockFerias() throws ParseException {
        LocalDate inicio = LocalDate.now().minusMonths(1);
        LocalDate fim = LocalDate.now();

        Ferias f = new Ferias();
        f.setId(ID);
        f.setFuncionario(getMockFuncionario());
        f.setInicio(DateUtils.convertLocalDateToDate(inicio));
        f.setFim(DateUtils.convertLocalDateToDate(fim));
        return f;
    }

    /**
     * Funcionário com dois anos de contratado
     * @return
     * @throws ParseException
     */
    private Funcionario getMockFuncionario() throws ParseException {
        Equipe equipe = new Equipe();
        equipe.setId(ID);
        equipe.setNome("Equipe 1");

        LocalDate dataContratacaoComMaisDeUmAno = LocalDate.now().minusYears(2);
        Funcionario funcionario = new Funcionario();
        funcionario.setId(ID);
        funcionario.setNome("NOME");
        funcionario.setDataNascimento(new SimpleDateFormat("dd-MM-yyyy").parse("07-07-1992"));
        funcionario.setDataContratacao(DateUtils.convertLocalDateToDate(dataContratacaoComMaisDeUmAno));
        funcionario.setEndereco(new Endereco("Av Brasil"));
        funcionario.setEquipe(equipe);
        return funcionario;
    }
    
}