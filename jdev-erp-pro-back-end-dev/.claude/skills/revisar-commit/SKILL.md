---
name: revisar-commit
description: Checklist antes do commit no JDevERP Pro — testes, revisão de código, auditoria de segurança quando aplicável e proposta de mensagem em Conventional Commits.
---
Faça o checklist de pré-commit das mudanças atuais.

1. Veja o que mudou: `git status` e `git diff --stat` (inclua `--staged`). Se não houver mudanças, avise e pare.
2. Rode todos os testes: `mvn test`. Se falhar, pare e mostre o erro resumido (teste, mensagem, `arquivo:linha`). Se falhar por banco indisponível, diga isso claramente.
3. Chame o subagente `revisor-codigo` nas mudanças atuais.
4. Se mudou algum arquivo em `controller/`, `dto/`, `security/`, `service/Usuario*`, `service/Role*`, `service/JwtService.java` ou `src/main/resources/application*.properties`, chame também o subagente `auditor-seguranca`, focado nesses arquivos.
5. Resuma só os achados **críticos** e de risco **alto**, com `arquivo:linha`. Se não houver, diga isso em uma linha.
6. Avise se algum arquivo sensível estiver para entrar no commit (`*.log`, `*.gz`, `.claude/settings.local.json`, `CLAUDE.local.md`, novos segredos em `.properties`).
7. Proponha uma mensagem de commit em Conventional Commits (`feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `chore:`), com assunto em português e no máximo 72 caracteres, mais um corpo curto opcional.
8. **Não faça o commit nem o push** sem eu pedir.
