package cz.vutbr.fit.pdb.gui;

import cz.vutbr.fit.pdb.utils.DatePicker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author Doma
 */
public class Sluzby extends javax.swing.JPanel {

    /**
     * Creates new form Sluzby
     */
    public Sluzby() {
        initComponents();
        hotelCompoundPanel1.setParentPanel(this);
        initTable();
    }

    private void comboBoxAction(ActionEvent evt) {
        JComboBox cb = (JComboBox)evt.getSource();
        item = (String)cb.getSelectedItem();
        int tmp = (int)cb.getSelectedIndex();
        //if(item != null ||tmp != -1) {
            System.out.println(item);
        //}
    }
    
    
    private ComboBoxModel getComboBoxItems(String[] toUpdate) {
        return new DefaultComboBoxModel(toUpdate);
    }
    
    private void initTable(){
        TableColumn tc = this.detail_dne_table.getColumnModel().getColumn(2);
        comboBox = new JComboBox();
        comboBox.setModel(getComboBoxItems(comboBoxItems));
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                comboBoxAction(ae);
            }
        });
        tc.setCellEditor(new DefaultCellEditor(comboBox));
 
        //Popisky
        DefaultTableCellRenderer renderer =
                new DefaultTableCellRenderer();
        renderer.setToolTipText("Vyberte jméno");
        tc.setCellRenderer(renderer);
        
        //Ziskani modelu tabulky
        model = (DefaultTableModel)detail_dne_table.getModel();
        
        //model.addRow(new Object[]{"Lala","Lala",(String)comboBox.getItemAt(2)});
        
    }
    
    
    private void updateTable(Object o) {
        model = (DefaultTableModel)detail_dne_table.getModel();
        //Odstraníme všechny řádky
        model.getDataVector().removeAllElements();
        //Updatneme kombobox
        comboBox.setModel(getComboBoxItems(tmp));
        //Pridame zaznam do tabulky
        //model.addRow(new Object[]{"ads","asd",(String)comboBox.getItemAt(2),date_field.getText()});
        //Překreslíme tabulku
        model.fireTableDataChanged();
    }
    
    private Locale getLocale(String loc) {
        if (loc != null && loc.length() > 0) {
            return new Locale(loc);
        } else {
            return Locale.US;
        }
    }

    public void updateTitle(String name) {
        nazev_sluzby.setText(name);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_sluzby = new javax.swing.JPanel();
        zoom_panel = new javax.swing.JPanel();
        hotelCompoundPanel1 = new cz.vutbr.fit.pdb.gui.HotelCompoundPanel();
        wrapper = new javax.swing.JPanel();
        nazev_sluzby = new javax.swing.JLabel();
        den_label = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        detail_dne_table = new javax.swing.JTable();
        date_field = new cz.vutbr.fit.pdb.utils.ObservingTextField();
        kalendar = new javax.swing.JLabel();
        ulozitZmena_button = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(911, 665));

        panel_sluzby.setBorder(javax.swing.BorderFactory.createTitledBorder("Vyberte službu"));

        javax.swing.GroupLayout hotelCompoundPanel1Layout = new javax.swing.GroupLayout(hotelCompoundPanel1);
        hotelCompoundPanel1.setLayout(hotelCompoundPanel1Layout);
        hotelCompoundPanel1Layout.setHorizontalGroup(
            hotelCompoundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 401, Short.MAX_VALUE)
        );
        hotelCompoundPanel1Layout.setVerticalGroup(
            hotelCompoundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 608, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout zoom_panelLayout = new javax.swing.GroupLayout(zoom_panel);
        zoom_panel.setLayout(zoom_panelLayout);
        zoom_panelLayout.setHorizontalGroup(
            zoom_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hotelCompoundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        zoom_panelLayout.setVerticalGroup(
            zoom_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hotelCompoundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel_sluzbyLayout = new javax.swing.GroupLayout(panel_sluzby);
        panel_sluzby.setLayout(panel_sluzbyLayout);
        panel_sluzbyLayout.setHorizontalGroup(
            panel_sluzbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(zoom_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_sluzbyLayout.setVerticalGroup(
            panel_sluzbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(zoom_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        wrapper.setBorder(javax.swing.BorderFactory.createTitledBorder("Rezervace"));

        nazev_sluzby.setText("Nazev sluzby");

        den_label.setText("Den:");

        detail_dne_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hodina", "Dostupnost", "Možnosti", "Poznámka"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        detail_dne_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                detail_dne_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(detail_dne_table);

        date_field.setText("dd/mm/yy");
        date_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                date_fieldActionPerformed(evt);
            }
        });

        kalendar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Calender Month.png"))); // NOI18N
        kalendar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kalendarMouseClicked(evt);
            }
        });

        ulozitZmena_button.setText("Uložit změny");
        ulozitZmena_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ulozitZmena_buttonActionPerformed(evt);
            }
        });

        jButton1.setText("Add_test");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Remove_test");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout wrapperLayout = new javax.swing.GroupLayout(wrapper);
        wrapper.setLayout(wrapperLayout);
        wrapperLayout.setHorizontalGroup(
            wrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(wrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nazev_sluzby, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(wrapperLayout.createSequentialGroup()
                        .addGroup(wrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(wrapperLayout.createSequentialGroup()
                                .addComponent(den_label)
                                .addGap(1, 1, 1)
                                .addComponent(date_field, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(kalendar))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(wrapperLayout.createSequentialGroup()
                                .addComponent(ulozitZmena_button)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        wrapperLayout.setVerticalGroup(
            wrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wrapperLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nazev_sluzby)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(wrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(date_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(den_label))
                    .addComponent(kalendar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(wrapperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ulozitZmena_button)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_sluzby, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(wrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(wrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_sluzby, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void kalendarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kalendarMouseClicked
        String lang = null;
        final Locale locale = getLocale(lang);
        DatePicker dp = new DatePicker(date_field, locale);
        // previously selected date
        Date selectedDate = dp.parseDate(date_field.getText());
        dp.setSelectedDate(selectedDate);
        dp.start(date_field);
    }//GEN-LAST:event_kalendarMouseClicked

    private void date_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_date_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_date_fieldActionPerformed

    private void detail_dne_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_detail_dne_tableMouseClicked
        //int row = detail_dne_table.rowAtPoint(evt.getPoint());
        //int col = detail_dne_table.columnAtPoint(evt.getPoint());
        //System.out.println("row: "+row+" col: "+col);
    }//GEN-LAST:event_detail_dne_tableMouseClicked

    private void ulozitZmena_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ulozitZmena_buttonActionPerformed
        //System.out.println("Klik");
        updateTable(new Object());
    }//GEN-LAST:event_ulozitZmena_buttonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        model.addRow(new Object[]{"Lala","Lala",(String)comboBox.getItemAt(2),"Defaultni poznamka"});
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int numRows = detail_dne_table.getSelectedRows().length;
        for(int i=0; i<numRows ; i++ ) {
            model.removeRow(detail_dne_table.getSelectedRow());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private String item;
    private String[] tmp = {null,"asdasdasd","2","3","4","5"};
    private String[] comboBoxItems = {"asdas","asdas","asddsa"};
    private JComboBox comboBox;
    private DefaultTableModel model;
    private Object [][] defaultValue;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private cz.vutbr.fit.pdb.utils.ObservingTextField date_field;
    private javax.swing.JLabel den_label;
    private javax.swing.JTable detail_dne_table;
    private cz.vutbr.fit.pdb.gui.HotelCompoundPanel hotelCompoundPanel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel kalendar;
    private javax.swing.JLabel nazev_sluzby;
    private javax.swing.JPanel panel_sluzby;
    private javax.swing.JButton ulozitZmena_button;
    private javax.swing.JPanel wrapper;
    private javax.swing.JPanel zoom_panel;
    // End of variables declaration//GEN-END:variables
}
