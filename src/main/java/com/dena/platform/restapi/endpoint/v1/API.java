package com.dena.platform.restapi.endpoint.v1;

import com.dena.platform.restapi.DenaRestProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = API.API_PATH, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class API {

    public final static String API_PATH = "/v1/";

    @Resource(name = "denaRestEntityProcessorImpl")
    protected DenaRestProcessor denaRestProcessor;


    /////////////////////////////////////////////
    //            User Management API
    /////////////////////////////////////////////

    @PostMapping(path = {"/{app-id}/users/register"}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity registerUser() {
        return denaRestProcessor.handleRegisterUser();
    }

    @PostMapping(path = {"/{app-id}/users/login"}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity login() {
        return denaRestProcessor.handleLoginUser();
    }

    @PostMapping(path = {"/{app-id}/users/logout"}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity logout() {
        return denaRestProcessor.handleLogoutUser();
    }


    /////////////////////////////////////////////
    //            Schema Management API
    /////////////////////////////////////////////

    /**
     * Create table in application.
     * @return
     */
    @PostMapping(path = {"/{app-id}/schema/{table-name}"})
    public ResponseEntity createTable() {
        return denaRestProcessor.handleCreateSchema();
    }


    @GetMapping(path = {"/{app-id}/schema"})
    public ResponseEntity getAllTable() {
        return denaRestProcessor.handleGetAllSchema();
    }


    @DeleteMapping(path = {"/{app-id}/schema/{table-name}"})
    public ResponseEntity deleteTable() {
        return denaRestProcessor.handleDeleteSchema();
    }


    /////////////////////////////////////////////
    //            Data Store API
    /////////////////////////////////////////////

    /**
     * Create new object in data store. This webservice may also create relation between objects
     *
     * @return number of created objects
     */
    @PostMapping(path = "/{app-id}/{table-name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createObjects() {
        return denaRestProcessor.handleCreateObject();
    }


    /**
     * Update (Merge) object in data store. calling this web service can lead to new field or relation creation.
     *
     * @return number of updated objects
     */

    @PatchMapping(path = "/{app-id}/{table-name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity mergeUpdateObjects() {
        return denaRestProcessor.handleUpdateObject();
    }

    /**
     * Update (Replace) object in data store. calling this web service can lead to new field or relation creation.
     *
     * @return number of updated objects
     */
    @PutMapping(path = "/{app-id}/{table-name}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity replaceUpdateObjects() {
        return denaRestProcessor.handleUpdateObject();
    }

    /**
     * Delete one or many objects
     *
     * @return
     */
    @DeleteMapping(path = "/{app-id}/{table-name}/{object-id}")
    public ResponseEntity deleteObjects() {
        return denaRestProcessor.handleDeleteObject();

    }

    /**
     * Delete only relation between (parent --> child)
     *
     * @return
     */
    @DeleteMapping(path = {
            "/{app-id}/{table-name}/{object-id}/relation/{table-name-2}/{object-id-2}",
            "/{app-id}/{table-name}/{object-id}/relation/{table-name-2}"})
    public ResponseEntity deleteRelationWithObjectId() {
        return denaRestProcessor.handleDeleteRelation();
    }

    /**
     * Find object with id or find related objects
     *
     * @return
     */
    @GetMapping(path = {
            "/{app-id}/{table-name}/{object-id}",
            "/{app-id}/{table-name}",
            "/{app-id}/{table-name}/{object-id}/relation/{relation-name}"})
    public ResponseEntity findObject() {
        return denaRestProcessor.handleFindObject();

    }

    /////////////////////////////////////////////
    //            Application Management API
    /////////////////////////////////////////////
    @PostMapping(path = {"/app/register"}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity registerApp() {
        return denaRestProcessor.handleRegisterApplication();
    }


    /**
     * Search object by a text based search tools
     *
     * @return list of DenaObjects
     */
    @GetMapping(path = {"/{app-id}/{type-name}/{user-name}/search/{query-string}"})
    public ResponseEntity searchObject() {
        return denaRestProcessor.handleSearch();
    }





}
