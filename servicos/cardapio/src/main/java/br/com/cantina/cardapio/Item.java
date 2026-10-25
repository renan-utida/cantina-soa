package br.com.cantina.cardapio;

public record Item(int id, String nome, int precoCentavos, boolean disponivel) {
}