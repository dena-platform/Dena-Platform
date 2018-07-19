package com.dena.platform.rest.persistence.schema;

import com.dena.platform.rest.dto.TestDenaResponseDTO;
import com.dena.platform.rest.dto.TestObjectResponseDTO;
import com.dena.platform.rest.persistence.AbstractDataStoreTest;
import com.dena.platform.restapi.dto.response.DenaObjectResponse;
import com.dena.platform.restapi.dto.response.DenaResponse;
import com.dena.platform.utils.CommonConfig;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Objects;

import static com.dena.platform.utils.JSONMapper.createJSONFromObject;
import static com.dena.platform.utils.JSONMapper.createObjectFromJSON;
import static com.dena.platform.utils.TestUtils.isTimeEqualRegardlessOfSecond;
import static org.junit.Assert.assertTrue;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@TestPropertySource(properties = {"dena.api.security.enabled=false"})
public class SchemaManagement extends AbstractDataStoreTest {

    @Test
    public void test_Create_Table() throws Exception {

        /////////////////////////////////////////////
        //         Send Create New Table Request
        /////////////////////////////////////////////

        DenaResponse actualReturnObject = performCreateTable("table1", DenaResponse.class);

        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.createTableCount = 1;
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        assertTrue(isTimeEqualRegardlessOfSecond(expectedReturnObject.timestamp, Instant.now().toEpochMilli()));
        JSONAssert.assertEquals(createJSONFromObject(expectedReturnObject), createJSONFromObject(actualReturnObject), false);

    }

    @Test
    public void test_Get_All_Schema() throws Exception {
        /////////////////////////////////////////////
        //         Send Get All Schema
        /////////////////////////////////////////////
        performCreateTable("table1", DenaResponse.class);
        DenaResponse actualReturnObject = performReadTableSchema(DenaResponse.class);

        TestDenaResponseDTO expectedReturnObject = new TestDenaResponseDTO();
        expectedReturnObject.timestamp = actualReturnObject.getTimestamp();


        boolean isTableFoundInResponseObject = false;
        for (DenaObjectResponse denaObjectResponse : actualReturnObject.getDenaObjectResponseList()) {
            if (denaObjectResponse.getFields().get("name").equals("table1") &&
                    Objects.equals(denaObjectResponse.getFields().get("record_count(s)"), 0)) {
                isTableFoundInResponseObject = true;
                break;
            }
        }

        /////////////////////////////////////////////
        //            Assert Found Object
        /////////////////////////////////////////////
        assertTrue(isTimeEqualRegardlessOfSecond(expectedReturnObject.timestamp, Instant.now().toEpochMilli()));
        assertTrue(isTableFoundInResponseObject);

    }


    protected <T> T performCreateTable(String tableName, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.CREATE_TABLE_URL + tableName)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    protected <T> T performReadTableSchema(Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.GET_ALL_TABLE_SCHEMA_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }


}
