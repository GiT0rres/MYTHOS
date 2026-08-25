# MYTHOS

Aplicativo Android nativo (Kotlin + Jetpack Compose) com autenticação no **Firebase
Authentication** e dados do tema no **Cloud Firestore**.

## Tema

**Mitologia Comparada: representações artísticas (estátuas, afrescos) de deuses e heróis
de diferentes culturas.**

O MYTHOS é um museu digital. O usuário navega por um acervo de deuses e heróis de
culturas diferentes (Grega, Egípcia, Romana, Nórdica e Hindu), vê a ficha de cada obra
(cultura, período, poder, símbolo, domínio e tipo de representação artística) e pode
**comparar dois personagens lado a lado**, com uma curiosidade explicando as semelhanças
entre as culturas.

## Objetivo

Mostrar que mitos distantes no tempo e no espaço criaram figuras divinas muito parecidas
— por exemplo Zeus (Grécia), Júpiter (Roma), Thor (Escandinávia) e Indra (Índia), todos
ligados ao trovão — e como a arte de cada povo representou essas divindades.

## Tecnologias

- Kotlin
- Jetpack Compose (UI 100% declarativa, Material 3)
- Navigation Compose
- ViewModel + StateFlow / LiveData
- Firebase Authentication (e-mail e senha)
- Cloud Firestore (backend do tema)

## Funcionalidades

- Cadastro de usuário (`createUserWithEmailAndPassword`)
- Login (`signInWithEmailAndPassword`)
- Recuperação de senha por e-mail
- Logout
- Home com identidade visual do museu e atalhos por categoria
- Galeria com filtro por cultura
- Tela de detalhes da obra em destaque
- Tela de comparação (Deus 1 × Deus 2) com curiosidade vinda do Firestore
- Favoritos
- Perfil com dados do usuário autenticado e sair

## Identidade visual

| Uso                | Cor       |
| ------------------ | --------- |
| Fundo              | `#06141C` |
| Dourado principal  | `#C69232` |
| Dourado claro      | `#E0B45A` |
| Branco/marfim      | `#F5F1E8` |
| Texto secundário   | `#B8B8B8` |

Tipografia serifada para títulos (estética de museu clássico) e sans-serif para textos.
Tokens centralizados em `ui/theme/Color.kt`, `Theme.kt` e `Type.kt`.

## Estrutura

```text
app/src/main/java/com/example/mythos/
├── MainActivity.kt
├── navigation/MyAppNavigation.kt
├── viewmodel/AuthViewModel.kt
├── viewmodel/MythologyViewModel.kt
├── model/Deity.kt
├── data/MythologyRepository.kt
├── pages/ (LoginPage, SignupPage, HomePage, GalleryPage,
│          DetailPage, ComparePage, FavoritesPage, ProfilePage, Components)
└── ui/theme/ (Color, Theme, Type)
```

## Backend (Firebase)

```text
Firebase
├── Authentication
│   └── Users (e-mail/senha)
└── Firestore Database
    ├── deities: zeus, ra, jupiter, thor, indra, hercules
    └── comparisons: zeus_thor, zeus_jupiter, thor_indra, ra_zeus
```

Na primeira execução o app popula as coleções automaticamente
(`MythologyRepository.seedIfEmpty()`), então os documentos do tema aparecem no
Firebase Console para a demonstração no vídeo.

## Como executar

1. Abra a pasta `android/` no Android Studio (Ladybug ou superior).
2. Crie um projeto no [Firebase Console](https://console.firebase.google.com).
3. Adicione um app Android com o pacote `com.example.mythos`.
4. Baixe o `google-services.json` e coloque em `android/app/`.
   > O arquivo **não** deve ser versionado no GitHub (já está no `.gitignore`).
5. No Firebase Console:
   - Authentication → Sign-in method → habilite **Email/Password**.
   - Firestore Database → Criar banco (modo de teste durante o desenvolvimento).
6. Rode o app no emulador ou em um dispositivo físico.

## Imagens das obras

As telas usam um moldura artística gerada em Compose (`ArtworkFrame`) como placeholder.
Para usar fotos reais de estátuas e afrescos, coloque os arquivos em
`app/src/main/res/drawable/` (ex.: `zeus.jpg`) e troque `ArtworkFrame` por:

```kotlin
Image(
    painter = painterResource(R.drawable.zeus),
    contentDescription = "Zeus",
    contentScale = ContentScale.Crop,
    modifier = Modifier.fillMaxSize()
)
```
