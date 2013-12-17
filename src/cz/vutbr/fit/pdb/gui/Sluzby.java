package cz.vutbr.fit.pdb.gui;

import cz.vutbr.fit.pdb.models.SluzbyModel;
import cz.vutbr.fit.pdb.models.ZakaznikModel;

import cz.vutbr.fit.pdb.utils.DatePicker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;



// GET DATE & TIME IN ANY FORMAT
import java.util.Calendar;
import java.text.SimpleDateFormat;

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

        modelSluzby = new SluzbyModel();
        modelZakaznik = new ZakaznikModel();
        date_field.setText(now());

        try {
            initTable();
        } catch (Exception ex) {
            Logger.getLogger(Sluzby.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void panelDidAppear() {
        hotelCompoundPanel1.update();
    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    private void comboBoxAction(ActionEvent evt) {
        JComboBox cb = (JComboBox) evt.getSource();
        item = (String) cb.getSelectedItem();
        int tmp = (int) cb.getSelectedIndex();
        //if(item != null ||tmp != -1) {
        //System.out.println(item);
        //}
    }

    private ComboBoxModel getComboBoxItems(String[] toUpdate) {
        return new DefaultComboBoxModel(toUpdate);
    }

    private void initTable() throws Exception {

        TableColumn tc = this.detail_dne_table.getColumnModel().getColumn(2);

        this.initComboBoxItems();

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
        refTableData = new ArrayList<>();

        //hodina.toString();
        //model.addRow(new Object[]{hodina.toString(),"Lala",(String)comboBox.getItemAt(2)});

    }

    private void initComboBoxItems() {
        customer_databaseIdToComboBoxId = new HashMap<>();

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
    }

    private void updateTable(Object o) {
        model = (DefaultTableModel) detail_dne_table.getModel();
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
        updateTable(name, date_field.getText());
    }

    public void updateTable(String serviceName, String formatedDate) {
        model = (DefaultTableModel) detail_dne_table.getModel();
        model.getDataVector().removeAllElements();
        refTableData = new ArrayList<>();

        try {
            List<Map<String, Object>> myRow = modelSluzby.getRezervace(serviceName, formatedDate);
            //Ziskani modelu tabulky
            Iterator<Map<String, Object>> it = myRow.iterator();

            while (it.hasNext()) {
                Map<String, Object> value = it.next();
                String hodina = value.get("hodina").toString();
                Map<String, Object> zakaznik = (Map<String, Object>) value.get("zakaznik");

                String stav = "";
                if (value.get("id") != null) {
                    stav = "rezervovano";
                }

                int comboBoxItemId = 0;

                if (value.get("id") != null) {
                    comboBoxItemId = customer_databaseIdToComboBoxId.get(zakaznik.get("id"));
                }

                String poznamka = value.get("poznamka") != null ? value.get("poznamka").toString() : null;

                Object[] row = new Object[]{hodina, stav, (String) comboBox.getItemAt(comboBoxItemId), poznamka};
                model.addRow(row);
                Map<Object, Object[]> mapItem = new HashMap<>();
                mapItem.put((Object) (value.get("id")), row);
                refTableData.add(mapItem);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Sluzby.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Sluzby.class.getName()).log(Level.SEVERE, null, ex);
        }
        model.fireTableDataChanged();
    }

    private boolean checkChangesInTable() {
        boolean modified = false;
        List<Map<Object, Object[]>> newTableData = new ArrayList<>();
        List<List<Object>> v = model.getDataVector();
        for (int i = 0; i < refTableData.size(); i++) {
            List<Object> row = v.get(i);
            Map<Object, Object[]> mapItem = refTableData.get(i);
            Object id = mapItem.keySet().iterator().next(); // VODO magic, do not touch !!!
            System.out.print("id");
            System.out.println(id);
            Object[] refRow = mapItem.get(id);
            try {
                if (id == null && !areRowsEqual(row, refRow) && row.get(2) != null && !row.get(2).toString().equals("")) {
                    // insert
                    System.out.println("insert");
                    modified = true;
                    Integer zakID = parseCustomerIdFromString(row.get(2).toString());
                    String sluzba = nazev_sluzby.getText();
                    String datum = date_field.getText();
                    String poznamka = (row.get(3) == null) ? null : row.get(3).toString();
                    int hodina = Integer.parseInt(row.get(0).toString());
                    modelSluzby.novaRezervace(zakID, sluzba, datum, hodina, poznamka);
                } else if (id != null) {
                    if (row.get(2).toString().equals("")) {
                        // delete
                        System.out.println("delete");
                        modified = true;
                        modelSluzby.smazatRezervaci(Integer.parseInt(id.toString()));
                    } else if (!areRowsEqual(row, refRow)) {
                        // update
                        System.out.println("update");
                        modified = true;
                        Integer zakID = parseCustomerIdFromString(row.get(2).toString());
                        String sluzba = nazev_sluzby.getText();
                        String datum = date_field.getText();
                        String poznamka = (row.get(3) == null) ? null : row.get(3).toString();
                        int hodina = Integer.parseInt(row.get(0).toString());
                        modelSluzby.upravitRezervaci(Integer.parseInt(id.toString()), zakID, sluzba, datum, hodina, poznamka);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(Sluzby.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Sluzby.class.getName()).log(Level.SEVERE, null, ex);
            }
            Object[] newRow = new Object[]{row.get(0), row.get(1), row.get(2), row.get(3)};
            Map<Object, Object[]> newMapItem = new HashMap<>();
            newMapItem.put(id, newRow);
            newTableData.add(newMapItem);
        }
        refTableData = newTableData;
        return modified;
    }

    private int parseCustomerIdFromString(String str) {
        String[] components = str.split(" ");
        return Integer.parseInt(components[0]);
    }

    private boolean areRowsEqual(List<Object> row, Object[] refRow) {
        for (int i = 0; i < row.size(); i++) {
            if (row.get(i) != refRow[i]) {
                System.out.println("refRow[i]");
                System.out.println(row.get(i));
                System.out.println(refRow[i]);
                return false;
            }
        }
        return true;
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
        sluzbyScroll_kontejner = new javax.swing.JScrollPane();
        hotelCompoundPanel1 = new cz.vutbr.fit.pdb.gui.HotelCompoundPanel();
        wrapper = new javax.swing.JPanel();
        nazev_sluzby = new javax.swing.JLabel();
        den_label = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        detail_dne_table = new javax.swing.JTable();
        date_field = new cz.vutbr.fit.pdb.utils.ObservingTextField();
        kalendar = new javax.swing.JLabel();
        ulozitZmena_button = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(911, 665));

        panel_sluzby.setBorder(javax.swing.BorderFactory.createTitledBorder("Vyberte službu"));

        sluzbyScroll_kontejner.setMaximumSize(new java.awt.Dimension(403, 610));
        sluzbyScroll_kontejner.setPreferredSize(new java.awt.Dimension(403, 610));

        javax.swing.GroupLayout hotelCompoundPanel1Layout = new javax.swing.GroupLayout(hotelCompoundPanel1);
        hotelCompoundPanel1.setLayout(hotelCompoundPanel1Layout);
        hotelCompoundPanel1Layout.setHorizontalGroup(
            hotelCompoundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 413, Short.MAX_VALUE)
        );
        hotelCompoundPanel1Layout.setVerticalGroup(
            hotelCompoundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 608, Short.MAX_VALUE)
        );

        sluzbyScroll_kontejner.setViewportView(hotelCompoundPanel1);

        javax.swing.GroupLayout zoom_panelLayout = new javax.swing.GroupLayout(zoom_panel);
        zoom_panel.setLayout(zoom_panelLayout);
        zoom_panelLayout.setHorizontalGroup(
            zoom_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sluzbyScroll_kontejner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
        );
        zoom_panelLayout.setVerticalGroup(
            zoom_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sluzbyScroll_kontejner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        date_field.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                date_fieldCaretUpdate(evt);
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
                            .addComponent(ulozitZmena_button))
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
                .addComponent(ulozitZmena_button)
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
                .addComponent(wrapper, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(wrapper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_sluzby, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
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

    private void detail_dne_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_detail_dne_tableMouseClicked
        //int row = detail_dne_table.rowAtPoint(evt.getPoint());
        //int col = detail_dne_table.columnAtPoint(evt.getPoint());
        //System.out.println("row: "+row+" col: "+col);
    }//GEN-LAST:event_detail_dne_tableMouseClicked

    private void ulozitZmena_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ulozitZmena_buttonActionPerformed
        if (checkChangesInTable()) {
            updateTable(nazev_sluzby.getText(), date_field.getText());
        }
    }//GEN-LAST:event_ulozitZmena_buttonActionPerformed

    private void date_fieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_date_fieldCaretUpdate
        String formatedDate = date_field.getText();
        if (formatedDate.length() > 0) {
            updateTable(nazev_sluzby.getText(), formatedDate);
        }
    }//GEN-LAST:event_date_fieldCaretUpdate
    private String item;
    private SluzbyModel modelSluzby;
    private ZakaznikModel modelZakaznik;
    private Map<Integer, Integer> customer_databaseIdToComboBoxId;
    private List<Map<Object, Object[]>> refTableData;
    private String[] tmp = {null, "asdasdasd", "2", "3", "4", "5"};
    private JComboBox comboBox;
    private String[] comboBoxItems;
    private DefaultTableModel model;
    private Object[][] defaultValue;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private cz.vutbr.fit.pdb.utils.ObservingTextField date_field;
    private javax.swing.JLabel den_label;
    private javax.swing.JTable detail_dne_table;
    private cz.vutbr.fit.pdb.gui.HotelCompoundPanel hotelCompoundPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel kalendar;
    private javax.swing.JLabel nazev_sluzby;
    private javax.swing.JPanel panel_sluzby;
    private javax.swing.JScrollPane sluzbyScroll_kontejner;
    private javax.swing.JButton ulozitZmena_button;
    private javax.swing.JPanel wrapper;
    private javax.swing.JPanel zoom_panel;
    // End of variables declaration//GEN-END:variables
}
