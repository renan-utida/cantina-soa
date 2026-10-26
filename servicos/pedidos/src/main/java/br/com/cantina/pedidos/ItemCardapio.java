package br.com.cantina.pedidos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Leitor tolerante: usa so os campos de que precisa e ignora o resto.
@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemCardapio(int id, String nome, int precoCentavos, boolean disponivel) {
}
