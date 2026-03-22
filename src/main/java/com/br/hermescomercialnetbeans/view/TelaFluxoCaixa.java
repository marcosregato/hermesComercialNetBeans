package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.controller.FluxoCaixaController;
import com.br.hermescomercialnetbeans.dao.FluxoCaixaDao;
import com.br.hermescomercialnetbeans.model.FluxoCaixa;
import com.br.hermescomercialnetbeans.util.ValidarCampo;
import java.util.List;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class TelaFluxoCaixa extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaFluxoCaixa
     */
    public TelaFluxoCaixa() {
        initComponents();
        comboEntradaSaida.setSelectedIndex(-1);
        comboPesqEntradaSaida.setSelectedIndex(-1);
        listar();
    }
    FluxoCaixaController fluxoController = new FluxoCaixaController();
    FluxoCaixa fluxo = new FluxoCaixa();
    ValidarCampo validar = new ValidarCampo();
    
    private void pegaValoresDosCampos(FluxoCaixa fluxoCaixa) { 

        fluxoCaixa.setDescricao(txtDescricao.getText());
        fluxoCaixa.setTipo(comboEntradaSaida.getSelectedItem().toString());
        fluxoCaixa.setValor(txtValor.getText());
        fluxoCaixa.setData(txtData.getValue().toString());
        
        
        
    }
    
    public void limparPesquisa() {
        comboEntradaSaida.setSelectedIndex(-1);
        txtDataInicio.setText("");
        txtDataFim.setText("");
    }
    
    public void limpar() {

        txtDescricao.setText("");
        comboEntradaSaida.setSelectedIndex(-1);
        txtValor.setText("");
        txtData.setText("");
        
    }
    
     public void listar(){
        
        FluxoCaixaDao dao = new FluxoCaixaDao();
        
        DefaultTableModel  model = (DefaultTableModel) tabelaFluxoCaixa.getModel();
        model.setRowCount(0);
        tabelaFluxoCaixa.setModel(model);
        List<FluxoCaixa> lista = dao.listar();
        
        Object[] objeto = new Object[4];
        //TODO GAMBIRRA PARA CONSERTO AS INFOs DAS COLUNAS
        for (int i = 0; i < lista.size(); i++) {
            objeto[3] = lista.get(i).getDescricao(); // COL VALOR
            objeto[0] = lista.get(i).getTipo(); // COL DESCRICAO
            objeto[2] = lista.get(i).getData();// COL DATA
            objeto[1] = lista.get(i).getValor();// COL TIPO
            
            model.addRow(objeto);
        }
        tabelaFluxoCaixa.setRowHeight(25);
        tabelaFluxoCaixa.setRowMargin(10);
    }
     
     public void getLinhaTabela() {
        int linha = tabelaFluxoCaixa.getSelectedRow();
        txtDescricao.setText(tabelaFluxoCaixa.getValueAt(linha, 0).toString());
        comboEntradaSaida.setSelectedItem(tabelaFluxoCaixa.getValueAt(linha, 1).toString());
        txtValor.setText(tabelaFluxoCaixa.getValueAt(linha, 2).toString());
        txtData.setText(tabelaFluxoCaixa.getValueAt(linha, 3).toString());
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JTextField();
        comboEntradaSaida = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtData = new javax.swing.JFormattedTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaFluxoCaixa = new javax.swing.JTable();
        btPesqFluxoCaixa = new javax.swing.JButton();
        comboPesqEntradaSaida = new javax.swing.JComboBox<>();
        txtDataInicio = new javax.swing.JFormattedTextField();
        txtDataFim = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btExcluir = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Descrição :");

        comboEntradaSaida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Entrada", "Saida" }));

        jLabel2.setText("Entrada / Saida :");

        jLabel3.setText("Valor :");

        jLabel4.setText("Data :");

        try {
            txtData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboEntradaSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(128, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboEntradaSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(68, Short.MAX_VALUE))
        );

        tabelaFluxoCaixa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descrição", "Entrada / Saida", "Data", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tabelaFluxoCaixa);
        if (tabelaFluxoCaixa.getColumnModel().getColumnCount() > 0) {
            tabelaFluxoCaixa.getColumnModel().getColumn(0).setResizable(false);
            tabelaFluxoCaixa.getColumnModel().getColumn(1).setResizable(false);
            tabelaFluxoCaixa.getColumnModel().getColumn(2).setResizable(false);
            tabelaFluxoCaixa.getColumnModel().getColumn(3).setResizable(false);
        }

        btPesqFluxoCaixa.setText("PESQUISAR");
        btPesqFluxoCaixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPesqFluxoCaixaActionPerformed(evt);
            }
        });

        comboPesqEntradaSaida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Entrada", "Saida" }));

        try {
            txtDataInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtDataFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel5.setText("Entrada / Saida :");

        jLabel6.setText("Data :");

        jLabel7.setText("até");

        btExcluir.setText("EXCLUIR");
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btFechar.setText("FECHAR");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        btEditar.setText("EDITAR");
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btSalvar.setText("SALVAR");
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboPesqEntradaSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(txtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btPesqFluxoCaixa)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btExcluir)
                .addGap(11, 11, 11)
                .addComponent(btEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btFechar)
                .addGap(43, 43, 43))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(comboPesqEntradaSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(txtDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btPesqFluxoCaixa))
                .addGap(41, 41, 41)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btExcluir)
                    .addComponent(btFechar)
                    .addComponent(btEditar)
                    .addComponent(btSalvar))
                .addContainerGap(117, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btPesqFluxoCaixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPesqFluxoCaixaActionPerformed

        FluxoCaixaDao dao = new FluxoCaixaDao();
        DefaultTableModel  model = (DefaultTableModel) tabelaFluxoCaixa.getModel();
        model.setRowCount(0);
        tabelaFluxoCaixa.setModel(model);
        
        DefaultTableCellRenderer rendererEsquerda = new DefaultTableCellRenderer();  
        rendererEsquerda.setHorizontalAlignment(SwingConstants.LEFT); 
        TableColumnModel modeloDaColuna = tabelaFluxoCaixa.getColumnModel();  
        modeloDaColuna.getColumn(0).setCellRenderer(rendererEsquerda);
        modeloDaColuna.getColumn(1).setCellRenderer(rendererEsquerda);
        modeloDaColuna.getColumn(2).setCellRenderer(rendererEsquerda);
        modeloDaColuna.getColumn(3).setCellRenderer(rendererEsquerda);
        
        modeloDaColuna.getColumn(0).setMaxWidth(50); 
        modeloDaColuna.getColumn(1).setMaxWidth(50);
        modeloDaColuna.getColumn(2).setMaxWidth(50);
        modeloDaColuna.getColumn(3).setMaxWidth(50);
        
        if(comboPesqEntradaSaida.getSelectedItem()!= null){
            
            List<FluxoCaixa> lista = dao.buscarEntraSaida(comboPesqEntradaSaida.getSelectedItem().toString().trim());
            
            Object[] objeto = new Object[4];
            for (int i = 0; i < lista.size(); i++) {
                objeto[3] = lista.get(i).getDescricao(); // COL VALOR
                objeto[0] = lista.get(i).getTipo(); // COL DESCRICAO
                objeto[2] = lista.get(i).getData();// COL DATA
                objeto[1] = lista.get(i).getValor();// COL TIPO
                model.addRow(objeto);
            }
            tabelaFluxoCaixa.setRowHeight(25);
            tabelaFluxoCaixa.setRowMargin(10);
            limparPesquisa();
        }
       
        
        if((validar.validarData(txtDataInicio.getText().trim(), txtDataFim.getText().trim()) != false)){
          
            List<FluxoCaixa> lista = dao.buscarData(txtDataInicio.getText(),txtDataFim.getText());
            
            Object[] objeto = new Object[4];
            for (int i = 0; i < lista.size(); i++) {
                objeto[3] = lista.get(i).getDescricao(); // COL VALOR
                objeto[0] = lista.get(i).getTipo(); // COL DESCRICAO
                objeto[2] = lista.get(i).getData();// COL DATA
                objeto[1] = lista.get(i).getValor();// COL TIPO
                model.addRow(objeto);
                
            }
            tabelaFluxoCaixa.setRowHeight(25);
            tabelaFluxoCaixa.setRowMargin(10);
            limparPesquisa();
        }
        if((comboEntradaSaida.getSelectedItem()!= null) & (txtDataInicio.getText().trim().isEmpty()) & (txtDataFim.getText().trim().isEmpty())){
            
            List<FluxoCaixa> lista = dao.buscarEntraSaidaData(comboPesqEntradaSaida.getSelectedItem().toString(),txtDataInicio.getText(),txtDataFim.getText());
            Object[] objeto = new Object[4];
            for (int i = 0; i < lista.size(); i++) {
                objeto[3] = lista.get(i).getDescricao(); // COL VALOR
                objeto[0] = lista.get(i).getTipo(); // COL DESCRICAO
                objeto[2] = lista.get(i).getData();// COL DATA
                objeto[1] = lista.get(i).getValor();// COL TIPO
                model.addRow(objeto);
            }
            tabelaFluxoCaixa.setRowHeight(25);
            tabelaFluxoCaixa.setRowMargin(10);
            limparPesquisa();
        }
       
    }//GEN-LAST:event_btPesqFluxoCaixaActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        dispose();
    }//GEN-LAST:event_btFecharActionPerformed

    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarActionPerformed
        pegaValoresDosCampos(fluxo);
        fluxoController.actionBtSalvar(fluxo);
        limpar();
        listar();
    }//GEN-LAST:event_btSalvarActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        // TODO add your handling code here:
         
    }//GEN-LAST:event_btEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btPesqFluxoCaixa;
    private javax.swing.JButton btSalvar;
    private javax.swing.JComboBox<String> comboEntradaSaida;
    private javax.swing.JComboBox<String> comboPesqEntradaSaida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelaFluxoCaixa;
    private javax.swing.JFormattedTextField txtData;
    private javax.swing.JFormattedTextField txtDataFim;
    private javax.swing.JFormattedTextField txtDataInicio;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
