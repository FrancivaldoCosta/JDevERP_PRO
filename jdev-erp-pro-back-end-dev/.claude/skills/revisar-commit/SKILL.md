---
name: revisar-commit
description: Checklist antes do commit no JDevERP Pro — testes, revisão de código, auditoria de segurança quando aplicável e proposta de mensagem em Conventional Commits.
disable-model-invocation: true
---
Faça o checklist de pré-commit das mudanças atuais.

1. `git status --short` e `git diff --stat HEAD`. Se não houver mudanças, avise e pare. Se só mudou documentação ou `.claude/`, pule os passos 2 a 4.
2. Ao mesmo tempo (em paralelo):
   - rode `mvn -q test`;
   - chame o subagente `revisor-codigo`;
   - se mudou `controller/`, `dto/`, `security/`, `service/Usuario*`, `service/Role*`, `service/JwtService.java` ou `application*.properties`, chame também o `auditor-seguranca`, focado nesses arquivos.
3. Testes falharam: mostre o resumo (teste, mensagem, `arquivo:linha`) a partir de `target/surefire-reports/*.txt`. `TestCategoriaRepository#testfindAll` é falha conhecida; qualquer outra bloqueia. Banco indisponível: diga isso claramente.
4. Resuma só os achados **críticos** e de risco **alto**, com `arquivo:linha`. Se não houver, diga isso em uma linha.
5. Avise sobre arquivos que não devem entrar no commit: `*.log`, `*.gz` (o `mvn test` os altera), `.claude/settings.local.json`, `CLAUDE.local.md`, segredos novos em `.properties`. Sugira o `git add` só dos arquivos certos.
6. Proponha uma mensagem em Conventional Commits (`feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `chore:`), com assunto em português de até 72 caracteres e corpo curto opcional.
7. **Não faça o commit nem o push** sem eu pedir.
