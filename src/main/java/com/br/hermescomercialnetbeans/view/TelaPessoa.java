/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.controller.pessoa.UsuarioController;
import com.br.hermescomercialnetbeans.dao.UsuarioDao;
import com.br.hermescomercialnetbeans.model.Pessoa;
import com.br.hermescomercialnetbeans.model.Usuario;
import com.br.hermescomercialnetbeans.util.ValidarCampo;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author marcos
 */
public class TelaPessoa extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaUsuario
     */
    public TelaPessoa() {
        initComponents();
        comboTipoUsuario.setSelectedIndex(0);
        listar();
    }
    
    Pessoa pessoa = new Pessoa();
    UsuarioController usuarioController;
    ValidarCampo validar = new ValidarCampo();
  
    
    private void pegaValoresDosCampos(Pessoa pessoa) {
        pessoa.setNome(txtNome.getText());
        pessoa.setEndereco(txtEndereco.getText());
        pessoa.setTelefone(txtTelefone.getText());
        pessoa.setCnpj(txtNumDocumento.getText());
        pessoa.setTipoUsuario(comboTipoUsuario.getSelectedItem().toString());
            
    }
    
    
    public void limpar() {

        txtNome.setText("");
        txtEndereco.setText("");
        txtNumDocumento.setText("");
        txtTelefone.setText("");
        comboTipoUsuario.setSelectedIndex(0);
    }
    
    public void listar(){
        
        UsuarioDao dao = new UsuarioDao();
        
        DefaultTableModel  model = (DefaultTableModel) tabelaUsuario.getModel();
        model.setRowCount(0);
        tabelaUsuario.setModel(model);
        List<Pessoa> lista = dao.lista();
        Object[] objeto = new Object[5];
        for (int i = 0; i < lista.size(); i++) {
            objeto[0] = lista.get(i).getNome();
            objeto[1] = lista.get(i).getEndereco();
            objeto[2] = lista.get(i).getTelefone();
            objeto[3] = lista.get(i).getTipoPessoa();
            objeto[4] = lista.get(i).getCnpj();
            
            model.addRow(objeto);
        }
        tabelaUsuario.setRowHeight(35);
        tabelaUsuario.setRowMargin(10);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtNome = new javax.swing.JTextField();
        txtNumDocumento = new javax.swing.JTextField();
        comboTipoUsuario = new javax.swing.JComboBox<>();
        txtEndereco = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTelefone = new javax.swing.JFormattedTextField();
        msgAlerta = new javax.swing.JLabel();
        msgAlerta1 = new javax.swing.JLabel();
        comboTipoDocumento = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtBairro = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCidade = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtWhatsapp = new javax.swing.JFormattedTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaUsuario = new javax.swing.JTable();
        btEditar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        txtPesqCnpjCpf = new javax.swing.JTextField();
        btPesquisar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNomeKeyTyped(evt);
            }
        });

        txtNumDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumDocumentoActionPerformed(evt);
            }
        });

        comboTipoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------", "Usuário", "Fornecedor" }));
        comboTipoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTipoUsuarioActionPerformed(evt);
            }
        });

        jLabel1.setText("Nome :");

        jLabel2.setText("Tipo de Uusário :");

        jLabel3.setText("Telefone :");

        jLabel4.setText("Tipo de documento");

        jLabel5.setText("Endereço :");

        try {
            txtTelefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ######-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        msgAlerta.setForeground(new java.awt.Color(255, 0, 51));

        msgAlerta1.setForeground(new java.awt.Color(255, 0, 51));

        comboTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-------", "CPF", "CNPJ" }));
        comboTipoDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTipoDocumentoActionPerformed(evt);
            }
        });

        jLabel7.setText("Número do documento :");

        txtBairro.setText("jTextField2");

        jLabel9.setText("Bairro :");

        txtCidade.setText("jTextField3");

        jLabel10.setText("Cidade :");

        jLabel11.setText("Estado :");

        txtEstado.setText("jTextField4");

        jLabel8.setText("whatsapp");

        txtWhatsapp.setText("(  )       -   ");

        txtEmail.setText("jTextField1");

        jLabel12.setText("Email :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(comboTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18)
                                        .addComponent(comboTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNumDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(msgAlerta, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(49, 49, 49))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(64, 64, 64)
                                .addComponent(msgAlerta1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtWhatsapp, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12)
                                .addGap(58, 58, 58)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(msgAlerta, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(comboTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(comboTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtEndereco)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8)
                    .addComponent(txtWhatsapp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msgAlerta1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        tabelaUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Endereço", "Telefone", "Tipo de Usuário", "CNPJ / CPF"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaUsuarioMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaUsuario);
        if (tabelaUsuario.getColumnModel().getColumnCount() > 0) {
            tabelaUsuario.getColumnModel().getColumn(0).setResizable(false);
            tabelaUsuario.getColumnModel().getColumn(1).setResizable(false);
            tabelaUsuario.getColumnModel().getColumn(2).setResizable(false);
            tabelaUsuario.getColumnModel().getColumn(3).setResizable(false);
            tabelaUsuario.getColumnModel().getColumn(4).setResizable(false);
        }

        btEditar.setText("EDITAR");
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btExcluir.setText("EXCLUIR");
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btSalvar.setText("SALVAR");
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        btFechar.setText("FECHAR");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        txtPesqCnpjCpf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesqCnpjCpfActionPerformed(evt);
            }
        });

        btPesquisar.setText("PESQUISAR");
        btPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPesquisarActionPerformed(evt);
            }
        });

        jLabel6.setText("Pesquisar :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btSalvar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btExcluir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btEditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btFechar)
                .addGap(71, 71, 71))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPesqCnpjCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btPesquisar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPesqCnpjCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btPesquisar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btEditar)
                    .addComponent(btExcluir)
                    .addComponent(btSalvar)
                    .addComponent(btFechar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNumDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumDocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumDocumentoActionPerformed

    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarActionPerformed
        // TODO add your handling code here:
        Usuario usuario = new Usuario();
        usuario.setNome(txtNome.getText());
        usuario.setTipousuario(comboTipoUsuario.getSelectedItem().toString());
        usuario.setTipoDocumento(comboTipoDocumento.getSelectedItem().toString());
        usuario.setEndereco(txtEndereco.getText());
        usuario.setBairro(txtBairro.getText());
        usuario.setCidade(txtCidade.getText());
        usuario.setEstado(txtEstado.getText());
        usuario.setNumeroDocumento(txtNumDocumento.getText());
        usuario.setWhatsapp(txtWhatsapp.getText());
        usuario.setTelefone(txtTelefone.getText());
        
        usuarioController = new UsuarioController();
        usuarioController.salvar(usuario);
        
        /*int numeroIndexCombo = comboTipoUsuario.getSelectedIndex();
        if(numeroIndexCombo > 0) {
            String tipoSelecionado = comboTipoUsuario.getSelectedItem().toString();
            String cpfCnpj = txtNumDocumento.getText();
            boolean valido = false;
            
            if("Usuário".equals(tipoSelecionado)) {
                valido = validar.isCPF(cpfCnpj);
            } else if("Fornecedor".equals(tipoSelecionado)) {
                valido = validar.isCNPJ(cpfCnpj);
            }
            
            if(valido) {
                pegaValoresDosCampos(pessoa);
                cadPessoaController.salvar(pessoa);
                limpar();
                listar();
            } else {
                JOptionPane.showMessageDialog(null, "O número CPF ou CNPJ está errado", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione um tipo de pessoa", "Erro", JOptionPane.ERROR_MESSAGE);
        }*/
    }//GEN-LAST:event_btSalvarActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        // TODO add your handling code here:
        int row = tabelaUsuario.getSelectedRow();
        
        if(row >= 0) {
            String nome = tabelaUsuario.getValueAt(row, 0).toString().trim();
            int resposta = JOptionPane.showConfirmDialog(null, "Deseja excluir este registro?", "Confirmacao", JOptionPane.YES_NO_OPTION);
            if(resposta == JOptionPane.YES_OPTION) {
                UsuarioDao dao = new UsuarioDao();
                dao.remove(nome);
                listar();
                limpar();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione um registro para excluir", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btFecharActionPerformed

    private void tabelaUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaUsuarioMouseClicked
        // TODO add your handling code here:
        int linhaTabela = tabelaUsuario.getSelectedRow();
        DefaultTableModel  model = (DefaultTableModel) tabelaUsuario.getModel();
        txtNome.setText(model.getValueAt(linhaTabela, 0).toString());
        txtEndereco.setText(model.getValueAt(linhaTabela, 1).toString());
        txtTelefone.setText(model.getValueAt(linhaTabela, 2).toString());
        //comboTipoUsuario.getEditor().setItem(model.getValueAt(linhaTabela, 3).toString());
        
        String comboTipo = model.getValueAt(linhaTabela, 3).toString();
        for(int x=0; x < comboTipoUsuario.getItemCount(); x++){
            if(comboTipoUsuario.getItemAt(x).toString().equalsIgnoreCase(comboTipo)){
                comboTipoUsuario.setSelectedIndex(x);
                
            }
        }
        
        txtNumDocumento.setText(model.getValueAt(linhaTabela, 4).toString());
    }//GEN-LAST:event_tabelaUsuarioMouseClicked

    private void comboTipoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTipoUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboTipoUsuarioActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        // TODO add your handling code here:
        int row = tabelaUsuario.getSelectedRow();
        if(row >= 0) {
            pegaValoresDosCampos(pessoa);
            usuarioController.update();
            limpar();
            listar();
        } else {
            JOptionPane.showMessageDialog(null, "Selecione um registro para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        
    }//GEN-LAST:event_btEditarActionPerformed

    private void txtPesqCnpjCpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesqCnpjCpfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesqCnpjCpfActionPerformed

    private void btPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPesquisarActionPerformed
        // TODO add your handling code here:
        try {
            
            if(txtPesqCnpjCpf.getText()!= null){
                 UsuarioDao dao = new UsuarioDao();
        
            DefaultTableModel  model = (DefaultTableModel) tabelaUsuario.getModel();
            model.setRowCount(0);
             tabelaUsuario.setModel(model);
            List<Pessoa> lista = dao.buscar(txtPesqCnpjCpf.getText());
            Object[] objeto = new Object[8];
            for (int i = 0; i < lista.size(); i++) {
                objeto[0] = lista.get(i).getNome();
                objeto[1] = lista.get(i).getEndereco();
                objeto[2] = lista.get(i).getTelefone();
                objeto[3] = lista.get(i).getTipoPessoa();
                objeto[4] = lista.get(i).getCnpj();
                
                model.addRow(objeto);
            }
            tabelaUsuario.setRowHeight(35);
            tabelaUsuario.setRowMargin(10);
                
            }else{
                listar();
            }
        
        } catch (Exception e) {
            System.err.println("ERRO >> "+ e.getMessage());
           
    }
        
    }//GEN-LAST:event_btPesquisarActionPerformed

    private void txtNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeKeyTyped

    private void comboTipoDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTipoDocumentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboTipoDocumentoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btPesquisar;
    private javax.swing.JButton btSalvar;
    private javax.swing.JComboBox<String> comboTipoDocumento;
    private javax.swing.JComboBox<String> comboTipoUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel msgAlerta;
    private javax.swing.JLabel msgAlerta1;
    private javax.swing.JTable tabelaUsuario;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JTextField txtCidade;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtNumDocumento;
    private javax.swing.JTextField txtPesqCnpjCpf;
    private javax.swing.JFormattedTextField txtTelefone;
    private javax.swing.JFormattedTextField txtWhatsapp;
    // End of variables declaration//GEN-END:variables
}
