# Login Seguro e Dashboard de Chamados

Sistema de login com cadastro, autenticação e controle de acesso por perfil, evoluído com um dashboard administrativo de chamados técnicos. Feito com Java Spring Boot, Spring Security, Thymeleaf, Chart.js e MongoDB Atlas.

O projeto foi organizado de forma genérica para servir de base a outros sistemas, bastando acrescentar novas entidades e páginas sem alterar a lógica de autenticação.

## Funcionalidades

* Cadastro de usuários com validação dos dados
* Senhas armazenadas com hash BCrypt
* Login e logout com Spring Security
* Três perfis de acesso: `ADMIN`, `GESTOR` e `USUARIO`
* Usuários, sessões e chamados armazenados no MongoDB Atlas
* Tema visual configurável
* API REST de chamados e de indicadores
* Dashboard com indicadores, gráficos, ranking e filtros

## Perfis e permissões

| Página | Rota | USUARIO | GESTOR | ADMIN |
|---|---|---|---|---|
| Início | `/` | Sim | Sim | Sim |
| Painel do gestor (dashboard) | `/gestor/dashboard` | Não | Sim | Sim |
| Usuários | `/admin/usuarios` | Não | Não | Sim |
| API | `/api/**` | Não | Sim | Sim |

Todo usuário que se cadastra recebe o perfil `USUARIO`. O administrador pode alterar o perfil ou excluir usuários na página Usuários.

## Dashboard de chamados

O painel do gestor apresenta:

* Indicadores gerais: total, abertos, em andamento, concluídos e tempo médio de atendimento
* Gráfico de chamados atendidos por período
* Gráfico da situação dos chamados
* Ranking dos tipos de chamado mais atendidos
* Tabela com os chamados e as observações técnicas
* Filtros por período de abertura, status, tipo e técnico
* Atualização pelo botão Atualizar e automática a cada minuto

A página carrega os dados chamando a API por JavaScript.

## API

Todos os endpoints de consulta aceitam os filtros opcionais `inicio` e `fim` (data de abertura, formato `AAAA-MM-DD`), `status`, `tipo` e `tecnico`.

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/chamados` | Lista os chamados |
| GET | `/api/chamados/{id}` | Busca um chamado |
| POST | `/api/chamados` | Cadastra um chamado |
| PUT | `/api/chamados/{id}` | Atualiza um chamado |
| GET | `/api/dashboard/indicadores` | Indicadores gerais |
| GET | `/api/dashboard/atendidos-por-periodo` | Chamados concluídos por data |
| GET | `/api/dashboard/status` | Quantidade de chamados por status |
| GET | `/api/dashboard/tipos` | Ranking dos tipos de chamado |

Exemplo de corpo para cadastro:

```json
{
  "descricao": "Computador não liga",
  "status": "Aberto",
  "tipo": "Manutenção",
  "tecnico": "João",
  "usuario": "Alberto"
}
```

Possíveis erros:

| Código | Quando acontece |
|---|---|
| 400 | Campo obrigatório vazio, JSON inválido ou data fora do formato |
| 401 | Requisição sem autenticação |
| 403 | Usuário sem perfil `GESTOR` ou `ADMIN` |
| 404 | Chamado não encontrado |

Fora do navegador (por exemplo, no Postman), a API aceita autenticação Basic com o e-mail e a senha de um usuário `GESTOR` ou `ADMIN`.

## Estrutura do projeto

```
src/main/java/com/loginseguro
├── config        Segurança, tema, administrador e chamados iniciais
├── controller    Páginas e endpoints da API
├── entity        Usuario, Perfil e Chamado
├── repository    Acesso ao MongoDB
└── service       Regras de negócio, autenticação e indicadores

src/main/resources
├── application.properties
├── static
│   ├── css
│   │   ├── style.css         Estilos das páginas
│   │   └── temas             Um arquivo de cores por tema
│   └── js/dashboard.js       Integração do dashboard com a API
└── templates
    ├── fragments/layout.html   Cabeçalho e menu reaproveitados
    ├── login.html
    ├── cadastro.html
    ├── home.html
    ├── acesso-negado.html
    ├── admin/usuarios.html
    └── gestor/dashboard.html
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

Clusters gratuitos são pausados após um período sem uso. Se o sistema não conectar, confira no Atlas se o cluster está ativo e se o seu IP atual está liberado.

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

A conexão é configurada no `application.properties` pela propriedade `spring.data.mongodb.uri`, que recebe o valor de `MONGODB_URI`. O banco usado é o `loginseguro`, com três coleções:

| Coleção | Conteúdo | Quem grava |
|---|---|---|
| `usuarios` | Nome, e-mail, senha com hash e perfil | `UsuarioRepository` (Spring Data MongoDB) |
| `sessoes` | Sessões dos usuários autenticados | Spring Session MongoDB |
| `chamados` | Descrição, status, tipo, técnico, usuário, datas e observação técnica | `ChamadoRepository` (Spring Data MongoDB) |

As coleções são criadas automaticamente na primeira execução. Se a coleção `chamados` estiver vazia, o sistema cadastra dois chamados de exemplo.

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