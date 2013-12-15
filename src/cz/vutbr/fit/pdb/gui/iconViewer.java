package cz.vutbr.fit.pdb.gui;

import cz.vutbr.fit.pdb.models.ObrazkyModel;
import cz.vutbr.fit.pdb.models.ZakaznikModel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

/**
 *
 * @author Doma
 */
public class iconViewer extends javax.swing.JPanel {

    /**
     * Creates new form iconViewer
     */
    public iconViewer() {
        initComponents();
        //myInit();
    }
    
    public void updateCombo(){
        customer_databaseIdToComboBoxId = new HashMap<>();
        ZakaznikModel modelZakaznik = new ZakaznikModel();

        try {
            int i = 0;
            Map<Integer, String> list = modelZakaznik.getList();

            String[] items = new String[list.size() + 1];
            items[i++] = "";
            for (Map.Entry<Integer, String> entry : list.entrySet()) {
                items[i] = entry.getValue();
                customer_databaseIdToComboBoxId.put(entry.getKey(), i);
                i++;
            }
            comboBoxItems = items;
        } catch (SQLException e) {
            comboBoxItems = new String[]{"chyba při načítání.."};
        }
        user_comboBox.setModel(new DefaultComboBoxModel(comboBoxItems));
    }
    
    public void setNewIcon(int usrId) {
        byte[] tmp;
        boolean notFound = false;
        try {
            tmp = modelObr.getImage(lastUserId);
            if(tmp != null){
                //System.out.println("Not NULL");
                i = new ImageIcon(tmp);
                smaz_button.setEnabled(true);
                otoc_button.setEnabled(true);
            } else {
                //System.out.println("NULL");
                String path = "/icons/Badge-cancel.png";
                i = new ImageIcon(getClass().getResource(path));
                smaz_button.setEnabled(false);
                otoc_button.setEnabled(false);
                notFound = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(iconViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(i != null){
            if(notFound) {
                obrazek.setText("No IMAGE");
            } else {
                obrazek.setText("");
            }
            obrazek.setVisible(true);
            obrazek.setIcon(i);
            obrazek.setIndex(lastUserId);
            obrazek_kontejner.add(obrazek);
            obrazek_kontejner.revalidate();
        }
   }
    
    public void comboBoxAction(ActionEvent ae) {
        JComboBox cb = (JComboBox) ae.getSource();
        String item = (String) cb.getSelectedItem();
        if(!item.equals("")){
            //int tmp = (int) cb.getSelectedIndex();
            String substring = item.substring(0, item.indexOf(" "));
            lastUserId = Integer.parseInt(substring);
            System.out.println(lastUserId);
            setNewIcon(lastUserId);
        }
    }
    
    public void myInit() {
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        obrazek_kontejner.setLayout(layout);
        updateCombo();
        //user_comboBox.setModel(new DefaultComboBoxModel(comboBoxItems));
        user_comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                comboBoxAction(ae);
            }
        });
        modelObr = new ObrazkyModel();
        obrazek = new myIcon();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        otoc_button = new javax.swing.JButton();
        smaz_button = new javax.swing.JButton();
        obrazek_kontejner = new javax.swing.JPanel();
        user_comboBox = new javax.swing.JComboBox();
        refreshUserList_button = new javax.swing.JButton();

        otoc_button.setText("Otoč");

        smaz_button.setText("Smaž");
        smaz_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smaz_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout obrazek_kontejnerLayout = new javax.swing.GroupLayout(obrazek_kontejner);
        obrazek_kontejner.setLayout(obrazek_kontejnerLayout);
        obrazek_kontejnerLayout.setHorizontalGroup(
            obrazek_kontejnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        obrazek_kontejnerLayout.setVerticalGroup(
            obrazek_kontejnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        user_comboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        refreshUserList_button.setText("Refresh user list");
        refreshUserList_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshUserList_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(otoc_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(smaz_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(user_comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshUserList_button)
                .addGap(0, 103, Short.MAX_VALUE))
            .addComponent(obrazek_kontejner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(otoc_button)
                    .addComponent(smaz_button)
                    .addComponent(user_comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshUserList_button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(obrazek_kontejner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refreshUserList_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshUserList_buttonActionPerformed
        updateCombo();
        obrazek.setVisible(false);
        obrazek.setIcon(null);
    }//GEN-LAST:event_refreshUserList_buttonActionPerformed

    private void smaz_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smaz_buttonActionPerformed
    if(obrazek.isActive()){
        obrazek.setVisible(false);
        obrazek.setIcon(null);
        //it.remove();
        //Mazani z DB!
        try {
            modelObr.delete(obrazek.getIndex());
            //tmp.getIndex();
            System.out.println("Chci smazat s indexem: "+obrazek.getIndex());
            //vozidla_kontejner.revalidate();
        } catch (SQLException ex) {
            Logger.getLogger(Rezervace.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }//GEN-LAST:event_smaz_buttonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel obrazek_kontejner;
    private javax.swing.JButton otoc_button;
    private javax.swing.JButton refreshUserList_button;
    private javax.swing.JButton smaz_button;
    private javax.swing.JComboBox user_comboBox;
    // End of variables declaration//GEN-END:variables
    private List<myIcon> iconList;
    private ObrazkyModel modelObr;
    private myIcon obrazek;
    private Map<Integer, Integer> customer_databaseIdToComboBoxId;
    private String[] comboBoxItems;
    private ImageIcon i;
    private int lastUserId;
}
