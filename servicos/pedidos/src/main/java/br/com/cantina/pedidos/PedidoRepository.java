package br.com.cantina.pedidos;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Repository;

@Repository
public class PedidoRepository {

    private final Map<Integer, Pedido> pedidos = new ConcurrentHashMap<>();
    private final AtomicInteger proximoId = new AtomicInteger(1);

    public Pedido salvar(List<ItemPedido> itens, int totalCentavos) {
        Pedido pedido = new Pedido(proximoId.getAndIncrement(), List.copyOf(itens), totalCentavos, "confirmado");
        pedidos.put(pedido.id(), pedido);
        return pedido;
    }

    public Optional<Pedido> porId(int id) {
        return Optional.ofNullable(pedidos.get(id));
    }
}
