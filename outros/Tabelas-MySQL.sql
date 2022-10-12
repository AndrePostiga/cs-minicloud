DROP TABLE banco.movimento;
DROP TABLE banco.conta;

CREATE TABLE banco.conta (
  id INT NOT NULL AUTO_INCREMENT,
  saldo DECIMAL(8, 2) NOT NULL,
  data_cadastro DATE NOT NULL,
  PRIMARY KEY (id)
)
ENGINE = INNODB
CHARACTER SET utf8mb4;

CREATE TABLE banco.movimento (
  id                INT NOT NULL AUTO_INCREMENT,
  valor             DECIMAL(10, 2) NOT NULL,
  data_criacao      DATE NOT NULL,
  conta_id          INT NOT NULL,
  tipo              CHAR(1) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT MOVIMENTO_CONTA_FK 
  FOREIGN KEY (conta_id)
  REFERENCES banco.conta(id) 
  ON DELETE NO ACTION ON UPDATE RESTRICT
)
ENGINE = INNODB
CHARACTER SET utf8mb4;

INSERT INTO conta(SALDO, DATA_CADASTRO)
VALUES(3000, curdate());

INSERT INTO conta(SALDO, DATA_CADASTRO)
VALUES(5000, curdate());

