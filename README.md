# Samurai Cars

A Samurai Cars é uma API REST que permite o cadastramento e a disponibilização de veículos online. 
Com ela, vendedores podem cadastrar seus veículos, e compradores podem encontrá-los de forma rápida e eficiente.

## Como Executar

### Execução via deploy
- A API está disponível no seguinte link: [https://samurai-cars.onrender.com](https://samurai-cars.onrender.com)
- O Swagger pode ser visualizado em [https://samurai-cars.onrender.com/swagger-ui/index.html#/](https://samurai-cars.onrender.com/swagger-ui/index.html#/).

### Execução com Docker
- Baixe a imagem :
```
docker pull brwnus/samurai_cars:1.0 
```
- Execute a imagem
```
docker run -p 8080:8080 brwnus/samurai_cars:1.0
```
- A API pode ser acessada em [localhost:8080](http://localhost:8080).
- O Swagger pode ser visualizado em [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

## Instruções para fazer login

- Para fazer login como administrador, use as seguintes credenciais:
```
{
  "email": "samuraiAdmin@email.com",
  "password": "samuraiAdmin"
}
```
- Para fazer login como um usuário, use as seguintes credenciais:
```
{
  "email": "xaropinho@email.com",
  "password": "xaropinhoUser"
}
``` 

## Práticas Adotadas
- API REST
- Validação de dados com anotações do Spring validations e Hibernate
- Consultas com Spring Data JPA
- Tratamento de respostas de erro
- Autenticação e autorização com Spring Security e JWT
- Proteção de endpoints e recursos
- Gestão de usuários e permições
- Geração automática do Swagger com a OpenAPI 3
- Docker
- Intregração do Back-end com o Front-end
