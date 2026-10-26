package br.com.cantina.avisos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Leitor tolerante: usa so os campos de que precisa e ignora o resto do envelope.
@JsonIgnoreProperties(ignoreUnknown = true)
public record EventoRecebido(String id, String type, Dados data) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Dados(int pedidoId, int totalCentavos) {
    }
}
