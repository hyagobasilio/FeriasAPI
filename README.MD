[![Build Status](https://travis-ci.org/hyagobasilio/feriasApi.svg?branch=master)](https://travis-ci.org/hyagobasilio/feriasApi)

# Ferias API

Projeto rodando no Heroku: https://feriasapi.herokuapp.com/

Projeto criado para servir de referência *API RESTfull com Spring Boot utilizando TDD, CI e CD*, a principal funcionalidade dessa API é permitir o cadastro funcionários e ferias e indicar os que precisam tirar ferias.

## Detalhes do projeto
*Esse projeto possui as seguintes características:*

* Projeto criado com Spring Boot e Java 8
* Banco de dados Postgres com JPA e Spring Data JPA
* Versionamento de banco de dados com Flyway
* Testes com JUnit e Mockito com banco H2 em memória
* Nuvem do Heroku
* Integração contínua com TravisCI
* Project Lombok
* Documentação dos endpoints com Swagger
* Segurança da API com autenticação via tokens JWT 

## Como executar a aplicação
Certifique-se de ter o Maven instalado e adicionado ao PATH de seu sistema operacional, assim como o Git, crie um banco de dados no mysql e altere o arquivo application.properties informando as credenciais para a aplicação acessar a base de dados, não se preocupe com a criação das tabelas, o flyway se encarregará dessa função.
```
git clone https://github.com/hyagobasilio/feriasApi
cd feriasApi
mvn spring-boot:run
Acesse os endpoints através da url http://localhost:8080
```

Também é possível compilar o projeto para executar em um ambiente de produção, para isso execute o seguinte comando na raiz do projeto

```
mvn clean install
```

O pacote será gerado dentro da pasta target, basta executá-lo com o comando abaixo, não esquecendo de configurar qual o profile e a porta que a aplicação deverá utilizar.
Também é necessário criar uma variável de ambiente com as credenciais de acesso ao banco de dados com o nome $DATABASE_URL ou alterar o arquivo application-prod.properties.

```
java -jar -Dspring.profiles.active=prod -Dserver.port=443 feriasApi-0.0.1-SNAPSHOT.jar
```
## Como autenticar na API
Antes de fazer requisições nos endpoint você deve estar autenticado.

Agora você pode autenticar na API e obter um token de acesso através do endpoint */auth*, utilizando o email e a senha cadastrados:

```
{
	"email": "administrador.ferias@castgroup.com.br",
	"password":"123a"
}
```

Você receberá um token do tipo Bearer que deve ser enviado no cabeçalho das requisições.
### Como executar os testes
Os testes podem ser executados com o seguinte comando:

```
mvn test
```
### Documentação
Utilize a interface do Swagger para ter acesso a documentação dos endpoints, ela está disponível na url http://localhost:8080/swagger-ui.html
### Heroku
A aplicação está disponível no heroku através da url 
https://feriasapi.herokuapp.com/