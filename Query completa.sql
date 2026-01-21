Create database Projeto;
USE Projeto;

CREATE TABLE tb_usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,  
    nome VARCHAR(100) NOT NULL,                
    senha VARCHAR(50) NOT NULL,               
    email VARCHAR(100) NOT NULL,              
    tipo_usuario ENUM('administrador', 'comum', 'funcionario') NOT NULL 
);

-- Alterando uma tabela colocando uma nova coluna
ALTER TABLE tb_usuario
ADD COLUMN cpf varchar(100);

 

Create  table tb_animal(
	id_animal INT auto_increment PRIMARY KEY,
    nome_animal VARCHAR(100) NOT NULL,
    peso int,
    porte enum('Pequeno','Médio','Grande'),
    raca varchar(100),
    status boolean,
    especie varchar(100),
    idade int,
    sexo enum('F','M'),
    vermifugado boolean,
    vacinado_gripe boolean,
    doenca varchar(200),
    id_usuario int,
    Foreign key (id_usuario) references tb_usuario(id_usuario)
    );
    
   
CREATE table tb_evento(
    id_evento int auto_increment primary key,
    nome_evento varchar(200),
    data_evento date,
    descricao varchar(500),
    id_usuario int,
    foreign key (id_usuario) references tb_usuario(id_usuario)
    
    );
    
 
    Create table tb_voluntario (
    id_voluntario int auto_increment primary key,
    nome_voluntario varchar(100),
    RG char(9),
    CPF char(11),
    atividade varchar(200),
    dias_disponiveis varchar(150),
    id_usuario int,
    Foreign key (id_usuario) references tb_usuario(id_usuario)
    );
    
   
    Create table tb_portal(
    id_portal int auto_increment primary key,
    data_registro date,
    descricao varchar(500),
    id_usuario int,
    foreign key (id_usuario) references tb_usuario(id_usuario)
    );
    
    CREATE TABLE tb_adocao (
    id_adocao INT AUTO_INCREMENT PRIMARY KEY, -- opcional, mas recomendado
    data_adocao DATE,
    id_usuario INT,
    id_animal INT,
    FOREIGN KEY (id_usuario) REFERENCES tb_usuario(id_usuario),
    FOREIGN KEY (id_animal) REFERENCES tb_animal(id_animal)
);

    


 -- Criando uma view para mostrar o animal e o dono do animal juntos em uma tabela   
  CREATE VIEW mostrar_usuario_animal AS
	SELECT u.nome, u.email,u.tipo_usuario,a.nome_animal,a.raca,a.doenca,a.vacinado_gripe,a.vermifugado
	FROM tb_usuario AS u
	JOIN tb_animal AS a 
	ON u.id_usuario = a.id_usuario;
    
  
  -- Criando uma trigger que antes de inserir eu vejo se já tem um cpf cadastrado no sistema
  -- se sim, eu exibo uma mensagem de erro 
  
DELIMITER $$ 
-- ⬆️ Define o delimitador temporário como $$

CREATE TRIGGER trg_verificar_cpf
BEFORE INSERT ON tb_usuario
FOR EACH ROW
BEGIN
    -- Verifica se já existe um CPF igual ao novo registro (NEW.cpf)
    IF EXISTS (SELECT 1 FROM tb_usuario WHERE cpf = NEW.cpf) THEN
        -- Se existir, impede a inserção e exibe a mensagem de erro
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'CPF já cadastrado no sistema!';
    END IF;
END$$ 

DELIMITER ;


DELIMITER ;

-- Criando uma stored procedure que receba o nome do voluntário e mostre que atividade ele faz e 
-- que dias ele está disponivel 

DELIMITER $$

CREATE PROCEDURE sp_mostrar_atividade_voluntario (IN nome_voluntario VARCHAR(100)) 
BEGIN
    SELECT atividade, dias_disponiveis
    FROM tb_voluntario
    WHERE nome_voluntario = nome_voluntario;
END $$

DELIMITER ;

-- Criar function para saber quantos animais um usuário possui 

DELIMITER $$

CREATE FUNCTION fn_qtd_animais(p_id_usuario INT)
RETURNS INT
DETERMINISTIC -- função sempre devolve o mesmo resultado se receber os mesmos dados
BEGIN
    DECLARE total_animais INT;

    SELECT COUNT(*) INTO total_animais
    FROM tb_animal
    WHERE id_usuario = p_id_usuario;

    RETURN total_animais;
END $$

DELIMITER ;

INSERT INTO tb_usuario (nome, senha, email, tipo_usuario, cpf) 
VALUES ('João da Silva', 'senha123', 'joao.silva@teste.com', 'comum', '12345678901');

INSERT INTO tb_usuario (nome, senha, email, tipo_usuario, cpf) 
VALUES ('admin', 'admin', 'admin@teste.com', 'administrador', '45061235819');

select * from tb_usuario;

ALTER TABLE tb_usuario
ADD CONSTRAINT uc_email UNIQUE (email);

ALTER TABLE tb_animal
DROP FOREIGN KEY tb_animal_ibfk_1;

-- Remove a chave estrangeira (se existir uma restrição nomeada, caso contrário, o DROP COLUMN faz o trabalho)
ALTER TABLE tb_animal DROP COLUMN id_usuario;

-- 1. Remove a coluna genérica
ALTER TABLE tb_portal DROP COLUMN descricao;

-- 2. Adiciona as colunas monetárias
ALTER TABLE tb_portal ADD COLUMN valor_racao DECIMAL(10, 2);
ALTER TABLE tb_portal ADD COLUMN valor_agua DECIMAL(10, 2);
ALTER TABLE tb_portal ADD COLUMN valor_vacina DECIMAL(10, 2);
-- Adiciona a coluna status com um valor padrão
ALTER TABLE tb_adocao
ADD COLUMN status ENUM('Em Andamento', 'Deferido', 'Indeferido') DEFAULT 'Em Andamento';

-- NOTA: Execute estes comandos antes de recompilar o Java.

DELIMITER $$

-- 1. TRIGGER DE VALIDAÇÃO (Impede adoção de animal indisponível)
CREATE TRIGGER trg_validar_disponibilidade_antes_insert
BEFORE INSERT ON tb_adocao
FOR EACH ROW
BEGIN
    DECLARE animal_disponivel BOOLEAN;

    -- Busca o status atual do animal
    SELECT status INTO animal_disponivel 
    FROM tb_animal 
    WHERE id_animal = NEW.id_animal;

    -- Se status for FALSE (0), cancela a operação e lança erro
    IF animal_disponivel = FALSE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Este animal não está disponível para adoção.';
    END IF;
END$$


-- 2. TRIGGER DE AUTOMAÇÃO NO INSERT (Muda status do animal para indisponível)
CREATE TRIGGER trg_atualizar_status_animal_apos_insert
AFTER INSERT ON tb_adocao
FOR EACH ROW
BEGIN
    -- Se criou como 'Em Andamento' ou 'Deferido', bloqueia o animal
    IF NEW.status IN ('Em Andamento', 'Deferido') THEN
        UPDATE tb_animal SET status = FALSE WHERE id_animal = NEW.id_animal;
    END IF;
END$$


-- 3. TRIGGER DE AUTOMAÇÃO NO UPDATE (Libera animal se indeferido)
CREATE TRIGGER trg_atualizar_status_animal_apos_update
AFTER UPDATE ON tb_adocao
FOR EACH ROW
BEGIN
    -- Se mudou para Indeferido, libera o animal (TRUE)
    IF NEW.status = 'Indeferido' THEN
        UPDATE tb_animal SET status = TRUE WHERE id_animal = NEW.id_animal;
    
    -- Se mudou para Deferido ou Em Andamento, garante que fique indisponível (FALSE)
    ELSEIF NEW.status IN ('Deferido', 'Em Andamento') THEN
        UPDATE tb_animal SET status = FALSE WHERE id_animal = NEW.id_animal;
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_liberar_animal_apos_delete_adocao
AFTER DELETE ON tb_adocao
FOR EACH ROW
BEGIN
    -- Acessamos os dados da linha excluída usando 'OLD'
    -- Se uma adoção é removida, o animal (OLD.id_animal) volta a ser 'true' (disponível)
    UPDATE tb_animal 
    SET status = TRUE 
    WHERE id_animal = OLD.id_animal;
END$$

DELIMITER ;