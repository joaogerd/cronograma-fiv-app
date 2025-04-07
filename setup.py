import cx_Freeze
import sys

base = None
if sys.platform == 'win32':
    base = "Win32GUI"

executables = [cx_Freeze.Executable("main.py", base=base, target_name="cronograma.exe")]

cx_Freeze.setup(
    name="Cronograma",
    options={
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
            "include_files": [
                ("images/icon.ico","images/icon.ico"),
                ("images/logo.png","images/logo.png")
            ],
        }
    },
    version="0.01",
    author="João Gerd Zell de Mattos",
    description="Aplicativo de interface gráfica (GUI) para cálculo automatizado de cronogramas reprodutivos bovinos",
    executables=executables,
)

