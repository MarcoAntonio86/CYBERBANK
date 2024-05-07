function validation(){
    if(document.CadastroLogin.Nome.value==""){
        document.getElementById("resultado").innerHTML="Insira nome de usuário";
        return false;
    }
    else if(document.CadastroLogin.Nome.value.length<6){
        document.getElementById("resultado").innerHTML="Pelo menos seis caracteres";
        return false
    }
    else if(document.CadastroLogin.Email.value==""){
        document.getElementById("resultado").innerHTML="Insira o Email";
        return false;
    }
    else if(document.CadastroLogin.Senha.value==""){
        document.getElementById("resultado").innerHTML="Insira a senha";
        return false;
    }
    else if(document.CadastroLogin.Senha.value.length<6){
        document.getElementById("resultado").innerHTML="A senha deve ter 6 dígitos";
        return false
    }
    else if(document.CadastroLogin.cSenha.value==""){
        document.getElementById("resultado").innerHTML="Insira a senha novamente";
        return false;
    }
    else if(document.CadastroLogin.Senha.value!==document.CadastroLogin.cSenha.value){
        document.getElementById("resultado").innerHTML="Suas senhas não correspondem";
        return false;
    }
    else if(document.CadastroLogin.depositoInicial.value==""){
        document.getElementById("resultado").innerHTML="Insira o depósito inicial";
        return false;
    }
    else if(document.CadastroLogin.Senha.value == document.CadastroLogin.cSenha.value){
        loginConfirmado.classList.add("fecharConfirmacao")
        return false;
    }
    
}

var loginConfirmado=document.getElementById('loginConfirmado')
function CloseSlide(){
    loginConfirmado.classList.remove("fecharConfirmacao")
}