-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS sistema_emprestimos;
USE sistema_emprestimos;

-- Tabela de usuários (agiotas e clientes)
CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo_usuario ENUM('AGIOTA', 'CLIENTE') NOT NULL,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultimo_acesso DATETIME,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de clientes
CREATE TABLE clientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(15),
    endereco TEXT,
    score_credito INT,
    limite_credito DECIMAL(10,2),
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Tabela de empréstimos
CREATE TABLE emprestimos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT,
    valor_principal DECIMAL(10,2),
    taxa_juros DECIMAL(5,4),
    tipo_juros ENUM('SIMPLES', 'COMPOSTO'),
    prazo INT,
    multa DECIMAL(10,2) DEFAULT 0,
    valor_total DECIMAL(10,2),
    parcelas_pagas INT DEFAULT 0, 
    status VARCHAR(20) DEFAULT 'PENDENTE',
    data_solicitacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_vencimento DATE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabela de pagamentos
CREATE TABLE pagamentos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    emprestimo_id INT,
    valor DECIMAL(10,2) NOT NULL,
    data_pagamento DATETIME DEFAULT CURRENT_TIMESTAMP,
    forma_pagamento ENUM('DINHEIRO', 'PIX', 'CARTAO', 'BOLETO') NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMADO',
    FOREIGN KEY (emprestimo_id) REFERENCES emprestimos(id)
);

-- Tabela de notificações
CREATE TABLE notificacoes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT,
    titulo VARCHAR(100) NOT NULL,
    mensagem TEXT NOT NULL,
    tipo ENUM('ALERTA', 'COBRANCA', 'SISTEMA', 'OUTRO') NOT NULL,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    lida BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabela de configurações do sistema
CREATE TABLE configuracoes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    chave VARCHAR(50) UNIQUE NOT NULL,
    valor TEXT,
    descricao TEXT,
    data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela de histórico de ações
CREATE TABLE historico_acoes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT,
    acao VARCHAR(100) NOT NULL,
    descricao TEXT,
    data_acao DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Índices para melhor performance
CREATE INDEX idx_emprestimos_cliente ON emprestimos(cliente_id);
CREATE INDEX idx_pagamentos_emprestimo ON pagamentos(emprestimo_id);
CREATE INDEX idx_notificacoes_usuario ON notificacoes(usuario_id);
CREATE INDEX idx_historico_usuario ON historico_acoes(usuario_id);

-- Inserir algumas configurações padrão
INSERT INTO configuracoes (chave, valor, descricao) VALUES
('taxa_juros_padrao', '1.99', 'Taxa de juros padrão do sistema'),
('prazo_maximo_dias', '360', 'Prazo máximo em dias para empréstimos'),
('limite_credito_padrao', '5000', 'Limite de crédito padrão para novos clientes');

-- Triggers para automação
DELIMITER //
CREATE TRIGGER after_emprestimo_insert
AFTER INSERT ON emprestimos
FOR EACH ROW
BEGIN
    INSERT INTO notificacoes (usuario_id, titulo, mensagem, tipo)
    SELECT cliente_id, 'Novo Empréstimo', 
           CONCAT('Empréstimo de R$ ', NEW.valor_principal, ' solicitado'), 'SISTEMA'
    FROM clientes WHERE id = NEW.cliente_id;
END//
DELIMITER ;