---
name: novo-crud
description: Cria um CRUD completo no padrão do JDevERP Pro (entidade, repository, DTO se necessário, service, controller com autorização e testes). Use quando pedirem um novo cadastro/CRUD.
argument-hint: Entidade e campos (ex.: Fornecedor razaoSocial, cnpj, email, ativo)
---
Crie um CRUD para: $ARGUMENTS

1. Referência: `Categoria` para cadastro simples por empresa; `Produto` se houver relação com outra entidade. Leia só os 4 arquivos dela (`model`, `repository`, `service`, `controller`) e diga qual escolheu.
2. Apresente um plano curto e **espere meu OK**:
   - arquivos a criar;
   - campos com tipo Java e validação (`BigDecimal` para valores monetários);
   - tabela, sequence, FKs e uniques (com `empresa_id`);
   - rotas e role de cada uma (`ROLE_ADMIN` ou autenticado).
3. Crie copiando a estrutura da referência, nesta ordem:
   1. entidade;
   2. repository;
   3. DTO, só se houver dado sensível ou relação a achatar;
   4. service;
   5. controller com autorização explícita.
4. Rode `mvn -q compile` e corrija até compilar. A subida da aplicação valida o filtro por empresa das `@Query`.
5. Delegue os testes ao subagente `testador`: service (unitário com Mockito) e controller (403 para não admin, se a rota exigir).
6. Chame o subagente `revisor-codigo` e corrija os achados críticos.
7. Schema: o `ddl-auto=update` cria a tabela. Índices ou algo que o update não faz: sugira `/nova-migration`.
8. Termine com a lista de arquivos e uma tabela de endpoints (método, rota, role).
