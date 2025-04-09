import cx_Freeze
import sys
from pathlib import Path

# Informações do projeto
company_name = "Projeto Bovino"
product_name = "Cronograma Reprodutivo"
TARGET_NAME = "cronograma.exe"
UPGRADE_CODE = "{150524-0000-0000-0000-000000000001}"  # GUID válido para upgrades

def find_data_file(filename: str) -> Path:
    """
    Retorna o caminho absoluto de um arquivo, considerando se o app está empacotado (frozen) ou não.
    """
    if getattr(sys, "frozen", False):
        datadir = Path(sys.executable).parent
    else:
        datadir = Path(__file__).parent
    return datadir / filename

# Base para executável Windows com interface gráfica
base = "Win32GUI" if sys.platform == "win32" else None

# Executável principal
executables = [
    cx_Freeze.Executable(
        script="main.py",
        base=base,
        target_name=TARGET_NAME,
        icon=str(find_data_file("images/icon.ico"))
    )
]

# Tabelas MSI personalizadas
shortcut_table = [
    (
        "DesktopShortcut",           # Shortcut name
        "DesktopFolder",             # Directory_
        "Cronograma Bovino",         # Shortcut name shown
        "TARGETDIR",                 # Component_
        f"[TARGETDIR]{TARGET_NAME}", # Target
        None,                        # Arguments
        "Atalho para o Cronograma",  # Description
        None, "", None, None, "TARGETDIR"
    ),
    (
        "StartMenuShortcut",
        "StartMenuFolder",
        "Cronograma Bovino",
        "TARGETDIR",
        f"[TARGETDIR]{TARGET_NAME}",
        None,
        "Atalho para o Cronograma",
        None, "", None, None, "TARGETDIR"
    ),
]

custom_action_table = [
    (
        "SetTargetDir",
        "51",
        "TARGETDIR",
        f"[ProgramFilesFolder]{company_name}\\{product_name}"
    )
]

sequence_table = [("SetTargetDir", "", "800")]

property_table = [
    ("TARGETDIR", "C:\\"),
    ("MANUFACTURER", company_name),
    ("PUBLISHER", company_name),
]

msi_data = {
    "Shortcut": shortcut_table,
    "Property": property_table,
    "CustomAction": custom_action_table,
    "InstallExecuteSequence": sequence_table,
}

bdist_msi_options = {
    "summary_data": {
        "author": "João Gerd Zell de Mattos",
        "comments": "Aplicativo de GUI para cálculo automatizado de cronogramas reprodutivos bovinos",
        "keywords": "FIV, OPU, reprodução bovina",
    },
    "install_icon": str(find_data_file("images/icon.ico")),
    "initial_target_dir": f"[ProgramFilesFolder]{company_name}\\{product_name}",
    "upgrade_code": UPGRADE_CODE,
    "data": msi_data,
    "all_users": True,
    "skip_build": False,
}

options = {
    "build_exe": {
        "packages": [
            "PyQt5.QtWidgets",
            "PyQt5.QtCore",
            "PyQt5.QtGui",
            "PyQt5.QtPrintSupport",
            "datetime",
            "sys",
            "pathlib"
        ],
        "zip_include_packages": "*",
        "zip_exclude_packages": [],
        "optimize": 2,
        "include_files": [
            (find_data_file("images/icon.ico"), "images/icon.ico"),
            (find_data_file("images/logo.png"), "images/logo.png"),
        ],
    },
    "bdist_msi": bdist_msi_options,
}

# Execução do setup
cx_Freeze.setup(
    name="Cronograma Reprodutivo Bovino",
    version="1.0",
    description="Ferramenta para cálculo automatizado de cronogramas FIV/OPU",
    options=options,
    executables=executables,
)

