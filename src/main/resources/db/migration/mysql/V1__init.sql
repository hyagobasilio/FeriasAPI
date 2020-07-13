CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    nome VARCHAR(200) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    password VARCHAR(255),
    PRIMARY  KEY(id)
);


CREATE TABLE IF NOT EXISTS equipes (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    nome VARCHAR(200) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS enderecos (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    rua VARCHAR(200) NOT NULL,
    numero VARCHAR(5),
    complemento VARCHAR(200),
    bairro VARCHAR(200),
    cidade VARCHAR(200),
    estado VARCHAR(200),
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS funcionarios (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    nome VARCHAR(200) NOT NULL,
    data_nascimento DATE,
    endereco_id BIGINT  UNSIGNED,
    data_contratacao DATE,
    avatar VARCHAR(255),
    equipe_id BIGINT UNSIGNED,
    matricula VARCHAR(100),
    PRIMARY KEY(id)
);

ALTER TABLE funcionarios
ADD CONSTRAINT fk_funcionarios_equipe_id
FOREIGN KEY (equipe_id) REFERENCES equipes(id);

ALTER TABLE funcionarios
ADD CONSTRAINT fk_funcionarios_endereco_id
FOREIGN KEY (endereco_id) REFERENCES enderecos(id);

CREATE TABLE IF NOT EXISTS ferias (
    id BIGINT UNSIGNED AUTO_INCREMENT,
    funcionario_id BIGINT UNSIGNED NOT NULL,
    inicio DATE NOT NULL,
    fim DATE NOT NULL,
    PRIMARY KEY(id)
);

ALTER TABLE ferias
ADD CONSTRAINT fk_ferias_funcionario_id
FOREIGN KEY (funcionario_id) REFERENCES funcionarios(id);