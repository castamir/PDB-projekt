
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
    
    public void setNewIcon(ImageIcon i){
        ic = i;
    }
    
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
    
    public boolean canFocus() {
        return focusable;
    }
    
    public void setFocus(boolean f) {
        focusable = f;
    }
    
    public boolean isActive(){
        return active;
    }
    
    public void setIndex(int i) {
        this.index = i;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setPath(String p){
        this.path = p;
    }
    
    public String getPath(){
        return this.path;
    }
    
    public ImageIcon getMyIcon(){
        return this.ic;
    }
    
    public void setScore(Double s){
        this.score = s;
    }
    
    public Double getScore(){
        return this.score;
    }
    
    public String getScoreAsString(){
        return String.valueOf(this.score);
    }
    
    public void setActive(boolean a) {
        this.active = a;
    }
}