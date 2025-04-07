from cx_Freeze import setup, Executable

setup(
    name="cronograma-fiv",
    version="1.0",
    description="App FIV",
    executables=[Executable("app.py")],
)

