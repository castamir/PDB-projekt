package cz.vutbr.fit.pdb.gui;

import cz.vutbr.fit.pdb.models.ObrazkyModel;
import cz.vutbr.fit.pdb.models.ZakaznikModel;
import cz.vutbr.fit.pdb.utils.DatePicker;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import oracle.ord.im.OrdImage;

/**
 *
 * @author Doma
 */
public class Rezervace extends javax.swing.JPanel {

    /**
     * Creates new form Rezervace
     */
    public Rezervace() {
        initComponents();
        myInit();
    }

    public void myInit(){
        modelObr = new ObrazkyModel();
        modelZakaznik = new ZakaznikModel();
        fc = new JFileChooser();
        iconList = new ArrayList<myIcon>();
        layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        defaultSearchDir = "src/icons/";
        kraj_combobox.setModel(new DefaultComboBoxModel(comboBoxItems));
        //System.out.println(new File(defaultSearchDir).getAbsolutePath());
        vozidla_kontejner.setLayout(layout);
        rezervaceDo_field.setText(now());
        rezervaceOd_field.setText(now());
        //loadImagesFromDb();
    }
    
    public void loadImagesFromDb(){
        //System.out.println("load");
        try {
            icon = new ImageIcon(modelObr.getImage(lastInsertedImgId));
        } catch (SQLException ex) {
            Logger.getLogger(Rezervace.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jmeno_field = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        prijimeni_field = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        adresa_field = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        mesto_field = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        psc_field = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        kraj_combobox = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        telefon_field = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        email_field = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        parkovaciMisto_checkbox = new javax.swing.JCheckBox();
        pocetParkovacihMist_spinner = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        pridatFotoAuta_button = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        vozidla_kontejner = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        rezervaceOd_field = new cz.vutbr.fit.pdb.utils.ObservingTextField();
        kalendar_od = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        rezervaceDo_field = new cz.vutbr.fit.pdb.utils.ObservingTextField();
        kalendar_do = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        pocetPokoju_spinner = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        pocetOsob_spinner = new javax.swing.JSpinner();
        jPanel5 = new javax.swing.JPanel();
        vlozitRezervaci_button = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Kontaktní údaje"));

        jLabel1.setText("Jméno");

        jLabel2.setText("Přijímení");

        prijimeni_field.setText("Vozembouch");

        jLabel3.setText("Adresa");

        adresa_field.setText("Z depa, 46");

        jLabel4.setText("Město");

        mesto_field.setText("Depo");

        jLabel5.setText("PSČ");

        psc_field.setText("12345");

        jLabel6.setText("Zvolte kraj");

        kraj_combobox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel7.setText("Telefon");

        telefon_field.setText("777666555");

        jLabel8.setText("E-mail");

        email_field.setText("pepa@zdepa.cz");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jmeno_field)
            .addComponent(prijimeni_field)
            .addComponent(adresa_field)
            .addComponent(mesto_field)
            .addComponent(psc_field)
            .addComponent(kraj_combobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(telefon_field)
            .addComponent(email_field)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jmeno_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prijimeni_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adresa_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mesto_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(psc_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kraj_combobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(telefon_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(email_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Parkování"));

        parkovaciMisto_checkbox.setText("Parkovací místa?");
        parkovaciMisto_checkbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parkovaciMisto_checkboxActionPerformed(evt);
            }
        });

        pocetParkovacihMist_spinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        pocetParkovacihMist_spinner.setEnabled(false);

        jLabel9.setText("Počet");

        pridatFotoAuta_button.setText("Přidat fotografii vozidla");
        pridatFotoAuta_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pridatFotoAuta_buttonActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Fotografie"));
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout vozidla_kontejnerLayout = new javax.swing.GroupLayout(vozidla_kontejner);
        vozidla_kontejner.setLayout(vozidla_kontejnerLayout);
        vozidla_kontejnerLayout.setHorizontalGroup(
            vozidla_kontejnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 289, Short.MAX_VALUE)
        );
        vozidla_kontejnerLayout.setVerticalGroup(
            vozidla_kontejnerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(vozidla_kontejner);

        jButton1.setText("Odstraň fotografie");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(parkovaciMisto_checkbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pocetParkovacihMist_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(pridatFotoAuta_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(parkovaciMisto_checkbox)
                    .addComponent(pocetParkovacihMist_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pridatFotoAuta_button)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Období"));

        jLabel10.setText("Od");

        rezervaceOd_field.setText("observingTextField1");

        kalendar_od.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Calender Month.png"))); // NOI18N
        kalendar_od.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kalendar_odMouseClicked(evt);
            }
        });

        jLabel12.setText("Do");

        rezervaceDo_field.setText("observingTextField2");

        kalendar_do.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Calender Month.png"))); // NOI18N
        kalendar_do.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kalendar_doMouseClicked(evt);
            }
        });

        jLabel14.setText("Počet pokojů");

        pocetPokoju_spinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10, 1));

        jLabel15.setText("Počet osob");

        pocetOsob_spinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 2, 1));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rezervaceOd_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kalendar_od))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pocetPokoju_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rezervaceDo_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kalendar_do))
                    .addComponent(pocetOsob_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 43, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(rezervaceOd_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kalendar_od)
                    .addComponent(jLabel12)
                    .addComponent(rezervaceDo_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kalendar_do))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(pocetPokoju_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(pocetOsob_spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Pokoje"));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        vlozitRezervaci_button.setText("Vložit rezervaci");
        vlozitRezervaci_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vlozitRezervaci_buttonActionPerformed(evt);
            }
        });

        jButton3.setText("Zpět");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(vlozitRezervaci_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(vlozitRezervaci_button)
                            .addComponent(jButton3)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    private void pridatFotoAuta_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pridatFotoAuta_buttonActionPerformed
        // TODO add your handling code here:
        
        fc.setCurrentDirectory(new File(defaultSearchDir));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showOpenDialog(fc);
        myIcon ic = null;
 
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String path = "/icons/"+file.getName();
            //icon = new ImageIcon(getClass().getResource(path));
            //System.out.println("Opening: " + file.getName() + ".");
            System.out.println("Opening: " +path);
            try {
                lastInsertedImgId = modelObr.insertImage(new File(defaultSearchDir).getAbsolutePath()+"/"+file.getName());
                ic = new myIcon();
                ic.setIndex(lastInsertedImgId);
                System.out.println("posledni id z obrazku:" + ic.getIndex());
                System.out.println("posledni id z db:" + lastInsertedImgId);
                //System.out.println("/icons/"+file.getName());
            } catch (SQLException ex) {
                Logger.getLogger(Rezervace.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Cancelled by user.");
        }
        loadImagesFromDb();
        if(icon != null){
            ic.setIcon(icon);
            iconList.add(ic);
            vozidla_kontejner.add(iconList.get(iconList.size()-1));
            vozidla_kontejner.revalidate();
            //auti.setVisible(true);
            //auti.setIcon(icon);
        }
            //log.setCaretPosition(log.getDocument().getLength());
    }//GEN-LAST:event_pridatFotoAuta_buttonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //auti.setVisible(false);
        //auti.setIcon(null);
        it = iconList.listIterator();
        myIcon tmp;
        while(it.hasNext()){
            tmp = it.next();
            if(tmp.isActive()){
                tmp.setVisible(false);
                tmp.setIcon(null);
                it.remove();
                try {
                    modelObr.delete(tmp.getIndex());
                    //tmp.getIndex();
                    System.out.println("Chci smazat index: "+tmp.getIndex());
                } catch (SQLException ex) {
                    Logger.getLogger(Rezervace.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            //System.out.println(index);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /*
     * Osetrit:
     *          1) od 12.12.2013 od 3.4.2013
     *          2) Pocet osob a pokoju musi byt > 0
     */
    private void vlozitRezervaci_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vlozitRezervaci_buttonActionPerformed
        // TODO add your handling code here:
        String jmeno = jmeno_field.getText();
        String prijimeni = prijimeni_field.getText();
        String adresa = adresa_field.getText();
        String mesto = mesto_field.getText();
        String psc = psc_field.getText();
        String kraj = (String)kraj_combobox.getSelectedItem();
        String telefon = telefon_field.getText();
        String email = email_field.getText();
        String rezervaceOd = rezervaceOd_field.getText();
        String rezervaceDo = rezervaceDo_field.getText();
        // implicitně je to 0
        int pocetParkovacichMist = 0;
        int pocetPokoju = (int)pocetPokoju_spinner.getValue();
        int pocetOsob = (int)pocetOsob_spinner.getValue();;
        if(jmeno.equals("")|| prijimeni.equals("") || adresa.equals("") || mesto.equals("") || 
           psc.equals("")|| kraj.equals("") || telefon.equals("") || email.equals("")) {
            JOptionPane.showMessageDialog(getParent(), "Všechna pole musí být vyplněna, prosím vyplňte je!","Chyba",JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                modelZakaznik.insert(jmeno, prijimeni, adresa, mesto, psc, kraj, telefon, email);
            } catch (SQLException ex) {
                Logger.getLogger(Rezervace.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(parkovaciMisto_checkbox.isSelected()){
            pocetParkovacichMist = (int) pocetParkovacihMist_spinner.getValue();
            //System.out.println(pocetParkovacichMist);
        }
        System.out.println("Parkovacich mist: "+pocetParkovacichMist);
        System.out.println("Pokoju: "+pocetPokoju);
        System.out.println("Osob: "+pocetOsob);
        System.out.println("Od: "+rezervaceOd);
        System.out.println("Do: "+rezervaceDo);
        
    }//GEN-LAST:event_vlozitRezervaci_buttonActionPerformed

    private void parkovaciMisto_checkboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parkovaciMisto_checkboxActionPerformed
        if(parkovaciMisto_checkbox.isSelected()) {
            //System.out.println("Checked");
            pocetParkovacihMist_spinner.setEnabled(true);
        } else {
            //System.out.println("Unchecked");
            pocetParkovacihMist_spinner.setEnabled(false);
        }
    }//GEN-LAST:event_parkovaciMisto_checkboxActionPerformed

    private void kalendar_odMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kalendar_odMouseClicked
        String lang = null;
        final Locale locale = getLocale(lang);
        DatePicker dp = new DatePicker(rezervaceOd_field, locale);
        // previously selected date
        Date selectedDate = dp.parseDate(rezervaceOd_field.getText());
        dp.setSelectedDate(selectedDate);
        dp.start(rezervaceOd_field);
    }//GEN-LAST:event_kalendar_odMouseClicked

    private void kalendar_doMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kalendar_doMouseClicked
        String lang = null;
        final Locale locale = getLocale(lang);
        DatePicker dp = new DatePicker(rezervaceDo_field, locale);
        // previously selected date
        Date selectedDate = dp.parseDate(rezervaceDo_field.getText());
        dp.setSelectedDate(selectedDate);
        dp.start(rezervaceDo_field);
    }//GEN-LAST:event_kalendar_doMouseClicked

    private Locale getLocale(String loc) {
        if (loc != null && loc.length() > 0) {
            return new Locale(loc);
        } else {
            return Locale.US;
        }
    }
    
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
    
    private Integer lastInsertedImgId;
    //Create a file chooser
    private ObrazkyModel modelObr;
    private ZakaznikModel modelZakaznik;
    private JFileChooser fc;
    private ImageIcon icon;
    private String defaultSearchDir;
    FlowLayout layout;
    List<myIcon> iconList;
    ListIterator<myIcon> it;
    private final String comboBoxItems[] = {"Karlovarsky", "Ustecky","Liberecky",
                                            "Kralovehradecky", "Pardubicky", "Olomoucky",
                                            "Ostravsky", "Zlinsky", "Brnensky",
                                            "Jihlavsky", "Budejovicky", "Plzensky",
                                            "Stredocesky", "Hl. m. Praha"};
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField adresa_field;
    private javax.swing.JTextField email_field;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jmeno_field;
    private javax.swing.JLabel kalendar_do;
    private javax.swing.JLabel kalendar_od;
    private javax.swing.JComboBox kraj_combobox;
    private javax.swing.JTextField mesto_field;
    private javax.swing.JCheckBox parkovaciMisto_checkbox;
    private javax.swing.JSpinner pocetOsob_spinner;
    private javax.swing.JSpinner pocetParkovacihMist_spinner;
    private javax.swing.JSpinner pocetPokoju_spinner;
    private javax.swing.JButton pridatFotoAuta_button;
    private javax.swing.JTextField prijimeni_field;
    private javax.swing.JTextField psc_field;
    private cz.vutbr.fit.pdb.utils.ObservingTextField rezervaceDo_field;
    private cz.vutbr.fit.pdb.utils.ObservingTextField rezervaceOd_field;
    private javax.swing.JTextField telefon_field;
    private javax.swing.JButton vlozitRezervaci_button;
    private javax.swing.JPanel vozidla_kontejner;
    // End of variables declaration//GEN-END:variables
}
