-- Script de criação do banco de dados PostgreSQL para Hermes Comercial PDV
-- Execute este script no PostgreSQL para criar o banco e as tabelas

-- Criar banco de dados (execute manualmente se necessário)
-- CREATE DATABASE hermescomercialdb;
-- \c hermescomercialdb;

-- Tabela de Usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    login VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    cargo VARCHAR(100),
    nivel_acesso VARCHAR(50),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acesso TIMESTAMP,
    ativo BOOLEAN DEFAULT true,
    permissao_venda BOOLEAN DEFAULT true,
    permissao_caixa BOOLEAN DEFAULT false,
    permissao_relatorio BOOLEAN DEFAULT false,
    tipo_usuario VARCHAR(20) DEFAULT 'FUNCIONARIO',
    
    -- Campos específicos de Funcionário
    matricula VARCHAR(50),
    departamento VARCHAR(100),
    salario DECIMAL(10,2),
    data_admissao DATE,
    data_demissao VARCHAR(50),
    
    -- Campos específicos de Cliente
    cpf VARCHAR(14),
    data_nascimento DATE,
    telefone VARCHAR(20),
    endereco VARCHAR(255),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(10),
    limite_credito DECIMAL(10,2) DEFAULT 0.00,
    pontos_fidelidade INTEGER DEFAULT 0,
    
    -- Campos específicos de Fornecedor
    cnpj VARCHAR(18),
    razao_social VARCHAR(255),
    nome_fantasia VARCHAR(255),
    inscricao_estadual VARCHAR(20),
    telefone_contato VARCHAR(20),
    email_contato VARCHAR(255),
    endereco_fornecedor VARCHAR(255),
    condicoes_pagamento TEXT,
    prazo_entrega INTEGER
);

-- Tabela de Produtos
CREATE TABLE IF NOT EXISTS produto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    categoria VARCHAR(100),
    sub_categoria VARCHAR(100),
    codigo VARCHAR(50) UNIQUE,
    marca VARCHAR(100),
    data_compra DATE,
    preco DECIMAL(10,2),
    preco_compra DECIMAL(10,2),
    estoque INTEGER DEFAULT 0,
    estoque_minimo INTEGER DEFAULT 5,
    ativo BOOLEAN DEFAULT true
);

-- Tabela de Vendas
CREATE TABLE IF NOT EXISTS venda (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cliente_id INTEGER REFERENCES usuarios(id),
    cliente_nome VARCHAR(255),
    usuario_id INTEGER REFERENCES usuarios(id),
    usuario_nome VARCHAR(255),
    valor_total DECIMAL(10,2) DEFAULT 0.00,
    valor_desconto DECIMAL(10,2) DEFAULT 0.00,
    valor_acrescimo DECIMAL(10,2) DEFAULT 0.00,
    valor_final DECIMAL(10,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'ABERTA',
    tipo_pagamento VARCHAR(50),
    observacoes TEXT,
    cancelada BOOLEAN DEFAULT false
);

-- Tabela de Itens de Venda
CREATE TABLE IF NOT EXISTS item_venda (
    id SERIAL PRIMARY KEY,
    venda_id INTEGER REFERENCES venda(id),
    produto_id INTEGER REFERENCES produto(id),
    produto_codigo VARCHAR(50),
    produto_descricao VARCHAR(255),
    quantidade INTEGER NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    desconto DECIMAL(10,2) DEFAULT 0.00
);

-- Tabela de Pagamentos
CREATE TABLE IF NOT EXISTS pagamento (
    id SERIAL PRIMARY KEY,
    venda_id INTEGER REFERENCES venda(id),
    forma_pagamento VARCHAR(50),
    valor DECIMAL(10,2) NOT NULL,
    valor_recebido DECIMAL(10,2),
    troco DECIMAL(10,2) DEFAULT 0.00,
    numero_parcelas VARCHAR(20),
    bandeira_cartao VARCHAR(50),
    autorizacao_cartao VARCHAR(50),
    chave_pix VARCHAR(255),
    numero_cheque VARCHAR(50),
    banco_cheque VARCHAR(100),
    data_pagamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDENTE',
    observacoes TEXT
);

-- Tabela de Movimento de Caixa
CREATE TABLE IF NOT EXISTS movimento_caixa (
    id SERIAL PRIMARY KEY,
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INTEGER REFERENCES usuarios(id),
    usuario_nome VARCHAR(255),
    tipo_movimento VARCHAR(20) NOT NULL, -- ABERTURA, FECHAMENTO, SANGRIA, SUPRIMENTO, VENDA, CANCELAMENTO
    valor DECIMAL(10,2) NOT NULL,
    saldo_anterior DECIMAL(10,2) DEFAULT 0.00,
    saldo_atual DECIMAL(10,2) DEFAULT 0.00,
    descricao VARCHAR(255),
    observacoes TEXT
);

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_usuarios_login ON usuarios(login);
CREATE INDEX IF NOT EXISTS idx_usuarios_tipo ON usuarios(tipo_usuario);
CREATE INDEX IF NOT EXISTS idx_usuarios_ativos ON usuarios(ativo);
CREATE INDEX IF NOT EXISTS idx_produto_codigo ON produto(codigo);
CREATE INDEX IF NOT EXISTS idx_produto_nome ON produto(nome);
CREATE INDEX IF NOT EXISTS idx_produto_categoria ON produto(categoria);
CREATE INDEX IF NOT EXISTS idx_venda_data ON venda(data_hora);
CREATE INDEX IF NOT EXISTS idx_venda_codigo ON venda(codigo);
CREATE INDEX IF NOT EXISTS idx_venda_cliente ON venda(cliente_id);
CREATE INDEX IF NOT EXISTS idx_venda_usuario ON venda(usuario_id);
CREATE INDEX IF NOT EXISTS idx_item_venda_venda ON item_venda(venda_id);
CREATE INDEX IF NOT EXISTS idx_item_venda_produto ON item_venda(produto_id);
CREATE INDEX IF NOT EXISTS idx_pagamento_venda ON pagamento(venda_id);
CREATE INDEX IF NOT EXISTS idx_movimento_caixa_data ON movimento_caixa(data_hora);
CREATE INDEX IF NOT EXISTS idx_movimento_caixa_usuario ON movimento_caixa(usuario_id);
CREATE INDEX IF NOT EXISTS idx_movimento_caixa_tipo ON movimento_caixa(tipo_movimento);

-- Inserir dados iniciais
BEGIN;

INSERT INTO usuarios (nome, login, senha, cargo, nivel_acesso, tipo_usuario, permissao_venda, permissao_caixa, permissao_relatorio) 
VALUES ('Administrador', 'admin', 'admin123', 'Administrador', 'ADMIN', 'FUNCIONARIO', true, true, true)
ON CONFLICT (login) DO NOTHING;

INSERT INTO usuarios (nome, login, senha, cargo, nivel_acesso, tipo_usuario, permissao_venda, permissao_caixa, permissao_relatorio) 
VALUES ('Operador Caixa', 'caixa', 'caixa123', 'Operador de Caixa', 'OPERADOR', 'FUNCIONARIO', true, true, false)
ON CONFLICT (login) DO NOTHING;

-- Inserir produtos de exemplo
INSERT INTO produto (nome, categoria, codigo, marca, preco, preco_compra, estoque, estoque_minimo, ativo) VALUES
('Arroz 5kg', 'Alimentos', '001', 'Tio João', 25.90, 18.50, 50, 10, true),
('Feijão 1kg', 'Alimentos', '002', 'Carioca', 8.50, 6.20, 100, 20, true),
('Óleo de Soja 900ml', 'Alimentos', '003', 'Soya', 7.90, 5.80, 80, 15, true),
('Açúcar 1kg', 'Alimentos', '004', 'União', 5.20, 3.90, 120, 25, true),
('Café 500g', 'Alimentos', '005', 'Pilão', 15.60, 11.80, 60, 12, true),
('Refrigerante 2L', 'Bebidas', '006', 'Coca-Cola', 8.90, 6.50, 40, 8, true),
('Água Mineral 1.5L', 'Bebidas', '007', 'Crystal', 2.50, 1.80, 150, 30, true),
('Sabão em Pó 1kg', 'Limpeza', '008', 'Omo', 12.90, 9.60, 70, 14, true),
('Detergente 500ml', 'Limpeza', '009', 'Ypê', 3.20, 2.40, 200, 40, true),
('Papel Higiênico 4 rolos', 'Higiene', '010', 'Neve', 9.80, 7.20, 90, 18, true)
ON CONFLICT (codigo) DO NOTHING;

COMMIT;
