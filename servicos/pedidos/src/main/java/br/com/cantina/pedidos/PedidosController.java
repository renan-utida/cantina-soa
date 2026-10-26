package br.com.cantina.pedidos;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PedidosController {

    private final CardapioClient cardapio;
    private final PedidoRepository repositorio;

    public PedidosController(CardapioClient cardapio, PedidoRepository repositorio) {
        this.cardapio = cardapio;
        this.repositorio = repositorio;
    }

    @PostMapping("/pedidos")
    public ResponseEntity<?> criar(@RequestBody NovoPedido novo) {
        if (novo.itens() == null || novo.itens().isEmpty()) {
            return recusa("O pedido precisa ter pelo menos um item.");
        }
        int total = 0;
        for (ItemPedido linha : novo.itens()) {
            if (linha.quantidade() < 1) {
                return recusa("A quantidade do item " + linha.itemId() + " precisa ser pelo menos 1.");
            }
            Optional<ItemCardapio> encontrado = cardapio.buscarItem(linha.itemId());
            if (encontrado.isEmpty()) {
                return recusa("O item " + linha.itemId() + " nao existe no cardapio.");
            }
            ItemCardapio item = encontrado.get();
            if (!item.disponivel()) {
                return recusa("O item " + item.nome() + " esta indisponivel.");
            }
            total += item.precoCentavos() * linha.quantidade();
        }
        Pedido pedido = repositorio.salvar(novo.itens(), total);
        return ResponseEntity.status(201).body(pedido);
    }

    @GetMapping("/pedidos/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable int id) {
        return repositorio.porId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/saude")
    public ResponseEntity<Void> saude() {
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(CardapioIndisponivelException.class)
    public ResponseEntity<Erro> cardapioIndisponivel(CardapioIndisponivelException e) {
        return ResponseEntity.status(503)
                .body(new Erro("O Cardapio nao respondeu em ate 2 segundos. Tente novamente em instantes."));
    }

    private ResponseEntity<Erro> recusa(String mensagem) {
        return ResponseEntity.status(422).body(new Erro(mensagem));
    }
}
