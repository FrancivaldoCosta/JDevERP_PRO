---
paths:
  - "src/main/java/br/com/jdeverp/pro/controller/**/*.java"
  - "src/main/java/br/com/jdeverp/pro/dto/**/*.java"
  - "src/main/java/br/com/jdeverp/pro/exception/**/*.java"
---
# Controllers, DTOs e erros

- Controller só recebe, valida (`@RequestBody @Valid`) e delega ao service; regra de negócio fica no service.
- Modelo de referência: `CategoriaController` (CRUD por empresa) e `UsuarioController` (com DTO).
- Rotas: `@RequestMapping("/api/<entidadeCamelCase>")` no singular, verbo no path, camelCase (`/buscarPorId/{id}`). Não use kebab-case (o `UsuarioController` é exceção legada).
- Verbos HTTP do projeto: `POST` para salvar e atualizar, `GET` para consultas, `DELETE` para exclusões, `PATCH` só para atualização parcial de um campo (ex.: `/atualizarLeitura`).
- Retorno sempre `ResponseEntity<...>`; o projeto devolve `ResponseEntity.ok(...)` (200) inclusive em criação e exclusão. Exclusão devolve `String` com mensagem ("X deletado com sucesso.").
- Entrada/saída: siga o módulo. CRUD simples usa a entidade; quando houver dado sensível, campos calculados ou relação a achatar, use DTO em `dto` (sufixo `DTO`).
- Mapeamento entidade↔DTO: manual, no service (ver `UsuarioService`, `EmpresaService`). Não há MapStruct nem classe mapper.
- DTOs: classe simples com Lombok `@Getter @Setter` (ou `@Data`); relações viram `xxxId` (`Long`) e nome descritivo (`pessoaNome`/`nomePessoa`); enums como `String` (`.name()`).
- Paginação: `GET /paginado` com `@RequestParam(defaultValue)` `page=0`, `size=10`, `sort=id`, `direction=asc`; monte `PageRequest.of(page, size, Sort.by(dir, sort))` e devolva `Page<T>`. Valide `sort` contra as propriedades da entidade.
- Erros: lance `MsgApiException` no service. Não monte resposta de erro no controller; o `GlobalExceptionHandler` converte para `ResponseApi` `{localDateTime, status, error, message, path}`.
- "Não encontrado" hoje sai como 400 (`MsgApiException` padrão). Para 404, use `new MsgApiException(msg, HttpStatus.NOT_FOUND)`.
- Novos tipos de exceção: adicione um `@ExceptionHandler` em `GlobalExceptionHandler` seguindo o mesmo formato e chamando `logException`.
