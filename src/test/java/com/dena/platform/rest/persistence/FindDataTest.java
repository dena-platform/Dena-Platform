package com.dena.platform.rest.persistence;

import com.dena.platform.rest.dto.TestDenaResponseDTO;
import com.dena.platform.rest.dto.TestObjectResponseDTO;
import com.dena.platform.rest.dto.TestDenaRelationDTO;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.assertTrue;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class FindDataTest extends AbstractDataStoreTest {

    @Test
    public void test_FindObject_When_Object_Exist() throws Exception {

        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performFindRequest(objectId3);

        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.createObjectCount = 1L;
        expectedReturnObject.timestamp = actualReturnObject.timestamp;

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////   
        TestObjectResponseDTO testObjectResponseDTO = new TestObjectResponseDTO();
        testObjectResponseDTO.objectId = objectId3;
        testObjectResponseDTO.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId3;
        testObjectResponseDTO.addProperty("name", "javad");
        testObjectResponseDTO.addProperty("job", "developer");
        expectedReturnObject.setTestObjectResponseDTOList(Collections.singletonList(testObjectResponseDTO));

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), true);

    }

    @Test
    public void test_FindRelatedObject() throws Exception {
        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performFindRelationRequest(objectId3, CommonConfig.COLLECTION_NAME);

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.createObjectCount = 2L;
        expectedReturnObject.timestamp = actualReturnObject.timestamp;


        TestObjectResponseDTO testObjectResponseDTO1 = new TestObjectResponseDTO();
        testObjectResponseDTO1.objectId = objectId1;
        testObjectResponseDTO1.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId1;
        testObjectResponseDTO1.addProperty("name", "javad");
        testObjectResponseDTO1.addProperty("job", "developer");

        TestObjectResponseDTO testObjectResponseDTO2 = new TestObjectResponseDTO();
        testObjectResponseDTO2.objectId = objectId2;
        testObjectResponseDTO2.objectURI = "/" + CommonConfig.COLLECTION_NAME + "/" + objectId2;
        testObjectResponseDTO2.addProperty("name", "javad");
        testObjectResponseDTO2.addProperty("job", "developer");

        expectedReturnObject.setTestObjectResponseDTOList(Arrays.asList(testObjectResponseDTO1, testObjectResponseDTO2));


        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }

    @Test
    public void test_FindObject_When_Object_Not_Exist() throws Exception {

        /////////////////////////////////////////////
        //            Send Find Object Request
        /////////////////////////////////////////////
        TestDenaResponseDTO actualReturnObject = performFindRequest(randomObjectId);

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.createObjectCount = 0L;
        expectedReturnObject.timestamp = actualReturnObject.timestamp;

        // check timestamp field of returned object
        assertTrue(isTimeEqualRegardlessOfSecond(actualReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), JSONCompareMode.NON_EXTENSIBLE);

    }

}
