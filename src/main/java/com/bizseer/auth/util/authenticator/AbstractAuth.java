package com.bizseer.auth.util.authenticator;

import org.json.JSONObject;

import java.util.Map;

public interface AbstractAuth{
    Map<String, Object> loadUserByObject(JSONObject object);
}
