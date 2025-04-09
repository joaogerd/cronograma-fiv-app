from PyQt5 import QtWidgets, QtCore, QtGui, QtPrintSupport
from datetime import datetime, timedelta
import sys
from pathlib import Path

# Constantes
MM_TO_PT = 2.83465
OFFSETS = {"inicio": 0, "transferencia": -17, "nascimento": -290}
PIX_KEY = "ef8c4f06-4770-40e3-85ed-fd9a21338ab5"  # Usada apenas para exibição

def find_data_file(filename: str) -> Path:
    """
    Encontra e retorna o caminho para um arquivo de dados de forma robusta,
    considerando se a aplicação está empacotada (frozen) ou sendo executada a partir do código fonte.

    Quando a aplicação está empacotada (por exemplo, usando PyInstaller), o atributo sys.frozen 
    será True. Nesse caso, o diretório base utilizado será o diretório onde o executável está localizado.
    Se a aplicação não estiver empacotada, o diretório base será aquele onde o arquivo fonte (__file__) se encontra.

    Parâmetros:
        filename (str): Nome do arquivo ou caminho relativo do arquivo de dados a ser encontrado,
                        em relação ao diretório base da aplicação.

    Retorna:
        Path: Um objeto do tipo Path que aponta para o arquivo de dados.

    Exemplos:
        >>> find_data_file("images/logo.png")
        PosixPath('/caminho/do/projeto/images/logo.png')
    """
    if getattr(sys, "frozen", False):
        # A aplicação está empacotada: utiliza o diretório do executável.
        datadir = Path(sys.executable).parent
    else:
        # A aplicação não está empacotada: utiliza o diretório do script.
        datadir = Path(__file__).parent
    return datadir / filename

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
        self.setWindowTitle("Cronograma Completo - Sincronização / FIV / IATF / Prenhez")
        self.setGeometry(100, 100, 600, 1050)
        self.setup_icons()
        self.init_ui()

    def setup_icons(self):
        """Carrega e configura ícones e logos, se disponíveis."""
        icon_file = find_data_file("images/icon.ico")
        if icon_file.exists():
            self.setWindowIcon(QtGui.QIcon(str(icon_file)))
        self.logo_path = find_data_file("images/logo.png")

    def init_ui(self):
        """Configura os elementos da interface gráfica."""
        layout = QtWidgets.QVBoxLayout()

        # Exibe o logo se disponível
        if self.logo_path.exists():
            logo_label = QtWidgets.QLabel()
            pixmap = QtGui.QPixmap(str(self.logo_path))
            logo_label.setPixmap(pixmap.scaledToWidth(600, QtCore.Qt.SmoothTransformation))
            logo_label.setAlignment(QtCore.Qt.AlignCenter)
            layout.addWidget(logo_label)

        # Grupo de entrada
        group_input = QtWidgets.QGroupBox("⚙️ Dados de Entrada")
        input_layout = QtWidgets.QFormLayout()

        self.combo_opcao = QtWidgets.QComboBox()
        self.combo_opcao.addItems([
            "Início do protocolo hormonal",
            "Transferência de embrião",
            "Nascimento desejado"
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
        self.btn_fechar.clicked.connect(QtWidgets.QApplication.quit)
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
            "D0 - Implante Vaginal + BE": d0,
            "D8 - Retirada do implante Vaginal + CE + PGF2α": d0 + timedelta(days=8),
            "D9 - OPU": d0 + timedelta(days=9),
            "D10 - IATF (Não Receptora)": d0 + timedelta(days=10)
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
        logo_width = logo.width()
        logo_height = logo.height()

        while y_offset < total_height:
            if page > 1:
                printer.newPage()
    
            # Cabeçalho: desenha o logo, se disponível
            if logo:
                largura_logo = page_rect.width()
                altura_logo = logo.height() * (largura_logo / logo.width())
                pos_x = (page_rect.width() - largura_logo) / 2
                pos_y = margem_sup - altura_logo + altura_util * 0.1
                painter.drawPixmap(pos_x, pos_y, logo.scaledToWidth(largura_logo, QtCore.Qt.SmoothTransformation))
    
            painter.save()
            painter.translate(margem_esq, margem_sup + largura_util * 0.25 - y_offset)
            layout.draw(painter, QtGui.QAbstractTextDocumentLayout.PaintContext())
            painter.restore()
    
            # Rodapé com número da página
            painter.setFont(QtGui.QFont("Arial", 8))
            rodape_y = page_rect.bottom() - margem_inf / 2
            painter.drawText(page_rect.right() - margem_dir - 100, rodape_y, f"Página {page}")
    
            y_offset += altura_util
            page += 1
    
        painter.end()

class AboutDialog(QtWidgets.QDialog):
    """Diálogo 'Sobre' com informações do programa, do desenvolvedor, texto de doação e o QR Code do PIX."""
    
    def __init__(self, parent=None):
        super().__init__(parent)
        self.setWindowTitle("Sobre")
        self.init_ui()
        
    def init_ui(self):
        layout = QtWidgets.QVBoxLayout()
        
        # Informações sobre o programa e desenvolvedor
        info_text = (
            "<h2>Cronograma Completo</h2>"
            "<p>Aplicativo para cálculo de cronogramas reprodutivos: Sincronização, FIV/IATF e Prenhez.</p>"
            "<p>Desenvolvido por: João Gerd Zell de Mattos</p>"
            "<p>Versão: 1.0</p>"
            "<p>Licenciado sob a <a href='https://www.gnu.org/licenses/lgpl-3.0.html' target='_blank'>LGPLv3</a>.</p>"
            "<p>Veja o projeto em <a href='https://github.com/joaogerd/cronograma-fiv-app' target='_blank'>GitHub</a>.</p>"
            "<p>Contato: <a href='mailto:joao.gerd@gmail.com'>joao.gerd@gmail.com</a></p>"
        )
        info_label = QtWidgets.QLabel(info_text)
        info_label.setWordWrap(True)
        # Habilita a interação com links
        info_label.setTextInteractionFlags(QtCore.Qt.TextBrowserInteraction)
        info_label.setOpenExternalLinks(True)
        layout.addWidget(info_label)
        
        # Texto de solicitação de doação
        doacao_text = (
            "<p><b>Ajude a melhorar este projeto!</b></p>"
            "<p>Se desejar, faça uma doação de qualquer valor para apoiar o desenvolvimento deste aplicativo.</p>"
        )
        doacao_label = QtWidgets.QLabel(doacao_text)
        doacao_label.setWordWrap(True)
        layout.addWidget(doacao_label)
        
        # Carrega o QR Code a partir do arquivo images/qr-code.png
        base_path = Path(__file__).parent / "images"
        qr_file = (base_path / "qr-code.png").resolve()
        qr_label = QtWidgets.QLabel()
        if qr_file.exists():
            qr_pixmap = QtGui.QPixmap(str(qr_file))
            qr_label.setPixmap(qr_pixmap.scaledToWidth(150, QtCore.Qt.SmoothTransformation))
        else:
            qr_label.setText("QR Code não encontrado.")
        qr_label.setAlignment(QtCore.Qt.AlignCenter)
        layout.addWidget(qr_label)
        
        # Exibe a chave PIX abaixo do QR Code
        pix_key_label = QtWidgets.QLabel(f"Chave PIX: {PIX_KEY}")
        pix_key_label.setAlignment(QtCore.Qt.AlignCenter)
        layout.addWidget(pix_key_label)
        
        # Botão para fechar o diálogo
        btn_fechar = QtWidgets.QPushButton("Fechar")
        btn_fechar.clicked.connect(self.close)
        layout.addWidget(btn_fechar, alignment=QtCore.Qt.AlignCenter)
        
        self.setLayout(layout)
class MainWindow(QtWidgets.QMainWindow):
    """Janela principal que utiliza um QMainWindow e exibe o diálogo 'Sobre' ao clicar no item do menu."""
    def __init__(self):
        super().__init__()
        self.setWindowTitle("Cronograma Completo - Sincronização / FIV / Prenhez")
        self.resize(600, 900)
        
        # Define o widget central como o aplicativo principal
        self.central_widget = CronogramaCompletoApp()
        self.setCentralWidget(self.central_widget)
        self.create_menu()

    def create_menu(self):
        """
        Cria um item na barra de menus chamado "Sobre".  
        Ao ser clicado, o diálogo 'Sobre' é aberto.
        """
        # Adiciona uma ação diretamente na barra de menus
        about_action = self.menuBar().addAction("Sobre")
        about_action.triggered.connect(self.show_about_dialog)

    def show_about_dialog(self):
        """Exibe o diálogo 'Sobre'."""
        dialog = AboutDialog(self)
        dialog.exec_()

if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)
    
    # Define o ícone global da aplicação
    icon_file = (Path(__file__).parent / "images" / "icon.ico").resolve()
    if icon_file.exists():
        app.setWindowIcon(QtGui.QIcon(str(icon_file)))
    
    window = MainWindow()
    window.show()
    sys.exit(app.exec_())


