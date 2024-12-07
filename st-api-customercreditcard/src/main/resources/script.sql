-- script criação das Tabelas

CREATE TABLE sistema.cartao (
    id BIGINT NOT NULL PRIMARY KEY,
    ativo BOOLEAN,
    bandeira VARCHAR(255),
    bloqueado BOOLEAN,
    codigoseguranca NUMERIC(19, 2),
    entreguevalidado BOOLEAN,
    limitecredito NUMERIC(19, 2),
    nometitular VARCHAR(255),
    numerocartao VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    conta_id BIGINT,
    pessoa_id BIGINT
);

CREATE TABLE sistema.cartaopessoa (
    id BIGINT NOT NULL PRIMARY KEY,
    limite_minimo NUMERIC(19, 2),
    nropedidocartao INTEGER,
    cartao_id BIGINT,
    pessoa_id BIGINT
);

CREATE TABLE sistema.conta (
    id BIGINT NOT NULL PRIMARY KEY,
    created_at TIMESTAMP,
    last_modified_date TIMESTAMP,
    update_at TIMESTAMP,
    agencia INTEGER,
    inativa BOOLEAN,
    limitecredito NUMERIC(19, 2),
    numero INTEGER,
    saldo NUMERIC(19, 2),
    pessoa_id BIGINT
);

CREATE TABLE sistema.endereco (
    id BIGINT NOT NULL PRIMARY KEY,
    created_at TIMESTAMP,
    last_modified_date TIMESTAMP,
    update_at TIMESTAMP,
    bairro VARCHAR(255) NOT NULL,
    complemento VARCHAR(255),
    logradouro VARCHAR(255),
    numero VARCHAR(255) NOT NULL,
    tipo_endereco VARCHAR(255)
);

CREATE TABLE sistema.fisica (
    id BIGINT NOT NULL PRIMARY KEY,
    num_cpf NUMERIC(19, 2) NOT NULL,
    rg VARCHAR(255),
    pessoa_id BIGINT
);

CREATE TABLE sistema.juridica (
    id BIGINT NOT NULL PRIMARY KEY,
    num_cnpj NUMERIC(19, 2),
    pessoa_id BIGINT
);

CREATE TABLE sistema.pessoa (
    id BIGINT NOT NULL PRIMARY KEY,
    created_at TIMESTAMP,
    last_modified_date TIMESTAMP,
    update_at TIMESTAMP,
    datanascimento TIMESTAMP,
    nome VARCHAR(255),
    tipopessoa VARCHAR(255),
    endereco_id BIGINT NOT NULL
);

-- Restrições de chave estrangeira

ALTER TABLE sistema.cartao
ADD CONSTRAINT FK_cartao_conta
FOREIGN KEY (conta_id) REFERENCES sistema.conta;

ALTER TABLE sistema.cartao
ADD CONSTRAINT FK_cartao_pessoa
FOREIGN KEY (pessoa_id) REFERENCES sistema.pessoa;

ALTER TABLE sistema.cartaopessoa
ADD CONSTRAINT FK_cartaopessoa_cartao
FOREIGN KEY (cartao_id) REFERENCES sistema.cartao;

ALTER TABLE sistema.cartaopessoa
ADD CONSTRAINT FK_cartaopessoa_pessoa
FOREIGN KEY (pessoa_id) REFERENCES sistema.pessoa;

ALTER TABLE sistema.conta
ADD CONSTRAINT FK_conta_pessoa
FOREIGN KEY (pessoa_id) REFERENCES sistema.pessoa;

ALTER TABLE sistema.fisica
ADD CONSTRAINT FK_fisica_pessoa
FOREIGN KEY (pessoa_id) REFERENCES sistema.pessoa;

ALTER TABLE sistema.juridica
ADD CONSTRAINT FK_juridica_pessoa
FOREIGN KEY (pessoa_id) REFERENCES sistema.pessoa;

ALTER TABLE sistema.pessoa
ADD CONSTRAINT FK_pessoa_endereco
FOREIGN KEY (endereco_id) REFERENCES sistema.endereco;
