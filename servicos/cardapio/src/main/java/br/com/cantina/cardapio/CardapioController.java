package br.com.cantina.cardapio;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardapioController {

    private final CardapioRepository repositorio;

    public CardapioController(CardapioRepository repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping("/itens")
    public List<Item> listar() {
        return repositorio.todos();
    }

    @GetMapping("/itens/{id}")
    public ResponseEntity<Item> buscar(@PathVariable int id) {
        return repositorio.porId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/saude")
    public ResponseEntity<Void> saude() {
        return ResponseEntity.ok().build();
    }
}