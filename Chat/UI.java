/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chat;

import java.awt.*;
import java.util.*;
import javafx.event.*;
import javax.swing.*;

/**
 *
 * @author Camper
 */
public class UI extends javax.swing.JFrame {
    chatClient Client;
    int index = -1;
    
    public UI(chatClient K) {
        this.Client = K;
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                initComponents();
            }
        });
        
        this.setVisible(true);
    }
  
    public String hostName = "You";
    
    public void setHost(String Host){
        hostName = Host;
    }
    @SuppressWarnings("unchecked")
   
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        list1 = new java.awt.List();
        jTextField2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("send");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField1.setText("enter message here");
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        list1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            }
        });


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(list1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(list1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }                     
    private void jScrollPane(java.awt.event.ActionEvent evt){
  
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String text = (jTextField1.getText());
        jTextField1.setText("");
        jTextArea1.append(text);
        jTextArea1.append("\n");
        
    }
    
    public void toServer(String name){
        Client.send(name);
    }
      
    public void Update(String[] Connections){
        list1.removeAll();
        list1.add("You");
    }
    
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            

    }                                           

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {                                         
        if (jTextField1.getText().equals("enter message here")){
            jTextField1.setText("");
        }        // TODO add your handling code here:
    }                                        

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {
        appendText(jTextField1.getText(),hostName);
    }                                   

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {                                       
        int enterCheck = evt.getKeyChar();
        if (enterCheck == 10){
            appendText(jTextField1.getText(),hostName);  
       }    
    }

    private void jTextField2MouseClicked(java.awt.event.MouseEvent evt) {                                         
        jTextField2.setText("");
    }                                        

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {                                       
        int enterCheck = evt.getKeyChar();
        if (enterCheck == 10){
           String text = (jTextField2.getText());
           jTextField2.setText("");
           if (!text.isEmpty()){
                toServer(text); 
           }
        }
    }        
    
    public void PING(){
        for (int i = 0;i <= 1000;i++){
            this.toServer("BEEP");
        }
    }
    
    HashMap<String,Integer> clientToIndex = new HashMap<>();
    
    //Unused
    public void removeName(String Address){
        
        list1.remove(clientToIndex.get(Address));
    }
    
    public void addName(String Address,String Name){
        int Index = list1.getItemCount();
        list1.add(Name,Index);
        list1.repaint();
        clientToIndex.put(Address, Index);
    }
    
    public void changeName(String Address, String newName){
        Client.log("[UI]" + Address);
        Client.log("[UI]" + newName);
        list1.replaceItem(newName, clientToIndex.get(Address));
    }


    public void addMessage(String name, String message){
        jTextArea1.append(name + ": " + message);
        jTextArea1.append("\n");
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }
    
    
    public void appendText(String Text, String Name){
        if (!Text.isEmpty()){
            jTextField1.setText("");
            jTextArea1.append(Name + ": " + Text);
            jTextArea1.append("\n");
            Client.send(Text);
            jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
        }
    }
    
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private java.awt.List list1;
}


