# DevTermo

O desafio das palavras da programacao. Mini-game inspirado no Termo, com palavras de tecnologia.
Projeto academico em grupo (6 integrantes): Kotlin, Jetpack Compose, MVVM, Room, Navigation Compose e Firebase Firestore.

## Como abrir
1. Android Studio: File > Open > selecione a pasta `DevTermo`.
2. Aguarde o Gradle Sync. Requer JDK 17 ou superior (o Android Studio ja inclui).
3. Rode no emulador ou aparelho (minSdk 24).

O app abre e funciona localmente (Room) sem Firebase. A sincronizacao com a nuvem so funciona depois do passo abaixo.

## Configurar o Firebase (Integrante 5)
1. Crie um projeto no console do Firebase e ative o Firestore.
2. Registre o app Android com o pacote `com.devtermo.app`.
3. Baixe o `google-services.json` e coloque em `app/`.
4. O build aplica o plugin do Google Services automaticamente quando esse arquivo existe.
5. O grupo decide se o arquivo vai ou nao para o Git (linha comentada no `.gitignore`).

Sem o arquivo, operacoes remotas falham de forma tratada: a palavra e salva no aparelho e fica marcada como pendente de sincronizacao.

## Versoes (fixadas em `gradle/libs.versions.toml`)
AGP 9.3.2, Gradle 9.7.1, Kotlin 2.3.0, KSP 2.3.4, Compose BOM 2026.09.00, Navigation 2.10.1, Room 2.8.5, Lifecycle 2.10.0, Firebase BoM 35.1.0. compileSdk 37, targetSdk 36, minSdk 24.
Alteracoes de versao devem passar pelo Integrante 1.

## Estrutura (com dono de cada arquivo)
```
app/src/main/java/com/devtermo/app/
  DevTermoApp.kt, MainActivity.kt         [1]
  model/Models.kt                         [todos leem; mudancas combinadas]
  logic/        WordValidator, GameRules  [3]
  data/local/   entity, dao, AppDatabase  [4]
  data/remote/  FirebaseDataSource        [5]
  data/repository/
    AppResult, Mappers                    [4]
    WordRepository                        [4 local + 5 sincronizacao]
    MatchRepository                       [4 local + 6 ranking]
    SessionRepository                     [1]
  viewmodel/
    ViewModelFactory                      [1]
    GameViewModel + GameUiState           [3]
    AdminViewModel + AdminUiState         [5]
    ProfileViewModel + ProfileUiState     [6]
  ui/theme                                [2]
  ui/navigation (Routes, NavGraph, BottomBar)   [1]
  ui/components  LetterTile, GameGrid, Keyboard [2]
                 ScreenHeader                   [1]
  ui/screens
    ProfileSelectScreen                   [1]
    HomeScreen, GameScreen                [2]
    AdminScreen, WordListScreen, WordFormScreen   [5]
    RankingScreen, HistoryScreen, ProfileScreen, StatisticsScreen   [6]
```

## Divisao do grupo
| # | Area | Entregas |
|---|------|----------|
| 1 | Arquitetura e navegacao | Projeto, pacotes, NavGraph, Bottom Bar por perfil, ViewModelFactory, SessionRepository |
| 2 | Interface do jogador | Home, Game, Result, componentes visuais, teclado, grade, identidade visual |
| 3 | Logica do jogo | WordValidator, GameRules, GameViewModel, GameUiState |
| 4 | Room | Entities, DAOs, AppDatabase, CRUD, Flow, parte local dos repositories |
| 5 | Admin e Firebase | FirebaseDataSource, sincronizacao, AdminViewModel, telas de administracao |
| 6 | Ranking, perfil e qualidade | Ranking, Historico, Perfil, Estatisticas, testes, documentacao |

## Regras de arquitetura
Direcao de dependencia: `ui -> viewmodel -> repository -> (local | remote)` e `viewmodel -> logic`.

- UI: sem regra de negocio; telas recebem estado e lambdas; listas com `LazyColumn` e `key`.
- ViewModel: expoe `StateFlow<UiState>` (o `MutableStateFlow` e privado), nunca acessa DAO ou Firebase, nao usa Context.
- UiState: um `data class` por ViewModel, com `isLoading`, `erro` e `mensagemSucesso`.
- Repository: unico ponto que conhece Room e Firestore; usa `try-catch` (via `safeCall`) e devolve `AppResult`.
- Entities do Room e documentos do Firestore nunca chegam a UI (conversao com `toDomain()` e `toEntity()`).
- `logic/`: funcoes puras em Kotlin, testaveis sem Android.
- Acesso por perfil: validado no NavGraph e tambem no ViewModel antes de escritas. E um controle demonstrativo (sem autenticacao real); registrar essa limitacao na documentacao.
- `map`, `filter` e `sortedBy` com lambdas ficam nos ViewModels (veja `AdminViewModel` e `ProfileViewModel`).
- Textos visiveis em `strings.xml`. Rotas apenas em `Routes`.

## Estado atual
Implementado (base funcional, a ser revisada e evoluida pelos responsaveis):
- Estrutura MVVM completa, ViewModelFactory, navegacao por perfil com Bottom Bar.
- Room com 2 entidades, DAOs, Flow e CRUD de palavras.
- Validador de palavras com letras repetidas e teste unitario.
- Fluxo de partida, registro de historico, ranking calculado localmente.
- CRUD administrativo com salvamento local e tentativa de envio ao Firestore.

Pendente (marcado com `TODO [Integrante N]` no codigo):
- Selecao de categoria e dificuldade, logo, visual refinado, tela de resultado dedicada (2).
- Filtros por chips, palavras mais usadas e mais dificeis, preferencias do jogador (5 e 6).
- Ranking compartilhado no Firestore, exclusoes pendentes de sincronizacao (5 e 6).
- Testes funcionais e documentacao final (6).

## Decisoes a confirmar com o grupo
1. Um ou dois repositories (hoje: `WordRepository` e `MatchRepository`).
2. Politica de conflito na sincronizacao (hoje: remoto prevalece, exceto pendencias locais).
3. Comportamento ao excluir palavra com partidas no historico (hoje: sem chave estrangeira).
4. Estrategia para palavras de tamanhos diferentes e valores de `GameRules`.
5. Ranking lido direto do Firestore ou espelhado no Room.
6. Se o `google-services.json` vai para o Git.
