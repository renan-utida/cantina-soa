import json
import os
from http.server import BaseHTTPRequestHandler, HTTPServer

ITENS = [
    {"id": 1, "nome": "Coxinha", "precoCentavos": 800, "disponivel": True, "categoria": "salgado"},
    {"id": 2, "nome": "Pão de queijo", "precoCentavos": 600, "disponivel": True, "categoria": "salgado"},
    {"id": 3, "nome": "Suco de laranja", "precoCentavos": 900, "disponivel": True, "categoria": "bebida"},
    {"id": 4, "nome": "Bolo de cenoura", "precoCentavos": 750, "disponivel": False, "categoria": "doce"},
]


class Handler(BaseHTTPRequestHandler):
    def responder(self, status, corpo=None):
        self.send_response(status)
        if corpo is None:
            self.send_header("Content-Length", "0")
            self.end_headers()
            return
        dados = json.dumps(corpo, ensure_ascii=False).encode("utf-8")
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(dados)))
        self.end_headers()
        self.wfile.write(dados)

    def do_GET(self):
        partes = self.path.split("?")[0].strip("/").split("/")
        if partes == ["saude"]:
            self.responder(200)
        elif partes == ["itens"]:
            self.responder(200, ITENS)
        elif len(partes) == 2 and partes[0] == "itens" and partes[1].isdigit():
            item = next((i for i in ITENS if i["id"] == int(partes[1])), None)
            if item is None:
                self.responder(404)
            else:
                self.responder(200, item)
        else:
            self.responder(404)


if __name__ == "__main__":
    porta = int(os.environ["PORTA"])
    HTTPServer(("0.0.0.0", porta), Handler).serve_forever()
