package com.bizseer.auth.config;

import lombok.Getter;
import org.springframework.stereotype.Component;


@Component("ConfigHelper")
public class ConfigHelper {

    @Getter
    private static Config config = new Config();

    public static void setMongoInfo(String mongoAddress) {
        int lastSlashIndex = mongoAddress.lastIndexOf("/");
        config.mongoUri = mongoAddress.substring(0, lastSlashIndex);
        config.mongoDB = mongoAddress.substring(lastSlashIndex + 1);
    }
}
