package com.bizseer.auth.config;

import com.bizseer.auth.constant.AuthType;
import lombok.ToString;
import org.springframework.stereotype.Component;

@ToString
@Component
public class Config {

    /**
     * assignment for testing
     */
    public String mongoUri = "mongodb://127.0.0.1:27017";

    public String mongoDB = "auth";

    public AuthType authType = AuthType.SELF;

}
