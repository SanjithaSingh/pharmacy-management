/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import java.sql.Date;
import java.time.LocalDate;

/**
 *
 * @author Sanjitha
 */
public class NotificationPane extends javax.swing.JFrame {

    /**
     * Creates new form NotificationPane
     */
    public NotificationPane() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        fetch();
    }
    Connection con = null;
    String custName,pName,supName;
    int qty;
    Date renewDate;
    
    public void fetch(){
        try {
             con = DriverManager.getConnection("jdbc:mysql://localhost/pharmacy","root","password");
            String q = "(select custname as `Customer's Name`,productName as `Medicine Name`,nextpurchase as `Date of Next Purchase`,quantity as `Left in Stock`,supplierName as `Supplier's Name` from customers,medication,inventory,suppliers"+
                    " where medication.custid=customers.custid"+
                    " and medication.medid=inventory.productId"+
                    " and suppliers.supplierId=inventory.supplierId"+
                    " and nextpurchase between now() and date_add(now(),interval 15 day)"+
                    " order by nextpurchase)"+
                    " union"+
                    " (select custname as `Customer's Name`,productName as `Medicine Name`,nextpurchase as `Date of Next Purchase`,quantity as `Left in Stock`,supplierName as `Supplier's Name` from customers,medication,inventory,suppliers"+
                    " where medication.custid=customers.custid"+
                    " and medication.medid=inventory.productId"+
                    " and suppliers.supplierId=inventory.supplierId"+
                    " and inventory.quantity<=30"+
                    " order by nextpurchase)";
            PreparedStatement pst = con.prepareStatement(q);
            ResultSet rs = pst.executeQuery();
            details.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            Logger.getLogger(NotificationPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        details = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        placeOrder = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        details.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Customer Name", "Medication", "Date of Renewal", "Quantity Left in Stock"
            }
        ));
        jScrollPane1.setViewportView(details);

        jButton1.setText("Notify Customer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        placeOrder.setText("Place Order");
        placeOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                placeOrderActionPerformed(evt);
            }
        });

        jButton2.setText("Exit to Main Menu");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Montserrat", 1, 36)); // NOI18N
        jLabel1.setText("Notification Pane");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(194, 194, 194)
                .addComponent(jButton1)
                .addGap(54, 54, 54)
                .addComponent(placeOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(186, 186, 186)
                .addComponent(jLabel1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(placeOrder)
                    .addComponent(jButton2))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void getDetailsFromTable(){
        DefaultTableModel model= (DefaultTableModel)details.getModel();
        int selectedRowIndex = details.getSelectedRow();
        custName=model.getValueAt(selectedRowIndex, 0).toString();
        pName=model.getValueAt(selectedRowIndex, 1).toString();
        renewDate=java.sql.Date.valueOf(model.getValueAt(selectedRowIndex, 2).toString());
        qty=Integer.parseInt(model.getValueAt(selectedRowIndex, 3).toString());
        supName=model.getValueAt(selectedRowIndex, 4).toString();
        
    }
    
    public String getCustomerEmail(){
        String emailid="";
        try {
            
            String q="select emailid from customers where custName=?";
            PreparedStatement pst = con.prepareStatement(q);
            pst.setString(1, custName);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                emailid=rs.getString("emailid");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(NotificationPane.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return emailid;
    }
    
    public String getCustomerEmailBody(){
        String body="";
        body+="Dear "+custName+",\n\n";
        body+="This is a reminder that the course of your prescribed medicine: "+pName;
        body+=" is completed on "+renewDate;
        body+="\nVisit us to renew your medicines\n\n";
        body+="Thank you";
        return body;
    }
    
    
    public String getCustomerName(){
        return custName;
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        getDetailsFromTable();
        String info[]=new String[4];
        info[0]=getCustomerName();
        info[1]=getCustomerEmail();
        info[2]=getCustomerEmailBody();
        CustomerEmail c= new CustomerEmail();
        c.main(info);
    }//GEN-LAST:event_jButton1ActionPerformed

    public String getSupplierEmail(){
        String emailid="";
        try {
            
            String q="select emailid from suppliers where SupplierName=?";
            PreparedStatement pst = con.prepareStatement(q);
            pst.setString(1, supName);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                emailid=rs.getString("emailid");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(NotificationPane.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return emailid;
    }
    
    public String getSupplierEmailBody(){
        String body="";
        body+="To,\n"+supName+",\n\n";
        body+="We would like to place an order for : \nProduct Name : "+pName;
        body+="\nQuantity : \n";
        body+="Please ensure that it reaches us before "+renewDate;
        body+="\nContact us for further details\n\n";
        body+="Thank you";
        return body;
    }
    
    
    
    private void placeOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_placeOrderActionPerformed
        // TODO add your handling code here:
        getDetailsFromTable();
        String info[]=new String[3];
        info[0]=supName;
        info[1]=getSupplierEmail();
        info[2]=getSupplierEmailBody();
        SupplierEmail s= new SupplierEmail();
        s.main(info);
    }//GEN-LAST:event_placeOrderActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //new billing().setVisible(false);
        this.dispose();
        //new pharmacy().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NotificationPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NotificationPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NotificationPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NotificationPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NotificationPane().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable details;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton placeOrder;
    // End of variables declaration//GEN-END:variables
}
