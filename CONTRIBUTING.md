# Como contribuir

## Branches
- `main`: versao estavel (somente via PR vindo de `develop`).
- `develop`: integracao.
- Uma branch por integrante: `feature/estrutura`, `feature/interface`, `feature/logica`, `feature/dados`, `feature/administracao`, `feature/qualidade`.

## Fluxo
1. Crie sua branch a partir de `develop`.
2. Faca commits pequenos no padrao `tipo: descricao` (ex.: `feat: adiciona DAO de palavras`).
3. Abra Pull Request para `develop` e peca revisao de outro integrante.
4. Apos aprovado, faca o merge. Ninguem faz push direto em `main` ou `develop`.

## Regra de ouro
Cada pessoa edita apenas os arquivos marcados com o seu numero no comentario de cabecalho ("Responsavel: Integrante N").
Mudancas em arquivos de outro integrante devem ser combinadas antes.
Arquivos comuns (`build.gradle.kts`, `libs.versions.toml`, `AndroidManifest.xml`, `strings.xml`) passam pelo Integrante 1.

## Regras estruturais
Veja a secao "Regras de arquitetura" do README.
