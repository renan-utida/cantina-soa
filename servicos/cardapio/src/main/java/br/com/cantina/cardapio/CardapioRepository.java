package br.com.cantina.cardapio;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class CardapioRepository {

    private final List<Item> itens = List.of(
            new Item(1, "Coxinha", 800, true),
            new Item(2, "Pão de queijo", 600, true),
            new Item(3, "Suco de laranja", 900, true),
            new Item(4, "Bolo de cenoura", 750, false));

    public List<Item> todos() {
        return itens;
    }

    public Optional<Item> porId(int id) {
        return itens.stream().filter(i -> i.id() == id).findFirst();
    }
}