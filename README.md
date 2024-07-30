# Jwt-Decode

Este projeto visa decodificar um 'token' JWT (extraindo o cabeçalho e corpo), para determinar se o claim
é válido ou não, de acordo com regras que veremos a seguir.

## 📝 Sobre o Projeto

Está seção visa informar a estruturação deste projeto e também as regras utilizadas para solucionar o problema proposto.

### Estrutura

O projeto é estruturado na forma de pacotes, abaixo serão listados os principais.

#### controllers

Pacote dedicado a conter os endpoints, classes de requisição e resposta e validadores de entrada.

#### services

Pacote dedicado a aplicação das regras de negócio.

#### handlers

Neste pacote é onde são tratadas as exceções geradas na aplicação.

#### configuration

Pacote com classes de configuração de algumas dependẽncias utilziadas no projeto

#### commons

Pacote de classes comunmente utilizadas por outros pacotes da aplicação.

### Regras

Abaixo serão listadas algumas regras que moldam o comportamento da aplicação e a solução proposta para que a mesma seja atendida.

#### Receber o token JWT por parâmetros

O 'token' JWT será recebido na aplicação através heeader **_Authorizaton_**, cujo **valor** deve ter o seguinte formato: _Bearer <token_jwt>_.
Este header deverá ser enviado no **endpoint**: _/api/v1/tokens/decode_ através do método **POST**.

#### Deve ser um JWT válido

Para ser um JWT válido, o 'token' deve ser composto de um header, payload e signature. Na ausência de qualquer um destes componentes, assume-se que o 'token' é inválido.

A validação destes 3 componentes é feita na classe **JwtService** através do método **_decode_** que recebe parâmetro o token JWT. 

A decodificação é realizada sem a nessecidade de saber a secret utilizada pelo servidor na hora da geração do 'token', caso o mesmo tenha um formato inválido, uma exceção será lançada.

#### Deve conter apenas 3 claims (Name, Role e Seed)

Após a validação do 'token' e **extração** do claim do **payload**, é realizada a tentativa de mapeamento para a classe **ClaimDTO**. 
Essa classe aceita somente as propriedades claim **Role**, **Seed** e **Name**. Qualquer propriedade não conhecida presente no claim fará com que uma exceção seja lançada durante este mapeamento.

#### Claim Name

A validação das regras referentes à claim name são feitas dentro da classe **ClaimDTOValidator** 

##### Não pode ter carácter de números

O método _**nameMustBeOnlyAlphaCharacters**_ verifica se a claim name é composta somente por caractéres alfabéticos.

##### O tamanho máximo é de 256 caracteres

O método **_nameSizeMustBeLessOrEqualToMaxAllowed_** verifica se o tamanho da claim name não supera o valor de 256 caracteres.

#### Claim Role deve conter apenas 1 dos três valores (Admin, Member e External)

A Da claim role é feita na mesma classe utilizada para validar a claim name: **ClaimDTOValidator**

Para validar a regra, foi criado o método **_roleMustBeOneOfTheAllowedRoles_**, cuja implementação verifica se a role
informada está presente em um dos valores de uma lista que contém os seguintes valores: ["ADMIN", "MEMBER", "EXTERNAL"]

#### A claim Seed deve ser um número primo

Novamente temos a validação de uma propriedade da claim sendo realizada pela classe **ClaimDTOValidator**

Para atender a esta regra, foi criado o método **_seedMustBeAPrimeNumber_** que verifica por um algoritmo otimizado se a claim seed informada é ou não um número primo.

Por definição, um número é primo se for um número positivo maior que 1 e tendo como únicos divisores o número 1 e ele mesmo.

#### Problem Detail para APIs HTTP

Este projeto utiliza a implemenação do spring boot para a RFC 9457, que visa padronizar os formatos de mensagens de erro das APIs HTTP.

Para os cenários onde a resposta da API tem o seu status entre 4xx e 5xx, um objeto descritivo do erro contendo as seguintes propriedades padrão (type, title, status, detail e instance) será enviado como resposta.

É possivel também customizar esse tipo de resposta, adicionando novas propriedades para descrever melhor o problema encontrado.

Aproveitando este recurso, foram adicionadas duas novas propriedades: **_isValid_** e **_reasons_** informando que o token é inválido e as razões.

## 🚀 Começando

Para obter uma cópia deste projeto, basta clonar o projeto do repositório ou baixar o arquivo .zip.

### 📋 Pré-requisitos

Requisitos necessários para executar o projeto localmente (via IDE) ou por um container no docker.

#### Local (via terminal ou runner na IDE)

- Java (17.0.11)
- Maven (3.9.8)
- IDE de desenvolvimento (**_opcional_**)
  - **recomendado:** _IntelliJ IDEA 2024.1.4 (Community Edition)

Caso não tenha instalado estes componentes, siga os seguintes tutoriais para instalação:

**_Java e Maven_**: [SDKMan - O gerenciador de versões SDK](https://brunolorencolopes.gitlab.io/blog/pt-br/ferramentas_uteis/SDKMAN.html)

**_Intelijj Community_**: [Intelijj - Download da IDE e configuração](https://www.jetbrains.com/idea/download/?section=linux)

#### Local (via Docker e Docker Compose)

- Docker version 27.1.0
- Docker Compose version v2.29.0

Caso precise instalar o **docker desktop**, siga o tutorial oficial no site para o seu SO correspondente:

[Guia Oficial - Instalando o docker](https://docs.docker.com/get-docker/)

### 🔧 Instalação

Guia para instalação da aplicação local, via terminal, runner da IDE ou container do docker.

#### Subindo aplicação local (_via terminal_)

Abra um novo terminal e avance até a pasta raiz do projeto, execute os seguintes comandos:

**Verifique a versão do Java:**

```
java -version
```

**Saída do console:**

```
openjdk version "17.0.11" 2024-04-16 LTS
OpenJDK Runtime Environment Zulu17.50+19-CA (build 17.0.11+9-LTS)
OpenJDK 64-Bit Server VM Zulu17.50+19-CA (build 17.0.11+9-LTS, mixed mode, sharing)
```

**Verifique a versão do Maven:**

```
mvn -v
```

**Saída do console:**

```
Apache Maven 3.9.8
Java version: 17.0.11, vendor: Azul Systems, Inc., runtime: /home/andre/.sdkman/candidates/java/17.0.11-zulu
Default locale: pt_BR, platform encoding: UTF-8
OS name: "linux", version: "6.8.0-39-generic", arch: "amd64", family: "unix"

```

**Instalando e executando a aplicação:**

```
mvn clean install -e -Plocal -DskipTests spring-boot:run
```

**Abaixo é possível visualizar a execução da aplicação via terminal:**

![Log de execução via terminal](/docs/application_start.png)

#### Subindo aplicação local (_via runner do Intelijj_)

Com a IDE devidamente instalada e configurada, é possível executar a aplicação via runner do Maven. As configurações de execução
ficam na raiz do projeto dentro do diretório **_.run_** e são acessíveis em: 

![Maven runner](/docs/maven_runner.png)

Desta forma é possível executar sem ou com a opção de debug, caso opte pelo execução com debug, será necessário executar o outro runner (_**debug**_)
após a inicialização da aplicação.

#### Subindo aplicação local (_via container do docker_)

Subindo via docker, serão criados os containers do **elasticsearch**, **kibana**, **apm-server**, além do container da aplicação.

Com essa stack nos containers será possível dar mais observabilidade (Logging/Tracing/Monitoring) para a aplicação.

Para iniciar, abra um terminal que tenha capacidade de executar comandos linux, em seguida, navegue até o diretório '**_/local/docker_**' e excute o script '**_build.sh_**'

```
sh ./build.sh
```

Este processo pode ser um pouco longo, devido ao docker ter que realizar o download das imagens e criaçao dos container. 

Se tudo correr bem, o seguinte log será exibido:

![Criação do container](/docs/containers.png)

É possível visualizar os logs de execução do container da aplicação através do comando:

```
docker logs -f jwt-decode
```

Caso não seja possível executar o script, faça o seguinte procedimento dentro do diretório '/local/docker':

Execute o comando para subir os containers: 

```
docker compose up -d --build
```

Quando receber a mensagem '**You may now test your application**' no console, a aplicação estará de pé e apta para testes.

##### Observbilidade

A parte de observabilidade pode ser vista através da url http://localhost:5601/ e está disponível somente na execução da aplicação via containers.

![Kibana](/docs/kibana.png)

É possível visualizar o APM service '**jwt-decode**' e o environment '**local**' para a coleta dos dados:

![APM server](/docs/apm_service.png)

Na parte de transações do tipo **request**, temos o **trace** de uma requisição inválida na aplicação:

![Request transaction](/docs/transaction_monitoring.png)

Para descobrir mais sobre o que essa ferramente tem a oferecer, basta realizar testes na aplicação e navegar nos menus do Kibana. 

#### Encerrando a execução dos containers

Para finalizar a execução dos container navegue até o diretório '**/local/docker**' e digite o comando no terminal:

```
docker compose down -v
```

Será exibido o seguinte **log** no console: 

![Removendo containers](/docs/removendo_containers.png)

## ⚙️ Executando os testes

Para testar a aplicação, foi criado uma coleção específica que pode ser importada pelo Insomnia. 
Veja como instalar o software utilizando o site oficial. [Insomnina: Guia oficial de instalação](https://docs.insomnia.rest/insomnia/install)

### Criando e importando coleções com o Insomnia

Para criar uma **coleção**, vá até a home da aplicação e selecione '**new collection**' ou clique no sinal de '**+**' para adicionar:

![Criar coleção no Insomnia](/docs/insomnia_collection.png)

Em seguida, dê um **nome** para sua coleção (**sugestão:** jwt-decode):

![Nomear a coleção do Insomnia](/docs/insomnia_collection_1.png)

Após a criação da coleção, volte até a home do Insomnina, clique nos '**3 pontinhos**' dentro da sua coleção e selecione '**import**':

![Importar a coleção](/docs/insomnia_collection_2.png)

Selecione o arquivo dentro da diretório '**/local/collection**' na **raiz do projeto** e avance até o arquivo ser importado:

![Buscando coleção no diretório](/docs/insomnia_collection_3.png)

Com a coleção importada, selecione o environmente 'local' para executar os testes:

![Selecionando environment local](/docs/insomnia_collection_4.png)

### Executando os cenários de teste

O environment 'local' já vem con variáveis de ambiente configuradas para rodar os cenários de JWT válido e inválido.

#### Cenários de JWT válido

Dentro da pasta '**valid**' é possível encontrar os cenários onde o token JWT informado é válido.
Todos os cenários desta pasta retornam o código de resposta '**200 OK**' e response body com objeto '**data**' informando que o token é válido:

![Cenário de token JWT válido](/docs/insomnia_collection_5.png)

#### Cenários de JWT inválido

Na pasta 'invalid' encontram-se os cenários onde o JWT é considerado inválido. O código de resposta retornado pode ser '**400 Bad Request**' 
ou '**422 Unprocessable Entity**' com o objeto retornado sendo um **ProblemDetail** com properties adicionais '**isValid**' e '**reasons**' descrevendo
que o token é inválido e os motivos:

![Cenário de JWT inválido](/docs/insomnia_collection_6.png)

Explique que eles verificam esses testes e porquê.

```
Dar exemplos
```

### ⌨️ E testes de estilo de codificação

Explique que eles verificam esses testes e porquê.

```
Dar exemplos
```

## 📦 Implantação

Adicione notas adicionais sobre como implantar isso em um sistema ativo

## 🛠️ Construído com

Mencione as ferramentas que você usou para criar seu projeto

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - O framework web usado
* [Maven](https://maven.apache.org/) - Gerente de Dependência
* [ROME](https://rometools.github.io/rome/) - Usada para gerar RSS

## 🖇️ Colaborando

Por favor, leia o [COLABORACAO.md](https://gist.github.com/usuario/linkParaInfoSobreContribuicoes) para obter detalhes sobre o nosso código de conduta e o processo para nos enviar pedidos de solicitação.

## 📌 Versão

Nós usamos [SemVer](http://semver.org/) para controle de versão. Para as versões disponíveis, observe as [tags neste repositório](https://github.com/suas/tags/do/projeto).

## ✒️ Autores

Mencione todos aqueles que ajudaram a levantar o projeto desde o seu início

* **Um desenvolvedor** - *Trabalho Inicial* - [umdesenvolvedor](https://github.com/linkParaPerfil)
* **Fulano De Tal** - *Documentação* - [fulanodetal](https://github.com/linkParaPerfil)

Você também pode ver a lista de todos os [colaboradores](https://github.com/usuario/projeto/colaboradores) que participaram deste projeto.

## 📄 Licença

Este projeto está sob a licença (sua licença) - veja o arquivo [LICENSE.md](https://github.com/usuario/projeto/licenca) para detalhes.

## 🎁 Expressões de gratidão

* Conte a outras pessoas sobre este projeto 📢;
* Convide alguém da equipe para uma cerveja 🍺;
* Um agradecimento publicamente 🫂;
* etc.


---
⌨️ com ❤️ por [Armstrong Lohãns](https://gist.github.com/lohhans) 😊