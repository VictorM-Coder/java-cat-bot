# 🐱 Java Cat Bot

## 🎯 Objetivos
O que pode ser melhor do que fotos de gatinhos fofos? Foi pensando nisso que eu desenvolvi este bot.

## 📝 Descrição
Este bot utiliza os serviços do [The Cat API](https://thecatapi.com) para a busca de imagens aleatórias de gatinhos. Em seguida, a partir da [API pública do Twitter](https://developer.twitter.com/en/docs/twitter-api) e de tratamentos internos, esta imagem é postada em uma conta específica na plataforma.

## 🛠️ Tecnologias
- [Twitter API](https://developer.twitter.com/en/docs/twitter-api)
- [The Cat API](https://thecatapi.com)
- [Http Client - Lib](https://hc.apache.org/httpcomponents-client-4.5.x/index.html)
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

## 🧠 Aprendizados
A partir do desenvolvimento deste bot, pude praticar os seguintes conceitos:
- Requisições HTTP
- Autenticação com OAuth1
- Consumo e tratamento de imagens
- Comunicação com múltiplos serviços externos

## ⚙️ Configurar projeto
Se deseja testar o projeto por si só, ou mesmo melhorar sua estrutura, tudo que precisa é configurar um arquivo *.env* no diretório raiz do projeto com as seguintes variáveis:
```
API_URL_POST_IMAGE=https://upload.twitter.com/1.1/media/upload.json
API_URL_POST_TWEET=https://api.twitter.com/2/tweets

CAT_API_URL=https://api.thecatapi.com/v1/images/search?mime_types=jpg

CONSUMER_KEY=<your-twitter-consumer-key>
CONSUMER_SECRET=<your-twitter-consumer-secret>
ACCESS_TOKEN=<your-twitter-access-token>
TOKEN_SECRET=<your-twitter-token-secret>
```

## 🚀 Observações
Este bot está funcional e posta imagens todos os dias às 9:30. [Nos siga](https://twitter.com/RandomCats62942) para ver imagens fofas! 🐾
