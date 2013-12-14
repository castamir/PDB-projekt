
package cz.vutbr.fit.pdb.gui;

import javax.swing.JLabel;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class myIcon extends JLabel{

    public myIcon(){
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myIconMouseClicked(evt);
            }
        });
        setText("");
        //setSize(60, 60);
    }
    
    public void myIconMouseClicked(java.awt.event.MouseEvent evt){
        System.out.println("Klik my Icon");
    }
}