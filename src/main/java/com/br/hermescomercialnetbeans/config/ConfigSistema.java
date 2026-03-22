
package com.br.hermescomercialnetbeans.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigSistema {

    private static final Logger logger = LogManager.getLogger(ConfigSistema.class);
    
    public String getDataBase(){
        try {
            return "hermescomercial";
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
}
