# 🐄 Cronograma FIV App

[![License: LGPL v3](https://img.shields.io/badge/License-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)
![Python](https://img.shields.io/badge/Python-3.8+-blue.svg)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20Linux-green.svg)
![Status](https://img.shields.io/badge/Status-Ativo-brightgreen)

Aplicativo de interface gráfica (GUI) para cálculo automatizado de **cronogramas reprodutivos bovinos**, incluindo:

- ✅ Sincronização hormonal
- ✅ Fertilização in vitro (FIV)
- ✅ Diagnóstico de prenhez e nascimento previsto

---

## 🚀 Funcionalidades

- Cálculo baseado em:
  - Início do protocolo (D0)
  - Transferência embrionária (D7)
  - Data de nascimento estimada (D280)
- Visual profissional e intuitivo
- Interface amigável com **PyQt5**
- Compatível com `.exe` para Windows

---

## 🛠 Requisitos

- Python 3.8 ou superior
- PyQt5

Instale as dependências:
```bash
pip install -r requirements.txt
```

---

## ▶️ Execução

```bash
python main.py
```

---

## 📦 Compilação para Windows (.exe)

```bash
pyinstaller --onefile --windowed --icon=images/icon.ico --add-data "images/logo.png;images" main.py
```

Ou utilize o arquivo `cronograma.spec` já incluído no projeto.

---

## 🧠 Estrutura de Diretórios

```
cronograma-fiv-app/
├── main.py
├── cronograma.spec
├── requirements.txt
├── environment.yaml
├── LICENSE
├── README.md
├── .gitignore
└── images/
    ├── logo.png
    └── icon.ico

```

---

## 📜 Licença

Este projeto está licenciado sob a [LGPL v3](https://www.gnu.org/licenses/lgpl-3.0.html).



