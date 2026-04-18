-- Script de INSERT para tabela venda com dados de exemplo
-- Execute após criar as tabelas e inserir usuários/produtos

BEGIN;

-- Inserir vendas de exemplo
INSERT INTO venda (codigo, data_hora, cliente_id, cliente_nome, usuario_id, usuario_nome, valor_total, valor_desconto, valor_acrescimo, valor_final, status, tipo_pagamento, observacoes, cancelada) VALUES
('V001', '2026-04-18 10:30:00', 1, 'Cliente Exemplo 1', 2, 'Operador Caixa', 89.20, 5.00, 0.00, 84.20, 'FINALIZADA', 'DINHEIRO', 'Venda com desconto de 5%', false),
('V002', '2026-04-18 11:15:00', NULL, 'Cliente Avulso', 2, 'Operador Caixa', 45.60, 0.00, 0.00, 45.60, 'FINALIZADA', 'CARTAO_DEBITO', 'Pagamento com cartão de débito', false),
('V003', '2026-04-18 14:20:00', 1, 'Cliente Exemplo 1', 2, 'Operador Caixa', 156.80, 10.00, 0.00, 146.80, 'FINALIZADA', 'DINHEIRO', 'Venda com desconto especial', false),
('V004', '2026-04-18 15:45:00', NULL, 'Cliente Avulso', 2, 'Operador Caixa', 23.70, 0.00, 0.00, 23.70, 'FINALIZADA', 'PIX', 'Pagamento via PIX', false),
('V005', '2026-04-18 16:30:00', 1, 'Cliente Exemplo 1', 2, 'Operador Caixa', 78.90, 0.00, 5.00, 83.90, 'FINALIZADA', 'CARTAO_CREDITO', 'Acréscimo de 5% no cartão de crédito', false),
('V006', '2026-04-18 17:10:00', NULL, 'Cliente Avulso', 2, 'Operador Caixa', 112.50, 0.00, 0.00, 112.50, 'ABERTA', NULL, 'Venda em andamento', false),
('V007', '2026-04-17 09:20:00', NULL, 'Cliente Avulso', 2, 'Operador Caixa', 34.60, 0.00, 0.00, 34.60, 'CANCELADA', NULL, 'Venda cancelada por erro do cliente', true)
ON CONFLICT (codigo) DO NOTHING;

-- Inserir itens de venda para cada venda
-- Venda V001 - Cliente comprou vários itens
INSERT INTO item_venda (venda_id, produto_id, produto_codigo, produto_descricao, quantidade, valor_unitario, subtotal, desconto) VALUES
(1, 1, '001', 'Arroz 5kg', 2, 25.90, 51.80, 0.00),
(1, 2, '002', 'Feijão 1kg', 1, 8.50, 8.50, 0.00),
(1, 3, '003', 'Óleo de Soja 900ml', 1, 7.90, 7.90, 0.00),
(1, 6, '006', 'Refrigerante 2L', 2, 8.90, 17.80, 0.00),
(1, 4, '004', 'Açúcar 1kg', 1, 5.20, 5.20, 3.20)
ON CONFLICT DO NOTHING;

-- Venda V002 - Compra simples
INSERT INTO item_venda (venda_id, produto_id, produto_codigo, produto_descricao, quantidade, valor_unitario, subtotal, desconto) VALUES
(2, 5, '005', 'Café 500g', 1, 15.60, 15.60, 0.00),
(2, 8, '008', 'Sabão em Pó 1kg', 1, 12.90, 12.90, 0.00),
(2, 9, '009', 'Detergente 500ml', 5, 3.20, 16.00, 0.00)
ON CONFLICT DO NOTHING;

-- Venda V003 - Compra maior
INSERT INTO item_venda (venda_id, produto_id, produto_codigo, produto_descricao, quantidade, valor_unitario, subtotal, desconto) VALUES
(3, 1, '001', 'Arroz 5kg', 3, 25.90, 77.70, 0.00),
(3, 2, '002', 'Feijão 1kg', 2, 8.50, 17.00, 0.00),
(3, 3, '003', 'Óleo de Soja 900ml', 2, 7.90, 15.80, 0.00),
(3, 5, '005', 'Café 500g', 1, 15.60, 15.60, 0.00),
(3, 8, '008', 'Sabão em Pó 1kg', 2, 12.90, 25.80, 0.00),
(3, 10, '010', 'Papel Higiênico 4 rolos', 1, 9.80, 9.80, 0.00)
ON CONFLICT DO NOTHING;

-- Venda V004 - Compra rápida
INSERT INTO item_venda (venda_id, produto_id, produto_codigo, produto_descricao, quantidade, valor_unitario, subtotal, desconto) VALUES
(4, 7, '007', 'Água Mineral 1.5L', 3, 2.50, 7.50, 0.00),
(4, 4, '004', 'Açúcar 1kg', 1, 5.20, 5.20, 0.00),
(4, 9, '009', 'Detergente 500ml', 3, 3.20, 9.60, 0.00),
(4, 10, '010', 'Papel Higiênico 4 rolos', 1, 9.80, 9.80, 0.00)
ON CONFLICT DO NOTHING;

-- Venda V005 - Compra com acréscimo
INSERT INTO item_venda (venda_id, produto_id, produto_codigo, produto_descricao, quantidade, valor_unitario, subtotal, desconto) VALUES
(5, 1, '001', 'Arroz 5kg', 1, 25.90, 25.90, 0.00),
(5, 2, '002', 'Feijão 1kg', 1, 8.50, 8.50, 0.00),
(5, 5, '005', 'Café 500g', 1, 15.60, 15.60, 0.00),
(5, 8, '008', 'Sabão em Pó 1kg', 1, 12.90, 12.90, 0.00),
(5, 6, '006', 'Refrigerante 2L', 1, 8.90, 8.90, 0.00),
(5, 7, '007', 'Água Mineral 1.5L', 1, 2.50, 2.50, 0.00),
(5, 10, '010', 'Papel Higiênico 4 rolos', 1, 9.80, 9.80, 0.00)
ON CONFLICT DO NOTHING;

-- Venda V006 - Venda em andamento (parcial)
INSERT INTO item_venda (venda_id, produto_id, produto_codigo, produto_descricao, quantidade, valor_unitario, subtotal, desconto) VALUES
(6, 1, '001', 'Arroz 5kg', 2, 25.90, 51.80, 0.00),
(6, 5, '005', 'Café 500g', 1, 15.60, 15.60, 0.00),
(6, 8, '008', 'Sabão em Pó 1kg', 1, 12.90, 12.90, 0.00),
(6, 3, '003', 'Óleo de Soja 900ml', 2, 7.90, 15.80, 0.00),
(6, 9, '009', 'Detergente 500ml', 1, 3.20, 3.20, 0.00),
(6, 10, '010', 'Papel Higiênico 4 rolos', 1, 9.80, 9.80, 0.00)
ON CONFLICT DO NOTHING;

-- Venda V007 - Venda cancelada
INSERT INTO item_venda (venda_id, produto_id, produto_codigo, produto_descricao, quantidade, valor_unitario, subtotal, desconto) VALUES
(7, 2, '002', 'Feijão 1kg', 1, 8.50, 8.50, 0.00),
(7, 4, '004', 'Açúcar 1kg', 1, 5.20, 5.20, 0.00),
(7, 7, '007', 'Água Mineral 1.5L', 2, 2.50, 5.00, 0.00),
(7, 9, '009', 'Detergente 500ml', 5, 3.20, 16.00, 0.00)
ON CONFLICT DO NOTHING;

-- Inserir pagamentos para as vendas finalizadas
-- V001 - Dinheiro
INSERT INTO pagamento (venda_id, forma_pagamento, valor, valor_recebido, troco, data_pagamento, status, observacoes) VALUES
(1, 'DINHEIRO', 84.20, 90.00, 5.80, '2026-04-18 10:30:00', 'PAGO', 'Cliente pagou com nota de 100')
ON CONFLICT DO NOTHING;

-- V002 - Cartão de Débito
INSERT INTO pagamento (venda_id, forma_pagamento, valor, valor_recebido, troco, bandeira_cartao, autorizacao_cartao, data_pagamento, status, observacoes) VALUES
(2, 'CARTAO_DEBITO', 45.60, 45.60, 0.00, 'VISA', 'AUTH123456', '2026-04-18 11:15:00', 'PAGO', 'Transação aprovada')
ON CONFLICT DO NOTHING;

-- V003 - Dinheiro
INSERT INTO pagamento (venda_id, forma_pagamento, valor, valor_recebido, troco, data_pagamento, status, observacoes) VALUES
(3, 'DINHEIRO', 146.80, 150.00, 3.20, '2026-04-18 14:20:00', 'PAGO', 'Cliente pagou com nota de 150')
ON CONFLICT DO NOTHING;

-- V004 - PIX
INSERT INTO pagamento (venda_id, forma_pagamento, valor, valor_recebido, troco, chave_pix, data_pagamento, status, observacoes) VALUES
(4, 'PIX', 23.70, 23.70, 0.00, 'cliente@example.com', '2026-04-18 15:45:00', 'PAGO', 'PIX transferido com sucesso')
ON CONFLICT DO NOTHING;

-- V005 - Cartão de Crédito (parcelado)
INSERT INTO pagamento (venda_id, forma_pagamento, valor, valor_recebido, troco, numero_parcelas, bandeira_cartao, autorizacao_cartao, data_pagamento, status, observacoes) VALUES
(5, 'CARTAO_CREDITO', 83.90, 83.90, 0.00, '3x', 'MASTERCARD', 'AUTH789012', '2026-04-18 16:30:00', 'PAGO', 'Parcelado em 3x sem juros')
ON CONFLICT DO NOTHING;

-- Inserir movimentos de caixa relacionados às vendas
-- Movimento de abertura do dia
INSERT INTO movimento_caixa (data_hora, usuario_id, usuario_nome, tipo_movimento, valor, saldo_anterior, saldo_atual, descricao, observacoes) VALUES
('2026-04-18 08:00:00', 2, 'Operador Caixa', 'ABERTURA', 500.00, 0.00, 500.00, 'Abertura do caixa', 'Caixa aberto com R$ 500,00')
ON CONFLICT DO NOTHING;

-- Movimentos das vendas
INSERT INTO movimento_caixa (data_hora, usuario_id, usuario_nome, tipo_movimento, valor, saldo_anterior, saldo_atual, descricao, observacoes) VALUES
('2026-04-18 10:30:00', 2, 'Operador Caixa', 'VENDA', 84.20, 500.00, 584.20, 'Venda V001', 'Venda finalizada em dinheiro'),
('2026-04-18 11:15:00', 2, 'Operador Caixa', 'VENDA', 45.60, 584.20, 629.80, 'Venda V002', 'Venda finalizada em cartão de débito'),
('2026-04-18 14:20:00', 2, 'Operador Caixa', 'VENDA', 146.80, 629.80, 776.60, 'Venda V003', 'Venda finalizada em dinheiro'),
('2026-04-18 15:45:00', 2, 'Operador Caixa', 'VENDA', 23.70, 776.60, 800.30, 'Venda V004', 'Venda finalizada via PIX'),
('2026-04-18 16:30:00', 2, 'Operador Caixa', 'VENDA', 83.90, 800.30, 884.20, 'Venda V005', 'Venda finalizada em cartão de crédito')
ON CONFLICT DO NOTHING;

-- Movimento de cancelamento
INSERT INTO movimento_caixa (data_hora, usuario_id, usuario_nome, tipo_movimento, valor, saldo_anterior, saldo_atual, descricao, observacoes) VALUES
('2026-04-17 09:25:00', 2, 'Operador Caixa', 'CANCELAMENTO', -34.60, 465.40, 430.80, 'Cancelamento V007', 'Venda cancelada por solicitação do cliente')
ON CONFLICT DO NOTHING;

COMMIT;

-- Resumo dos dados inseridos:
-- 7 vendas (6 finalizadas, 1 aberta, 1 cancelada)
-- Múltiplos itens por venda
-- Diversas formas de pagamento (Dinheiro, Cartão, PIX)
-- Movimentos de caixa correspondentes
-- Dados realistas para testar o sistema PDV
