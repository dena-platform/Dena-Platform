package com.dena.platform.rest.persistence.store;

import com.dena.platform.core.feature.user.domain.User;
import com.dena.platform.core.feature.user.service.DenaUserManagement;
import com.dena.platform.test.ObjectModelHelper;
import com.dena.platform.utils.CommonConfig;
import com.mongodb.MongoClient;
import junitparams.JUnitParamsRunner;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.keyvalue.DefaultMapEntry;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.*;

import static com.dena.platform.utils.CommonConfig.DENA_APPLICATION_INFO_COLLECTION_NAME;
import static com.dena.platform.utils.JSONMapper.createObjectFromJSON;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */

@AutoConfigureMockMvc
@RunWith(JUnitParamsRunner.class)
@SpringBootTest
public class AbstractDataStoreTest {
    protected final String objectId1 = "5a316b1b4e5f450104c31909";
    protected final String objectId2 = "5a1bd6176f017921441d4a50";
    protected final String objectId3 = "5a206dafcc2a9b26e483d663";
    protected final String objectId4 = "5aaa11d2ecb1ef188094eed6";
    protected final String objectId5 = "5aaa445ebb19df061c79f8f0";
    protected final String objectId6 = "5aaa445ebb19df061c79f8f1";
    protected final String objectId7 = "5aaa4460bb19df061c79f8f2";
    protected final String objectId8 = "5aaa4460bb19df061c79f8f3";
    protected final String objectId9 = "5aaa8234bb19df25acce463d";
    protected final String objectId10 = "5aaa8234bb19df25acce463e";
    protected final String objectId11 = "5ab557484611681f7c07a6dd";

    protected final String randomObjectId = ObjectId.get().toHexString();

    protected User user = ObjectModelHelper.getSampleUser();

    protected final String SECRET_KEY = UUID.randomUUID().toString();


    // for parametrize test runner
    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Resource
    protected MockMvc mockMvc;

    @Resource
    protected MongoClient mongoClient;

    @Resource
    private DenaUserManagement userManagement;

    @Before
    public void setup() {

        //////////////////////////////////////////////////////
        //       Initialize database
        //////////////////////////////////////////////////////

        mongoClient.getDatabase(CommonConfig.APP_ID).drop();
        mongoClient.getDatabase(CommonConfig.DENA_APPLICATION).drop();

        Document document1 = createDocument(objectId1, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document2 = createDocument(objectId2, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));


        Document document3 = createDocument(objectId3, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document relatedDocument = new Document("relation_type", "ONE-TO-ONE")
                .append("target_name", CommonConfig.TABLE_NAME)
                .append("ids", Arrays.asList(new ObjectId(objectId1), new ObjectId(objectId2)));

        document3.put(CommonConfig.RELATION_NAME, relatedDocument);


        Document document4 = createDocument(objectId4, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document5 = createDocument(objectId5, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document6 = createDocument(objectId6, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document7 = createDocument(objectId7, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document8 = createDocument(objectId8, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document9 = createDocument(objectId9, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));

        Document document10 = createDocument(objectId10, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));
        Document document11 = createDocument(objectId11, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("name", "javad"),
                        new DefaultMapEntry<>("job", "developer")
                }
        ));


        // register main application
        Document denaApplication = createDocument(objectId11, MapUtils.putAll(new HashMap<>(), new Map.Entry[]{
                        new DefaultMapEntry<>("creator_id", this.user.getEmail()),
                        new DefaultMapEntry<>("application_name", CommonConfig.APP_NAME),
                        new DefaultMapEntry<>("application_id", CommonConfig.APP_ID),
                        new DefaultMapEntry<>("secret_key", SECRET_KEY)
                }
        ));

        mongoClient.getDatabase(CommonConfig.DENA_APPLICATION)
                .getCollection(DENA_APPLICATION_INFO_COLLECTION_NAME)
                .insertOne(denaApplication);

        mongoClient.getDatabase(CommonConfig.APP_ID)
                .getCollection(CommonConfig.TABLE_NAME)
                .insertMany(Arrays.asList(document1, document2, document3, document4,
                        document5, document6, document7, document8,
                        document9, document10, document11
                ));

        userManagement.registerUser(CommonConfig.APP_ID, this.user);


    }

    /////////////////////////////////////////////
    //            DATA ACCESS REQUEST
    /////////////////////////////////////////////

    protected <T> T performFindRequestByObjectId(String objectId1, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.BASE_URL + "/" + objectId1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);
    }

    protected <T> T performFindRequestInTable(String tableName, int startIndex, int pageSize, Class<T> klass) throws Exception {
        String URITemplate = CommonConfig.ROOT_URL + CommonConfig.APP_ID + "/" + tableName;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(URITemplate)
                .queryParam("startIndex", startIndex)
                .queryParam("pageSize", pageSize);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uriComponentsBuilder.toUriString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);
    }


    protected <T> T performFindRelationRequest(String objectId, String relationName, int startIndex,
                                               int pageSize, Class<T> klass) throws Exception {
        String URITemplate = CommonConfig.BASE_URL + "/" + objectId + "/relation/" + relationName;
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(URITemplate)
                .queryParam("startIndex", startIndex)
                .queryParam("pageSize", pageSize);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uriComponentsBuilder.toUriString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);
    }

    protected <T> T performDeleteRequest(List<String> objectList, int status, Class<T> klass) throws Exception {
        String objectIds = String.join(",", objectList);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(CommonConfig.BASE_URL + "/" + objectIds))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);
    }

    protected <T> T performDeleteRelationWithObject(String parentObjectId, String relationName, int status, String targetObjectId, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(CommonConfig.BASE_URL + "/" + parentObjectId + "/relation/" + relationName + "/" + targetObjectId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    protected <T> T performDeleteRelation(String type1, String relationName, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(CommonConfig.BASE_URL + "/" + type1 + "/relation/" + relationName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    protected <T> T performMergeObject(String body, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }


    protected <T> T performUpdateObject(String body, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    protected <T> T performUpdateObject(String body, int status, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    protected <T> T performCreateObject(String body, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }

    protected <T> T performCreateObject(String body, int status, Class<T> klass) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(CommonConfig.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        return createObjectFromJSON(returnContent, klass);

    }


    protected <T> T performSearchWithToken(String username, String query, Class<T> klass, String token) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CommonConfig.BASE_URL + "/" + username + "/search/" + query)
                .header("token", token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String returnContent = result.getResponse().getContentAsString();
        if (!StringUtils.isEmpty(returnContent))
            return createObjectFromJSON(returnContent, klass);
        else
            return null;

    }

    private Document createDocument(String objectId, Map<String, ?> parameters) {
        Document document = new Document();
        document.put("_id", new ObjectId(objectId));
        document.putAll(parameters);
        document.put("object_uri", "/" + CommonConfig.TABLE_NAME + "/" + objectId);

        return document;

    }


}
