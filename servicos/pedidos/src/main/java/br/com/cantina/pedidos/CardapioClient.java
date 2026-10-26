package br.com.cantina.pedidos;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class CardapioClient {

    private static final Duration TEMPO_LIMITE = Duration.ofSeconds(2);

    private final RestClient restClient;

    public CardapioClient(@Value("${cardapio.url}") String cardapioUrl) {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(TEMPO_LIMITE)
                .build();
        JdkClientHttpRequestFactory fabrica = new JdkClientHttpRequestFactory(httpClient);
        fabrica.setReadTimeout(TEMPO_LIMITE);
        this.restClient = RestClient.builder()
                .baseUrl(cardapioUrl)
                .requestFactory(fabrica)
                .build();
    }

    public Optional<ItemCardapio> buscarItem(int id) {
        try {
            return Optional.ofNullable(
                    restClient.get().uri("/itens/{id}", id).retrieve().body(ItemCardapio.class));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (RestClientException e) {
            throw new CardapioIndisponivelException("O Cardapio nao respondeu a tempo.", e);
        }
    }
}
