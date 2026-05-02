# Android Migration Roadmap

Este documento define o plano técnico para transformar o projeto `cronograma-fiv-app`, atualmente uma aplicação desktop em Python/PyQt5, em um aplicativo Android funcional, robusto, simples de usar e com potencial de monetização.

## 1. Diagnóstico do estado atual

O projeto atual é um protótipo desktop funcional. Ele concentra em `main.py` a interface gráfica, a regra de cálculo, a formatação do resultado e a impressão do cronograma.

### Pontos aproveitáveis

- conceito do produto;
- regra inicial de cálculo por datas e offsets;
- lista inicial de eventos de sincronização, FIV/PIVE e prenhez;
- identidade visual inicial em `images/`;
- README inicial;
- licença livre;
- simplicidade funcional.

### Limitações atuais

- não há projeto Android;
- não há separação entre interface, domínio e dados;
- não há persistência local;
- não há histórico;
- não há protocolos editáveis;
- não há lembretes;
- não há exportação mobile;
- não há testes automatizados;
- não há CI/CD;
- não há arquitetura preparada para monetização futura.

## 2. Decisão técnica principal

A recomendação é reconstruir o aplicativo como Android nativo usando Kotlin e Jetpack Compose.

A versão Python/PyQt5 deve ser mantida apenas como referência funcional e histórica. Não é recomendado tentar adaptar PyQt para Android, pois isso criaria uma solução pouco natural, difícil de manter e inadequada para publicação moderna em Android.

## 3. Stack Android recomendada

| Área | Recomendação |
|---|---|
| Linguagem | Kotlin |
| Interface | Jetpack Compose |
| Arquitetura | MVVM simples |
| Estado | ViewModel + StateFlow |
| Persistência | Room |
| Preferências | DataStore |
| Datas | java.time.LocalDate |
| Notificações | WorkManager e, quando necessário, AlarmManager |
| Exportação | PDF simples e Android Sharesheet |
| Testes | JUnit, testes de ViewModel e testes instrumentados mínimos |
| CI | GitHub Actions |
| Build | Gradle Kotlin DSL |

## 4. Arquitetura proposta

```text
app/
├── build.gradle.kts
├── src/main/
│   ├── AndroidManifest.xml
│   ├── java/br/com/cronogramafiv/
│   │   ├── MainActivity.kt
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │   ├── Protocol.kt
│   │   │   │   ├── ProtocolStep.kt
│   │   │   │   ├── Schedule.kt
│   │   │   │   └── ScheduleEvent.kt
│   │   │   ├── service/
│   │   │   │   └── ScheduleGenerator.kt
│   │   │   └── repository/
│   │   │       └── ScheduleRepository.kt
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── AppDatabase.kt
│   │   │   │   ├── ProtocolDao.kt
│   │   │   │   └── ScheduleDao.kt
│   │   │   └── mapper/
│   │   ├── ui/
│   │   │   ├── theme/
│   │   │   ├── screens/
│   │   │   │   ├── home/
│   │   │   │   ├── protocol/
│   │   │   │   ├── schedule/
│   │   │   │   └── history/
│   │   │   └── components/
│   │   └── notification/
│   └── res/
└── src/test/
    └── java/br/com/cronogramafiv/
        └── ScheduleGeneratorTest.kt
```

## 5. Produto proposto

### Nome comercial sugerido

**FIV Agenda**

Alternativas:

- CronoFIV;
- FIV Fácil;
- Agenda Repro;
- Protocolo FIV;
- FIV Campo.

### Proposta de valor

Gerar, salvar e compartilhar cronogramas de FIV de forma rápida, segura e adequada ao uso em campo.

### Público prioritário

- médicos veterinários autônomos;
- técnicos de campo;
- fazendas;
- centrais de reprodução;
- laboratórios de FIV;
- equipes envolvidas em OPU, FIV, CIV, feeding, TE e criopreservação.

## 6. MVP recomendado

O MVP deve focar em criar cronogramas de forma rápida e confiável.

### Funcionalidades essenciais

- protocolos padrão pré-cadastrados;
- criação de cronograma a partir de uma data inicial;
- visualização das etapas por data;
- cadastro simples de propriedade/fazenda;
- cadastro simples de responsável;
- edição manual de etapas;
- salvamento local;
- histórico simples;
- compartilhamento em texto;
- testes unitários da geração de datas.

### Fora do MVP

- login;
- backend;
- sincronização em nuvem;
- assinatura;
- gestão completa de animais;
- integração com Google Calendar;
- relatórios produtivos avançados;
- multiusuário;
- inteligência artificial.

## 7. Roadmap por fases

| Fase | Objetivo | Resultado esperado | Branch sugerido | PR sugerido |
|---:|---|---|---|---|
| 1 | Diagnóstico e documentação inicial | README e roadmap alinhados à migração Android | `docs/android-roadmap` | `docs: add Android migration roadmap` |
| 2 | Bootstrap Android | Projeto Android nativo compila com Kotlin/Compose | `chore/android-bootstrap` | `chore: bootstrap Android project` |
| 3 | Domínio | Modelos de protocolo, etapa, cronograma e evento | `feat/domain-models` | `feat: add protocol and schedule domain models` |
| 4 | Motor de cronograma | Geração automática de datas com testes | `feat/schedule-generator` | `feat: implement schedule generation engine` |
| 5 | Interface MVP | Tela inicial, criação e visualização de cronograma | `feat/mvp-ui` | `feat: add MVP schedule creation UI` |
| 6 | Persistência local | Room salva protocolos e cronogramas | `feat/local-storage` | `feat: add local persistence with Room` |
| 7 | Protocolos editáveis | Usuário cria e edita etapas | `feat/editable-protocols` | `feat: add editable protocols` |
| 8 | Histórico | Lista de cronogramas anteriores | `feat/schedule-history` | `feat: add schedule history` |
| 9 | Compartilhamento | Exportação em texto/PDF simples | `feat/share-export` | `feat: add schedule sharing and PDF export` |
| 10 | Lembretes | Notificações locais para etapas futuras | `feat/reminders` | `feat: add local reminders` |
| 11 | Qualidade | Testes, lint e GitHub Actions | `ci/android-quality` | `ci: add Android build and test workflow` |
| 12 | Release MVP | APK/AAB inicial preparado | `release/0.1.0-mvp` | `release: prepare MVP 0.1.0` |
| 13 | Monetização futura | Plano técnico de recursos premium | `docs/monetization-plan` | `docs: add monetization strategy` |

## 8. Estratégia de monetização

A monetização deve ser pensada desde o início, mas não deve bloquear o MVP.

### Modelo gratuito

- protocolos padrão;
- número limitado de cronogramas salvos;
- compartilhamento em texto;
- uso offline.

### Premium individual

Possibilidades:

- compra única;
- assinatura mensal;
- assinatura anual.

Recursos possíveis:

- cronogramas ilimitados;
- protocolos personalizados ilimitados;
- exportação em PDF com logo;
- histórico completo;
- lembretes avançados;
- duplicação de cronograma;
- observações por etapa;
- backup futuro.

### Plano para fazendas/equipes

Deve ficar para versões futuras, com sincronização, usuários múltiplos, controle por propriedade e relatórios.

## 9. Estratégia de desenvolvimento no GitHub

Cada fase deve ser desenvolvida em branch própria e entregue por Pull Request.

Padrão de commits recomendado:

```text
docs: update README for Android roadmap
chore: bootstrap Android project
feat: add schedule domain models
feat: implement schedule generator
test: add schedule generator unit tests
fix: correct license reference
ci: add Android build workflow
```

Cada PR deve conter:

- resumo técnico;
- arquivos alterados;
- testes executados;
- pendências conhecidas;
- riscos;
- próximos passos.

## 10. Critérios gerais de qualidade

- simplicidade de uso;
- código limpo;
- arquitetura clara;
- MVP funcional antes de recursos avançados;
- interface adequada para campo;
- ausência de backend no início;
- documentação suficiente;
- testes na lógica de cronograma;
- PRs pequenos e revisáveis.

## 11. Próxima fase

Após aprovação desta fase documental, a próxima fase deve criar a base Android nativa no branch:

```text
chore/android-bootstrap
```

Pull Request sugerido:

```text
chore: bootstrap Android project
```
