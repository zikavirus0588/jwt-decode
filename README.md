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

Nesta seção encontram-se instruções de como baixar, instalar e executar o projeto localmente.

### 📋 Pré-requisitos

- [Git](https://git-scm.com/book/pt-br/v2/Come%C3%A7ando-Instalando-o-Git)
- [Docker e Docker Compose](https://docs.docker.com/get-docker/)

### Clonando o repositório

Certifique-se de ter as chaves SSH devidamente configuradas em sua máquina e adicionadas à sua conta do GitHub. Caso ainda não tenha feito este procedimento,
siga o [tutorial oficial](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/about-ssh) para gerar e adicionar chaves SSH.

Após realizar o procedimento acima, abra um terminal no diretório onde deseja baixar o projeto e digite:

```
git clone git@github.com:zikavirus0588/jwt-decode.git
```

### 🔧 Instalação
Guia para instalação da aplicação utilizando containers do docker.

#### Subindo aplicação local (_via container do docker_)

Subindo via docker, serão criados os containers do **elasticsearch**, **kibana**, **apm-server**, além do container da aplicação.

Com essa stack nos containers será possível dar observabilidade (Logging/Tracing/Monitoring) para a aplicação.

Para começar, abra um terminal e navegue até a raiz do projeto. Certifique-se de ter instalado o 'Make' e execute o seguinte comando:

```
make docker-compose-local-up
```

Este processo pode ser um pouco longo, devido ao docker ter que realizar o download das imagens e criaçao dos container. 

Se tudo correr bem, o seguinte log será exibido:

![Criação do container](/docs/containers.png)

É possível visualizar os logs de execução do container da aplicação através do comando:

```
docker logs -f jwt-decode
```

##### Observbilidade

Com os containers de pé, é possível acessar a url do kibana através do endereço http://localhost:5601/ e começar a explorar o serviço.

![Kibana](/docs/kibana.png)

É possível visualizar o APM service '**jwt-decode**' e o environment '**local**' para a coleta dos dados:

![APM server](/docs/apm_service.png)

Na parte de transações do tipo **request**, temos o **trace** de uma requisição inválida na aplicação:

![Request transaction](/docs/transaction_monitoring.png)

#### Encerrando a execução dos containers

Para finalizar a execução dos container navegue até o diretório '**/local/docker**' e digite o comando no terminal:

```
make docker-compose-local-down
```

Será exibido o seguinte **log** no console: 

![Removendo containers](/docs/removendo_containers.png)

## ⚙️ Executando os testes

Para testar a aplicação, foi criado uma coleção específica que pode ser importada pelo Insomnia. 

### Pré-requisitos
- [Insomnia](https://docs.insomnia.rest/insomnia/install)

### Criando e importando coleções com o Insomnia

Para criar uma **coleção**, vá até a home da aplicação e selecione '**new collection**' ou clique no sinal de '**+**' para adicionar:

![Criar coleção no Insomnia](/docs/insomnia_collection.png)

Em seguida, dê um **nome** para sua coleção (**sugestão:** jwt-decode):

![Nomear a coleção do Insomnia](/docs/insomnia_collection_1.png)

Após a criação da coleção, volte até a home do Insomnia, clique no menu '**...**' dentro da sua coleção e selecione '**import**':

![Importar a coleção](/docs/insomnia_collection_2.png)

Selecione o arquivo dentro da diretório '**collections**' na **raiz do projeto** e avance até o arquivo ser importado:

![Buscando coleção no diretório](/docs/insomnia_collection_3.png)

Com a coleção importada, selecione o environment '**local**' para executar os testes:

![Selecionando environment local](/docs/insomnia_collection_4.png)

### Executando os cenários de teste

O environment '**local**' já vem con variáveis de ambiente configuradas para rodar os cenários de JWT **válido** e **inválido**.

#### Cenários de JWT válido

Dentro da pasta '**valid**' é possível encontrar os cenários onde o token JWT informado é válido.
Todos os cenários desta pasta retornam o código de resposta '**200 OK**' e response body com objeto '**data**' informando que o token é válido:

![Cenário de token JWT válido](/docs/insomnia_collection_5.png)

#### Cenários de JWT inválido

Na pasta 'invalid' encontram-se os cenários onde o JWT é considerado inválido. O código de resposta retornado pode ser '**400 Bad Request**' 
ou '**422 Unprocessable Entity**' com o objeto retornado sendo um **ProblemDetail** com properties adicionais '**isValid**' e '**reasons**' descrevendo
que o token é inválido e os motivos:

![Cenário de JWT inválido](/docs/insomnia_collection_6.png)


## 📦 Implantação

Está seção tem como objetivo criar uma infraestrutura mínima necessária para executar essa aplicação na núvem pública da AWS.

### Pré-Requisitos
- Makefile
  - [Windows](https://earthly.dev/blog/makefiles-on-windows/)
  - Linux (sudo apt-get install build-essential)
- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
- [OpenTofu](https://opentofu.org/docs/intro/install/)

### Diagrama da solução

![Diagrama da solução](/docs/arquitetura.png)

### Criando repositório do ECR

Antes de criar toda infraestrutura, é necessário ter um repositório para guardar a imagem docker da aplicação na AWS.

Antes de dar início à criação, é necessário [configurar o AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html).

Após este procedimento, [crie um repisório privado do ECR](https://docs.aws.amazon.com/pt_br/AmazonECR/latest/userguide/repository-create.html)
com o nome **'jwt-decode-des'** na mesma região onde o usuário do AWS CLI foi configurado.

Com o usuário configurado e repositório criado,é hora de criar a imagem docker e enviar para o repositório privado do ECR. Para isto, abra um terminal
na raiz do projeto e execute o comando:

```
make docker-build
```
Após a conclusão do comando, faça o envio da imagem diretamente para o repositório:

```
make ecr-push
```

Este comando procura o profile default da AWS nas configurações locais, o ID da conta e também a região configurada. Com estes valores, ele realiza
o login no ECR, cria a tag para a imagem do projeto e faz o envio para o repositório privado do ECR:

![ECR push](/docs/ecr_push.png)

### Criando infraestrutura

Com a imagem do projeto já armazenada no repositório privado do ECR, agora é a hora de criar a infraestrutura restante.

Abra um terminal na raiz do projeto e então execute: 

```
make tf-init
```

Este comando dá início ao processo para criar a infraestrutura na AWS, mapeando os recursos existentes dentro do diretório **'infrastructure'**.

Proxímo passo é planejar o que será, de fato, criado, digite o comando:

```
make tf-plan
```

Com tudo devidamente planejado, é hora de aplicar o procedimento e criar os recursos na AWS, digite no terminal:

```
make tf-apply
```

Vai demorar um pouco até que toda infraestrutura seja criada, quando o procedimento finalizar, teremos a mensagem:

![tf-apply](/docs/tf_apply.png)

Copie o valor da variável **'api_endpoint'**, ela será o ponto de entrada para testar a aplicação na AWS.

Para validar a criação da infraestrutura e a aplicação funciona corretamente, cria a url abaixo com o valor copiado e cole em algum navegador:

**ex:** https://6js8bfnwza.execute-api.us-east-2.amazonaws.com/jwt-decode/api/swagger-ui/index.html

Será possível visualizar a documentação da API:

![Documentação API](/docs/documentacao_api.png)

### Destruindo infraestrutura

Para destruir a infraestrutura, removedo todos os recursos criados no passo a passo anterior, abra um terminal na raiz do projeto e execute o comando:

```
make tf-destroy
```

Os recursos serão removidos da AWS e teremos o seguinte log ao finalizar: 

![Removendo infraestrutura](/docs/tf_destroy.png)

Por último, para remover a imagem enviada ao repositório privado, acesse o console da AWS e acesse a página referente ao recurso do ECR. Em seguida, procure pela 
imagem criada e delete do seu repositório privado. 

### Testando aplicação na AWS

Este projeto foi implantado na núvem da AWS, executando o passo a passo descrito acima. Abaixo encontra-se a url do API Gateway da aplicação e o link da documentação:

- URL-BASE: https://6js8bfnwza.execute-api.us-east-2.amazonaws.com/jwt-decode/api
- Documentação: https://6js8bfnwza.execute-api.us-east-2.amazonaws.com/jwt-decode/api/swagger-ui/index.html

Para realizar testes da aplicação rodando na AWS, copia a URL e crie uma collection nova no insomnia, substituindo a url da coleção local.