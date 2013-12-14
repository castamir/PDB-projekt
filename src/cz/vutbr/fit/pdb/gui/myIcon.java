
package cz.vutbr.fit.pdb.gui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class myIcon extends JLabel{

    private boolean active = false;
    public myIcon(){
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myIconMouseClicked(evt);
            }
        });
        setText("");
        //setSize(60, 60);
    }
    
    public myIcon(Icon i){
       addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myIconMouseClicked(evt);
            }
        });
        setText("");
        setIcon(i);
    }
    
    public void myIconMouseClicked(java.awt.event.MouseEvent evt){
        System.out.println("Klik my Icon");
        if(!active){
            setBorder(BorderFactory.createLineBorder(Color.black));
            active = true;
        } else {
            active = false;
            setBorder(null);
        }
    }
    
    public boolean isActive(){
        return active;
    }
}