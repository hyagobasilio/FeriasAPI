package com.hyagohenrique.ferias.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hyagohenrique.ferias.dto.FuncionarioDTO;
import com.hyagohenrique.ferias.model.Funcionario;

import org.apache.tomcat.util.codec.binary.Base64;

public class QRCodeUtils {
    private QRCodeUtils() {
    }

    public static String gerarQRCodeAPartirDeFuncionarioDTO(FuncionarioDTO dto, int width, int height) {

        String resultImage = "";
        ObjectMapper mapper = new ObjectMapper();
        String content;
        try {
            content = mapper.writeValueAsString(dto.convertParaEntidade());
        } catch (JsonProcessingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return "";
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        @SuppressWarnings("rawtypes")
        HashMap<EncodeHintType, Comparable> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // Specify the character encoding as "utf-8"
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // Specify the error correction level of the
        hints.put(EncodeHintType.MARGIN, 2); // Set the margins of the image
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ImageIO.write(bufferedImage, "png", os);
            /**
             * There is no data:image/png;base64 in front of the original transcoding. These
             * fields are returned to the front end and cannot be parsed. You can add the
             * front end or add it below.
             */
            resultImage = new String("data:image/png;base64," + Base64.encodeBase64String(os.toByteArray()));

            return resultImage;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return resultImage;
    }

    public static byte[] getBytesQrCodeComDadosDoFuncionario(Funcionario funcionario)
            throws IOException, WriterException {
        
            ObjectMapper mapper = new ObjectMapper();
            String content;
            content = mapper.writeValueAsString(funcionario.converteParaDTO());

        HashMap<EncodeHintType, Comparable> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // Specify the character encoding as "utf-8"
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // Specify the error correction level of the
        hints.put(EncodeHintType.MARGIN, 2);
        
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 250, hints);
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(bufferedImage, "png", os);
        return os.toByteArray();
	}
}