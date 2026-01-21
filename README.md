# 🐾 Sistema de Gestão para ONG de Adoção de Animais

Este projeto consiste no desenvolvimento de um sistema completo (Fullstack) para gerenciamento de uma ONG de proteção animal. O objetivo é facilitar o controle de animais resgatados, cadastro de adotantes e o acompanhamento dos processos de adoção.

Este trabalho foi desenvolvido como parte da disciplina de **Banco de Dados 2** do curso de **Bacharelado em Sistemas de Informação** no **IFSP (Instituto Federal de São Paulo)**.

## 🚀 Funcionalidades

O sistema permite a realização de operações CRUD (Create, Read, Update, Delete) completas para as principais entidades do negócio:

- **Gestão de Animais:** Cadastro de animais (nome, espécie, idade, histórico médico e status de adoção).
- **Gestão de Adotantes:** Registro de interessados em adotar.
- **Controle de Adoções:** Vinculação entre animais e adotantes, gerando histórico.
- **Consultas Avançadas:** Relatórios filtrados via SQL e acessíveis pela aplicação.

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17+**
- **Spring Boot:** Framework principal para criação da API REST.
- **Spring Data JPA:** Para persistência de dados e abstração do SQL.
- **Maven:** Gerenciamento de dependências.

### Banco de Dados
- **MySQL:** Sistema gerenciador de banco de dados relacional (SGBD).
- **Modelagem de Dados:** Script SQL robusto (`Query completa.sql`) com tabelas normalizadas, chaves estrangeiras e constraints.

### Frontend
- **React / HTML5 & CSS3:** Interface do usuário para interação com a API.

## 📂 Estrutura do Projeto

O repositório está organizado da seguinte forma:

```text
ONG-adocao-animais/
├── BACKEND_BD2/       # Código fonte da API Java (Spring Boot)
├── FRONTEND_BD2/      # Código fonte da interface (React/Web)
├── Query completa.sql # Script SQL para criação e população do banco
└── README.md          # Documentação do projeto
```

## ⚙️ Como Executar o Projeto

### 1. Configuração do Banco de Dados
Antes de iniciar a aplicação, você precisa preparar o banco de dados:
1. Certifique-se de ter o **MySQL** instalado e rodando.
2. Execute o script `Query completa.sql` no seu cliente SQL (Workbench, DBeaver, etc.) para criar o banco e as tabelas.

### 2. Configuração de Variáveis de Ambiente
Por questões de segurança, este projeto não contém senhas "hardcoded". Crie as variáveis de ambiente no seu sistema ou configure na sua IDE:

* **DB_URL**: `jdbc:mysql://localhost:3306/Projeto` (ajuste se necessário)
* **DB_USER**: Seu usuário do MySQL (ex: `root`)
* **DB_PASSWORD**: Sua senha do MySQL

### 3. Executando o Backend
1. Navegue até a pasta `BACKEND_BD2`.
2. Execute o comando Maven para baixar as dependências e rodar:

```bash
mvn spring-boot:run
```

O servidor iniciará na porta `8080` (padrão).

## 📄 Licença

Este projeto é de uso acadêmico. Sinta-se à vontade para realizar forks e contribuir.

---
Desenvolvido por **Thiago Galvão**
