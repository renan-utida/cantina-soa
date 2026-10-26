# Cantina SOA

Laboratório SOA da Cantina: três serviços independentes (Cardápio, Pedidos e Avisos) com contratos OpenAPI, composição por HTTP e eventos CloudEvents. Checkpoint de Arquitetura Orientada a Serviços e Web Services, FIAP.

- **Aluno:** Renan Dias Utida (RM 558540)
- **Linguagem:** Java (Spring Boot)

## Prova de fogo (Missão 3)

O Pedidos (Java, Spring Boot) foi apontado, apenas pela variável `CARDAPIO_URL` e sem alterar nenhuma linha de código, para um Cardápio alternativo em **Python 3** (biblioteca padrão), no arquivo `evidencias/prova-de-fogo/cardapio_python.py`.

Nenhum outro aluno estava fazendo este checkpoint, por isso a prova foi feita com um Cardápio alternativo em outra linguagem, e não com o de outro grupo. Ele segue o mesmo `contratos/cardapio.yaml` e devolve um campo extra (`categoria`), que o Pedidos ignora.

Resultado: o `POST /pedidos` devolveu `201` com `totalCentavos` 2500, e o pedido com o Bolo de cenoura devolveu `422`, igual ao Cardápio em Java. A saída está em `evidencias/missao3.txt`.

## Eventos e Avisos (Missão 4)

**Trilha escolhida: A (sem broker).** O Pedidos, depois de confirmar um pedido, publica o evento `cantina.pedido.confirmado` (envelope CloudEvents, igual a `contratos/pedido-confirmado.json`) com um `POST` para cada endereço da variável de ambiente `ASSINANTES`, uma lista separada por vírgulas. O Avisos expõe `POST /eventos` (porta 8083) e registra uma linha como `Pedido 1 confirmado, total R$ 25,00`.

Escolhi a Trilha A porque prova os mesmos princípios (o Pedidos não sabe quem escuta e não espera resposta) sem exigir infraestrutura extra, como um broker, o que cabia melhor no prazo.

- **O pedido não depende do aviso:** a entrega do evento roda em segundo plano e qualquer falha só vira um aviso no log do Pedidos. Com o Avisos fora do ar, o `POST /pedidos` continua devolvendo `201`.
- **Novo consumidor só por configuração:** uma segunda instância do Avisos (porta 8084) passou a receber os eventos apenas acrescentando seu endereço em `ASSINANTES`, sem alterar o código do Pedidos.

As saídas dos testes estão em `evidencias/missao4.txt`.