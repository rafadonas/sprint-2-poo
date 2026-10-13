package br.com.motiva.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/** Converte booleanos Java para o padrão S/N usado nas tabelas da Sprint 3. */
@Converter
public class SimNaoBooleanConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean valor) {
        return Boolean.TRUE.equals(valor) ? "S" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String valor) {
        return "S".equalsIgnoreCase(valor);
    }
}
