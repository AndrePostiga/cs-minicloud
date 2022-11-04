https://dev.mysql.com/downloads/

Download MySQL Community Server

�ltimas vers�es:
- 5.5
- 5.6
- 5.7
- 8.0.12 (atual)

DROP TABLE produto02;

CREATE TABLE banco.produto02 (
                                 id INT(11) NOT NULL AUTO_INCREMENT,
                                 nome VARCHAR(30) NOT NULL,
                                 lance_minimo DECIMAL(8, 2) NOT NULL,
                                 data_cadastro DATE NOT NULL,
                                 data_venda DATE DEFAULT NULL,
                                 versao INT(11) DEFAULT 0,
                                 PRIMARY KEY (id)
)
    ENGINE = INNODB
CHARACTER SET utf8
COLLATE utf8_general_ci;

INSERT INTO produto02(NOME, LANCE_MINIMO, DATA_CADASTRO)
VALUES('TV SAMSUNG 20 POL', 2000, curdate());

INSERT INTO produto02(NOME, LANCE_MINIMO, DATA_CADASTRO)
VALUES('TV SAMSUNG 22 POL', 2500, curdate());

