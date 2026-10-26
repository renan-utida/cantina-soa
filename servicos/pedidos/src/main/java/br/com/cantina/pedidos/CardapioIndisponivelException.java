package br.com.cantina.pedidos;

public class CardapioIndisponivelException extends RuntimeException {

    public CardapioIndisponivelException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
