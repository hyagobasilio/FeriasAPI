package com.hyagohenrique.ferias.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.hyagohenrique.ferias.dto.FeriasDTO;
import com.hyagohenrique.ferias.dto.FuncionarioDTO;
import com.hyagohenrique.ferias.iservice.IFeriasService;
import com.hyagohenrique.ferias.iservice.IFuncionarioService;
import com.hyagohenrique.ferias.model.Funcionario;
import com.hyagohenrique.ferias.response.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("funcionario")
public class FuncionarioController {

    @Autowired
    private IFuncionarioService funcionarioService;
    @Autowired
    private IFeriasService feriasService;
    
    @GetMapping
    public ResponseEntity<Response<List<FuncionarioDTO>>> index() {
        Response<List<FuncionarioDTO>> resposta = new Response<>();
        List<FuncionarioDTO> itens = funcionarioService.listarFuncionarios().stream().map(i -> i.converteParaDTO()).collect(Collectors.toList());
        resposta.setData(itens);
        return ResponseEntity.ok(resposta);
    }


    @GetMapping("/ferias-vencer/{meses}")
    public ResponseEntity<Response<List<FuncionarioDTO>>> funcionriosQueDevemTirarFerias(@PathVariable("meses") int meses) {
        Response<List<FuncionarioDTO>> resposta = new Response<>();
        List<FuncionarioDTO> itens = funcionarioService.listarFuncionarioQueDevemTirarFerias(meses).stream().map(i -> i.converteParaDTO()).collect(Collectors.toList());
        resposta.setData(itens);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<FuncionarioDTO> > show(@PathVariable Long id) {
        Response<FuncionarioDTO> resposta = new Response<>();
        resposta.setData(funcionarioService.buscarPorId(id).converteParaDTO());

        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<Response<FuncionarioDTO>> salvar(@Valid @RequestBody FuncionarioDTO dto, BindingResult result) {
        Response<FuncionarioDTO> response = new Response<>();
        if (result.hasErrors()) {
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
        Funcionario funcionario = this.funcionarioService.salvar(dto.convertParaEntidade());
        response.setData(funcionario.converteParaDTO());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}