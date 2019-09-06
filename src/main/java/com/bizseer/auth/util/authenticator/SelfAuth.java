package com.bizseer.auth.util.authenticator;

import com.bizseer.auth.constant.AuthRoleType;
import com.bizseer.auth.constant.ConstAuth;
import com.bizseer.auth.service.AuthService;
import com.bizseer.auth.util.database.document.DocumentDBHelper;
import com.bizseer.auth.util.exception.AuthException;
import com.bizseer.auth.util.exception.UnauthorizedException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.bizseer.auth.util.database.document.DocumentDBQueryBuilder.*;

@Slf4j
public class SelfAuth implements AbstractAuth {

    private DocumentDBHelper docDBHelper;

    private AuthService authService;

    public SelfAuth(DocumentDBHelper dbHelper,AuthService authService){
        this.docDBHelper = dbHelper;
        this.authService = authService;
        initFirstAdmin();
    }

    @Override
    public Map<String, Object> loadUserByObject(JSONObject object) {
        String username = object.getString(ConstAuth.USERNAME), password = object.getString(ConstAuth.PASSWORD);
        Map<String, Object> result = docDBHelper.getDocDB().findOne(ConstAuth.USER_TABLE_NAME, term(ConstAuth.USERNAME).eq(username), nonAggregate());
        if (result == null) {
            throw new UnauthorizedException();
        }
        if (getShaString(password).equals(result.get(ConstAuth.PASSWORD))) {
            result.remove(ConstAuth.PASSWORD);
            return result;
        }
        throw new UnauthorizedException();
    }

    private void initFirstAdmin() {
        try{
            authService.register("aiops", "aiops", AuthRoleType.SUPER_ADMIN.getName());
        }catch (AuthException e){
            log.debug("Init first Super Admin failed!"+e.getMessage());
        }

    }

    private static String getShaString(@NonNull String src) {
        return DigestUtils.sha256Hex(src);
    }
}
