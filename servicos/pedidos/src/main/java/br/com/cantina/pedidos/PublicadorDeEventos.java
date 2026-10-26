package br.com.cantina.pedidos;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PublicadorDeEventos {

    private static final Logger log = LoggerFactory.getLogger(PublicadorDeEventos.class);
    private static final Duration TEMPO_LIMITE = Duration.ofSeconds(2);

    private final List<String> assinantes;
    private final RestClient restClient;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public PublicadorDeEventos(@Value("${eventos.assinantes:}") String assinantes) {
        this.assinantes = Arrays.stream(assinantes.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(TEMPO_LIMITE)
                .build();
        JdkClientHttpRequestFactory fabrica = new JdkClientHttpRequestFactory(httpClient);
        fabrica.setReadTimeout(TEMPO_LIMITE);
        this.restClient = RestClient.builder().requestFactory(fabrica).build();
        log.info("Assinantes de eventos: {}", this.assinantes);
    }

    // Nao espera resposta: cada entrega roda em segundo plano e qualquer falha so vira um aviso no log.
    public void publicarPedidoConfirmado(Pedido pedido) {
        EventoPedidoConfirmado evento = new EventoPedidoConfirmado(
                "1.0",
                UUID.randomUUID().toString(),
                "/cantina/pedidos",
                "cantina.pedido.confirmado",
                Instant.now().toString(),
                "application/json",
                new EventoPedidoConfirmado.Dados(pedido.id(), pedido.totalCentavos()));
        for (String destino : assinantes) {
            executor.execute(() -> entregar(destino, evento));
        }
    }

    private void entregar(String destino, EventoPedidoConfirmado evento) {
        try {
            restClient.post()
                    .uri(destino)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(evento)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Nao foi possivel entregar o evento {} a {}: {}", evento.id(), destino, e.getMessage());
        }
    }
}
