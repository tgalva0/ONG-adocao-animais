// script.js
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formCadastro");

  // Se não encontrar o form, aborta (ajuda a detectar se o script foi carregado cedo)
  if (!form) {
    console.error("Formulário #formCadastro não encontrado.");
    return;
  }

  form.addEventListener("submit", function (event) {
    event.preventDefault(); // evita envio/recarregamento

    // pegar valores
    const nome = document.getElementById("nome").value.trim();
    const cpf = document.getElementById("cpf").value.trim();
    const email = document.getElementById("email").value.trim();
    const telefone = document.getElementById("telefone").value.trim();
    const rua = document.getElementById("rua").value.trim();
    const cidade = document.getElementById("cidade").value.trim();
    const estado = document.getElementById("estado").value;
    const complemento = document.getElementById("complemento").value.trim();
    const cep = document.getElementById("cep").value.trim();
    const senha = document.getElementById("senha").value;
    const confirmaSenha = document.getElementById("confirma-senha").value;

    // lista de erros
    const erros = [];

    // checar campos obrigatórios
    if (!nome) erros.push("Nome é obrigatório.");
    if (!email) erros.push("E-mail é obrigatório.");
    if (!rua) erros.push("Rua é obrigatória.");
    if (!cidade) erros.push("Cidade é obrigatória.");
    if (!cep) erros.push("CEP é obrigatório.");
    if (!senha) erros.push("Senha é obrigatória.");
    if (!confirmaSenha) erros.push("Confirmação de senha é obrigatória.");

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (email && !emailRegex.test(email)) erros.push("E-mail inválido.");

    // senha
    if (senha && senha.length < 6) erros.push("Senha deve ter pelo menos 6 caracteres.");
    if (senha && confirmaSenha && senha !== confirmaSenha) erros.push("As senhas não são iguais.");

    // CEP simples: 5 dígitos + opcionalmente - + 3
    const cepRegex = /^\d{5}-?\d{3}$/;
    if (cep && !cepRegex.test(cep)) erros.push("CEP inválido (formato 00000-000).");

    // CPF simples: apenas checagem de formato (não cálculo de dígitos)
    const cpfRegex = /^\d{3}\.?\d{3}\.?\d{3}-?\d{2}$/;
    if (cpf && !cpfRegex.test(cpf)) erros.push("CPF inválido (formato 000.000.000-00).");

    if (erros.length > 0) {
      
      alert("Por favor corrija os seguintes erros:\n\n- " + erros.join("\n- "));
      
      const primeiroErro = erros[0];
      if (primeiroErro.includes("Nome")) document.getElementById("nome").focus();
      else if (primeiroErro.includes("E-mail")) document.getElementById("email").focus();
      else if (primeiroErro.includes("Rua")) document.getElementById("rua").focus();
      else if (primeiroErro.includes("Cidade")) document.getElementById("cidade").focus();
      else if (primeiroErro.includes("CEP")) document.getElementById("cep").focus();
      else if (primeiroErro.includes("Senha")) document.getElementById("senha").focus();
      return; 
    }

    // Se chegou aqui, está tudo ok: montar mensagem/resumo
    const resumo =
      `Nome: ${nome}\n` +
      `CPF: ${cpf}\n` +
      `E-mail: ${email}\n` +
      `Telefone: ${telefone}\n` +
      `Endereço: ${rua}${complemento ? ", " + complemento : ""} - ${cidade}/${estado}\n` +
      `CEP: ${cep}`;

    

    
    const modalEl = document.getElementById("resumoModal");
    if (modalEl) {
      // preenche o conteúdo do modal
      const modalBody = modalEl.querySelector(".modal-body");
      const modalTitle = modalEl.querySelector(".modal-title");
      if (modalTitle) modalTitle.textContent = "Resumo do Cadastro";
      if (modalBody) modalBody.textContent = resumo;

      // usa API do bootstrap para mostrar o modal
      const bsModal = new bootstrap.Modal(modalEl);
      bsModal.show();
    } else {
    
      alert("Cadastro válido!\n\n" + resumo);
    }

   
  });
});


document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formContato");

  form.addEventListener("submit", (event) => {
    event.preventDefault(); // evita recarregar página

    const nome = document.getElementById("nome").value.trim();
    const email = document.getElementById("email").value.trim();
    const telefone = document.getElementById("telefone").value.trim();
    const assunto = document.querySelector("select[name='assunto']").value;
    const descricao = document.getElementById("descricao").value.trim();

    let erros = [];

    // Validações
    if (!nome) erros.push("O campo NOME é obrigatório.");
    if (!email) erros.push("O campo E-MAIL é obrigatório.");
    if (!telefone) erros.push("O campo TELEFONE é obrigatório.");
    if (!assunto) erros.push("Selecione um ASSUNTO.");
    if (!descricao) erros.push("A DESCRIÇÃO não pode ficar vazia.");

    // Regex simples para email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (email && !emailRegex.test(email)) {
      erros.push("Digite um e-mail válido.");
    }

    // Regex simples para telefone (mínimo 10 dígitos)
    const telRegex = /^\d{10,11}$/;
    const telLimpo = telefone.replace(/\D/g, "");
    if (telLimpo && !telRegex.test(telLimpo)) {
      erros.push("Digite um telefone válido com DDD.");
    }

    // Exibe erros
    if (erros.length > 0) {
      alert("⚠️ Corrija os seguintes erros:\n\n" + erros.join("\n"));
      return;
    }

    // Se estiver tudo certo ➜ monta resumo:
    const resumo = `
Nome: ${nome}
E-mail: ${email}
Telefone: ${telefone}
Assunto: ${assunto}
Mensagem: ${descricao}
    `;

    alert("✔️ Formulário enviado com sucesso!\n\n" + resumo);

    // Você pode limpar o formulário se quiser:
    // form.reset();
  });
});

// script.js
document.addEventListener("DOMContentLoaded", () => {
  const tbody = document.getElementById("tabelaDados");
  if (!tbody) return console.error("Elemento #tabelaDados não encontrado.");

  function formatBRL(valor) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);
  }
});
