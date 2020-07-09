package com.hyagohenrique.ferias.utils;

import static org.junit.Assert.assertEquals;

import org.flywaydb.core.internal.util.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class StringUtilsTest {
    
    @Test
    public void testAdicionandoZeroAEsquerda() {
        Long id = new Long("1");
        String expected = "000001";
        String result = StringUtils.leftPad(id.toString(), 6, '0');

        assertEquals(expected, result);
    }
}