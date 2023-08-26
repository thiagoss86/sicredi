# API Sicredi
Esta API é referente ao desafio da NTCounsult / Sicredi. Trata-se de um sistema de votação com cadastro de pauta e abertura de sessão.

## Build | Maven
Executar o comando `mvn clean install`.

## Desenvolvimento local / docker-compose
Entrar no diretório **src/main/resources/docker** e executar o comando `docker-compose up -d`.

Será criado um container **sicredi** com usuário `sicredi_user` e senha `sicredi_psw`

>**Host:** *localhost* | **Port:** *3306* | **Database:** *sicredi*

##Kafka
Será criado um container **kafka** para envio de mensagens para o tópico

>**Kafdrop:** *localhost* | **Port:** *1900*

##Swagger
API está documentada com o **OpenApi** do sprinboot.

Para acessar o **Spring-Docs** utilizar a url no navegador `http://localhost:8080/sicredi/v1/v3/api-docs`.

Para acessar o **Swagger** utilizar a url no navegador `http://localhost:8080/sicredi/v1/swagger-ui/index.html`.

##Considerações
```
O desafio tem uma tarefa bônus para consumir uma API extenar para consultar o CPF. Foi criado um cliente utilizando o WebFlux mas o mesmo não foi implementado,
pois no momento da criação do desáfio o serviço estava fora do ar.

```
