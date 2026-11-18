---
paths:
  - "src/main/java/br/com/jdeverp/pro/security/**/*.java"
  - "src/main/java/br/com/jdeverp/pro/controller/**/*.java"
  - "src/main/java/br/com/jdeverp/pro/service/UsuarioService.java"
  - "src/main/java/br/com/jdeverp/pro/service/UsuarioDetailsService.java"
  - "src/main/java/br/com/jdeverp/pro/service/UsuarioLogadoService.java"
  - "src/main/java/br/com/jdeverp/pro/service/JwtService.java"
  - "src/main/java/br/com/jdeverp/pro/service/Role*Service.java"
  - "src/main/resources/application*.properties"
---
# Segurança

- Use só as roles listadas no `CLAUDE.md` (entidade `Role.acesso`, sempre com prefixo `ROLE_`). Não invente roles; se precisar de uma nova, pergunte.
- Autorização de endpoint novo, sempre explícita:
  - preferencial: `@PreAuthorize("hasAuthority('ROLE_ADMIN')")` ou `@PreAuthorize("isAuthenticated()")` no método do controller;
  - padrão atual em `EmpresaService`/`UsuarioService`: `if (!usuarioLogadoService.isAdmin()) throw new MsgApiException(msg, HttpStatus.FORBIDDEN);`.
- Operações administrativas (planos, roles, associação de roles, empresas, cadastro e exclusão de usuários) exigem `ROLE_ADMIN`.
- Nunca adicione `permitAll()` no `SecurityConfig` sem pedido explícito.
- Senhas: só via bean `PasswordEncoder` de `SecurityConfig.passwordEncoder()` (BCrypt), com `encode` e `matches`. Nunca compare senha em texto.
- Para não expor campos sensíveis, use DTO ou `@JsonIgnore` (já presentes em `Usuario`).
- Nunca logar ou imprimir token, senha, `jwt.secret` ou dado pessoal (CPF, e-mail). Não use `System.out`/`printStackTrace` com esses dados.
- Usuário e empresa logados vêm de `UsuarioLogadoService`; nunca confie em `empresaId` ou `usuarioId` vindos do JSON.
- O JWT carrega `usuarioId`, `empresaId` e `login` (`JwtService.gerarToken`). Novas claims não podem conter dado sensível.
- Login e token: `UsuarioService.login()` grava `tokenSessao`; `JwtFilter` exige token = `tokenSessao`. Preserve esse fluxo.
- Segredo novo no `.properties`: `${NOME_VARIAVEL}`, sem valor literal.
