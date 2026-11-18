# Jdev ERP Pro (back-end)
ERP SaaS multiempresa: planos/empresas, pessoas, clientes/funcionários, usuários/roles, categorias, produtos, estoque, pedidos, chamados.
Java 25, Spring Boot 4.0.6, Spring Data JPA/Hibernate, Spring Security + JWT (jjwt 0.12.7), PostgreSQL, Lombok, Maven 3.9 (sem wrapper).

## Comandos
- Checagem rápida (use primeiro): `mvn -q compile`
- Um teste: `mvn -q test -Dtest=NomeDaClasse` (ou `#metodo`). Todos: `mvn test` (~25 s, precisa do banco dev)
- Build: `mvn clean package -DskipTests`. Rodar: `mvn spring-boot:run` → `http://localhost:8081/jdev-erp-pro-back-end-dev`
- Perfil: variável `SPRING_PROFILES_JDEV_ERP_PRO` (`dev`|`prod`, padrão `dev`). PostgreSQL local, sem docker-compose.

## Estrutura (pacote base `br.com.jdeverp.pro`, organizado por camada)
- `controller` → `service` → `repository` (estende `JpaJdevRepository`) → `model` (entidades, Lombok `@Data`)
- `dto`, `enums`, `exception` (`GlobalExceptionHandler`, `MsgApiException`), `security` (`SecurityConfig`, `JwtFilter`)
- `validator/RepositoryEmpresaValidator`, `anotacoes/@IgnoreEmpresaId`, `config`, `util`, `app/JDevERPPROApplication`
- Config: `src/main/resources/application{,-dev,-prod}.properties`. Testes: `src/test/java/br/com/jdeverp/pro/` (base `contexto/TestContextoSpring`)
- Schema: sem Flyway/Liquibase, `ddl-auto=update`. Script manual `src/main/webapp/db_sql_versionamento/V1_INIT.sql` **apaga dados**: nunca rodar.

## Convenções
- Tudo em português (classes, métodos, rotas, mensagens): `salvar`, `atualizar`, `buscarPorId`, `existePorNome`, `listarPaginado`.
- Erro de regra: `throw new MsgApiException("msg")` (400) ou `new MsgApiException(msg, HttpStatus.X)`.
- Injeção por `@Autowired` em campo. Bean Validation com mensagem em português + `@Valid`.
- Para criar algo novo, copie o padrão de `Categoria` (Controller/Service/Repository/model).
- Detalhes de API, JPA e segurança: `.claude/rules/` (carregam sozinhos pelo caminho do arquivo).

### Multiempresa (obrigatório)
- Toda entidade de negócio tem `empresa` (`empresa_id`), exceto `Empresa`, `Plano`, `Role`, `RoleUsuario`.
- Controller passa `usuarioLogadoService.getEmpresaIdLogada()`; service faz `setEmpresa(usuarioLogadoService.getEmpresaLogada())` (nunca confia no JSON).
- Todo método declarado em repository precisa de `@Query` com `empresa.id`/`empresa_id`, senão a app NÃO sobe. Exceção: `@IgnoreEmpresaId(motivo = "...")`.
- Prefira os métodos genéricos de `JpaJdevRepository` (`listar`, `buscarPorId`, `listarPaginado`, `total`, `existsById`...).

### Segurança
- JWT stateless; `JwtFilter` exige token = `Usuario.tokenSessao`. Público só `POST /api/usuario/login`.
- Roles: `ROLE_ADMIN`, `ROLE_USER` (dada a todo usuário novo).

### Testes
- Integração estende `TestContextoSpring` (`@SpringBootTest`, rollback) e usa o banco dev real: não dependa de dados existentes nem de ordem.
- Falha conhecida, não é regressão: `TestCategoriaRepository#testfindAll` (depende da ordem dos dados).
- `mvn test` reescreve `jdev-erp-pro-back-end-dev.log*` (versionados): não commitar e não ler esses logs inteiros.

## Fluxo padrão
Implementar → `mvn -q compile` → subagente `testador` → subagente `revisor-codigo` → `/revisar-commit`.
Mexeu em login/roles/segurança/endpoint novo → subagente `auditor-seguranca`. Entidade/SQL → `especialista-banco`.

## Regras que nunca podem ser quebradas
- Mudança de entidade que renomeie/remova coluna ou mude tipo: avisar antes e entregar script SQL (o `update` não faz isso).
- Nunca usar `ddl-auto=create`/`create-drop` nem mudar `ddl-auto` sem eu pedir.
- Nunca criar consulta sem filtro por empresa nem usar `@IgnoreEmpresaId` sem `motivo`.
- Endpoint novo com autorização explícita (`@PreAuthorize`, `usuarioLogadoService.isAdmin()` ou regra no `SecurityConfig`).
- Nunca adicionar credenciais ao código/`.properties` (use `${VARIAVEL}`); nunca expor `senha`, `tokenSessao`, `refreshToken`.
- Rodar os testes afetados antes de dizer que terminou (avisar se o banco estiver indisponível).
- Não fazer git commit nem git push sem eu pedir.

## Mais contexto (ler só quando a tarefa pedir)
- `docs/regras-negocio.md` — regras por módulo (consulte antes de alterar um service)
- `docs/arquitetura.md` — módulos, relações, fluxo de requisição
- `docs/analise-projeto.md` — análise técnica, pontos de atenção e dúvidas em aberto
