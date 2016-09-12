/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBean;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author hanfengwei
 */
@Named(value = "pleaseDeleteAfterCreatingNewFiles1")
@RequestScoped
public class pleaseDeleteAfterCreatingNewFiles {
    private String dummy;

    /**
     * Creates a new instance of pleaseDeleteAfterCreatingNewFiles
     */
    public pleaseDeleteAfterCreatingNewFiles() {
    }
    
}
