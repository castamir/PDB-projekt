
package cz.vutbr.fit.pdb.gui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

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
     *
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
     *
     * @param i
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
     *
     * @param i
     */
    public void setNewIcon(ImageIcon i){
        ic = i;
    }
    
    
    /**
     *
     * @param evt
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
     * @return
     */
    public boolean canFocus() {
        return focusable;
    }
    
    /**
     *
     * @param f
     */
    public void setFocus(boolean f) {
        focusable = f;
    }
    
    /**
     *
     * @return
     */
    public boolean isActive(){
        return active;
    }
    
    /**
     *
     * @param i
     */
    public void setIndex(int i) {
        this.index = i;
    }
    
    /**
     *
     * @return
     */
    public int getIndex() {
        return this.index;
    }
    
    /**
     *
     * @param p
     */
    public void setPath(String p){
        this.path = p;
    }
    
    /**
     *
     * @return
     */
    public String getPath(){
        return this.path;
    }
    
    /**
     *
     * @return
     */
    public ImageIcon getMyIcon(){
        return this.ic;
    }
    
    /**
     *
     * @param s
     */
    public void setScore(Double s){
        this.score = s;
    }
    
    /**
     *
     * @return
     */
    public Double getScore(){
        return this.score;
    }
    
    /**
     *
     * @return
     */
    public String getScoreAsString(){
        return String.valueOf(this.score);
    }
    
    /**
     *
     * @param a
     */
    public void setActive(boolean a) {
        this.active = a;
    }
}