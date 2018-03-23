package com.dena.platform.core.feature.security;

import com.dena.platform.common.config.DenaConfigReader;
import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.security.domain.User;
import com.dena.platform.core.feature.security.exception.UserManagementException;
import com.dena.platform.core.feature.security.service.SecurityService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

@Service("denaDenaUserManagementImpl")
public class DenaUserManagementImpl implements DenaUserManagement {

    private String userTypeName;

    @Resource
    private DenaDataStore denaDataStore;

    @Resource(name = "denaSecurityService")
    private SecurityService securityService;


    @PostConstruct
    public void init() {
        userTypeName = DenaConfigReader.readProperty("dena.UserManagement.user.type");
    }

    @Override
    public DenaObject registerUser(final String appId, final User user) {
        if (isUserExist(appId, user)) {
            throw new UserManagementException(String.format("User with this identity [%s] already exist", user.getEmail()), ErrorCode.USER_ALREADY_EXIST_EXCEPTION);
        }
        String encodedPassword = securityService.encodePassword(user.getUnencodedPassword());
        user.setPassword(encodedPassword);

        if (user.getActive() == null) {
            boolean isActive = DenaConfigReader.readBooleanProperty("dena.UserManagement.register.default_status", false);
            user.setActive(isActive);
        }


        DenaObject denaObject = new DenaObject();
        denaObject.addProperty(User.EMAIL_FIELD_NAME, user.getEmail());
        denaObject.addProperty(User.PASSWORD_FIELD_NAME, user.getPassword());
        denaObject.addProperty(User.IS_ACTIVE, user.getActive());
        denaObject.addFields(user.getOtherFields());

        DenaObject returnObject = denaDataStore.store(appId, userTypeName, denaObject).get(0);
        return returnObject;
    }

    @Override
    public boolean isUserExist(String appId, User user) {
        // todo: when we implement search capability in DanaStore module, then refactor this method to use it
        List<DenaObject> denaObjects = denaDataStore.findAll(appId, userTypeName, new DenaPager());
        Optional<DenaObject> foundUser = denaObjects.stream()
                .filter(denaObject -> denaObject.hasProperty(User.EMAIL_FIELD_NAME, user.getEmail()))
                .findAny();

        return foundUser.isPresent();

    }

}
