# Login Seguro

Sistema de login com cadastro, autenticação e controle de acesso por perfil, feito com Java Spring Boot, Spring Security, Thymeleaf e MongoDB Atlas.

O projeto foi organizado de forma genérica para servir de base a outros sistemas, bastando acrescentar novas entidades e páginas sem alterar a lógica de autenticação.

## Funcionalidades

* Cadastro de usuários com validação dos dados
* Senhas armazenadas com hash BCrypt
* Login e logout com Spring Security
* Três perfis de acesso: `ADMIN`, `GESTOR` e `USUARIO`
* Usuários e sessões armazenados no MongoDB Atlas
* Tema visual configurável

## Perfis e permissões

| Página | Rota | USUARIO | GESTOR | ADMIN |
|---|---|---|---|---|
| Início | `/` | Sim | Sim | Sim |
| Painel do gestor | `/gestor/painel` | Não | Sim | Sim |
| Usuários | `/admin/usuarios` | Não | Não | Sim |

Todo usuário que se cadastra recebe o perfil `USUARIO`. O administrador pode alterar o perfil ou excluir usuários na página Usuários.

## Estrutura do projeto

```
src/main/java/com/loginseguro
├── config        Configurações (segurança, tema e administrador inicial)
├── controller    Rotas e páginas
├── entity        Usuario e Perfil
├── repository    Acesso ao MongoDB
└── service       Regras de negócio e autenticação

src/main/resources
├── application.properties
├── static/css
│   ├── style.css         Estilos das páginas
│   └── temas             Um arquivo de cores por tema
└── templates
    ├── fragments/layout.html   Cabeçalho e menu reaproveitados
    ├── login.html
    ├── cadastro.html
    ├── home.html
    ├── acesso-negado.html
    ├── admin/usuarios.html
    └── gestor/painel.html
```

## Pré-requisitos

* Java 17 ou superior
* Conta gratuita no [MongoDB Atlas](https://www.mongodb.com/products/platform)
* Git

Não é necessário instalar o Maven, o projeto já inclui o Maven Wrapper (`mvnw`).

## Configuração do MongoDB Atlas

1. Crie um cluster gratuito (M0) no MongoDB Atlas.
2. Em **Database Access**, crie um usuário de banco com senha.
3. Em **Network Access**, adicione o seu endereço IP.
4. Em **Database > Connect > Drivers**, copie a string de conexão.

## Arquivo de configuração

A string de conexão não fica no código. Ela é lida de um arquivo `.env`, que não é enviado ao GitHub.

1. Faça uma cópia do arquivo `.env.example` com o nome `.env`.
2. Preencha os valores:

```
MONGODB_URI=mongodb+srv://USUARIO:SENHA@SEU_CLUSTER.mongodb.net/?retryWrites=true&w=majority
ADMIN_EMAIL=admin@loginseguro.com
ADMIN_SENHA=uma-senha-forte
```

`ADMIN_EMAIL` e `ADMIN_SENHA` são usados para criar o primeiro administrador quando o sistema inicia pela primeira vez.

## Como executar

No Windows:

```
mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```
./mvnw spring-boot:run
```

Depois acesse http://localhost:8080 e entre com o e-mail e a senha do administrador definidos no `.env`.

## Integração com o MongoDB Atlas

A conexão é configurada no `application.properties` pela propriedade `spring.data.mongodb.uri`, que recebe o valor de `MONGODB_URI`. O banco usado é o `loginseguro`, com duas coleções:

| Coleção | Conteúdo | Quem grava |
|---|---|---|
| `usuarios` | Nome, e-mail, senha com hash e perfil | `UsuarioRepository` (Spring Data MongoDB) |
| `sessoes` | Sessões dos usuários autenticados | Spring Session MongoDB |

As coleções são criadas automaticamente na primeira execução. O campo `email` possui índice único.

## Como trocar o tema

Cada tema é um arquivo em `src/main/resources/static/css/temas` com as variáveis de cor do sistema. Para trocar, altere a propriedade no `application.properties`:

```
app.tema=escuro
```

Para criar um novo tema, copie o arquivo `padrao.css`, mude as cores e informe o nome do novo arquivo em `app.tema`. O nome exibido no sistema é definido em `app.nome`.

## Como adaptar para outro sistema

1. Crie as novas entidades em `entity`, com o repository e o service correspondentes.
2. Crie os controllers e as páginas, reaproveitando os fragmentos de `fragments/layout.html`.
3. Libere as novas rotas por perfil no `SecurityConfig`.
4. Se precisar de outros perfis, acrescente no enum `Perfil`.

## Fluxo de branches

O projeto segue o Gitflow: `main` para as versões entregues, `develop` para a integração e branches `feature/*` para cada funcionalidade.
