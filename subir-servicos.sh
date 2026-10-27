#!/usr/bin/env bash
# Sobe os servicos da Cantina: Cardapio (8081 e 8091), Avisos (8083) e Pedidos (8082).
# Uso: ./subir-servicos.sh
RAIZ="$(cd "$(dirname "$0")" && pwd)"

for s in cardapio avisos pedidos; do
  if ! ls "$RAIZ"/servicos/$s/target/*.jar > /dev/null 2>&1; then
    echo "Compilando $s..."
    (cd "$RAIZ/servicos/$s" && mvn -q -DskipTests package) || exit 1
  fi
done

pkill -f "cardapio-0.0.1|avisos-0.0.1|pedidos-0.0.1" 2> /dev/null
sleep 2

cd "$RAIZ/servicos/cardapio"
PORTA=8081 setsid nohup java -jar target/cardapio-0.0.1-SNAPSHOT.jar > /tmp/cardapio-8081.log 2>&1 &
PORTA=8091 setsid nohup java -jar target/cardapio-0.0.1-SNAPSHOT.jar > /tmp/cardapio-8091.log 2>&1 &
cd "$RAIZ/servicos/avisos"
PORTA=8083 INSTANCIA=avisos-8083 setsid nohup java -jar target/avisos-0.0.1-SNAPSHOT.jar > /tmp/avisos-8083.log 2>&1 &
cd "$RAIZ/servicos/pedidos"
PORTA=8082 CARDAPIO_URL=http://localhost:8081 ASSINANTES=http://localhost:8083/eventos setsid nohup java -jar target/pedidos-0.0.1-SNAPSHOT.jar > /tmp/pedidos.log 2>&1 &

for p in 8081 8091 8083 8082; do
  c=000
  for i in $(seq 1 30); do
    c=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:$p/saude")
    [ "$c" = "200" ] && break
    sleep 2
  done
  echo "porta $p: $c"
done
