package com.bizseer.auth.util.authenticator;

import com.bizseer.auth.config.Config;
import com.bizseer.auth.config.ConfigHelper;
import com.bizseer.auth.constant.AuthType;
import com.bizseer.auth.util.database.document.DocumentDBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：BruceChen
 * @date ：Created in 2019-03-07
 */

@Component("AuthFactory")
public class AuthFactory {

    @Autowired
    private DocumentDBHelper dbHelper;

    public AbstractAuth getAuthenticator(AuthType authType) {
        AbstractAuth authenticator;
        switch (authType) {
            case SELF:
                authenticator = new SelfAuth(dbHelper);
                break;
            default:
                authenticator = null;
        }
        return authenticator;
    }
}
