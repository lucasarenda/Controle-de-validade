CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       data_cadastro TIMESTAMP NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       nome VARCHAR(255) NOT NULL,
                       senha VARCHAR(255) NOT NULL
);

CREATE TABLE estabelecimentos (
                                  id UUID PRIMARY KEY,
                                  cnpj VARCHAR(255) NOT NULL UNIQUE,
                                  email VARCHAR(255) NOT NULL,
                                  nome VARCHAR(255) NOT NULL,
                                  telefone VARCHAR(255) NOT NULL,
                                  endereco VARCHAR(255) NOT NULL,

                                  user_id UUID NOT NULL,

                                  CONSTRAINT fk_estabelecimentos_users
                                      FOREIGN KEY (user_id)
                                          REFERENCES users(id)
);

CREATE TABLE produtos (
                          id UUID PRIMARY KEY,
                          categoria VARCHAR(255) NOT NULL,
                          data_cadastro TIMESTAMP NOT NULL,
                          descricao VARCHAR(255) NOT NULL,
                          marca VARCHAR(255) NOT NULL,
                          nome VARCHAR(255) NOT NULL,

                          estabelecimento_id UUID NOT NULL,

                          CONSTRAINT fk_produtos_estabelecimentos
                              FOREIGN KEY (estabelecimento_id)
                                  REFERENCES estabelecimentos(id)
);

CREATE TABLE lote (
                      id UUID PRIMARY KEY,
                      custo_unitario NUMERIC(10, 2) NOT NULL,
                      data_entrada DATE NOT NULL,
                      data_validade DATE NOT NULL,
                      endereco VARCHAR(255) NOT NULL,
                      numero_lote VARCHAR(255) NOT NULL,
                      quantidade INTEGER NOT NULL,

                      produto_id UUID NOT NULL,

                      CONSTRAINT fk_lote_produtos
                          FOREIGN KEY (produto_id)
                              REFERENCES produtos(id)
);
