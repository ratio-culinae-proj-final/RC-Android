// utils/SpeechHelper.java
package com.example.ratioculinae.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeechHelper {

    private static final Map<String, String> extensoParaNumero = new HashMap<>();

    static {
        extensoParaNumero.put("um", "1");
        extensoParaNumero.put("uma", "1");
        extensoParaNumero.put("dois", "2");
        extensoParaNumero.put("duas", "2");
        extensoParaNumero.put("três", "3");
        extensoParaNumero.put("quatro", "4");
        extensoParaNumero.put("cinco", "5");
        extensoParaNumero.put("seis", "6");
        extensoParaNumero.put("sete", "7");
        extensoParaNumero.put("oito", "8");
        extensoParaNumero.put("nove", "9");
        extensoParaNumero.put("dez", "10");
    }

    public static String normalizarTexto(String texto) {
        texto = texto.toLowerCase();
        for (Map.Entry<String, String> entry : extensoParaNumero.entrySet()) {
            texto = texto.replace(entry.getKey() + " ", entry.getValue() + " ");
        }
        return texto;
    }

    public static String[] extrairQuantidadeENomeComRegex(String texto) {
        texto = normalizarTexto(texto);

        // Padrão: número antes do nome
        Pattern padrao1 = Pattern.compile("(?:(\\d+)\\s+)([a-zçãõáéíóúâêôàèìòùäëïöüñ\\s]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = padrao1.matcher(texto);
        if (matcher1.matches()) {
            return new String[]{matcher1.group(1), matcher1.group(2).trim()};
        }

        // Padrão: nome antes do número
        Pattern padrao2 = Pattern.compile("([a-zçãõáéíóúâêôàèìòùäëïöüñ\\s]+)\\s+(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher2 = padrao2.matcher(texto);
        if (matcher2.matches()) {
            return new String[]{matcher2.group(2), matcher2.group(1).trim()};
        }

        // Caso não tenha número: assume apenas nome
        return new String[]{"", texto.trim()};
    }
}
