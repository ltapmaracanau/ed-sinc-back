# ed-sinc

Sistema de inscrições e gerenciamento de turmas e usuários para exportação.

## Índice

- [Recursos](#recursos)
- [Instalação](#instalação)
- [Uso](#uso)
- [Licença](#licença)
- [Contato](#contato)

## Recursos

- Autenticação e autorização de usuários
- Envio de notificações por email
- Suporte a operações CRUD (Create, Read, Update, Delete)
- Documentação da API com Swagger
- Armazenamento de mídia de perfil

## Instalação

1. Clone o repositório:
    ```bash
    git clone https://github.com/ltapmaracanau/ed-sinc-back.git
    cd ed-sinc-back
    ```

2. Configure o Maven e o Lombok conforme descrito na seção de instalação. Atualize o arquivo `application.properties` com suas credenciais e configurações:

    ```properties
    # Configurações do Banco de Dados (utilize as configurações do seu DB local ou remoto como porta, usuario, senha e db (neste caso, mysql))
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.username=[SEU_USUARIO_DO_BD]
    spring.datasource.password=[SUA_SENHA_DO_BD]
    spring.datasource.url=jdbc:[mysql://localhost:3306/]ed-sinc?createDatabaseIfNotExist=true&zeroDateTimeBehavior=convertToNull&autoReconnect=true&allowPublicKeyRetrieval=true&useSSL=false

    # Configurações de Email SMTP
    spring.mail.host=smtpout.secureserver.net
    spring.mail.username=[SEU_EMAIL]
    spring.mail.password=[SUA_SENHA_DO_EMAIL]
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.socketFactory.port=[SUA_PORTA]
    spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
    spring.mail.properties.mail.smtp.socketFactory.fallback=false
    spring.mail.properties.mail.smtp.connectiontimeout=5000
    spring.mail.properties.mail.smtp.timeout=5000
    spring.mail.properties.mail.smtp.writetimeout=5000

    # Configurações de Armazenamento (gerado automaticamente a partir da raiz do sistema)
    edsinc.storage.local.diretorio-fotos=/home/edsinc/assetstore/imagens/

    # URL do Frontend (em construção)
    edsinc.frontend-url=https://www.frontend.gov.br

    # Perfil Ativo em `application[perfil].properties`
    spring.profiles.active=[perfil] (dev;local;prod;hom)
    ```

3. Configure a versão do Java e o Lombok no seu ambiente de desenvolvimento:

    A versão do Java necessária para este projeto é a 17. Certifique-se de que o JDK 17 está instalado no seu sistema. Você pode verificar a versão do Java instalada com o comando:
    ```bash
    java -version
    ```

    Se necessário, baixe e instale o JDK 17 a partir do site oficial da Oracle ou do OpenJDK.

    Para configurar o Lombok, você precisa adicionar o plugin do Lombok na sua IDE:

    - No **Spring Tool Suite (STS)** ou **Eclipse**:
      - Vá para `Help > Eclipse Marketplace`.
      - Procure por `Lombok` e instale o plugin.
      - Após a instalação, reinicie a IDE.
      - Certifique-se de que o `annotation processing` está habilitado em `Window > Preferences > Maven > Annotation Processing`.

4. Instale as dependências do projeto usando o Maven ou utilize uma IDE que faça a instalação automática como STS ou Eclipse:

    - No **Spring Tool Suite (STS)** ou **Eclipse**:
      - Clique com o botão direito no projeto.
      - Selecione `Maven > Update Project...` ou use o atalho `Alt + F5`.
      - Certifique-se de que todas as opções estão selecionadas e clique em `OK`.

    - Ou, navegue até o diretório do projeto e execute:
    ```bash
    mvn clean install
    ```

5. Execute a aplicação:

    - Usando Maven:
    ```bash
    mvn spring-boot:run
    ```

    - Ou diretamente a partir da sua IDE (STS ou Eclipse):
      - No painel de navegação do projeto, encontre o arquivo `EdSincApplication.java`.
      - Clique com o botão direito no arquivo e selecione `Run As > Java Application` ou utilize `Alt + Shift + X` depois a opção `J`.

## Uso

1. Acesso à Documentação da API:

A documentação da API é gerada usando Springdoc OpenAPI. Acesse a interface do Swagger como exemplo o servidor local (http://localhost:8080):
```bash
http://localhost:8080/swagger-ui.html
 ```
2. JWT token
Authorization: Bearer [seu_jwt_token]

3. Utilização de end-points (Exemplo para Usuários):
- DELETE [servidor]/usuarios/{id}: Deleta um usuário com base no seu ID.
- GET [servidor]/usuarios/{id}: Busca um usuário com base no seu ID.
- GET [servidor]/usuarios/restaurar/{id}: Restaura um usuário com base no seu ID.
- GET [servidor]/usuarios/relatorio: Retorna todos os usuários em uma lista organizada para um relatório.
- GET [servidor]/usuarios/consultar: Retorna todos os usuários com filtros de consulta em uma lista.
- GET [servidor]/usuarios/bloquear/{id}: Bloqueia um usuário com base no seu ID.
- GET [servidor]/usuarios/arquivar/{id}: Arquiva um usuário com base no seu ID.
- POST [servidor]/usuarios: Cadastro de um novo usuário.
- PUT [servidor]/usuarios/{id}: Edita um usuário com base no seu ID.

## Licença
- Versão 0.0.1

## Contato
- Contatar o manutendor do repositório via email.

