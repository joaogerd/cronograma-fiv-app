from PyQt5 import QtWidgets, QtCore, QtGui
from datetime import datetime, timedelta
import sys
import os

class CronogramaCompletoApp(QtWidgets.QWidget):
    """
    Aplicativo de interface gráfica para cálculo de cronogramas reprodutivos envolvendo:
    - Sincronização hormonal (D0 - D10)
    - Fertilização in vitro (FIV) (D-1 a D7)
    - Avaliação da prenhez (D7 a D280)

    O cálculo pode ser baseado na data de início do protocolo, data de transferência embrionária ou data prevista de nascimento.
    """

    def __init__(self):
        """Inicializa a interface gráfica do aplicativo."""
        super().__init__()
        self.setWindowTitle("Cronograma Completo - Sincronização / FIV / Prenhez")
        self.setGeometry(100, 100, 500, 750)

        # Ícone do programa
        icon_path = os.path.join(os.path.dirname(__file__), "images", "icon.ico")
        if os.path.exists(icon_path):
            self.setWindowIcon(QtGui.QIcon(icon_path))

        self.init_ui()

    def init_ui(self):
        """Configura os elementos da interface gráfica."""
        layout = QtWidgets.QVBoxLayout()

        # Logo no topo (se existir)
        logo_path = os.path.join(os.path.dirname(__file__), "images", "logo.png")
        if os.path.exists(logo_path):
            logo_label = QtWidgets.QLabel()
            pixmap = QtGui.QPixmap(logo_path)
            pixmap = pixmap.scaledToWidth(500, QtCore.Qt.SmoothTransformation)
            logo_label.setPixmap(pixmap)
            logo_label.setAlignment(QtCore.Qt.AlignCenter)
            layout.addWidget(logo_label)

        # Grupo de entrada
        group_input = QtWidgets.QGroupBox("⚙️ Dados de Entrada")
        input_layout = QtWidgets.QFormLayout()

        self.combo_opcao = QtWidgets.QComboBox()
        self.combo_opcao.addItems([
            "Início do protocolo (D0 - Sincronização)",
            "Transferência de embrião (D7 - FIV)",
            "Nascimento desejado (D280 - Prenhez)"
        ])
        input_layout.addRow("Escolha a base para o cálculo:", self.combo_opcao)

        self.date_edit = QtWidgets.QDateEdit()
        self.date_edit.setCalendarPopup(True)
        self.date_edit.setDate(QtCore.QDate.currentDate())
        input_layout.addRow("Selecione a data:", self.date_edit)

        group_input.setLayout(input_layout)
        layout.addWidget(group_input)

        # Botão de ação
        self.btn_calcular = QtWidgets.QPushButton("📊 Calcular Cronogramas")
        self.btn_calcular.setStyleSheet("padding: 8px; font-weight: bold;")
        self.btn_calcular.clicked.connect(self.calcular)
        layout.addWidget(self.btn_calcular, alignment=QtCore.Qt.AlignCenter)

        # Área de resultados
        group_result = QtWidgets.QGroupBox("📋 Resultado")
        result_layout = QtWidgets.QVBoxLayout()

        self.resultado = QtWidgets.QTextEdit()
        self.resultado.setReadOnly(True)
        self.resultado.setStyleSheet("font-family: 'Consolas'; font-size: 13px; background-color: #FDFEFE; padding: 10px; border: 1px solid #D5D8DC;")
        result_layout.addWidget(self.resultado)

        group_result.setLayout(result_layout)
        layout.addWidget(group_result)

        self.setLayout(layout)

    def calcular_calendarios(self, base: datetime, tipo: str):
        """
        Calcula os eventos do cronograma com base na data informada.

        Args:
            base (datetime): Data informada pelo usuário
            tipo (str): Tipo de base utilizada: 'inicio', 'transferencia' ou 'nascimento'

        Returns:
            tuple: três dicionários com os cronogramas de sincronização, FIV e prenhez
        """
        if tipo == "inicio":
            d0 = base
        elif tipo == "transferencia":
            d0 = base - timedelta(days=17)
        elif tipo == "nascimento":
            d0 = base - timedelta(days=290)  # 280 + 10
        else:
            raise ValueError("Tipo inválido.")

        sincronizacao = {
            "D0 - Início da sincronização": d0,
            "D8 - Retirada do implante + BE": d0 + timedelta(days=8),
            "D9 - OPU": d0 + timedelta(days=9),
            "D10 - FIV": d0 + timedelta(days=10)
        }

        d_fiv0 = d0 + timedelta(days=10)  # D0 da FIV
        fiv = {
            "D-1 - Coleta de oócitos (OPU)": d_fiv0 - timedelta(days=1),
            "D0 - Fertilização in vitro (FIV)": d_fiv0,
            "D1 - CIIV (Desenvolvimento embrionário)": d_fiv0 + timedelta(days=1),
            "D3 - Avaliação dos embriões (feeding 50%)": d_fiv0 + timedelta(days=3),
            "D5 - feeding 80%": d_fiv0 + timedelta(days=5),
            "D7 - Transferência embrionária (TE)": d_fiv0 + timedelta(days=7)
        }

        d_te = d_fiv0 + timedelta(days=7)
        prenhez = {
            "D7 - Transferência embrionária (TE)": d_te,
            "D8 - Relatório de TE": d_te + timedelta(days=1),
            "D30 - Diagnóstico de prenhez": d_te + timedelta(days=23),
            "D60 - Sexagem fetal": d_te + timedelta(days=53),
            "D280 - Nascimento previsto": d_te + timedelta(days=273)
        }

        return sincronizacao, fiv, prenhez

    def calcular(self):
        """Obtém dados da interface, calcula cronogramas e exibe os resultados formatados."""
        opcao = self.combo_opcao.currentIndex()
        data_qt = self.date_edit.date()
        data_base = datetime(data_qt.year(), data_qt.month(), data_qt.day())
        tipo = ["inicio", "transferencia", "nascimento"][opcao]

        sinc, fiv, prenhez = self.calcular_calendarios(data_base, tipo)

        def formatar_bloco(titulo, dados):
            texto = f"<b><u>{titulo}</u></b><br>"
            for etapa, data in dados.items():
                texto += f"<b>{etapa}</b>: {data.strftime('%d/%m/%Y')}<br>"
            return texto + "<br>"

        html_resultado = (
            formatar_bloco("CALENDÁRIO DE SINCRONIZAÇÃO", sinc) +
            formatar_bloco("CALENDÁRIO FIV / EMBRIÃO", fiv) +
            formatar_bloco("CALENDÁRIO DE PRENHEZ", prenhez)
        )

        self.resultado.setHtml(html_resultado)


if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)

    # Usa o tema nativo do sistema operacional
    #app.setStyle(QtWidgets.QStyleFactory.create(QtWidgets.QStyleFactory.keys()[0]))

    window = CronogramaCompletoApp()
    window.show()
    sys.exit(app.exec_())

