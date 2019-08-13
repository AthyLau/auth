package com.bizseer.auth.repository;

import com.bizseer.auth.constant.ConstAuth;
import com.bizseer.auth.constant.ConstTableNames;
import com.bizseer.auth.util.database.document.DocumentDBHelper;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.swing.text.DocumentFilter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.bizseer.auth.util.database.document.DocumentDBQueryBuilder.nonAggregate;
import static com.bizseer.auth.util.database.document.DocumentDBQueryBuilder.term;

/**
 * Function:
 *
 * @author liubing
 * Date: 2019/8/7 1:35 PM
 * @since JDK 1.8
 */
@Repository("AuthRepository")
public class AuthRepository {

    @Autowired
    private DocumentDBHelper documentDBHelper;


    public Map<String,Object> getUser(String username) {
        return documentDBHelper.getDocDB().findOne(ConstTableNames.USER_TABLE,term(ConstAuth.USERNAME).eq(username),nonAggregate());
    }

    public void register(String username, String password, String role) {
        String passwordSha = getShaString(password);
        Map<String,Object> user = new HashMap<String,Object>(){{
            put(ConstAuth.USERNAME,username);
            put(ConstAuth.PASSWORD_SHA,passwordSha);
            put(ConstAuth.ROLE,role);
            put(ConstAuth.STATE,"logout");
        }};
        documentDBHelper.getDocDB().insert(ConstTableNames.USER_TABLE,user);
    }

    public static String getShaString(String src) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digest != null;
        byte[] passwordShaByte = digest.digest(src.getBytes());
        return bytes2Hex(passwordShaByte);
    }

    private static String bytes2Hex(byte[] bts) {
        StringBuilder dst = new StringBuilder();
        String tmp;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                dst.append('0');
            }
            dst.append(tmp);
        }
        return dst.toString();
    }
}
