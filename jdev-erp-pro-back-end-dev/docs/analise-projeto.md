# Análise do projeto — JDevERP Pro (back-end)

> Gerado na Etapa 1 (análise, sem alteração de código). Data: 2026-11-17.
> Raiz do projeto: `jdev-erp-pro-back-end-dev/` (dentro do repositório Git `JDevERP_PRO/`).
> Itens não confirmados no código estão marcados com `[CONFIRMAR]`.

## 1. Build

- **Maven**, `pom.xml` na raiz do projeto. **Não existe wrapper** (`mvnw` ausente) → usar o `mvn` instalado (3.9.16 na máquina).
- **Java 25** (`java.version`, `maven.compiler.source/target` = 25).
- **Spring Boot 4.0.6** (`spring-boot-starter-parent`).
- groupId/artifactId: `jdev-erp-pro-back-end-dev`; packaging `jar`; único plugin: `spring-boot-maven-plugin`.

## 2. Dependências relevantes

- Starters: `data-jpa`, `security`, `webmvc`, `validation`.
- Testes: `spring-boot-starter-data-jpa-test`, `security-test`, `webmvc-test`.
- `postgresql` (driver), **Lombok** (`@Data` nas entidades), `commons-collections4` 4.5.0.
- **JWT**: `io.jsonwebtoken:jjwt` 0.12.7.
- `jackson-datatype-jsr310` 2.13.5 (versão fixa, Jackson 2).
- **Não usados**: MapStruct, Flyway, Liquibase, springdoc/Swagger, Testcontainers, DevTools (apesar de existirem propriedades `spring.devtools.*`).

## 3. Pacote base e camadas

- Pacote base: **`br.com.jdeverp.pro`**. Classe principal: `br.com.jdeverp.pro.app.JDevERPPROApplication`
  (`@EntityScan` em `.model`, `@EnableJpaRepositories` em `.repository` com `repositoryBaseClass = JpaJdevRepositoryImpl`, `@ComponentScan("br.com.jdeverp.pro")`, `@EnableCaching/@EnableScheduling/@EnableAsync`).
- Organização **por camada**:
  - `anotacoes` — `@IgnoreEmpresaId`
  - `app` — classe main
  - `config` — `ConfiguracaoProducao`, `PostgreSQLFunctionContributor` (registrado em `META-INF/services/org.hibernate.boot.model.FunctionContributor`)
  - `controller` — 14 `*Controller`
  - `dto` — `AlterarSenhaDTO`, `ClienteFuncionarioDTO`, `EmpresaDTO`, `LoginDTO`, `TokenDTO`, `UsuarioDto`
  - `enums`, `exception`, `model` (entidades), `repository`, `security`, `service`, `util`, `validator`
  - **Não há pasta `mapper`**: a conversão entidade↔DTO é feita à mão nos services. `util/ObjectMapperUtil` só serializa JSON nos handlers `JwtAccessDeniedHandler`/`JwtAuthenticationEntryPoint` (confirmado na etapa 2).
- Pacote fora do base: `br.jdev.estoque.erp.multitanent.EmpresaContext` (ver seção 10).

## 4. Módulos de negócio (entidades)

ERP SaaS **multiempresa** (quase toda entidade tem `empresa` → `empresa_id`).

| Módulo | Entidades | Enums |
|---|---|---|
| SaaS/Assinatura | `Plano`, `Empresa` | `TipoPlano` |
| Cadastros | `Pessoa`, `ClienteFuncionario` | `TipoPessoa`, `TipoClienteFuncionario` |
| Acesso | `Usuario`, `Role`, `RoleUsuario` | — |
| Produtos/Estoque | `Categoria`, `Produto`, `MovimentacaoProduto` | `UnidadeMedida`, `TipoMovimentacaoProduto` |
| Vendas | `Pedido`, `ItemPedido` | `StatusPedido`, `TipoPedido`, `FormaPagamento` |
| Suporte | `Chamado`, `Mensagem` | `TipoChamado`, `PrioridadeChamado`, `StatusChamado` |

- IDs: `Long` com `@SequenceGenerator` `seq_<tabela>` (`allocationSize = 1`).
- Tabelas em snake_case (`item_pedido`, `cliente_funcionario`, `movimentacao_produto`, `role_usuario`).
- Relacionamentos majoritariamente `@ManyToOne(fetch = LAZY)`; exceções EAGER: `Produto.categoria`, `Usuario.empresa`, `Usuario.acessos` (`@ManyToMany` via `role_usuario`).

## 5. Banco de dados

- Arquivos: `src/main/resources/application.properties`, `application-dev.properties`, `application-prod.properties`.
- `application.properties` só define o perfil: `spring.profiles.active=${SPRING_PROFILES_JDEV_ERP_PRO:dev}` (variável de ambiente própria, documentada em `documentação/1 - Documentação`).
- Perfis: **dev** e **prod**. **Não existe perfil `test`**.
- PostgreSQL, pool Hikari (`auto-commit=false`, máx. 15), `open-in-view=true`.
- `ddl-auto`: **`update` em dev e em prod** (a chave aparece duas vezes em cada arquivo, linhas 16 e 88).
- **Sem ferramenta de migration** (sem Flyway/Liquibase). O schema é gerado pelo Hibernate.
- Script manual existente (descoberto na etapa 7): `src/main/webapp/db_sql_versionamento/V1_INIT.sql`. É uma carga inicial que:
  - **apaga** `plano`, `empresa` e `pessoa`;
  - recria `plano_valor_mensal_check` (FREE = 0; os demais entre 49 e 200);
  - cria a extensão `unaccent`;
  - insere as roles `ROLE_USER` e `ROLE_ADMIN`, pessoas, empresa, usuários e categorias;
  - ajusta colunas (`usuario.liberado`).
  Padrão de nome: `V<n>_<DESCRICAO>.sql`. Última versão: V1.
- Servidor: porta `8081`, context-path `/jdev-erp-pro-back-end-dev` (igual nos dois perfis).
- Credenciais, `jwt.secret` e URL do banco estão **literais** nos arquivos de perfil (valores não copiados aqui).

## 6. Segurança

- **JWT stateless** (`SessionCreationPolicy.STATELESS`, CSRF desabilitado).
- `SecurityFilterChain` em `security/SecurityConfig.java`; filtro `security/JwtFilter` (`addFilterBefore UsernamePasswordAuthenticationFilter`); erros 401/403 em `JwtAuthenticationEntryPoint` / `JwtAccessDeniedHandler`.
- Geração/validação do token em `service/JwtService`; o token também é conferido no banco (`UsuarioDetailsService.existsByTokenParaUser`, campo `Usuario.tokenSessao`).
- Principal: `security/UsuarioAutenticado` (envolve `Usuario`); dados do logado via `service/UsuarioLogadoService` (`getEmpresaIdLogada()`, `isAdmin()`).
- `PasswordEncoder`: `BCryptPasswordEncoder`.
- Regras de URL:
  - público: `POST /api/usuario/login`
  - `/api/admin/**` → `hasAuthority("ROLE_ADMIN")` (nenhum controller usa esse prefixo hoje)
  - demais → `authenticated()`
- Roles encontradas no código: **`ROLE_ADMIN`**, **`ROLE_USER`** (atribuída no cadastro de usuário, `UsuarioService`). Comentário em `Usuario` cita `ROLE_GERENTE`, mas o seed `V1_INIT.sql` só cria `ROLE_USER` e `ROLE_ADMIN` (resolvido na etapa 7).
- `@EnableMethodSecurity(prePostEnabled, jsr250, secured)` ativo, mas **nenhum `@PreAuthorize`/`@Secured`/`@RolesAllowed` em uso**.
- CORS: `allowCredentials(true)` + `addAllowedOriginPattern("*")`, todos os headers e métodos.
- **Multi-tenant**: `validator/RepositoryEmpresaValidator` roda na inicialização e **falha o boot** se um método de repository não tiver `@Query` contendo `empresa.id`/`empresa_id`, a menos que tenha `@IgnoreEmpresaId` (no método ou na interface).

## 7. Padrões de código

- Nomes em **português** (`salvar`, `atualizar`, `buscarPorId`, `existePorNome`, `listarPaginado`).
- Injeção: **`@Autowired` em campo** (exceção: `RepositoryEmpresaValidator` usa construtor).
- Rotas: `/api/<entidadeCamelCase>` com verbos no path: `POST /salvar`, `POST /atualizar`, `DELETE /deletar/{id}`, `GET /listar`, `/buscarPorId/{id}`, `/buscaPorNome/{nome}`, `/existePorNome/{nome}`, `/existePorNomeDiferenteId/{id}/{nome}`, `/existePorId/{id}`, `POST /buscarPorIds`, `/total`, `/paginado`, `DELETE /deletarTodos`, `/deletarTodosPorIds`. `UsuarioController` mistura kebab-case (`/buscar-por-nome`, `/deletar-todos`).
- **Entidade vs DTO**: a maioria dos controllers recebe e devolve **entidades** (`@RequestBody @Valid Categoria`). DTOs só em `Usuario` (`UsuarioDto`, `LoginDTO`, `TokenDTO`, `AlterarSenhaDTO`), `Empresa` (retorna `EmpresaDTO`, recebe entidade) e `ClienteFuncionario` (retorna `ClienteFuncionarioDTO`).
- Controller sempre passa `usuarioLogadoService.getEmpresaIdLogada()` para o service.
- Repository base genérico: `JpaJdevRepository<T, ID>` / `JpaJdevRepositoryImpl` com métodos filtrados por empresa (`listarPaginado`, `total`, `buscarPorId`, `listar`, `existsById`, `buscarPorIds`, `deletarAllById`, `deleteAll(empresaId)`).
- `@Transactional` (`jakarta.transaction.Transactional`) aparece **só nos repositories** (métodos de escrita); **services não usam `@Transactional`**.
- Exceções: `exception/GlobalExceptionHandler` (`@RestControllerAdvice`); exceção de negócio `MsgApiException(message[, HttpStatus])` (padrão 400).
- Formato do erro (`ResponseApi`):
  ```json
  { "localDateTime": "...", "status": 400, "error": "...", "message": "...", "path": "/jdev-erp-pro-back-end-dev/api/..." }
  ```
- Paginação: `GET /paginado?page=0&size=10&sort=id&direction=asc`, o controller monta o `PageRequest` e devolve `Page<Entidade>`.

## 8. Testes

- JUnit 5 + Spring Boot Test (Mockito disponível pelo starter, não usado).
- Base: `src/test/java/br/com/jdeverp/pro/contexto/TestContextoSpring` (`@SpringBootTest`, `@AutoConfigureMockMvc(addFilters = false)`, `@TestPropertySource("classpath:application.properties")`, `@Transactional` → rollback ao final).
- Único teste de fato: `repository/TestCategoriaRepository` (integração com repository, estende `TestContextoSpring`).
- Não há `@WebMvcTest`, `@DataJpaTest` nem testes unitários puros.
- Os testes usam o **banco real do perfil ativo** (dev por padrão); não há H2/Testcontainers.
- Rodar um teste só: `mvn test -Dtest=TestCategoriaRepository` ou `mvn test -Dtest=TestCategoriaRepository#nomeDoMetodo`. O Surefire 3.5.5 acha as classes `Test*` (confirmado na etapa 8: `mvn test` rodou 9 testes em cerca de 24 s).
- Estado na etapa 8: 8 passam e 1 falha (`TestCategoriaRepository.testfindAll:69`). O teste supõe que `findAll(empresaId).get(0)` é a categoria recém-criada, mas não há ordenação e o banco de dev já tem categorias, inclusive as do `V1_INIT.sql`. É um teste frágil e dependente de dados, não uma falha de configuração.

## 9. Comandos

| Ação | Comando |
|---|---|
| Build | `mvn clean package` (`-DskipTests` para pular os testes, que exigem o banco) |
| Testes | `mvn test` |
| Rodar local | `mvn spring-boot:run` → `http://localhost:8081/jdev-erp-pro-back-end-dev` |
| Escolher perfil | variável `SPRING_PROFILES_JDEV_ERP_PRO=dev|prod` (padrão `dev`) |
| Banco local | **sem docker-compose**; PostgreSQL instalado localmente `[CONFIRMAR]` host/porta/nome do banco usados no dev |

## 10. Pontos de atenção (só registrados, não corrigidos)

1. **Segredos versionados**: senha do banco, usuário/senha do `spring.security.user` e `jwt.secret` estão em texto puro em `application-dev.properties` e `application-prod.properties` (commitados no Git).
2. **dev e prod são iguais**: os dois arquivos só diferem em `spring.application.name`, ou seja, mesmo banco, mesmo segredo JWT e mesmo context-path (`...-dev`). `[CONFIRMAR]` se é intencional nesta fase do curso.
3. `ddl-auto=update` em **prod**, sem ferramenta de migration.
4. **Entidades expostas direto na API** na maioria dos controllers (entrada e saída), o que permite mass assignment. Mitigado para `empresa`: os services das entidades por empresa fazem `setEmpresa(usuarioLogadoService.getEmpresaLogada())` em salvar/atualizar (confirmado na etapa 2). `Empresa`, `Plano`, `Role` e `RoleUsuario` não são por empresa e não fazem isso. Os demais campos continuam sujeitos a mass assignment.
5. **Sem autorização por perfil**: `Plano`, `Empresa`, `Role`, `RoleUsuario` e `Usuario` (`/deletar-todos`, etc.) ficam acessíveis a qualquer usuário autenticado; `/api/admin/**` não é usado; nenhum `@PreAuthorize`.
6. CORS com `allowCredentials(true)` + origem `*`.
7. `JDevERPPROApplication.configTimeZone()` imprime no console o hash de uma senha padrão escrita no código.
8. `ConfiguracaoProducao` está anotada com `@Profile("dev")` (nome e perfil divergentes).
9. `EmpresaContext` está em `br.jdev.estoque.erp.multitanent`, **fora** do `@ComponentScan`, então não vira bean (código morto ou bug).
10. `Usuario` tem `@UniqueConstraint` na coluna **`senha`** (`unique_senha`).
11. `GlobalExceptionHandler` trata `ObjectOptimisticLockingFailureException`, mas nenhuma entidade tem `@Version`.
12. `ConstraintViolationException` devolve **500** (deveria ser 400); o handler de `Exception` devolve a mensagem interna ao cliente.
13. `JwtFilter` usa `e.printStackTrace()` e lança `MsgApiException` dentro do filtro, fora do alcance do `@RestControllerAdvice` `[CONFIRMAR]` o formato da resposta nesse caso.
14. Services sem `@Transactional`; `@Transactional` do **jakarta** (não Spring) nos repositories.
15. `spring.jpa.database-platform=PostgreSQL9Dialect` e `hibernate.dialect=PostgreSQLDialect` coexistem; propriedades `redisson`/`jcache`/`devtools`/`spring.session.jdbc` sem a dependência correspondente.
16. `jackson-datatype-jsr310` 2.13.5 fixado manualmente, enquanto o Boot 4 usa Jackson 3 `[CONFIRMAR]` compatibilidade.
17. Rotas inconsistentes (camelCase vs kebab-case; `buscaPorNome` vs `buscarPorId`); `POST` para atualizar em vez de `PUT`.
18. Testes dependem do banco de desenvolvimento real.
19. Arquivos de log (`jdev-erp-pro-back-end-dev.log`, `.gz`) versionados no Git.
20. **Injeção de JPQL na paginação** (encontrado na etapa 4): `JpaJdevRepositoryImpl.listarPaginado()` concatena `order.getProperty()` no JPQL, e o valor vem direto do `@RequestParam sort` dos controllers `/paginado`.
21. Valores monetários em `Double` em `Produto.preco` e `Plano.valorMensal` (o resto usa `BigDecimal`).

## 11. Dúvidas em aberto (registradas na etapa 2, aguardando decisão)

| # | Dúvida | Origem |
|---|---|---|
| ~~D1~~ | **Resolvida (etapa 3):** `docs/arquitetura.md` e `docs/regras-negocio.md` foram criados. | Etapa 2 |
| D2 | Adotar `@PreAuthorize` como padrão para endpoints novos? Hoje nenhum endpoint usa, embora `@EnableMethodSecurity` esteja ativo. | Etapa 2 |
| ~~D3~~ | **Resolvida (etapa 7):** `ROLE_GERENTE` não está no seed `V1_INIT.sql`; só `ROLE_USER` e `ROLE_ADMIN`. | Seção 6 |
| D10 | Novos scripts SQL devem seguir `src/main/webapp/db_sql_versionamento/V<n>_<DESCRICAO>.sql` (padrão existente) em vez de `docs/sql/` (usado na skill `/nova-migration` e no agente `especialista-banco`)? | Etapa 7 |
| D4 | Host, porta e nome do banco PostgreSQL local usados no perfil dev. | Seção 9 |
| D5 | dev e prod com configuração idêntica (mesmo banco, segredo JWT e context-path) é intencional nesta fase do curso? | Seção 10, item 2 |
| D6 | Qual resposta chega ao cliente quando `JwtFilter` lança `MsgApiException` (fora do `@RestControllerAdvice`)? | Seção 10, item 13 |
| D7 | `jackson-datatype-jsr310` 2.13.5 é compatível/necessário com o Spring Boot 4 (Jackson 3)? | Seção 10, item 16 |
| D8 | Raiz do Claude Code: o `CLAUDE.md` está em `jdev-erp-pro-back-end-dev/`; abrir a sessão em `JDevERP_PRO/` não o carrega. Manter assim? | Etapa 2 |
| ~~D9~~ | **Resolvida (etapa 8):** o Surefire executa as classes `Test*`. | Seção 8 |
