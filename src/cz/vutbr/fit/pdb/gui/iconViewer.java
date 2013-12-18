package cz.vutbr.fit.pdb.gui;

import cz.vutbr.fit.pdb.models.ObrazkyModel;
import cz.vutbr.fit.pdb.models.ZakaznikModel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class iconViewer extends javax.swing.JPanel {

    /**
     * Konstruktor
     */
    public iconViewer() {
        initComponents();
        myInit();
    }
    
    /**
     * Obnovi obsah comboboxu - nacte znova data z db
     */
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
        user_comboBox.setSelectedIndex(0);
        obrazek.setVisible(false);
        obrazek.setIcon(null);
    }
    
    private void getNextIcon() {
        if(obrazkyAktualnihoUz != null) {
            if(list!=null && !list.isEmpty() && aktualniIndex < list.size()-1) {
                aktualniIndex += 1;
                Entry<Integer, myIcon> novyItem = list.get(aktualniIndex);
                i = novyItem.getValue().getMyIcon();
                smaz_button.setEnabled(true);
                otoc_button.setEnabled(true);
                obrazek.setIndex(novyItem.getKey());
                setNewIcon(i, false);
                aktIndex_label.setText("Index: "+(aktualniIndex+1)+"/"+list.size());
            }
        }
    }
    
    private void getPreviousIcon() {
        if(obrazkyAktualnihoUz != null) {
            if(list!=null && !list.isEmpty() && aktualniIndex > 0) {
                aktualniIndex -= 1;
                Entry<Integer, myIcon> novyItem = list.get(aktualniIndex);
                i = novyItem.getValue().getMyIcon();
                smaz_button.setEnabled(true);
                otoc_button.setEnabled(true);
                obrazek.setIndex(novyItem.getKey());
                setNewIcon(i, false);
                aktIndex_label.setText("Index: "+(aktualniIndex+1)+"/"+list.size());
            } 
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
    
    
    /**
     * Nastavi novou ikonu
     * @param i ikona
     * @param notFound pokud neni zadny obrazek 
     */
    public void setNewIcon(ImageIcon i, boolean notFound){
        if(i != null){
            if(notFound) {
                obrazek.setText("No IMAGE");
                obrazek.setFocus(false);
                aktIndex_label.setText("Index: 0/0");
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
    
    /**
     * Vyber veci z comboboxu
     * @param ae akce
     */
    public void comboBoxAction(ActionEvent ae) {
        JComboBox cb = (JComboBox) ae.getSource();
        String comboBoxitem = (String) cb.getSelectedItem();
        list = null;
        statusbar_panel.setVisible(false);
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
                
                list = new ArrayList<>(obrazkyAktualnihoUz.entrySet());
                aktualniIndex = 0;
                Entry<Integer, myIcon> entry = list.get(aktualniIndex);
                i = entry.getValue().getMyIcon();
                obrazek.setIndex(entry.getKey());
                smaz_button.setEnabled(true);
                otoc_button.setEnabled(true);
                aktIndex_label.setText("Index: "+(aktualniIndex+1)+"/"+(list.size()));
            }
            setNewIcon(i,notFound);
        } else {
            obrazek.setVisible(false);
            obrazek.setIcon(null);
            obrazek.setText("");
            aktIndex_label.setText("Index: 0/0");
        }
    }
    
    private void myInit() {
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        obrazek_kontejner.setLayout(layout);
        modelObr = new ObrazkyModel();
        obrazek = new myIcon();
        updateCombo();
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
        obrazek_kontejner_parent = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        obrazek_kontejner = new javax.swing.JPanel();
        statusbar_panel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        user_comboBox = new javax.swing.JComboBox();
        refreshUserList_button = new javax.swing.JButton();
        podobne_button = new javax.swing.JButton();
        zpet_button = new javax.swing.JButton();
        dalsi_button = new javax.swing.JButton();
        aktIndex_label = new javax.swing.JLabel();

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

        jScrollPane1.setMaximumSize(new java.awt.Dimension(600, 500));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(600, 500));

        javax.swing.GroupLayout obrazek_kontejnerLayout = new javax.swing.GroupLayout(obrazek_kontejner);
        obrazek_kontejner.setLayout(obrazek_kontejnerLayout);
        obrazek_kontejnerLayout.setHorizontalGroup(
            obrazek_kontejnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 637, Short.MAX_VALUE)
        );
        obrazek_kontejnerLayout.setVerticalGroup(
            obrazek_kontejnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 521, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(obrazek_kontejner);

        jLabel1.setText("Nejdřív musíte vybrat obrázek (kliknutím levého tlačítka myši na obrázek)");

        javax.swing.GroupLayout statusbar_panelLayout = new javax.swing.GroupLayout(statusbar_panel);
        statusbar_panel.setLayout(statusbar_panelLayout);
        statusbar_panelLayout.setHorizontalGroup(
            statusbar_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusbar_panelLayout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(157, 157, 157))
        );
        statusbar_panelLayout.setVerticalGroup(
            statusbar_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusbar_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout obrazek_kontejner_parentLayout = new javax.swing.GroupLayout(obrazek_kontejner_parent);
        obrazek_kontejner_parent.setLayout(obrazek_kontejner_parentLayout);
        obrazek_kontejner_parentLayout.setHorizontalGroup(
            obrazek_kontejner_parentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(statusbar_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        obrazek_kontejner_parentLayout.setVerticalGroup(
            obrazek_kontejner_parentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(obrazek_kontejner_parentLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusbar_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        user_comboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        refreshUserList_button.setText("Refresh user list");
        refreshUserList_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshUserList_buttonActionPerformed(evt);
            }
        });

        podobne_button.setText("Vyhledej podobné");
        podobne_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                podobne_buttonActionPerformed(evt);
            }
        });

        zpet_button.setText("<");
        zpet_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zpet_buttonActionPerformed(evt);
            }
        });

        dalsi_button.setText(">");
        dalsi_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dalsi_buttonActionPerformed(evt);
            }
        });

        aktIndex_label.setText("Index: 0/0");

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
                .addComponent(podobne_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(zpet_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dalsi_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aktIndex_label)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(obrazek_kontejner_parent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(otoc_button)
                    .addComponent(smaz_button)
                    .addComponent(user_comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshUserList_button)
                    .addComponent(podobne_button)
                    .addComponent(zpet_button)
                    .addComponent(dalsi_button)
                    .addComponent(aktIndex_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(obrazek_kontejner_parent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void refreshUserList_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshUserList_buttonActionPerformed
        updateCombo();
        obrazek.setVisible(false);
        obrazek.setIcon(null);
    }//GEN-LAST:event_refreshUserList_buttonActionPerformed

    private void smaz_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smaz_buttonActionPerformed
        if(obrazek.isActive()){
            statusbar_panel.setVisible(false);
            obrazek.setVisible(false);
            obrazek.setIcon(null);
            obrazek.setBorder(null);
            try {
                modelObr.delete(obrazek.getIndex());
                System.out.println("Chci smazat s indexem: "+obrazek.getIndex());
                myIcon remove = obrazkyAktualnihoUz.remove(obrazek.getIndex());
                list = new ArrayList<>(obrazkyAktualnihoUz.entrySet());
                list.remove(aktualniIndex);
            } catch (SQLException ex) {
                Logger.getLogger(Rezervace.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(list != null && !list.isEmpty() && aktualniIndex < list.size()-1){
                getNextIcon();
            } else if(list != null && !list.isEmpty() && aktualniIndex > 0){
                getPreviousIcon();
            } else {
                String path = "/icons/Badge-cancel.png";
                ImageIcon ic = new ImageIcon(getClass().getResource(path));
                smaz_button.setEnabled(false);
                otoc_button.setEnabled(false);
                setNewIcon(ic, true);
            }
        } else {
            statusbar_panel.setVisible(true);
            statusbar_panel.setBackground(Color.red);
        }
    }//GEN-LAST:event_smaz_buttonActionPerformed

    private void otoc_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otoc_buttonActionPerformed
        if(obrazek.isActive()){
            try {
                statusbar_panel.setVisible(false);
                modelObr.rotateImage(obrazek.getIndex());
                ImageIcon ic = new ImageIcon(modelObr.getImage(obrazek.getIndex()));
                obrazek.setNewIcon(ic);
                setNewIcon(ic, false);
                Entry<Integer, myIcon> en = new AbstractMap.SimpleEntry<Integer, myIcon>(obrazek.getIndex(), obrazek); 
                list = new ArrayList<>(obrazkyAktualnihoUz.entrySet());
                list.set(aktualniIndex, en);
            } catch (SQLException ex) {
                Logger.getLogger(iconViewer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            statusbar_panel.setVisible(true);
            statusbar_panel.setBackground(Color.red);
        }
    }//GEN-LAST:event_otoc_buttonActionPerformed

    private void dalsi_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dalsi_buttonActionPerformed
        statusbar_panel.setVisible(false);
        getNextIcon();
    }//GEN-LAST:event_dalsi_buttonActionPerformed

    private void zpet_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zpet_buttonActionPerformed
        statusbar_panel.setVisible(false);
        getPreviousIcon();
    }//GEN-LAST:event_zpet_buttonActionPerformed

    private void podobne_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_podobne_buttonActionPerformed
        if(obrazek.isActive()){
            statusbar_panel.setVisible(false);
            JFrame frame = new JFrame("Nejpodobnejsi vysledky");
            vyhledaniVysledky vv = new vyhledaniVysledky(obrazek.getIndex());
            frame.add(vv);
            frame.pack();
            frame.setVisible(true);
        } else {
            statusbar_panel.setVisible(true);
            statusbar_panel.setBackground(Color.red);
        }
    }//GEN-LAST:event_podobne_buttonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aktIndex_label;
    private javax.swing.JButton dalsi_button;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel obrazek_kontejner;
    private javax.swing.JPanel obrazek_kontejner_parent;
    private javax.swing.JButton otoc_button;
    private javax.swing.JButton podobne_button;
    private javax.swing.JButton refreshUserList_button;
    private javax.swing.JButton smaz_button;
    private javax.swing.JPanel statusbar_panel;
    private javax.swing.JComboBox user_comboBox;
    private javax.swing.JButton zpet_button;
    // End of variables declaration//GEN-END:variables
    private ObrazkyModel modelObr;
    private myIcon obrazek;
    private Map<Integer, Integer> customer_databaseIdToComboBoxId;
    private String[] comboBoxItems;
    private ImageIcon i;
    private int lastUserId;
    private Map<Integer, myIcon> obrazkyAktualnihoUz;
    private Integer aktualniIndex;
    private List<Map.Entry<Integer, myIcon>> list;
}
