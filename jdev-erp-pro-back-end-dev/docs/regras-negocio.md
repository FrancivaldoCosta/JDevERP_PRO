# Regras de negócio — JDevERP Pro (back-end)

> Levantadas do código na etapa 3 (services, entidades, validações). Só o que está implementado.
> Formato: regra — `Classe.metodo()` (ou `Entidade.campo` para Bean Validation).
> `[CONFIRMAR]` = a intenção não está clara no código.

## 0. Regras transversais

- R0.1 Todo registro de negócio pertence à empresa do usuário logado; a empresa vinda no JSON é ignorada — `*Service.salvar()/atualizar()` (`setEmpresa(usuarioLogadoService.getEmpresaLogada())`).
- R0.2 Toda consulta de repository filtra por empresa, ou a aplicação não sobe — `RepositoryEmpresaValidator.afterSingletonsInstantiated()`.
- R0.3 Atualizar exige `id`, senão erro "Id ... não informado para atualizar" — `*Service.atualizar()`.
- R0.4 Atualizar/excluir só encontra registros da empresa logada — `*Service.atualizar()/deleteById()` via `buscarPorId(id, empresaId)`/`existsById(id, empresaId)`.
- R0.5 Excluir em lote (`deletarTodosPorIds`) falha se a lista vier vazia ou se algum id não existir, e informa os ids não encontrados — `*Service.deletarAllById()`.
- R0.6 Excluir todos (`deletarTodos`) falha se não houver registros — `*Service.deleteAll()`.
- R0.7 Conflito de versão gera a mensagem "registro alterado por outro usuário" — `GlobalExceptionHandler.lockVersion()` (nenhuma entidade tem `@Version`; ver Pontos de atenção).

## 1. SaaS / Assinatura

### Plano
- R1.1 Nome do plano é único (global) — `PlanoService.salvar()/atualizar()`.
- R1.2 Nome e descrição obrigatórios — `Plano.nome`, `Plano.descricao` (`@NotBlank`).
- R1.3 Valor mensal entre R$ 49 e R$ 200 — `Plano.valorMensal` (`@Min(49) @Max(200)`).
- R1.4 Limite de usuários entre 1 e 150 — `Plano.limiteUsuario`.
- R1.5 Limite de clientes entre 1 e 150 — `Plano.limiteCliente`.
- R1.6 Tipo do plano obrigatório (`FREE`, `STARTER`, `BASIC`, `PRO`, `BUSINESS`, `ENTERPRISE`) — `Plano.tipoPlano`.

### Empresa
- R1.7 Só administrador (`ROLE_ADMIN`) cadastra, atualiza ou exclui empresa (403) — `EmpresaService.salvar()/atualizar()/deleteById()`.
- R1.8 Uma pessoa só pode ser vinculada a uma empresa — `EmpresaService.salvar()` + `unique_pessoa_empresa`.
- R1.9 Pessoa e plano obrigatórios; o plano precisa existir — `EmpresaService.salvar()/atualizar()/validarPlano()`.
- R1.10 Logomarca obrigatória — `Empresa.logoMarca` (`@NotNull @NotEmpty`).
- R1.11 Empresa nasce com `totalUsuario=0`, `totalCliente=0`, `planoAtivo=false`, `bloqueio=false` — defaults em `Empresa`.
- R1.12 Empresa bloqueada impede login, uso do token e troca de senha (401) — `UsuarioService.login()`, `UsuarioDetailsService.loadUserByUsername()`, `UsuarioService.alterarSenha()`.
- R1.13 Os limites do plano (`limiteUsuario`, `limiteCliente`), `vigenciaPlano` e `planoAtivo` são só gravados; **não há regra que os aplique** `[CONFIRMAR]` se é funcionalidade futura.

## 2. Cadastros

### Pessoa
- R2.1 Nome único por empresa — `PessoaService.salvar()/atualizar()`.
- R2.2 Obrigatórios: nome, telefone, tipo (`FISICA`/`JURIDICA`), CEP, logradouro, bairro, estado, cidade, país — `Pessoa` (`@NotBlank/@NotNull`).
- R2.3 CPF válido quando informado — `Pessoa.cpf` (`@CPF`).
- R2.4 CNPJ válido quando informado, aceitando o formato alfanumérico — `Pessoa.cnpj` (`@CNPJ(format = ALPHANUMERIC)`).
- R2.5 E-mail válido — `Pessoa.email` (`@Email`).
- R2.6 CPF, CNPJ, e-mail e inscrição estadual únicos **no banco inteiro** (não por empresa) — constraints `unique_cpf`, `unique_cnpj`, `unique_email`, `unique_incricao_estadual`.
- R2.7 Pessoa nasce ativa (`ativo=true`) — default em `Pessoa`.
- R2.8 Não há regra ligando `tipoPessoa` a CPF/CNPJ (ex.: PF exigir CPF) `[CONFIRMAR]`.

### ClienteFuncionario
- R2.9 Uma pessoa só pode ter um cadastro de cliente/funcionário na empresa — `ClienteFuncionarioService.salvar()` + `unique_pessoa`.
- R2.10 Tipo obrigatório (`CLIENTE`, `FUNCIONARIO`, `PRESTADOR_SERVICO`) e pessoa obrigatória — `ClienteFuncionario.tipoClienteFuncionario`, `.pessoa`.
- R2.11 Ao atualizar, pessoa **e usuário** são obrigatórios — `ClienteFuncionarioService.atualizar()` `[CONFIRMAR]`: no cadastro o usuário é opcional, e ele só é vinculado depois, em `UsuarioService.salvar()`.

## 3. Acesso

### Usuario
- R3.1 Só administrador cadastra ou atualiza usuário — `UsuarioService.salvar()/atualizar()` (mensagem igual nos dois: "cadastrar").
- R3.2 Login único — `UsuarioService.salvar()` (verifica por empresa) + `unique_login` (global).
- R3.3 Senha com no mínimo 5 caracteres — `UsuarioService.salvar()` (`length() < 5`) `[CONFIRMAR]`: a mensagem diz "mais de 5".
- R3.4 Uma pessoa só pode ter um usuário — `UsuarioService.salvar()/atualizar()` (`existePorPessoa`, `existeOutroUsuarioComPessoa`).
- R3.5 O usuário só é criado se a pessoa já tiver cadastro de `ClienteFuncionario` na empresa — `UsuarioService.salvar()/atualizar()`.
- R3.6 Usuário novo recebe automaticamente todas as roles `ROLE_USER` e fica vinculado ao `ClienteFuncionario` — `UsuarioService.salvar()`.
- R3.7 Senha gravada com BCrypt e nunca devolvida (DTO volta com "***Ocultada***") — `UsuarioService.salvar()`.
- R3.8 Usuário não liberado (`liberado=false`) não loga nem usa o token — `UsuarioService.login()`, `UsuarioDetailsService.loadUserByUsername()` (`Usuario.isEnabled()`).

### Login e sessão
- R3.9 Login recusado (401) para usuário inexistente, bloqueado, com empresa bloqueada ou senha errada (mensagens distintas) — `UsuarioService.login()`.
- R3.10 Token JWT com `usuarioId`, `empresaId` e `login`, válido por `jwt.expiration` — `JwtService.gerarToken()`.
- R3.11 Só o último token emitido é aceito (o token precisa ser igual a `Usuario.tokenSessao`) — `UsuarioService.login()` + `UsuarioDetailsService.existsByTokenParaUser()`.

### Troca de senha
- R3.12 A nova senha e a confirmação precisam ser iguais — `UsuarioService.alterarSenha()`.
- R3.13 A nova senha precisa ser diferente da atual — `UsuarioService.alterarSenha()`.
- R3.14 A senha atual precisa estar correta — `UsuarioService.alterarSenha()`.
- R3.15 Usuário bloqueado ou com empresa bloqueada não troca a senha — `UsuarioService.alterarSenha()`.
- R3.16 O usuário vem do `id` do DTO (qualquer usuário da mesma empresa), não do usuário logado `[CONFIRMAR]` se é intencional.

### Role / RoleUsuario
- R3.17 O nome do acesso precisa começar com `ROLE_` — `RoleService.salvar()/atualizar()`.
- R3.18 O nome do acesso é único — `RoleService.salvar()/atualizar()` + `unique_acesso`.
- R3.19 Um usuário não pode receber o mesmo acesso duas vezes — `RoleUsuarioService.salvar()/atualizar()` + `unique_role_user`.
- R3.20 Acesso e usuário são obrigatórios na associação — `RoleUsuarioService.salvar()/atualizar()`.

## 4. Produtos / Estoque

### Categoria
- R4.1 Nome obrigatório e único por empresa — `Categoria.nome`, `CategoriaService.salvar()/atualizar()`.
- R4.2 Só exclui uma categoria da empresa logada — `CategoriaService.deleteById()`.

### Produto
- R4.3 Nome único por empresa — `ProdutoService.salvar()/atualizar()` + `unique_nome_produto_empresa`.
- R4.4 SKU obrigatório e único por empresa — `Produto.sku` + `unique_sku_empresa`.
- R4.5 Obrigatórios: imagem, unidade de medida (`CENTIMETRO`, `METRO`, `UNIDADE`, `QUILO`) e categoria — `Produto` + `ProdutoService.atualizar()`.
- R4.6 Preço ≥ 1, estoque ≥ 1 e estoque mínimo ≥ 1 — `Produto.preco/estoque/estoqueMinimo` (`@Min(1)`) `[CONFIRMAR]`: estoque ≥ 1 impede produto zerado.
- R4.7 A categoria do produto precisa existir na empresa — `ProdutoService.salvar()/atualizar()` (`validarCategoria`).

### MovimentacaoProduto
- R4.8 Quantidade mínima de 0,1 — `MovimentacaoProduto.quantidade` (`@DecimalMin("0.1")`).
- R4.9 Tipo obrigatório (`ENTRADA_*`, `SAIDA_*`, `RESERVA_ESTOQUE`, `LIBERACAO_RESERVA`) e produto obrigatório — `MovimentacaoProduto` + `MovimentacaoProdutoService.validarProduto()`.
- R4.10 Pedido opcional; se informado, precisa existir na empresa — `MovimentacaoProdutoService.validarPedido()`.
- R4.11 Data do movimento vale hoje se não for informada; ao atualizar, só muda se vier preenchida — `MovimentacaoProdutoService.salvar()/atualizar()`.
- R4.12 **Movimentar não altera `Produto.estoque`** e não há checagem de saldo `[CONFIRMAR]` se é funcionalidade futura.

## 5. Vendas

### Pedido
- R5.1 Número do pedido obrigatório e único por empresa — `Pedido.numeroPedido`, `PedidoService.salvar()/atualizar()`.
- R5.2 Obrigatórios: status, forma de pagamento, tipo e data do pedido — `Pedido` (`@NotNull`).
- R5.3 Cliente e vendedor obrigatórios, e precisam ser usuários da empresa — `PedidoService.validarUsuario()`.
- R5.4 Valores (`subTotal`, `desconto`, `frete`, `taxas`, `total`) começam em zero e são **aceitos como vierem**, sem cálculo no back-end — `Pedido`, `PedidoService.atualizar()` `[CONFIRMAR]`.
- R5.5 Status livre: qualquer `StatusPedido` pode ir para qualquer outro, sem transições validadas e sem preencher `dataPagamento`/`dataCancelamento` automaticamente — `PedidoService.atualizar()` `[CONFIRMAR]`.

### ItemPedido
- R5.6 Quantidade mínima de 0,1 (padrão 1) — `ItemPedido.quantidade`.
- R5.7 Pedido e produto obrigatórios, e precisam existir na empresa — `ItemPedidoService.validarPedido()/validarProduto()`.
- R5.8 O mesmo produto não pode entrar duas vezes no pedido (a comparação é **pelo nome** do produto) — `ItemPedidoService.salvar()/atualizar()`.
- R5.9 Só exclui o item se ele pertencer ao pedido informado — `ItemPedidoService.deleteById()/deleteByIdAndPedido()`.
- R5.10 `subTotal`, `desconto` e `total` do item não são calculados nem somados ao pedido — `ItemPedidoService.atualizar()` `[CONFIRMAR]`.
- R5.11 Incluir item não baixa estoque nem gera `MovimentacaoProduto` `[CONFIRMAR]`.

## 6. Suporte

### Chamado
- R6.1 Título único por empresa — `ChamadoService.salvar()/atualizar()`.
- R6.2 Obrigatórios: título, descrição, tipo, prioridade (`BAIXA`, `MEDIA`, `ALTA`, `CRITICA`) e status — `Chamado` (`@NotNull`).
- R6.3 Na abertura: `dataAbertura` = hoje, `abertoUser` = usuário logado, `fechadoUser` e `dataFechamento` vazios — `ChamadoService.salvar()`.
- R6.4 Atendente e cliente obrigatórios e da empresa — `ChamadoService.validarUsuario()`.
- R6.5 Status livre, sem fluxo validado; `dataFechamento` e `fechadoUser` vêm do cliente da API — `ChamadoService.atualizar()` `[CONFIRMAR]`.

### Mensagem
- R6.6 Conteúdo obrigatório — `Mensagem.conteudo`.
- R6.7 `dataEnvio` = hoje; mensagem nasce não lida (`lida=false`) — `MensagemService.salvar()`, default em `Mensagem`.
- R6.8 Chamado, cliente e atendente obrigatórios e da empresa logada — `MensagemService.salvar()/atualizar()` (`validarChamado`, `validarUsuario`).
- R6.9 Só exclui a mensagem se ela pertencer ao chamado informado — `MensagemService.deleteByIdAndChamado()`.

## Pontos de atenção

1. **Login único:** o service verifica por empresa (`UsuarioService.salvar()`), mas a constraint `unique_login` é global. Um login repetido em outra empresa passa pela checagem e estoura no banco (`DataIntegrityViolationException`).
2. **Pessoa:** a unicidade por **nome** é por empresa (service), mas CPF, CNPJ e e-mail são únicos no banco inteiro. A mesma pessoa não pode ser cadastrada em duas empresas.
3. **Senha:** a regra `length() < 5` aceita 5 caracteres, mas a mensagem diz "mais de 5". Além disso, `Usuario` tem `unique_senha`, e dois usuários com hashes iguais seriam recusados (na prática o BCrypt usa salt).
4. **Admin incompleto:**
   - `Plano`, `Role` e `RoleUsuario` não verificam `isAdmin()`.
   - Qualquer usuário autenticado pode criar planos e roles, e **associar `ROLE_ADMIN` a si mesmo** via `/api/roleUsuario/salvar`.
   - `EmpresaService` só checa admin em salvar, atualizar e excluir. A listagem e a busca de empresas (todas, de todos os tenants) ficam abertas.
   - Exclusão de usuário (`/deletar`, `/deletar-todos`) não verifica admin.
5. **Ordem das validações em `EmpresaService.salvar()`:** `existePorPessoa(empresa.getPessoa().getId())` roda antes do null-check da pessoa e antes do `isAdmin()`. Pessoa nula vira `NullPointerException`, não a mensagem prevista.
6. **`UsuarioService.alterarSenha()`** chama `.get()` no `Optional` antes do null-check. Usuário inexistente vira `NoSuchElementException` (500), não "Usuário não encontrado.".
7. **`RoleUsuarioService.salvar()`** não confere se o usuário informado é da empresa logada (salva os ids vindos do JSON).
8. **Validação só no `atualizar()`:** `ClienteFuncionarioService` exige usuário no atualizar, mas não no salvar (R2.11).
9. **Estoque e totais não integrados:** `ItemPedido`, `Pedido` e `MovimentacaoProduto` não se atualizam entre si (R4.12, R5.4, R5.10, R5.11).
10. **Duplicidade de item comparada pelo nome** do produto, não pelo id (R5.8).
11. **`role_usuario` mapeada duas vezes** (`Usuario.acessos` `@ManyToMany` e entidade `RoleUsuario` com id de sequence). Inserir pelo `@ManyToMany` não preenche o `id` da entidade.
12. **`@Version` ausente:** o handler de lock otimista nunca é acionado (R0.7).
13. **Mensagens de login distintas** ("Usuário não encontrado" vs "Senha digitada é inválida") permitem descobrir quais logins existem.
14. **`Produto.estoque @Min(1)`** impede salvar um produto com estoque zero (R4.6).
