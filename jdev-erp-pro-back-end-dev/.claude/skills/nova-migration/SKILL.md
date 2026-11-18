---
name: nova-migration
description: Cria um script SQL versionado de mudança de schema PostgreSQL (o projeto não usa Flyway/Liquibase), coerente com as entidades JPA do JDevERP Pro.
argument-hint: O que a mudança de schema deve fazer
---
Mudança de schema pedida: $ARGUMENTS

Contexto: não há ferramenta de migration. O Hibernate aplica `ddl-auto=update`, que só adiciona tabelas e colunas. Scripts manuais ficam em `docs/sql/`.

1. Liste `docs/sql/` (crie a pasta se não existir) e defina o nome do próximo script: `AAAA-MM-DD_NN_descricao.sql` (data de hoje, `NN` sequencial no dia, descrição em snake_case).
2. Leia as entidades afetadas em `src/main/java/br/com/jdeverp/pro/model/` e `.claude/rules/jpa-entidades.md`.
3. Diga se a mudança já é coberta pelo `ddl-auto=update` (só adicionar coluna ou tabela). Se for, avise que o script é opcional e pergunte se devo criá-lo para registro.
4. Se for destrutiva (DROP, RENAME, mudança de tipo, NOT NULL em tabela com dados, remoção de constraint), **pare**, explique o risco e o impacto nos dados e espere meu aval.
5. Crie o script com:
   - cabeçalho comentado (objetivo, data, entidades);
   - nomes em snake_case, FKs `*_fk`, uniques `unique_*`;
   - índice em toda FK nova;
   - `IF EXISTS`/`IF NOT EXISTS` quando possível.
6. Ajuste a entidade para ficar coerente com o script (nomes de coluna, constraints, nullable).
7. Nunca edite um script já existente em `docs/sql/`; crie outro.
8. Rode os testes que usam essas entidades (`mvn test -Dtest=...`). Não execute o SQL no banco; me entregue o comando para rodar.
9. Para modelagem mais complexa, delegue ao subagente `especialista-banco`.
