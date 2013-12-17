
package cz.vutbr.fit.pdb.gui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class myIcon extends JLabel{

    private boolean active = false;
    private int index;
    private String path = null;
    private boolean focusable = true;
    private ImageIcon ic;
    private Double score = 0.0;
    
    /**
     * Konstuktor
     */
    public myIcon(){
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myIconMouseClicked(evt);
            }
        });
        setVerticalTextPosition(JLabel.BOTTOM);
        setHorizontalTextPosition(JLabel.CENTER);
        setText("");
        //setSize(60, 60);
    }
    
    /**
     * Konstruktor, ktery nastavi navic tridni promennou ic
     * @param i ikona
     */
    public myIcon(ImageIcon i){
       addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myIconMouseClicked(evt);
            }
        });
        setText("");
        setVerticalTextPosition(JLabel.BOTTOM);
        setHorizontalTextPosition(JLabel.CENTER);
        //setIcon(i);
        ic = i;
    }
    
    /**
     * Prenastavi aktualni ikonu
     * @param i ikona
     */
    public void setNewIcon(ImageIcon i){
        ic = i;
    }
    
    
    /**
     * Zpracovani kliku na ikonu
     * @param evt event
     */
    public void myIconMouseClicked(java.awt.event.MouseEvent evt){
        //System.out.println("Klik my Icon");
        if(focusable) {
            if(!active){
                setBorder(BorderFactory.createLineBorder(Color.red));
                active = true;
            } else {
                active = false;
                setBorder(null);
            }
        }
    }
    
    /**
     * 
     * @return focusable
     */
    public boolean canFocus() {
        return focusable;
    }
    
    /**
     * Nastavi focusable atribut
     * @param f nastaveni
     */
    public void setFocus(boolean f) {
        focusable = f;
    }
    
    /**
     * Vraci, zda je ikona aktivni
     * @return vraci zda je aktivni
     */
    public boolean isActive(){
        return active;
    }
    
    /**
     * Nastavi index ikony z DB
     * @param i index
     */
    public void setIndex(int i) {
        this.index = i;
    }
    
    /**
     * Vrati index ikony
     * @return index ikony
     */
    public int getIndex() {
        return this.index;
    }
    
    /**
     * Nastavi cestu pro nacteni obrazku do DB
     * @param p cesta
     */
    public void setPath(String p){
        this.path = p;
    }
    
    /**
     * Vraci cestu pro nacteni do db
     * @return cesta
     */
    public String getPath(){
        return this.path;
    }
    
    /**
     * Vrati aktualni ikonu
     * @return aktualni ikona
     */
    public ImageIcon getMyIcon(){
        return this.ic;
    }
    
    /**
     * Nastavi skore pro porovnani obrazku
     * @param s skore
     */
    public void setScore(Double s){
        this.score = s;
    }
    
    /**
     * Vrati skore
     * @return skore
     */
    public Double getScore(){
        return this.score;
    }
    
    /**
     * Vrati skore jako string
     * @return skore
     */
    public String getScoreAsString(){
        return String.valueOf(this.score);
    }
    
    /**
     * Nastavi, zda je ikona aktivni
     * @param a aktivni
     */
    public void setActive(boolean a) {
        this.active = a;
    }
}