package br.com.cantina.avisos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AvisosController {

    private static final Logger log = LoggerFactory.getLogger(AvisosController.class);
    private static final String TIPO_ESPERADO = "cantina.pedido.confirmado";

    private final String instancia;

    public AvisosController(@Value("${avisos.instancia}") String instancia) {
        this.instancia = instancia;
    }

    @PostMapping("/eventos")
    public ResponseEntity<Void> receber(@RequestBody EventoRecebido evento) {
        if (TIPO_ESPERADO.equals(evento.type()) && evento.data() != null) {
            int total = evento.data().totalCentavos();
            log.info("[{}] Pedido {} confirmado, total R$ {},{}",
                    instancia, evento.data().pedidoId(), total / 100, String.format("%02d", total % 100));
        }
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/saude")
    public ResponseEntity<Void> saude() {
        return ResponseEntity.ok().build();
    }
}
