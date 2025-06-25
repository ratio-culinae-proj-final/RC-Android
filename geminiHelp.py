import requests
from flask import Flask, request, jsonify
import json
import re

API_KEY = "SUA_API_KEY_AQUI"
MODEL_NAME = "models/gemini-2.5-pro-preview-tts"
ENDPOINT = f"https://generativelanguage.googleapis.com/v1beta/{MODEL_NAME}:generateContent?key={API_KEY}"

app = Flask(__name__)

@app.route("/sugerir_receitas", methods=["POST"])
def sugerir_receitas():
    print("Headers:", request.headers)
    print("Body raw:", request.data)

    try:
        dados = request.get_json(force=True)
    except Exception as e:
        return jsonify({"erro": "Erro ao ler o JSON: " + str(e)}), 400

    print("JSON recebido:", dados)

    if not dados or "ingredientes" not in dados:
        return jsonify({"erro": "JSON inválido ou campo 'ingredientes' ausente"}), 400

    ingredientes = dados.get("ingredientes", "")
    prompt = f"""
Você é um assistente de receitas. Baseado nos ingredientes abaixo:

{ingredientes}

Gere uma **lista com 5 receitas criativas** no seguinte formato JSON:
[
  {{
    "nome": "Nome da receita",
    "ingredientes": ["item1", "item2", "..."],
    "modo_preparo": "Descrição do modo de preparo passo a passo.",
    "dificuldade": "Fácil/Média/Difícil",
    "tempo_preparo": "Tempo estimado, ex: 30 minutos",
    "imagem": "URL de uma imagem ilustrativa ou string vazia se não disponível"
  }},
  ...
]
Apenas retorne o JSON diretamente, sem explicações extras.
"""

    payload = {
        "contents": [
            {
                "parts": [
                    {"text": prompt}
                ]
            }
        ]
    }

    response = requests.post(ENDPOINT, json=payload)
    print(f"Resposta da API Gemini Pro: {response.status_code}, resposta: {response.text}")

    if response.status_code == 200:
        result = response.json()
        try:
            texto_resposta = result["candidates"][0]["content"]["parts"][0]["text"]

            json_match = re.search(r"\[\s*{.*}\s*\]", texto_resposta, re.DOTALL)
            if json_match:
                json_str = json_match.group(0)
            else:
                json_str = texto_resposta

            receitas = json.loads(json_str)
            return jsonify({"receitas": receitas})

        except Exception as e:
            return jsonify({"erro": "Erro ao interpretar resposta da API: " + str(e)}), 500
    else:
        return jsonify({"erro": f"Erro na requisição: {response.status_code}"}), response.status_code

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=5000)