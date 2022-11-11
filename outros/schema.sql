DROP DATABASE IF EXISTS banco;
CREATE DATABASE banco;

CREATE TABLE banco.produto (
     id INT(11) NOT NULL AUTO_INCREMENT,
     nome VARCHAR(30) NOT NULL,
     lance_minimo DECIMAL(8, 2) NOT NULL,
     data_cadastro DATE NOT NULL,
     data_venda DATE DEFAULT NULL,
     versao INT(11) DEFAULT 0,
     PRIMARY KEY (id)
)
ENGINE = INNODB
CHARACTER SET utf8mb4;