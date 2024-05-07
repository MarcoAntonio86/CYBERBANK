document.addEventListener('DOMContentLoaded', function() {
  const cadastroForm = document.getElementById('cadastroForm');
  if (cadastroForm) {
    cadastroForm.addEventListener('submit', cadastrar);
  }

  const loginForm = document.getElementById('loginForm');
  if (loginForm) {
    loginForm.addEventListener('submit', login);
  }
});

function cadastrar(event) {
  event.preventDefault();

  const nome = document.getElementById('nome').value;
  const email = document.getElementById('email').value;
  const senha = document.getElementById('senha').value;
  const depositoInicial = document.getElementById('depositoInicial').value;

  const usuario = {
    nome: nome,
    senha: senha,
    depositoInicial: depositoInicial
  };

  localStorage.setItem(email, JSON.stringify(usuario));
  localStorage.setItem('emailLogado', email); // Adiciona o email do usuário logado ao localStorage
  alert('Usuário cadastrado com sucesso');
  window.location.href = '/BANCO/index.html'; // Redireciona para a página do banco
}

function login(event) {
  event.preventDefault();

  const email = document.getElementById('email').value;
  const senha = document.getElementById('senha').value;

  const userData = localStorage.getItem(email);
  if (userData) {
    const usuario = JSON.parse(userData);
    if (senha === usuario.senha) {
      localStorage.setItem('emailLogado', email); // Adiciona o email do usuário logado ao localStorage
      alert('Login realizado com sucesso');
      window.location.href = '/BANCO/index.html'; // Redireciona para a página do banco
    } else {
      alert('Senha incorreta');
    }
  } else {
    alert('Usuário não encontrado');
  }
}
