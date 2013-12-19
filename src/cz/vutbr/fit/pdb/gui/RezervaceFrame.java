package cz.vutbr.fit.pdb.gui;

import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * Třída pro rezervace
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class RezervaceFrame extends JFrame {

    private PrehledRezervaci pz;

    /**
     *
     * @param prehledRezervaci
     * @throws HeadlessException
     */
    public RezervaceFrame(PrehledRezervaci prehledRezervaci) throws HeadlessException {
        pz = prehledRezervaci;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pz.updateTable();
            }
        });
    }
}
