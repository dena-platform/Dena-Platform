package com.dena.platform.utils;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

public interface CommonConfig {
    String ROOT_URL = "/v1/";

    String APP_NAME = "denaTestDB";

    String APP_ID = "4584-9652-9623-9863";

    String DENA_APPLICATION = "DENA_APPLICATIONS";

    String DENA_APPLICATION_INFO_COLLECTION_NAME = "DENA_APPLICATION_INFO";

    String TABLE_NAME = "denaTestCollection";

    String RELATION_NAME = "test_relation_name";

    String BASE_URL = ROOT_URL + APP_ID + "/" + TABLE_NAME;

    String REGISTER_USER_URL = ROOT_URL + APP_ID + "/users/register";

    String CREATE_TABLE_URL = ROOT_URL + APP_ID + "/schema/";

    String GET_ALL_TABLE_SCHEMA_URL = ROOT_URL + APP_ID + "/schema";

    String DELETE_TABLE_SCHEMA_URL = ROOT_URL + APP_ID + "/schema/";

    String LOGIN_URL = ROOT_URL + APP_ID + "/users/login";

    String LOGOUT_URL = ROOT_URL + APP_ID + "/users/logout";

    String REGISTER_APPLICATION_URL = ROOT_URL + "/app/register";

}
