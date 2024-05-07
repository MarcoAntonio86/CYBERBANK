import sqlite3
from tkinter import *
import customtkinter
from PIL import Image
import os

# Classe para representar uma conta
class Conta():
    def __init__(self, id, titular, email, senha, saldo, cheque_especial):
        self.id = id
        self.titular = titular
        self.email = email
        self.senha = senha
        self.saldo = saldo
        self.cheque_especial = cheque_especial
        
    def depositar(self, valor):
        self.saldo += valor
    
    def sacar(self, valor):
        if self.saldo > 0:
            self.saldo -= valor
        else:
            pass
    
    def consultar_saldo(self):
        return self.saldo

# Classe para manipular o banco de dados
class Banco():
    def __init__(self):
        self.banco = sqlite3.connect("bancoDip.db")
        self.cursor = self.banco.cursor()
        self.cursor.execute('''
                            CREATE TABLE IF NOT EXISTS banco(id INTEGER PRIMARY KEY AUTOINCREMENT, 
                            titular VARCHAR(80), 
                            email VARCHAR(80), 
                            senha VARCHAR(24),
                            saldo DECIMAL, 
                            cheque_especial DECIMAL)
                            ''')
        
        self.banco.commit()
        self.banco.close()
        
    def criar_conta(self, conta):
        try:
            self.banco = sqlite3.connect("bancoDip.db")
            self.cursor = self.banco.cursor()
            self.cursor.execute("INSERT INTO banco (titular, email, senha, saldo, cheque_especial) VALUES (?, ?, ?, ?, ?)", (conta.titular, conta.email, conta.senha, conta.saldo, 
                                                                                                                             conta.saldo * 4))
            self.banco.commit()
            self.banco.close()
            
        except sqlite3.Error as err:
            print(err)
            
    def verificar_login(self, email, senha):
        try:
            self.banco = sqlite3.connect("bancoDip.db")
            self.cursor = self.banco.cursor()
            self.cursor.execute("SELECT * FROM banco WHERE email = ? AND senha = ?", (email, senha))
            usuario = self.cursor.fetchone()
            return usuario
            
        except sqlite3.Error as err:
            print(err)
        finally:
            self.banco.close()

# Classe para a interface do usuário
class Interface():
    @staticmethod
    def tela_login():
        def janela_login():
            # Criando janela de login
            janela_login = customtkinter.CTkToplevel()
            janela_login.geometry("700x400")
            janela_login.title("BancoDIP")

            # Customizando janela no geral
            direito = customtkinter.CTkFrame(master=janela_login, width=340, height=400, fg_color="#192042")
            direito.place(x=360, y=0)

            # Adição de elementos
            logo = os.path.join(os.path.dirname(__file__), 'Logo/Logo.png')
            imagem = customtkinter.CTkImage(light_image=Image.open(logo))
            label_logo = customtkinter.CTkLabel(master=direito, image=imagem, text="")
            nomebanco = customtkinter.CTkLabel(janela_login, text="Banco DIP")
            email = customtkinter.CTkEntry(janela_login, placeholder_text="Email")
            senha = customtkinter.CTkEntry(janela_login, placeholder_text="Senha", show="*")
            login = customtkinter.CTkButton(janela_login, text="Login", command=lambda: Interface.fazer_login(email.get(), senha.get()))
            cadastrar = customtkinter.CTkButton(janela_login, text="Criar Conta", command=Interface.registrar_conta)

            # Customização dos elementos
            label_logo.place(x=-87, y=-45)
            imagem.configure(size=(500, 500))

            nomebanco.place(x=140, y=80)
            nomebanco.configure(font=("Roboto", 22, "bold"))

            email.place(x=40, y=150)
            email.configure(font=("Roboto", 16), width=300, height=38, border_width=0)
            senha.place(x=40, y=200)
            senha.configure(font=("Roboto", 16), width=300, height=38, border_width=0)

            login.place(x=40, y=250)
            login.configure(font=("Roboto", 16), width=300, height=38, fg_color="#192042")

            cadastrar.place(x=40, y=290)
            cadastrar.configure(font=("Roboto", 14), width=300, height=38, fg_color="#192042")

        janela_login()

    @staticmethod
    def janela_principal(usuario):
        # Criando janela principal
        janela_principal = customtkinter.CTkToplevel()
        janela_principal.geometry("1000x750")
        janela_principal.title("BancoDIP")

        # Customizando janela no geral
        header = customtkinter.CTkFrame(master=janela_principal, width=1000, height=200, fg_color="#192042")
        header.place(x=0, y=0)
        frame_saldo = customtkinter.CTkFrame(master=janela_principal, width=300, height=400, fg_color="#192042")
        frame_saldo.place(x=25, y=300)
        frame_saque = customtkinter.CTkFrame(master=janela_principal, width=300, height=400, fg_color="#192042")
        frame_saque.place(x=350, y=300)
        frame_deposito = customtkinter.CTkFrame(master=janela_principal, width=300, height=400, fg_color="#192042")
        frame_deposito.place(x=675, y=300)

        # Adição de elementos
        logo = os.path.join(os.path.dirname(__file__), 'Logo/Logo.png')
        imagem = customtkinter.CTkImage(light_image=Image.open(logo))
        label_logo = customtkinter.CTkLabel(master=header, image=imagem, text="")
        titulo_saldo = customtkinter.CTkLabel(master=frame_saldo, text="Saldo")
        texto_saldo = customtkinter.CTkLabel(master=frame_saldo, text=f"Olá, {usuario[1]}!\nSeu saldo é: R${usuario[4]:.2f}")
        titulo_saque = customtkinter.CTkLabel(master=frame_saque, text="Saque")
        texto_saque = customtkinter.CTkLabel(master=frame_saque, text="Informe um valor para sacar:")
        entry_saque = customtkinter.CTkEntry(master=frame_saque, placeholder_text="Valor")
        
        titulo_deposito = customtkinter.CTkLabel(master=frame_deposito, text="Depósito")
        texto_deposito = customtkinter.CTkLabel(master=frame_deposito, text="Informe um valor para depositar:")
        entry_deposito = customtkinter.CTkEntry(master=frame_deposito, placeholder_text="Valor")
        botao_deposito = customtkinter.CTkButton(master=frame_deposito, text="Depositar", command=lambda: Interface.depositar(usuario[0], entry_deposito.get()))

        # Customização dos elementos
        label_logo.place(x=350, y=-50)
        imagem.configure(size=(300, 300))
        
        titulo_saldo.configure(font=("Roboto", 28), text_color="white")
        titulo_saldo.place(x=90, y=30)
        texto_saldo.configure(font=("Roboto", 18), text_color="white")
        texto_saldo.place(x=35, y=90)
        
        titulo_saque.configure(font=("Roboto", 28), text_color="white")
        titulo_saque.place(x=115, y=30)
        texto_saque.configure(font=("Roboto", 18), text_color="white")
        texto_saque.place(x=35, y=90)
        entry_saque.configure(font=("Roboto", 18))
        entry_saque.place(x=35, y=170)
        
        titulo_deposito.configure(font=("Roboto", 28), text_color="white")
        titulo_deposito.place(x=90, y=30)
        texto_deposito.configure(font=("Roboto", 18), text_color="white")
        texto_deposito.place(x=35, y=90)
        entry_deposito.configure(font=("Roboto", 18))
        entry_deposito.place(x=35, y=170)
        botao_deposito.configure(font=("Roboto", 18), fg_color="#192042")
        botao_deposito.place(x=35, y=250)

    @staticmethod
    def registrar_conta():
        titular = usuario.get()
        email_val = email.get()
        senha_val = senha.get()
        saldo_inicial_str = saldoinicial.get()
        
        if saldo_inicial_str:  # Verifica se o campo de saldo inicial não está vazio
            saldo_inicial = float(saldo_inicial_str)
            conta = Conta(None, titular, email_val, senha_val, saldo_inicial, saldo_inicial * 4)
            banco = Banco()
            banco.criar_conta(conta)
            Interface.tela_login()  # Após criar a conta com sucesso, ir para a tela de login
        else:
            print("Por favor, insira um saldo inicial válido.")

    @staticmethod
    def fazer_login(email, senha):
        banco = Banco()
        usuario = banco.verificar_login(email, senha)
        if usuario:
            Interface.janela_principal(usuario)
        else:
            print("Credenciais inválidas. Por favor, tente novamente.")

    @staticmethod
    def depositar(id_usuario, valor):
        banco = Banco()
        usuario = banco.verificar_login(email, senha)
        if usuario:
            saldo_atual = usuario[4]
            novo_saldo = saldo_atual + float(valor)
            banco.depositar(id_usuario, novo_saldo)
            Interface.janela_principal(usuario)
        else:
            print("Erro ao depositar. Por favor, faça login novamente.")

# Criar janela principal
janela = customtkinter.CTk()
janela.geometry("700x400")
janela.title("BancoDIP")

# Customizando janela no geral
customtkinter.set_appearance_mode('Light')
esquerdo = customtkinter.CTkFrame(master=janela, width=340, height=400, fg_color="#192042")
esquerdo.place(x=-10, y=0)

# Adição de elementos
logo = os.path.join(os.path.dirname(__file__), 'Logo/Logo.png')
imagem = customtkinter.CTkImage(light_image=Image.open(logo))
label_logo = customtkinter.CTkLabel(master=esquerdo, image=imagem, text="")
nomebanco = customtkinter.CTkLabel(janela, text="Banco DIP")
usuario = customtkinter.CTkEntry(janela, placeholder_text="Usuario")
email = customtkinter.CTkEntry(janela, placeholder_text="Email")
senha = customtkinter.CTkEntry(janela, placeholder_text="Senha", show="*")
confirmarsenha = customtkinter.CTkEntry(janela, placeholder_text="Confirmar Senha", show="*")
saldoinicial = customtkinter.CTkEntry(janela, placeholder_text="Insira um saldo inicial")
cadastrar = customtkinter.CTkButton(janela, text="Registrar-se", command=Interface.registrar_conta)
login = customtkinter.CTkButton(janela, text="Fazer Login", command=Interface.tela_login)

# Customização dos elementos
label_logo.place(x=-87, y=-45)
imagem.configure(size=(500, 500))
nomebanco.place(x=465, y=15)
nomebanco.configure(font=("Roboto", 22, "bold"))
usuario.place(x=370, y=60)
usuario.configure(font=("Roboto", 16), width=300, height=38, border_width=0)
email.place(x=370, y=110)
email.configure(font=("Roboto", 16), width=300, height=38, border_width=0)
senha.place(x=370, y=160)
senha.configure(font=("Roboto", 16), width=300, height=38, border_width=0)
confirmarsenha.place(x=370, y=210)
confirmarsenha.configure(font=("Roboto", 16), width=300, height=38, border_width=0)
saldoinicial.place(x=370, y=260)
saldoinicial.configure(font=("Roboto", 16), width=300, height=38, border_width=0)
cadastrar.place(x=370, y=310)
cadastrar.configure(font=("Roboto", 16), width=300, height=38, fg_color="#192042")
login.place(x=370, y=350)
login.configure(font=("Roboto", 14), width=300, height=38, fg_color="#192042")

janela.mainloop()
