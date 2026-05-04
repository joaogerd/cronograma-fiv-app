# Cronograma FIV App

Aplicativo para cálculo e organização de cronogramas reprodutivos bovinos, com foco em protocolos de sincronização, FIV/PIVE, transferência de embriões, acompanhamento de prenhez e rotinas associadas.

> Estado atual: o repositório possui a aplicação desktop original em Python/PyQt5 e uma base Android nativa em Kotlin/Jetpack Compose com fluxo inicial de criação de cronograma em desenvolvimento.

## Objetivo do produto

O objetivo do projeto é evoluir para um aplicativo Android que permita a profissionais da reprodução bovina criar, visualizar, salvar e compartilhar cronogramas de FIV de forma rápida e confiável.

O app deverá atender principalmente:

- médicos veterinários;
- técnicos de campo;
- fazendas;
- centrais de reprodução;
- laboratórios de FIV;
- equipes que realizam OPU, FIV, CIV, feeding, transferência de embriões e criopreservação.

## Especificação de produto e interface

A direção oficial do produto, UX, interface, navegação e roadmap visual está documentada em:

- [`docs/PRODUCT_AND_UI_SPEC.md`](docs/PRODUCT_AND_UI_SPEC.md)

Essa especificação define o app como uma ferramenta simples de campo, organizada em torno do fluxo:

```text
Escolher protocolo → escolher data → gerar cronograma → revisar → salvar/compartilhar
```

## Funcionalidades atuais

A versão atual em Python/PyQt5 permite:

- informar uma data base;
- calcular cronogramas a partir do início do protocolo, transferência embrionária ou nascimento desejado;
- preencher nome da fazenda/propriedade;
- preencher veterinário responsável;
- visualizar o cronograma calculado;
- imprimir o cronograma na aplicação desktop.

A versão Android em desenvolvimento já contém:

- projeto Gradle Kotlin DSL;
- módulo Android `app`;
- `MainActivity` em Kotlin;
- Jetpack Compose;
- Material 3;
- modelos de domínio;
- protocolos padrão;
- motor de geração de cronogramas;
- tela MVP para criação e visualização inicial de cronograma.

## Stack atual

### Desktop legado

- Python 3.8+
- PyQt5
- Aplicação desktop para Windows/Linux

Execução local da versão desktop:

```bash
pip install -r requirements.txt
python main.py
```

Também há um ambiente Conda básico em `environment.yaml`.

### Android em desenvolvimento

- Kotlin;
- Android Gradle Plugin 8.13.x;
- Jetpack Compose;
- Material 3;
- JDK 17;
- Android SDK Platform 36.

Instruções iniciais de build Android:

- [`docs/ANDROID_BUILD.md`](docs/ANDROID_BUILD.md)

## Limitações atuais

A versão Android ainda não possui:

- ViewModel formal para a tela de criação;
- DatePicker visual;
- persistência local;
- histórico de cronogramas;
- cadastro editável de protocolos;
- notificações e lembretes;
- exportação mobile em PDF/compartilhamento;
- CI/CD completo.

## Roadmap Android

O plano técnico para transformar este projeto em um aplicativo Android está documentado em:

- [`docs/ANDROID_ROADMAP.md`](docs/ANDROID_ROADMAP.md)
- [`docs/PRODUCT_AND_UI_SPEC.md`](docs/PRODUCT_AND_UI_SPEC.md)

Sequência definida a partir do MVP UI:

1. MVP UI funcional básica;
2. ViewModel e estado de UI;
3. persistência local com Room;
4. histórico;
5. compartilhamento/exportação;
6. lembretes/notificações.

## Arquitetura Android recomendada

A migração recomendada é para Android nativo com:

- Kotlin;
- Jetpack Compose;
- MVVM simples;
- `StateFlow` para estado de tela;
- Room para persistência local;
- DataStore para preferências;
- WorkManager/AlarmManager para lembretes;
- JUnit para testes de domínio;
- GitHub Actions para validação automática.

A aplicação Python atual deve ser preservada como referência funcional, mas a versão Android deve ser reconstruída de forma nativa.

## Protocolos de referência

### Receptora

- D0: sincronização;
- D8: retirada de implante;
- D17: transferência de embriões.

### Doadora / PIVE

- D9 ou D-1: OPU;
- D10 ou D0: FIV;
- D11 ou D1: CIV;
- D13 ou D3: feeding 50%;
- D15 ou D5: feeding 80%;
- D17 ou D7: transferência de embriões ou criopreservação.

## Licença

Este projeto está licenciado sob a GNU Lesser General Public License v3.0 ou posterior. Veja o arquivo [`LICENSE`](LICENSE).
