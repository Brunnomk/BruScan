<div align="center">

# BruScan

### Aplicativo Android em Java para leitura de códigos em dispositivo Zebra TC26

![Java](https://img.shields.io/badge/Java-Android-orange)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![Device](https://img.shields.io/badge/Device-Zebra%20TC26-blue)
![Status](https://img.shields.io/badge/Status-Functional-success)
![Project](https://img.shields.io/badge/Project-Portfolio-lightgrey)

</div>

---

## 📱 Sobre o Projeto

**BruScan** é um aplicativo Android desenvolvido em **Java** para dispositivos Zebra, criado com foco em leitura rápida, organização e exportação de códigos de barras/QR Codes em ambientes de logística, inventário, armazém, conferência de produtos e operações de campo.

O projeto foi desenvolvido e testado em um dispositivo **Zebra TC26**, utilizando o scanner físico integrado do aparelho para capturar códigos de forma prática, rápida e confiável.

O app permite que o utilizador leia códigos automaticamente, associe opcionalmente um nome de produto, visualize histórico em tempo real, acompanhe um resumo por produto, desfaça a última leitura em caso de erro, exporte os dados para CSV e compartilhe o arquivo diretamente pelo Android.

---

## 🎯 Objetivo

O objetivo do **BruScan** é transformar um coletor Zebra Android em uma ferramenta simples, eficiente e funcional para operações reais de leitura e conferência de códigos.

O fluxo principal do aplicativo foi pensado para reduzir etapas manuais e acelerar o trabalho operacional:

```text
Escrever produto opcional → Ler código → Entrada automática no histórico → Exportar CSV → Compartilhar arquivo
```

---

## 🚀 Funcionalidades

- Leitura de códigos pelo scanner físico do Zebra TC26;
- Entrada automática da leitura no histórico;
- Campo opcional para nome do produto;
- Atualização em tempo real do histórico;
- Resumo automático por produto;
- Botão para desfazer a última leitura;
- Exportação dos dados para CSV;
- Compartilhamento do CSV por aplicativos instalados no Android;
- Interface simples, objetiva e otimizada para uso operacional;
- Funcionamento local, sem necessidade de internet.

---

## 🧠 Problema Resolvido

Em operações de logística, inventário, armazém ou conferência de produtos, é comum que operadores precisem registrar rapidamente códigos de barras, QR Codes, volumes ou encomendas.

Processos manuais podem gerar:

- Erros de digitação;
- Perda de tempo;
- Falta de organização dos dados;
- Dificuldade para contar produtos;
- Dificuldade para exportar informações;
- Necessidade de retrabalho;
- Baixa rastreabilidade das leituras realizadas.

O **BruScan** resolve esse problema criando um fluxo direto, onde cada leitura feita pelo scanner físico é automaticamente registrada, organizada e preparada para exportação.

---

## 🛠️ Tecnologias Utilizadas

- Java;
- Android Studio;
- Android SDK;
- XML Layout;
- ViewBinding;
- FileProvider;
- Android Intent;
- CSV Export;
- Zebra TC26;
- Scanner físico Android.

---

## 📦 Dispositivo de Teste

O projeto foi desenvolvido e testado em:

```text
Zebra TC26
Android
Scanner físico integrado
```

O app pode ser adaptado para outros dispositivos Zebra Android compatíveis com entrada via scanner físico, teclado ou Zebra DataWedge.

---

## 🔄 Fluxo de Funcionamento

1. O utilizador abre o BruScan no dispositivo Zebra.
2. Opcionalmente, informa o nome do produto.
3. Aponta o scanner para o código de barras ou QR Code.
4. Pressiona o botão físico de leitura do Zebra.
5. O código entra automaticamente no histórico.
6. O resumo por produto é atualizado em tempo real.
7. Caso ocorra erro, o utilizador pode desfazer a última leitura.
8. Ao final, os dados podem ser exportados em CSV.
9. O arquivo CSV pode ser compartilhado diretamente pelo Android.

---

## 📊 Exemplo de Uso

Se o utilizador informar o produto:

```text
carregador
```

E fizer três leituras diferentes, o app atualiza automaticamente o resumo:

```text
carregador: 3 códigos lidos
```

No histórico, cada leitura fica registrada com hora, produto e código:

```text
1. 16:46:02 | Produto: carregador | Código: 402461032
2. 16:47:10 | Produto: carregador | Código: 4957054507916
3. 16:48:22 | Produto: carregador | Código: 761294413296
```

---

## 📂 Exemplo de CSV Exportado

O **BruScan** exporta os dados no formato CSV usando `;` como separador, facilitando a abertura em versões do Excel configuradas para Portugal e Brasil.

```csv
data;hora;produto;codigo
26/06/2026;16:12:54;PSA-230S;4957054507916
26/06/2026;16:13:32;Boss PS-6;761294413296
26/06/2026;16:14:50;Livro programador pragmático;9788577807000
```

---

## 🧩 Estrutura Principal do Projeto

```text
BruScan/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── brunno/
│           │           └── zebrascan/
│           │               ├── MainActivity.java
│           │               └── FirstFragment.java
│           │
│           ├── res/
│           │   ├── layout/
│           │   │   ├── activity_main.xml
│           │   │   ├── content_main.xml
│           │   │   └── fragment_first.xml
│           │   │
│           │   ├── xml/
│           │   │   └── file_paths.xml
│           │   │
│           │   └── values/
│           │
│           └── AndroidManifest.xml
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

---

## 📌 Arquivos Principais

### `FirstFragment.java`

Arquivo responsável pela lógica principal do aplicativo:

- Recebe os códigos lidos;
- Adiciona automaticamente cada leitura ao histórico;
- Atualiza o resumo por produto;
- Permite desfazer a última leitura;
- Exporta os dados para CSV;
- Compartilha o arquivo CSV através de Intent Android.

---

### `fragment_first.xml`

Arquivo responsável pela interface principal:

- Campo opcional para produto;
- Campo de entrada do código lido;
- Status da operação;
- Último código lido;
- Resumo por produto;
- Histórico de leituras;
- Botões de exportação, compartilhamento, desfazer e limpeza.

---

### `file_paths.xml`

Arquivo utilizado pelo `FileProvider` para permitir o compartilhamento seguro do arquivo CSV com outros aplicativos Android.

---

### `AndroidManifest.xml`

Arquivo responsável pelas configurações principais do app, incluindo a declaração do `FileProvider`.

---

## 📷 Screenshots

> Adicionar aqui imagens do app rodando no Zebra TC26.

Sugestão de imagens:

```text
screenshots/bruscan-home.jpg
screenshots/bruscan-scan.jpg
screenshots/bruscan-history.jpg
screenshots/bruscan-csv-share.jpg
screenshots/bruscan-excel.jpg
```

Exemplo de uso no README:

```markdown
![Tela inicial do BruScan](screenshots/bruscan-home.jpg)
![Histórico em tempo real](screenshots/bruscan-history.jpg)
```

---

## ✅ Estado Atual

O projeto encontra-se funcional com:

- Aplicativo Android rodando em dispositivo Zebra real;
- Leitura automática pelo scanner físico;
- Produto opcional;
- Histórico em tempo real;
- Resumo por produto;
- Desfazer última leitura;
- Exportação CSV;
- Compartilhamento CSV;
- Interface ajustada para uso operacional.

---

## 💼 Valor do Projeto

O **BruScan** demonstra desenvolvimento Android aplicado a um cenário real de logística e operação de campo.

Este projeto envolve conceitos importantes como:

- Desenvolvimento Android com Java;
- Criação de interface com XML;
- Uso de ViewBinding;
- Manipulação de eventos;
- Entrada de dados via scanner físico;
- Organização de dados em memória;
- Geração de arquivos CSV;
- Compartilhamento de arquivos entre aplicativos Android;
- Configuração de FileProvider;
- Testes em dispositivo físico corporativo;
- Resolução de um problema real com software.

---

## 🏭 Casos de Uso

O BruScan pode ser utilizado em cenários como:

- Inventário de armazém;
- Conferência de produtos;
- Controle de volumes;
- Operações de entrega;
- Leitura de encomendas;
- Registro de códigos de barras;
- Contagem simples de itens;
- Coleta operacional de dados;
- Apoio a processos logísticos.

---

## 🔮 Possíveis Melhorias Futuras

Algumas melhorias que podem ser implementadas futuramente:

- Persistência local com SQLite ou Room;
- Histórico com RecyclerView;
- Exportação em `.xlsx`;
- Cadastro local de produtos;
- Busca por produto ou código;
- Validação de códigos;
- Login de utilizador;
- Sincronização com API;
- Integração com sistemas de inventário;
- Modo escuro;
- Configuração avançada via Zebra DataWedge Intent;
- Backup em nuvem.

---

## 🧪 Como Executar o Projeto

1. Clonar o repositório:

```bash
git clone https://github.com/Brunnomk/BruScan.git
```

2. Abrir o projeto no Android Studio.

3. Aguardar a sincronização do Gradle.

4. Conectar um dispositivo Android ou Zebra TC26.

5. Executar o app pelo Android Studio.

---

## 📱 Requisitos

- Android Studio;
- Java/JDK compatível com o projeto;
- Android SDK;
- Dispositivo Android ou emulador;
- Zebra TC26 ou dispositivo Zebra compatível para uso completo com scanner físico.

---

## 👨‍💻 Autor

Desenvolvido por **Brunno Xavier de Oliveira**.

Projeto criado como solução prática para leitura, organização, exportação e compartilhamento de códigos utilizando um dispositivo Android Zebra.

---

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais, demonstrativos e de portfólio.

Uso livre para estudo e referência.
