package br.com.cantina.pedidos;

import java.util.List;

public record NovoPedido(List<ItemPedido> itens) {
}
