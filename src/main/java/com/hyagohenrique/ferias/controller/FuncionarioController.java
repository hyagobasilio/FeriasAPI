package com.hyagohenrique.ferias.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hyagohenrique.ferias.dto.FuncionarioDTO;
import com.hyagohenrique.ferias.iservice.IFeriasService;
import com.hyagohenrique.ferias.iservice.IFuncionarioService;
import com.hyagohenrique.ferias.model.Funcionario;
import com.hyagohenrique.ferias.response.Response;
import com.hyagohenrique.ferias.utils.QRCodeUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        List<FuncionarioDTO> itens = funcionarioService.listarFuncionarios().stream().map(i -> i.converteParaDTO())
                .collect(Collectors.toList());
        resposta.setData(itens);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/ferias-vencer/{meses}")
    public ResponseEntity<Response<List<FuncionarioDTO>>> funcionriosQueDevemTirarFerias(
            @PathVariable("meses") int meses) {
        Response<List<FuncionarioDTO>> resposta = new Response<>();
        List<FuncionarioDTO> itens = funcionarioService.listarFuncionarioQueDevemTirarFerias(meses).stream()
                .map(i -> i.converteParaDTO()).collect(Collectors.toList());
        resposta.setData(itens);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<FuncionarioDTO>> show(@PathVariable Long id) {
        Response<FuncionarioDTO> resposta = new Response<>();
        resposta.setData(funcionarioService.buscarPorId(id).converteParaDTO());

        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<Response<FuncionarioDTO>> salvar(@Valid @ModelAttribute FuncionarioDTO dto,
            BindingResult result) {

        Response<FuncionarioDTO> response = new Response<>();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
        FuncionarioDTO funcionario = this.funcionarioService.salvar(dto.convertParaEntidade(), dto.getArquivo())
                .converteParaDTO();
        funcionario.gerarQrCodeBase64();
        response.setData(funcionario);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @CrossOrigin(allowedHeaders = "*")
    @GetMapping(value = "/code/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImageWithMediaType(@PathVariable("id") Long id) throws WriterException, IOException {

        Funcionario funcionario = this.funcionarioService.buscarPorId(id);
        return QRCodeUtils.getBytesQrCodeComDadosDoFuncionario(funcionario);
        
    }

}