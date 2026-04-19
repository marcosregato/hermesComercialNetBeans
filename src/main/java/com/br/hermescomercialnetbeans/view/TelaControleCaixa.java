package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.dao.MovimentoCaixaDao;
import com.br.hermescomercialnetbeans.model.MovimentoCaixa;
import com.br.hermescomercialnetbeans.model.Usuario;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TelaControleCaixa extends javax.swing.JInternalFrame {

    private DefaultTableModel modeloMovimentos;
    private MovimentoCaixaDao movimentoDao = new MovimentoCaixaDao();
    private Usuario usuarioLogado;
    private MovimentoCaixa caixaAberto;

    public TelaControleCaixa() {
        initComponents();
        configurarTabela();
        carregarMovimentos();
        verificarCaixaAberto();
    }
    
    public TelaControleCaixa(Usuario usuario) {
        this.usuarioLogado = usuario;
        initComponents();
        configurarTabela();
        carregarMovimentos();
        verificarCaixaAberto();
    }

    private void configurarTabela() {
        modeloMovimentos = (DefaultTableModel) tabelaMovimentos.getModel();
        tabelaMovimentos.setRowHeight(25);
    }

    private void carregarMovimentos() {
        modeloMovimentos.setRowCount(0);
        List<MovimentoCaixa> movimentos = movimentoDao.listar();
        
        for (MovimentoCaixa mov : movimentos) {
            Object[] linha = {
                mov.getDataHoraFormatada(),
                mov.getUsuarioNome(),
                mov.getTipoMovimento().getDescricao(),
                String.format("R$ %.2f", mov.getValor()),
                String.format("R$ %.2f", mov.getSaldoAtual()),
                mov.getDescricao()
            };
            modeloMovimentos.addRow(linha);
        }
        
        atualizarSaldoAtual();
    }

    private void verificarCaixaAberto() {
        List<MovimentoCaixa> caixasAbertos = movimentoDao.buscarMovimentosAbertos();
        if (!caixasAbertos.isEmpty()) {
            caixaAberto = caixasAbertos.get(0);
            btAbrirCaixa.setEnabled(false);
            btFecharCaixa.setEnabled(true);
            btSangria.setEnabled(true);
            btSuprimento.setEnabled(true);
            lblStatusCaixa.setText("CAIXA ABERTO - ID: " + caixaAberto.getId());
            lblStatusCaixa.setForeground(new java.awt.Color(0, 128, 0));
        } else {
            caixaAberto = null;
            btAbrirCaixa.setEnabled(true);
            btFecharCaixa.setEnabled(false);
            btSangria.setEnabled(false);
            btSuprimento.setEnabled(false);
            lblStatusCaixa.setText("CAIXA FECHADO");
            lblStatusCaixa.setForeground(new java.awt.Color(255, 0, 0));
        }
    }

    private void atualizarSaldoAtual() {
        double saldo = movimentoDao.calcularSaldoAtual();
        lblSaldoAtual.setText(String.format("R$ %.2f", saldo));
    }

    private void btAbrirCaixaActionPerformed(java.awt.event.ActionEvent evt) {
        String valorStr = JOptionPane.showInputDialog(this, "Valor de abertura do caixa:", "Abrir Caixa", JOptionPane.QUESTION_MESSAGE);
        
        if (valorStr != null && !valorStr.trim().isEmpty()) {
            try {
                double valor = Double.parseDouble(valorStr.replace(",", "."));
                if (valor < 0) {
                    JOptionPane.showMessageDialog(this, "Valor não pode ser negativo!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double saldoAnterior = movimentoDao.calcularSaldoAtual();
                
                MovimentoCaixa abertura = new MovimentoCaixa(
                    usuarioLogado != null ? usuarioLogado.getId() != null ? usuarioLogado.getId().longValue() : null : null,
                    usuarioLogado != null ? usuarioLogado.getNome() : "Sistema",
                    MovimentoCaixa.TipoMovimento.ABERTURA,
                    valor,
                    saldoAnterior,
                    "Abertura de caixa"
                );
                
                movimentoDao.salvar(abertura);
                carregarMovimentos();
                verificarCaixaAberto();
                
                JOptionPane.showMessageDialog(this, "Caixa aberto com sucesso!");
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir caixa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void btFecharCaixaActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja fechar o caixa?", "Fechar Caixa", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                double saldoAtual = movimentoDao.calcularSaldoAtual();
                
                MovimentoCaixa fechamento = new MovimentoCaixa(
                    usuarioLogado != null ? usuarioLogado.getId() != null ? usuarioLogado.getId().longValue() : null : null,
                    usuarioLogado != null ? usuarioLogado.getNome() : "Sistema",
                    MovimentoCaixa.TipoMovimento.FECHAMENTO,
                    saldoAtual,
                    saldoAtual,
                    "Fechamento de caixa"
                );
                
                movimentoDao.salvar(fechamento);
                carregarMovimentos();
                verificarCaixaAberto();
                
                JOptionPane.showMessageDialog(this, "Caixa fechado com sucesso!\nSaldo final: R$ " + String.format("%.2f", saldoAtual));
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao fechar caixa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void btSangriaActionPerformed(java.awt.event.ActionEvent evt) {
        String valorStr = JOptionPane.showInputDialog(this, "Valor da sangria:", "Sangria", JOptionPane.QUESTION_MESSAGE);
        
        if (valorStr != null && !valorStr.trim().isEmpty()) {
            try {
                double valor = Double.parseDouble(valorStr.replace(",", "."));
                if (valor <= 0) {
                    JOptionPane.showMessageDialog(this, "Valor deve ser maior que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double saldoAnterior = movimentoDao.calcularSaldoAtual();
                if (valor > saldoAnterior) {
                    JOptionPane.showMessageDialog(this, "Valor da sangria não pode ser maior que o saldo atual!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String motivo = JOptionPane.showInputDialog(this, "Motivo da sangria:", "Sangria", JOptionPane.QUESTION_MESSAGE);
                
                MovimentoCaixa sangria = new MovimentoCaixa(
                    usuarioLogado != null ? usuarioLogado.getId() != null ? usuarioLogado.getId().longValue() : null : null,
                    usuarioLogado != null ? usuarioLogado.getNome() : "Sistema",
                    MovimentoCaixa.TipoMovimento.SANGRIA,
                    valor,
                    saldoAnterior,
                    motivo != null ? motivo : "Sangria"
                );
                
                movimentoDao.salvar(sangria);
                carregarMovimentos();
                
                JOptionPane.showMessageDialog(this, "Sangria realizada com sucesso!");
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao realizar sangria: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void btSuprimentoActionPerformed(java.awt.event.ActionEvent evt) {
        String valorStr = JOptionPane.showInputDialog(this, "Valor do suprimento:", "Suprimento", JOptionPane.QUESTION_MESSAGE);
        
        if (valorStr != null && !valorStr.trim().isEmpty()) {
            try {
                double valor = Double.parseDouble(valorStr.replace(",", "."));
                if (valor <= 0) {
                    JOptionPane.showMessageDialog(this, "Valor deve ser maior que zero!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double saldoAnterior = movimentoDao.calcularSaldoAtual();
                String motivo = JOptionPane.showInputDialog(this, "Motivo do suprimento:", "Suprimento", JOptionPane.QUESTION_MESSAGE);
                
                MovimentoCaixa suprimento = new MovimentoCaixa(
                    usuarioLogado != null ? usuarioLogado.getId() != null ? usuarioLogado.getId().longValue() : null : null,
                    usuarioLogado != null ? usuarioLogado.getNome() : "Sistema",
                    MovimentoCaixa.TipoMovimento.SUPRIMENTO,
                    valor,
                    saldoAnterior,
                    motivo != null ? motivo : "Suprimento"
                );
                
                movimentoDao.salvar(suprimento);
                carregarMovimentos();
                
                JOptionPane.showMessageDialog(this, "Suprimento realizado com sucesso!");
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao realizar suprimento: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void btAtualizarActionPerformed(java.awt.event.ActionEvent evt) {
        carregarMovimentos();
        verificarCaixaAberto();
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblStatusCaixa = new javax.swing.JLabel();
        lblSaldoAtual = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btAbrirCaixa = new javax.swing.JButton();
        btFecharCaixa = new javax.swing.JButton();
        btSangria = new javax.swing.JButton();
        btSuprimento = new javax.swing.JButton();
        btAtualizar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaMovimentos = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Controle de Caixa");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblStatusCaixa.setFont(new java.awt.Font("Tahoma", 1, 18));
        lblStatusCaixa.setText("CAIXA FECHADO");
        lblStatusCaixa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblSaldoAtual.setFont(new java.awt.Font("Tahoma", 1, 24));
        lblSaldoAtual.setText("R$ 0,00");
        lblSaldoAtual.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel1.setText("Status do Caixa:");

        jLabel2.setText("Saldo Atual:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblSaldoAtual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblStatusCaixa, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblStatusCaixa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblSaldoAtual))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btAbrirCaixa.setBackground(new java.awt.Color(0, 153, 51));
        btAbrirCaixa.setForeground(new java.awt.Color(255, 255, 255));
        btAbrirCaixa.setText("Abrir Caixa");
        btAbrirCaixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAbrirCaixaActionPerformed(evt);
            }
        });

        btFecharCaixa.setBackground(new java.awt.Color(255, 0, 0));
        btFecharCaixa.setForeground(new java.awt.Color(255, 255, 255));
        btFecharCaixa.setText("Fechar Caixa");
        btFecharCaixa.setEnabled(false);
        btFecharCaixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharCaixaActionPerformed(evt);
            }
        });

        btSangria.setBackground(new java.awt.Color(255, 153, 0));
        btSangria.setText("Sangria");
        btSangria.setEnabled(false);
        btSangria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSangriaActionPerformed(evt);
            }
        });

        btSuprimento.setBackground(new java.awt.Color(0, 102, 204));
        btSuprimento.setForeground(new java.awt.Color(255, 255, 255));
        btSuprimento.setText("Suprimento");
        btSuprimento.setEnabled(false);
        btSuprimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSuprimentoActionPerformed(evt);
            }
        });

        btAtualizar.setText("Atualizar");
        btAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAtualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btAbrirCaixa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btFecharCaixa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSangria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSuprimento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btAtualizar)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btAbrirCaixa)
                    .addComponent(btFecharCaixa)
                    .addComponent(btSangria)
                    .addComponent(btSuprimento)
                    .addComponent(btAtualizar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabelaMovimentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data/Hora", "Usuário", "Tipo", "Valor", "Saldo", "Descrição"
            }
        ));
        jScrollPane1.setViewportView(tabelaMovimentos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }

    // Variables declaration
    private javax.swing.JButton btAbrirCaixa;
    private javax.swing.JButton btAtualizar;
    private javax.swing.JButton btFecharCaixa;
    private javax.swing.JButton btSangria;
    private javax.swing.JButton btSuprimento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelaMovimentos;
    private javax.swing.JLabel lblSaldoAtual;
    private javax.swing.JLabel lblStatusCaixa;
}
