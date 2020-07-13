package com.hyagohenrique.ferias.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.hyagohenrique.ferias.dto.FeriasDTO;
import com.hyagohenrique.ferias.iservice.IFeriasService;
import com.hyagohenrique.ferias.model.Ferias;
import com.hyagohenrique.ferias.response.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("ferias")
public class FeriasController {

    @Autowired
    private IFeriasService feriasService;

    @GetMapping
    public ResponseEntity<Response<List<FeriasDTO>>> index() {
        Response<List<FeriasDTO>> response = new Response<>();
        List<FeriasDTO> lista = this.feriasService.listar().stream().map(i -> i.converteParaDTO()).collect(Collectors.toList());
        response.setData(lista);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<FeriasDTO>> salvar(@Valid @RequestBody FeriasDTO dto, BindingResult result) {
        Response<FeriasDTO> response = new Response<>();
        if (result.hasErrors()) {
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
        Ferias f = this.feriasService.salvar(dto.converterParaEntidade());
        response.setData(f.converteParaDTO());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/funcionario/{matricula}")
    public ResponseEntity<Response<List<FeriasDTO>>> funcionriosQueDevemTirarFerias(@PathVariable("matricula") String matricula) {
        Response<List<FeriasDTO>> resposta = new Response<>();
        List<FeriasDTO> itens = feriasService.buscarFeriasPorMatriculaDoFuncionario(matricula).stream().map(i -> i.converteParaDTO()).collect(Collectors.toList());
        resposta.setData(itens);
        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> delete(@PathVariable("id") Long id) {
        Response<String> response = new Response<>();
        response.setData("Ferias removida com sucesso!");
        
        this.feriasService.deletar(id);
        
        return ResponseEntity.ok(response);
    }
}