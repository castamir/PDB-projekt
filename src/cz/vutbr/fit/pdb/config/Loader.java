package cz.vutbr.fit.pdb.config;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
public class Loader {

    private Properties properties;

    public Properties getProperties() {
        if (properties == null) {
            Properties prop1 = parsePropertiesFrom("config.properties");
            Properties prop2 = parsePropertiesFrom("config.local.properties");

            prop1.putAll(prop2);

            properties = prop1;
        }
        return properties;
    }
    
    private Properties parsePropertiesFrom(String configFileName) {
            Properties prop = new Properties();
            try {
                prop.load(getClass().getResourceAsStream(configFileName));
            } catch (IOException ex) {
                Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, configFileName + " not found", ex);
            }
            return prop;
    }
}
