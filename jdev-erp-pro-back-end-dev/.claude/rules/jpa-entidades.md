---
paths:
  - "src/main/java/br/com/jdeverp/pro/model/**/*.java"
  - "src/main/java/br/com/jdeverp/pro/repository/**/*.java"
  - "src/main/java/br/com/jdeverp/pro/enums/**/*.java"
---
# Entidades e repositories

- Relações `@ManyToOne`/`@OneToOne` sempre `fetch = FetchType.LAZY`. Não criar novas EAGER (já existem em `Produto.categoria`, `Usuario.empresa`, `Usuario.acessos`).
- Lado "muitos" apenas; o projeto não mapeia `@OneToMany`. Só adicione se a tarefa pedir.
- FK com nome: `@JoinColumn(name = "x_id", nullable = ..., foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "x_fk"))`.
- IDs `Long` com `@SequenceGenerator(name = "seq_<tabela>", sequenceName = "seq_<tabela>", allocationSize = 1)`; tabelas e colunas em snake_case.
- Unicidade em `@Table(uniqueConstraints = @UniqueConstraint(name = "unique_...", columnNames = {...}))`; inclua `empresa_id` quando a unicidade for por empresa.
- Lombok: o projeto usa `@Data` em todas as entidades. Mantenha o padrão e não inclua coleções em `toString`/`equals` de entidades novas (`@ToString.Exclude`, `@EqualsAndHashCode.Exclude`).
- Valores monetários novos em `BigDecimal` com default `BigDecimal.ZERO`; quantidades seguem `Double`.
- Enums com `@Enumerated(EnumType.STRING)`, construtor com `descricao` e `getDescricao()`.
- Textos longos: `@Column(columnDefinition = "text")`.
- Datas de negócio em `LocalDate`, preenchidas no service (`LocalDate.now()`). Não há auditoria (`createdAt`/`updatedAt`) no projeto.
- Mensagens de Bean Validation em português, no atributo `message`.
- Toda mudança de entidade altera o schema via `ddl-auto=update`: verifique se é só adição (seguro) ou se exige script SQL (ver `CLAUDE.md`).

## Repositories
- JPQL em `@Query` com parâmetros `@Param` nomeados (`:idEmpresa`); nunca concatene entrada do usuário no JPQL.
- Existência: `select count(x.id) > 0 from X x where ...` retornando `boolean`.
- Busca por nome: `unaccent(upper(trim(x.nome))) like unaccent(upper(concat('%', trim(:nome), '%')))` (função registrada em `PostgreSQLFunctionContributor`).
- Escrita: `@Transactional` + `@Modifying(flushAutomatically = true, clearAutomatically = true)`.
- Listagens: use `listarPaginado(empresaId, pageable)` do `JpaJdevRepository`. O campo de `sort` precisa vir de uma lista fixa de propriedades válidas, porque `JpaJdevRepositoryImpl` o concatena no JPQL.
- Para evitar N+1 em listagens que exibem relações, use `join fetch` na `@Query` (o projeto ainda não usa; prefira isso a trocar para EAGER).
