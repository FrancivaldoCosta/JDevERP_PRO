# Jdev ERP Pro
ERP SaaS multiempresa (back-end): planos/empresas, pessoas, clientes/funcionários, usuários/roles, categorias, produtos, estoque, pedidos e chamados de suporte.
Java 25, Spring Boot 4.0.6, Spring Data JPA/Hibernate, Spring Security + JWT (jjwt 0.12.7), PostgreSQL, Lombok, Maven 3.9 (sem wrapper).

## Comandos
- Build: `mvn clean package` (`-DskipTests` pula os testes, que precisam do banco)
- Todos os testes: `mvn test`
- Um teste: `mvn test -Dtest=TestCategoriaRepository` ou `-Dtest=TestCategoriaRepository#metodo`
- Rodar local: `mvn spring-boot:run` → `http://localhost:8081/jdev-erp-pro-back-end-dev`
- Perfil: variável `SPRING_PROFILES_JDEV_ERP_PRO` (`dev` | `prod`, padrão `dev`); não é `SPRING_PROFILES_ACTIVE`
- Banco local: PostgreSQL instalado na máquina (não há docker-compose)

## Estrutura (pacote base: `br.com.jdeverp.pro`)
Organização por camada:
- `app` — `JDevERPPROApplication` (main; define scan de entidades, repositories e componentes)
- `controller` — REST `@RestController`, prefixo `/api/<entidadeCamelCase>`
- `service` — regras de negócio; `UsuarioLogadoService` fornece usuário/empresa logados
- `repository` — interfaces que estendem `JpaJdevRepository<T, ID>` (base `JpaJdevRepositoryImpl`)
- `model` — entidades JPA (Lombok `@Data`)
- `dto` — DTOs (hoje só Usuario, Login, Token, AlterarSenha, Empresa, ClienteFuncionario)
- `enums` — enums persistidos com `@Enumerated(EnumType.STRING)`
- `exception` — `GlobalExceptionHandler`, `MsgApiException`, `ResponseApi`
- `security` — `SecurityConfig`, `JwtFilter`, `UsuarioAutenticado`, handlers 401/403
- `validator` — `RepositoryEmpresaValidator` (checa o filtro por empresa na subida)
- `anotacoes` — `@IgnoreEmpresaId`
- `config`, `util` — configs e utilitários (`ExceptionUtil`, `ObjectMapperUtil`)
- Configuração: `src/main/resources/application.properties` (só escolhe o perfil), `application-dev.properties`, `application-prod.properties`
- Testes: `src/test/java/br/com/jdeverp/pro/` (base `contexto/TestContextoSpring`)
- Migrations: sem Flyway/Liquibase; o schema é gerado pelo Hibernate (`ddl-auto=update`).
  Script manual de carga: `src/main/webapp/db_sql_versionamento/V1_INIT.sql` (apaga dados; nunca rodar sem eu pedir).

## Convenções
- Nomes em português: classes, métodos, rotas, mensagens (`salvar`, `atualizar`, `buscarPorId`, `existePorNome`, `listarPaginado`).
- A maioria dos CRUDs recebe/devolve a entidade; dados sensíveis (usuário, login) usam DTO.
- Validação: Bean Validation na entidade/DTO com mensagem em português + `@Valid` no `@RequestBody`.
- Regra de negócio violada: `throw new MsgApiException("mensagem")` (400) ou `new MsgApiException(msg, HttpStatus.X)`.
- Injeção de dependência: `@Autowired` em campo (padrão do projeto).
- Detalhes de controllers/DTOs/erros e de entidades/repositories ficam em `.claude/rules/` (carregados por caminho).

### Multiempresa (obrigatório)
- Toda entidade de negócio tem `empresa` (`empresa_id`), exceto `Empresa`, `Plano`, `Role`, `RoleUsuario`.
- Controller passa `usuarioLogadoService.getEmpresaIdLogada()` ao service; o service faz
  `entidade.setEmpresa(usuarioLogadoService.getEmpresaLogada())` em salvar/atualizar (nunca confia no JSON).
- Todo método declarado em repository precisa de `@Query` contendo `empresa.id` ou `empresa_id`;
  senão a aplicação NÃO sobe (`RepositoryEmpresaValidator`). Exceção só com `@IgnoreEmpresaId(motivo = "...")`.
- Prefira os métodos genéricos do `JpaJdevRepository` (`listar`, `buscarPorId`, `listarPaginado`, `total`,
  `existsById`, `buscarPorIds`, `deletarAllById`, `deleteAll(empresaId)`) aos do `JpaRepository` puro.

### Segurança
- JWT stateless; token validado no `JwtFilter` e conferido no banco (`Usuario.tokenSessao`).
- Rota pública: só `POST /api/usuario/login`. `/api/admin/**` exige `ROLE_ADMIN`; o resto exige autenticação.
- Roles existentes: `ROLE_ADMIN`, `ROLE_USER` (atribuída no cadastro de usuário; seed em `V1_INIT.sql`).

### Testes
- Testes de integração estendem `TestContextoSpring` (`@SpringBootTest`, `@Transactional` com rollback).
- Eles rodam contra o banco do perfil ativo (dev); não há H2/Testcontainers.

## Regras que nunca podem ser quebradas
- Não há migrations: mudança de entidade que renomeie/remova coluna ou mude tipo exige me avisar antes
  e entregar o script SQL correspondente (o `update` do Hibernate não faz isso sozinho).
- Nunca usar `ddl-auto=create`/`create-drop` e não mudar o valor de `ddl-auto` sem eu pedir.
- Nunca criar método de repository sem filtro por empresa, nem usar `@IgnoreEmpresaId` sem `motivo` justificado.
- Todo endpoint novo precisa de regra de autorização explícita (`@PreAuthorize`, `usuarioLogadoService.isAdmin()` no service ou regra no `SecurityConfig`).
- Nunca adicionar credenciais ao código ou aos `.properties`; segredos novos vêm de variáveis de ambiente.
- Nunca expor `senha`, `tokenSessao` ou `refreshToken` em respostas da API.
- Rodar os testes afetados antes de dizer que uma tarefa terminou (e avisar se o banco não estiver disponível).
- Não fazer git commit nem git push sem eu pedir.

## Mais contexto (ler só quando a tarefa pedir)
- Arquitetura e módulos: `docs/arquitetura.md`
- Regras de negócio: `docs/regras-negocio.md`
- Análise técnica completa e pontos de atenção: `docs/analise-projeto.md`
- Variável de perfil (Windows/Linux): `documentação/1 - Documentação`
