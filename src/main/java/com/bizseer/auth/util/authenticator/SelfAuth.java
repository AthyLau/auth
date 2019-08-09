package com.bizseer.auth.util.authenticator;

import com.bizseer.auth.constant.AuthRoleType;
import com.bizseer.auth.constant.ConstAuth;
import com.bizseer.auth.service.AuthService;
import com.bizseer.auth.util.database.document.DocumentDBHelper;
import com.bizseer.auth.util.exception.UnauthorizedException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.bizseer.auth.util.database.document.DocumentDBQueryBuilder.*;
import static com.bizseer.auth.util.database.document.MongoDBCollections.SELF_AUTH;

@Slf4j
public class SelfAuth implements AbstractAuth {

    private DocumentDBHelper docDBHelper;
    @Autowired
    private AuthService authService;
    public SelfAuth(DocumentDBHelper dbHelper) {
        this.docDBHelper = dbHelper;
        initFirstAdmin();
    }

    @Override
    public Map<String, Object> loadUserByObject(JSONObject object) {
        String username = object.getString(ConstAuth.USERNAME), password = object.getString(ConstAuth.PASSWORD);
        Map<String, Object> result = docDBHelper.getDocDB().findOne(SELF_AUTH, term(ConstAuth.USERNAME).eq(username), nonAggregate());
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
        if(!authService.register("aiops", "aiops", AuthRoleType.SUPER_ADMIN.getName())){
            log.debug("Init first Super Admin failed!");
        }
    }

    private static String getShaString(@NonNull String src) {
        return DigestUtils.sha256Hex(src);
    }
}
