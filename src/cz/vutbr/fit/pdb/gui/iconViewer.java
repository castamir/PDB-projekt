package cz.vutbr.fit.pdb.gui;

import cz.vutbr.fit.pdb.models.ObrazkyModel;
import cz.vutbr.fit.pdb.models.ZakaznikModel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
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
        obrazek.setVisible(false);
        obrazek.setIcon(null);
    }
    
    private void getNextIcon() {
        //Map.Entry<Integer, myIcon>  novy;
        if(it != null && it.hasNext()) {
            novy = it.next();
            i = novy.getValue().getMyIcon();
            smaz_button.setEnabled(true);
            otoc_button.setEnabled(true);
            obrazek.setIndex(novy.getKey());
            setNewIcon(i, false);
        }
    }
    
    private void getPreviousIcon() {
        //Map.Entry<Integer, myIcon>  novy;
        if(it != null && it.hasPrevious()) {
            novy = it.previous();
            i = novy.getValue().getMyIcon();
            smaz_button.setEnabled(true);
            otoc_button.setEnabled(true);
            obrazek.setIndex(novy.getKey());
            setNewIcon(i, false);
        }
        
    }
    
    private Map<Integer, myIcon> updateUserImages(int usrId){
        Map<Integer, myIcon> result = null;
        try {
            result =  modelObr.getImagesOfCustomer(usrId);
        } catch (SQLException ex) {
            Logger.getLogger(iconViewer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return result;
    }
    
    
    public void setNewIcon(ImageIcon i, boolean notFound){
        if(i != null){
            if(notFound) {
                obrazek.setText("No IMAGE");
                obrazek.setFocus(false);
            } else {
                obrazek.setText("");
                obrazek.setFocus(true);
            }
            obrazek.setVisible(true);
            obrazek.setIcon(i);
            //obrazek.setIndex(lastUserId);
            obrazek_kontejner.add(obrazek);
            obrazek_kontejner.revalidate();
        }
    }
    /*
    public void setNewIcon(int usrId) {
        boolean notFound = false;
        obrazkyAktualnihoUz = updateUserImages(usrId);
        if(obrazkyAktualnihoUz.isEmpty()) {
            System.out.println("NULL");
            String path = "/icons/Badge-cancel.png";
            i = new ImageIcon(getClass().getResource(path));
            smaz_button.setEnabled(false);
            otoc_button.setEnabled(false);
            notFound = true;
        } else {
            Map.Entry<Integer, myIcon> item = obrazkyAktualnihoUz.entrySet().iterator().next();
            i = item.getValue().getMyIcon();
            smaz_button.setEnabled(true);
            otoc_button.setEnabled(true);
            obrazek.setIndex(item.getKey());
        }
        if(i != null){
            if(notFound) {
                obrazek.setText("No IMAGE");
                obrazek.setFocus(false);
            } else {
                obrazek.setText("");
                obrazek.setFocus(true);
            }
            obrazek.setVisible(true);
            obrazek.setIcon(i);
            //obrazek.setIndex(lastUserId);
            obrazek_kontejner.add(obrazek);
            obrazek_kontejner.revalidate();
        }
   }*/
    
    public void comboBoxAction(ActionEvent ae) {
        JComboBox cb = (JComboBox) ae.getSource();
        String comboBoxitem = (String) cb.getSelectedItem();
        it = null;
        if(!comboBoxitem.equals("")){
            //int tmp = (int) cb.getSelectedIndex();
            String substring = comboBoxitem.substring(0, comboBoxitem.indexOf(" "));
            lastUserId = Integer.parseInt(substring);
            System.out.println(lastUserId);
            boolean notFound = false;
            obrazkyAktualnihoUz = updateUserImages(lastUserId);
            if(obrazkyAktualnihoUz.isEmpty()) {
                System.out.println("NULL");
                String path = "/icons/Badge-cancel.png";
                i = new ImageIcon(getClass().getResource(path));
                smaz_button.setEnabled(false);
                otoc_button.setEnabled(false);
                notFound = true;
            } else {
                
                List<Map.Entry<Integer, myIcon>> list = new ArrayList<>(obrazkyAktualnihoUz.entrySet());
                
                it = list.listIterator();
                item = it.next();
                
                i = item.getValue().getMyIcon();
                smaz_button.setEnabled(true);
                otoc_button.setEnabled(true);
                obrazek.setIndex(item.getKey());
            }
            setNewIcon(i,notFound);
        } else {
            obrazek.setVisible(false);
            obrazek.setIcon(null);
            obrazek.setText("");
        }
    }
    
    public void myInit() {
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        obrazek_kontejner.setLayout(layout);
        modelObr = new ObrazkyModel();
        obrazek = new myIcon();
        updateCombo();
        //user_comboBox.setModel(new DefaultComboBoxModel(comboBoxItems));
        user_comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                comboBoxAction(ae);
            }
        });
        
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        otoc_button.setText("Otoč");
        otoc_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otoc_buttonActionPerformed(evt);
            }
        });

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

        jButton1.setText("Vyhledej podobné");

        jButton2.setText("<");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText(">");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(obrazek_kontejner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(otoc_button)
                    .addComponent(smaz_button)
                    .addComponent(user_comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshUserList_button)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
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
            obrazek.setBorder(null);
            //it.remove();
            //Mazani z DB!
            try {
                modelObr.delete(obrazek.getIndex());
                //Odebrat z mapy
                //obrazkyAktualnihoUz.remove(obrazek.getIndex());
                //tmp.getIndex();
                System.out.println("Chci smazat s indexem: "+obrazek.getIndex());
                myIcon remove = obrazkyAktualnihoUz.remove(obrazek.getIndex());
                //List<Map.Entry<Integer, myIcon>> list = new ArrayList<>(obrazkyAktualnihoUz.entrySet());
                //it = list.listIterator();
                it.remove();
                //vozidla_kontejner.revalidate();
            } catch (SQLException ex) {
                Logger.getLogger(Rezervace.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(it != null && it.hasNext()) {
           getNextIcon();
        } else if(it != null && it.hasPrevious()) {
           getPreviousIcon();
        }
        //getNextIcon();
    }//GEN-LAST:event_smaz_buttonActionPerformed

    private void otoc_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otoc_buttonActionPerformed
        if(obrazek.isActive()){
            try {
                modelObr.rotateImage(obrazek.getIndex());
                ImageIcon ic = new ImageIcon(modelObr.getImage(obrazek.getIndex()));
                setNewIcon(ic, false);
            } catch (SQLException ex) {
                Logger.getLogger(iconViewer.class.getName()).log(Level.SEVERE, null, ex);
            }
            //it.
        }
    }//GEN-LAST:event_otoc_buttonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        getNextIcon();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        getPreviousIcon();
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel obrazek_kontejner;
    private javax.swing.JButton otoc_button;
    private javax.swing.JButton refreshUserList_button;
    private javax.swing.JButton smaz_button;
    private javax.swing.JComboBox user_comboBox;
    // End of variables declaration//GEN-END:variables
    private ObrazkyModel modelObr;
    private myIcon obrazek;
    private Map<Integer, Integer> customer_databaseIdToComboBoxId;
    private String[] comboBoxItems;
    private ImageIcon i;
    private int lastUserId;
    private Map<Integer, myIcon> obrazkyAktualnihoUz;
    private Map.Entry<Integer, myIcon> item;
    private ListIterator<Map.Entry<Integer, myIcon>> it = null;
    Map.Entry<Integer, myIcon>  novy;
}
