import requests
from flask import Flask, request, jsonify
import json
import re

# Configurações
GEMINI_API_KEY = "AIzaSyCYrZUyHs5JEMj-tiAbDqGcmFOAvKrHsqY"
MODEL_NAME = "models/gemini-1.5-flash"
GEMINI_ENDPOINT = f"https://generativelanguage.googleapis.com/v1beta/{MODEL_NAME}:generateContent?key={GEMINI_API_KEY}"

CSE_API_KEY = "AIzaSyCd_jGMEyTI3yXCS-aGl1USnse-54GOd6g"
CSE_CX = "1204e613a54724f2b"

app = Flask(__name__)

def buscar_imagem_google(nome_receita):
    print(f"\n🔎 Buscando imagem para: {nome_receita}")
    search_url = "https://www.googleapis.com/customsearch/v1"
    params = {
        "key": CSE_API_KEY,
        "cx": CSE_CX,
        "q": nome_receita,
        "searchType": "image",
        "num": 1,
        "imgSize": "medium"
    }

    try:
        resposta = requests.get(search_url, params=params)
        print(f"📱 Requisição feita: {resposta.url}")
        print(f"📄 Status Code: {resposta.status_code}")
        data = resposta.json()
        print(f"📦 Resposta JSON: {json.dumps(data, indent=2)}")

        if "items" in data and data["items"]:
            imagem_link = data["items"][0]["link"]
            print(f"✅ Imagem encontrada: {imagem_link}")
            return imagem_link
        else:
            print("⚠️ Nenhuma imagem encontrada nos resultados.")
    except Exception as e:
        print(f"❌ Erro ao buscar imagem: {e}")

    return ""

@app.route("/sugerir_receitas", methods=["POST"])
def sugerir_receitas():
    print("📥 Headers:", request.headers)
    print("📥 Body raw:", request.data)

    try:
        dados = request.get_json(force=True)
    except Exception as e:
        return jsonify({"erro": "Erro ao ler o JSON: " + str(e)}), 400

    print("📦 JSON recebido:", dados)

    if not dados or "ingredientes" not in dados:
        return jsonify({"erro": "JSON inválido ou campo 'ingredientes' ausente"}), 400

    ingredientes = dados.get("ingredientes", "")
    preferencias = dados.get("preferencias", "").strip()

    # Prompt com ou sem preferências
    if preferencias:
        prompt = f"""
Você é um assistente culinário inteligente. Com base nos ingredientes abaixo:

{ingredientes}

E respeitando as seguintes **preferências alimentares** do usuário: {preferencias}

Gere uma **lista com 5 receitas criativas e compatíveis com essas preferências**, no seguinte formato JSON:
[
  {{
    "nome": "Nome da receita",
    "ingredientes": ["item1", "item2", "..."],
    "modo_preparo": "Modo de preparo em etapas numeradas (1. Passo 1. 2. Passo 2. ...)",
    "dificuldade": "Fácil/Média/Difícil",
    "tempo_preparo": "Tempo estimado, ex: 30 minutos",
    "imagem": ""
  }},
  ...
]
Apenas retorne o JSON diretamente, sem explicações extras.
"""
    else:
        prompt = f"""
Você é um assistente culinário inteligente. Com base nos ingredientes abaixo:

{ingredientes}

Gere uma **lista com 5 receitas criativas** no seguinte formato JSON:
[
  {{
    "nome": "Nome da receita",
    "ingredientes": ["item1", "item2", "..."],
    "modo_preparo": "Modo de preparo em etapas numeradas (1. Passo 1. 2. Passo 2. ...)",
    "dificuldade": "Fácil/Média/Difícil",
    "tempo_preparo": "Tempo estimado, ex: 30 minutos",
    "imagem": ""
  }},
  ...
]
Apenas retorne o JSON diretamente, sem explicações extras.
"""

    payload = {
        "contents": [
            {
                "parts": [{"text": prompt}]
            }
        ]
    }

    response = requests.post(GEMINI_ENDPOINT, json=payload)
    print(f"\n🤖 Resposta da API Gemini Flash: {response.status_code}")
    print(f"📄 Conteúdo: {response.text[:1000]}...")

    if response.status_code == 200:
        try:
            result = response.json()
            texto_resposta = result["candidates"][0]["content"]["parts"][0]["text"]

            json_match = re.search(r"\[\s*{.*}\s*\]", texto_resposta, re.DOTALL)
            json_str = json_match.group(0) if json_match else texto_resposta

            receitas = json.loads(json_str)

            # Buscar imagem real para cada receita
            for receita in receitas:
                nome = receita.get("nome", "")
                imagem_url = buscar_imagem_google(nome)
                receita["imagem"] = imagem_url

            return jsonify({"receitas": receitas})

        except Exception as e:
            print(f"❌ Erro ao interpretar resposta da API: {e}")
            return jsonify({"erro": "Erro ao interpretar resposta da API: " + str(e)}), 500
    else:
        return jsonify({"erro": f"Erro na requisição: {response.status_code}"}), response.status_code

if __name__ == "__main__":
    print("🚀 Servidor Flask iniciado na porta 5000")
    app.run(debug=True, host="0.0.0.0", port=5000)