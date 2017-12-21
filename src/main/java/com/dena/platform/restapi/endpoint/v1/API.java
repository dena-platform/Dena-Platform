package com.dena.platform.restapi.endpoint.v1;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.core.DenaRequestContext;
import com.dena.platform.core.feature.persistence.exception.DataStoreException;
import com.dena.platform.restapi.DenaRestProcessor;
import com.dena.platform.restapi.exception.DenaRestException.DenaRestExceptionBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@RestController
@RequestMapping(value = API.API_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class API {

    public final static String API_PATH = "/v1/";

    @Resource(name = "denaRestEntityProcessorImpl")
    private DenaRestProcessor denaRestProcessor;


    @PostMapping(path = "/{app-id}/{type-name}")
    public ResponseEntity createObjects(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        return denaRestProcessor.handleCreateObject(denaRequestContext);
    }

    @PutMapping(path = "/{app-id}/{type-name}")
    public ResponseEntity updateObjects(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        return denaRestProcessor.handlePutRequest(denaRequestContext);
    }


    /**
     * Delete one or many specified objects
     *
     * @param request
     * @return
     */
    @DeleteMapping(path = "/{app-id}/{type-name}/{object-id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity deleteObjects(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        return denaRestProcessor.handleDeleteObject(denaRequestContext);

    }

    @DeleteMapping(path = {
            "/{app-id}/{type-name}/{object-id}/relation/{type-name-2}/{object-id-2}",
            "/{app-id}/{type-name}/{object-id}/relation/{type-name-2}"}
            , consumes = MediaType.ALL_VALUE)
    public ResponseEntity deleteRelationWithObjectId(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        return denaRestProcessor.handleDeleteRelation(denaRequestContext);
    }

    @GetMapping(path = {
            "/{app-id}/{type-name}/{object-id}",
            "/{app-id}/{type-name}/{object-id}/relation/{target-type}"},
            consumes = MediaType.ALL_VALUE)
    public ResponseEntity findObject(HttpServletRequest request) {
        DenaRequestContext denaRequestContext = new DenaRequestContext(request);
        return denaRestProcessor.handleFindObject(denaRequestContext);

    }

}
