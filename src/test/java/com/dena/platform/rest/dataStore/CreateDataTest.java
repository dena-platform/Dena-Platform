package com.dena.platform.rest.dataStore;

import com.dena.platform.rest.dto.*;
import com.dena.platform.utils.CommonConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.assertTrue;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateDataTest extends AbstractDataStoreTest {
    @Test
    public void test_CreateObject() throws Exception {

        /////////////////////////////////////////////
        //           Send Create Object Request
        /////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.addProperty("job", "new developer");
        requestObject.addProperty("name", "developer");
        requestObject.getRelatedObjects().add(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME));

        ReturnedObject actualReturnObject = performCreateObject(createJSONFromObject(requestObject));

        /////////////////////////////////////////////
        //            Assert Create Object Response
        /////////////////////////////////////////////
        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.addProperty("job", "new developer");
        testObjectResponse.addProperty("name", "developer");
        testObjectResponse.testRelatedObjects = Collections.singletonList(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME));


        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(testObjectResponse));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }


    @Test
    public void test_CreateObject_When_Relation_Is_Invalid() throws Exception {
        /////////////////////////////////////////////////////////////////
        //           Send Create Object Request - object id not exist
        /////////////////////////////////////////////////////////////////
        TestRequestObject requestObject = new TestRequestObject();
        requestObject.addProperty("job", "new developer");
        requestObject.addProperty("name", "developer");
        requestObject.getRelatedObjects().add(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME));

        ReturnedObject actualReturnObject = performCreateObject(createJSONFromObject(requestObject));

        /////////////////////////////////////////////////////////////////
        //            Assert Create Object Response - object id not exist
        /////////////////////////////////////////////////////////////////
        TestObjectResponse testObjectResponse = new TestObjectResponse();
        testObjectResponse.addProperty("job", "new developer");
        testObjectResponse.addProperty("name", "developer");
        testObjectResponse.testRelatedObjects = Collections.singletonList(new TestRelatedObject(objectId1, CommonConfig.COLLECTION_NAME));


        ReturnedObject expectedReturnObject = new ReturnedObject();
        expectedReturnObject.setTimestamp(actualReturnObject.getTimestamp());
        expectedReturnObject.setCount(1L);
        expectedReturnObject.setTestObjectResponseList(Collections.singletonList(testObjectResponse));

        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.getTimestamp(), Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);


    }


}
