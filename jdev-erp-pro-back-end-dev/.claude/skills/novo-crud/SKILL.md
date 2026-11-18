---
name: novo-crud
description: Cria um CRUD completo no padrão do JDevERP Pro (entidade, repository, DTOs se necessário, service, controller com autorização, testes e script SQL quando preciso).
argument-hint: Entidade e campos (ex.: Fornecedor razaoSocial, cnpj, email, ativo)
---
Crie um CRUD para: $ARGUMENTS

1. Escolha o CRUD existente mais parecido como referência (padrão: `Categoria` para cadastro simples por empresa; `Produto` se houver relação com outra entidade). Diga qual escolheu e por quê.
2. Apresente um plano curto e **espere meu OK**:
   - arquivos em `src/main/java/br/com/jdeverp/pro/{model,repository,service,controller,dto}`;
   - campos com tipos Java (`BigDecimal` para valores monetários) e Bean Validation;
   - tabela e sequence (`seq_<tabela>`), FKs e unique constraints (com `empresa_id`);
   - rotas em `/api/<entidadeCamelCase>` e role de cada uma (`ROLE_ADMIN` ou autenticado).
3. Crie nesta ordem:
   1. entidade em `model`, com `empresa` LAZY;
   2. repository estendendo `JpaJdevRepository`, com toda `@Query` filtrando `empresa.id`;
   3. DTO só se houver dado sensível ou relação a achatar;
   4. service com `setEmpresa(usuarioLogadoService.getEmpresaLogada())` e `MsgApiException` nas regras;
   5. controller no padrão `CategoriaController`, com autorização explícita;
   6. testes.
4. Schema: não há migrations; o `ddl-auto=update` cria a tabela. Se precisar de índice ou de algo que o update não faz, use `/nova-migration`.
5. Rode só os testes novos: `mvn test -Dtest=NomeDaClasse`. Corrija até passarem; se o banco estiver indisponível, avise.
6. Chame o subagente `revisor-codigo` nas mudanças e corrija os achados críticos.
7. Termine com a lista de arquivos criados e uma tabela de endpoints (método, rota, role).
