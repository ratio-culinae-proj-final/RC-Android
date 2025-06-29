# 🍳 Ratio Culinae

**Ratio Culinae** é um aplicativo Android desenvolvido para facilitar o dia a dia na cozinha. Com o apoio de inteligência artificial, o app sugere receitas criativas com base nos ingredientes disponíveis na sua casa, ajudando a economizar, evitar desperdícios e tornar a experiência culinária mais inteligente e prática.

## 📲 Funcionalidades

- **Cadastro e login de usuários** com autenticação via Firebase.
- **Cadastro de ingredientes disponíveis**, com entrada manual ou por voz.
- **Sugestão de receitas com base nos ingredientes**, usando IA (Gemini).
- **Interface de receitas com swipe** e salvamento de favoritas.
- **Imagens ilustrativas** para cada receita, obtidas via APIs externas (Custom Search API).
- **Visualização detalhada da receita** com ingredientes, preparo, tempo e dificuldade.
- **Filtros de preferência alimentar** (vegano, vegetariano, sem glúten, etc).
- **Lista de compras**.
- **Atualização automática da lista de ingredientes** conforme receitas são preparadas.
- **Temporizador embutido**, com contagem regressiva e notificações.
- **Integração com IA generativa (Gemini)** para sugestões de receitas.

## 🛠 Tecnologias Utilizadas

- **Android Studio 2025**
- **Kotlin / Java**
- **Firebase Authentication**
- **Gemini Pro (Google)**
- **APIs de imagem e Custom Search API** (Picasso)
- **Figma** (protótipos de tela)
- **GitHub + Trello** (versionamento e organização)

## 🧠 Design Thinking

- **Fontes:**
  - *Poppins* – Títulos
  - *Raleway* – Botões
  - *Quicksand* – Textos

- **Paleta de Cores:**
  - Marrom: `#8E633B`
  - Amarelo: `#E4B704`
  - Laranja: `#FF8400`

## 🖼 Protótipo

Confira o protótipo visual do app no Figma:
📎 [Protótipo Figma](https://www.figma.com/design/pBSvcesb8zOoxIAveenFjr/Ratio-Culinae?node-id=0-1&t=AzOsjt5t0r8o0JsI-1)

## 🖥️ Instalação

**Android**

Para rodar o aplicativo Android em ambiente de teste pelo computador:

1. Baixe e instale o [Android Studio](https://developer.android.com/studio?hl=pt-br)

2. Caso não tenha o repositório do projeto, você pode utilizar o seguinte comando no terminal: `git clone https://github.com/ratio-culinae-proj-final/RC-Android`

3. Após a instalação, abra o Android Studio, clique na opção **"Open Project"** e selecione a pasta do projeto que tenha o ícone do Android.

4. Aguarde alguns instantes enquanto o Android Studio faz a instalação do projeto. Você pode visualizar o progresso pela barra azul no canto direito inferior.

5. Feito a instalação, vá ao menu lateral do lado direito, selecione a opção Device Manager e clique em "+" para criar um emulador que irá rodar o aplicativo.

6. Durante a criação do novo emulador:
  - Selecione a opção Medium Phone > Next > Mude para API 29 - Android v10.0 > Selecione a imagem do sistema com o ícone de estrela > Finish.

7. Feito isso, clique em ▶️ Run 'app'.

8. Siga os passos abaixo para rodar a API intermediária em Python.

**Python**

O arquivo python que utiliza a API do Gemini para geração de receitas já está dentro da pasta do projeto com o nome `./geminiHelp.py`

1. Primeiramente, [baixe o Python](https://www.python.org/downloads/) em sua máquina.
2. Abra o terminal do próprio Android Studio e digite os seguintes comandos:

        pip install flask
        pip install requests

3. Com isto, digite o seguinte comando para rodar o arquivo python: `py .\geminiHelp.py`

## 👨‍💻 Equipe

- Arthur Guaritá Brasil
- Giovanna Couto
- Sanderson Machado de Oliveira
- Iuri Guimarães Pinheiro
- Arthur Cavalcante

---

> Projeto desenvolvido como trabalho final da disciplina **Programação para Dispositivos Móveis** – Bacharelado em Ciência da Computação, **Centro Universitário de Brasília (CEUB)** – Maio de 2025.