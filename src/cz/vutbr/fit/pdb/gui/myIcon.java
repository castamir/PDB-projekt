
package cz.vutbr.fit.pdb.gui;

import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
    
    public myIcon(){
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myIconMouseClicked(evt);
            }
        });
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
        //setIcon(i);
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
}