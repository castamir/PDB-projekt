

package cz.vutbr.fit.pdb.models;

import cz.vutbr.fit.pdb.application.ServiceLocator;

/**
 *
 * @author Paulík Miroslav
 * @author Mikulica Tomáš
 * @author Gajdoš Pavel
 */
abstract class BaseModel {
   
    protected ServiceLocator serviceLocator;
    
    public BaseModel() {
        serviceLocator = new ServiceLocator();
    }
}
