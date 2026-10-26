# Cantina SOA

Laboratório SOA da Cantina: três serviços independentes (Cardápio, Pedidos e Avisos) com contratos OpenAPI, composição por HTTP e eventos CloudEvents. Checkpoint de Arquitetura Orientada a Serviços e Web Services, FIAP.

- **Aluno:** Renan Dias Utida (RM 558540)
- **Linguagem:** Java (Spring Boot)

## Prova de fogo (Missão 3)

O Pedidos (Java, Spring Boot) foi apontado, apenas pela variável `CARDAPIO_URL` e sem alterar nenhuma linha de código, para um Cardápio alternativo em **Python 3** (biblioteca padrão), no arquivo `evidencias/prova-de-fogo/cardapio_python.py`.

Nenhum outro aluno estava fazendo este checkpoint, por isso a prova foi feita com um Cardápio alternativo em outra linguagem, e não com o de outro grupo. Ele segue o mesmo `contratos/cardapio.yaml` e devolve um campo extra (`categoria`), que o Pedidos ignora.

Resultado: o `POST /pedidos` devolveu `201` com `totalCentavos` 2500, e o pedido com o Bolo de cenoura devolveu `422`, igual ao Cardápio em Java. A saída está em `evidencias/missao3.txt`.