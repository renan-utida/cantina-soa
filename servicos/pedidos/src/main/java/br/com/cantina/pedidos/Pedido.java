package br.com.cantina.pedidos;

import java.util.List;

public record Pedido(int id, List<ItemPedido> itens, int totalCentavos, String status) {
}
