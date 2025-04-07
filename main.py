from PyQt5 import QtWidgets, QtCore, QtGui, QtPrintSupport
from datetime import datetime, timedelta
import sys
from pathlib import Path

# Constantes
MM_TO_PT = 2.83465
OFFSETS = {"inicio": 0, "transferencia": -17, "nascimento": -290}

class CronogramaCompletoApp(QtWidgets.QWidget):
    """
    Aplicativo de interface gráfica para cálculo de cronogramas reprodutivos:
    - Sincronização hormonal (D0 - D10)
    - Fertilização in vitro (FIV) (D-1 a D7)
    - Avaliação da prenhez (D7 a D280)

    O cálculo pode ser baseado na data de início do protocolo, data de transferência embrionária ou data prevista de nascimento.
    """

    def __init__(self):
        super().__init__()
        self.setWindowTitle("Cronograma Completo - Sincronização / FIV / Prenhez")
        self.setGeometry(100, 100, 500, 850)
        self.setup_icons()
        self.init_ui()

    def setup_icons(self):
        """Carrega e configura ícones e logos, se disponíveis."""
        base_path = Path(__file__).parent / "images"
        icon_file = base_path / "icon.ico"
        if icon_file.exists():
            self.setWindowIcon(QtGui.QIcon(str(icon_file)))
        self.logo_path = base_path / "logo.png"

    def init_ui(self):
        """Configura os elementos da interface gráfica."""
        layout = QtWidgets.QVBoxLayout()

        # Exibe o logo se disponível
        if self.logo_path.exists():
            logo_label = QtWidgets.QLabel()
            pixmap = QtGui.QPixmap(str(self.logo_path))
            logo_label.setPixmap(pixmap.scaledToWidth(500, QtCore.Qt.SmoothTransformation))
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

        self.date_edit = QtWidgets.QDateEdit(calendarPopup=True)
        self.date_edit.setDate(QtCore.QDate.currentDate())
        input_layout.addRow("Selecione a data:", self.date_edit)

        self.input_fazenda = QtWidgets.QLineEdit()
        input_layout.addRow("Nome da Fazenda / Propriedade:", self.input_fazenda)

        self.input_vet = QtWidgets.QLineEdit()
        input_layout.addRow("Veterinário Responsável:", self.input_vet)

        group_input.setLayout(input_layout)
        layout.addWidget(group_input)

        # Área de resultados
        group_result = QtWidgets.QGroupBox("📋 Resultado")
        result_layout = QtWidgets.QVBoxLayout()
        self.resultado = QtWidgets.QTextEdit()
        self.resultado.setReadOnly(True)
        self.resultado.setStyleSheet(
            "font-family: 'Consolas'; font-size: 13px; background-color: #FDFEFE; padding: 10px; border: 1px solid #D5D8DC;"
        )
        result_layout.addWidget(self.resultado)
        group_result.setLayout(result_layout)
        layout.addWidget(group_result)

        self.setLayout(layout)

        # Botões
        btn_layout = QtWidgets.QHBoxLayout()
        self.btn_calcular = QtWidgets.QPushButton("📊 Calcular Cronogramas")
        self.btn_calcular.setStyleSheet("padding: 8px; font-weight: bold;")
        self.btn_calcular.clicked.connect(self.calcular)
        btn_layout.addWidget(self.btn_calcular)

        self.btn_imprimir = QtWidgets.QPushButton("🖨️ Imprimir Cronograma")
        self.btn_imprimir.setStyleSheet("padding: 8px; font-weight: bold;")
        self.btn_imprimir.clicked.connect(self.imprimir)
        btn_layout.addWidget(self.btn_imprimir)
        
        # Botão para fechar o app com ícone padrão
        self.btn_fechar = QtWidgets.QPushButton(" Fechar")
        # Obtém o ícone padrão para fechar janela
        icon_close = self.style().standardIcon(QtWidgets.QStyle.SP_TitleBarCloseButton)
        self.btn_fechar.setIcon(icon_close)
        self.btn_fechar.setStyleSheet("padding: 8px; font-weight: bold;")
        self.btn_fechar.clicked.connect(self.close)
        btn_layout.addWidget(self.btn_fechar)
        
        layout.addLayout(btn_layout)


    def calcular_calendarios(self, base: datetime, tipo: str):
        """
        Calcula os cronogramas de sincronização, FIV e prenhez.
        
        Args:
            base (datetime): Data base para o cálculo.
            tipo (str): Pode ser "inicio", "transferencia" ou "nascimento".
        
        Returns:
            tuple: Três dicionários com os cronogramas.
        """
        if tipo not in OFFSETS:
            raise ValueError("Tipo inválido.")
        d0 = base + timedelta(days=OFFSETS[tipo])
        
        sincronizacao = {
            "D0 - Início da sincronização": d0,
            "D8 - Retirada do implante + BE": d0 + timedelta(days=8),
            "D9 - OPU": d0 + timedelta(days=9),
            "D10 - FIV": d0 + timedelta(days=10)
        }

        d_fiv0 = d0 + timedelta(days=10)
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

    def formatar_bloco(self, titulo: str, dados: dict) -> str:
        """
        Formata um bloco HTML com título e etapas.

        Args:
            titulo (str): Título do bloco.
            dados (dict): Dicionário com as etapas e respectivas datas.
        
        Returns:
            str: Bloco HTML formatado.
        """
        html = f"<b><u>{titulo}</u></b><br>"
        for etapa, data in dados.items():
            html += f"<b>{etapa}</b>: {data.strftime('%d/%m/%Y')}<br>"
        return html + "<br>"

    def calcular(self):
        """Realiza o cálculo dos cronogramas e exibe o resultado na interface."""
        opcao = self.combo_opcao.currentIndex()
        data_qt = self.date_edit.date()
        data_base = datetime(data_qt.year(), data_qt.month(), data_qt.day())
        tipo = ["inicio", "transferencia", "nascimento"][opcao]

        sinc, fiv, prenhez = self.calcular_calendarios(data_base, tipo)
        fazenda = self.input_fazenda.text().strip() or "-"
        vet = self.input_vet.text().strip() or "-"

        html_resultado = f"<b>Propriedade:</b> {fazenda}<br><b>Veterinário responsável:</b> {vet}<br><br>"
        html_resultado += self.formatar_bloco("CALENDÁRIO DE SINCRONIZAÇÃO", sinc)
        html_resultado += self.formatar_bloco("CALENDÁRIO FIV / EMBRIÃO", fiv)
        html_resultado += self.formatar_bloco("CALENDÁRIO DE PRENHEZ", prenhez)

        self.resultado.setHtml(html_resultado)

    def imprimir(self):
        """Realiza a impressão do cronograma configurado."""
        printer = QtPrintSupport.QPrinter()
        printer.setPageSize(QtPrintSupport.QPrinter.A4)
        printer.setFullPage(True)
    
        dialog = QtPrintSupport.QPrintDialog(printer, self)
        if dialog.exec_() != QtWidgets.QDialog.Accepted:
            return
    
        # Define margens conforme ABNT2 (1 mm ≈ 2.83465 pts)
        margem_esq = 30 * MM_TO_PT
        margem_dir = 20 * MM_TO_PT
        margem_sup = 30 * MM_TO_PT
        margem_inf = 20 * MM_TO_PT
    
        page_rect = printer.pageRect()
        largura_util = page_rect.width() - margem_esq - margem_dir
        altura_util = page_rect.height() - margem_sup - margem_inf
    
        document = QtGui.QTextDocument()
        document.setHtml(self.resultado.toHtml())
        document.setPageSize(QtCore.QSizeF(largura_util, altura_util))
    
        painter = QtGui.QPainter()
        if not painter.begin(printer):
            return
    
        layout = document.documentLayout()
        total_height = layout.documentSize().height()
        y_offset = 0
        page = 1
    
        logo = QtGui.QPixmap(str(self.logo_path)) if self.logo_path.exists() else None
    
        while y_offset < total_height:
            if page > 1:
                printer.newPage()
    
            # Cabeçalho: desenha o logo, se disponível
            if logo:
                largura_logo = min(largura_util, 450)
                altura_logo = logo.height() * (largura_logo / logo.width())
                pos_x = margem_esq + (largura_util - largura_logo) / 2
                pos_y = margem_sup - altura_logo + largura_util * 0.05
                painter.drawPixmap(pos_x, pos_y, logo.scaledToWidth(largura_logo, QtCore.Qt.SmoothTransformation))
    
            painter.save()
            painter.translate(margem_esq, margem_sup + largura_util * 0.15 - y_offset)
            layout.draw(painter, QtGui.QAbstractTextDocumentLayout.PaintContext())
            painter.restore()
    
            # Rodapé com número da página
            painter.setFont(QtGui.QFont("Arial", 8))
            rodape_y = page_rect.bottom() - margem_inf / 2
            painter.drawText(page_rect.right() - margem_dir - 100, rodape_y, f"Página {page}")
    
            y_offset += altura_util
            page += 1
    
        painter.end()

if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)
    app.setStyle(QtWidgets.QStyleFactory.create(QtWidgets.QStyleFactory.keys()[0]))
    window = CronogramaCompletoApp()
    window.show()
    sys.exit(app.exec_())


