package br.com.cantina.pedidos;

// Envelope CloudEvents, igual ao exemplo em contratos/pedido-confirmado.json
public record EventoPedidoConfirmado(
        String specversion,
        String id,
        String source,
        String type,
        String time,
        String datacontenttype,
        Dados data) {

    public record Dados(int pedidoId, int totalCentavos) {
    }
}
