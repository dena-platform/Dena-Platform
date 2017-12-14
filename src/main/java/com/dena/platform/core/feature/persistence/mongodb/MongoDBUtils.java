package com.dena.platform.core.feature.persistence.mongodb;

import com.dena.platform.core.feature.persistence.DenaPager;
import com.mongodb.MongoClient;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@Component
public class MongoDBUtils {
    private final static Logger log = LoggerFactory.getLogger(MongoDBUtils.class);
    private static MongoClient mongoClient;

    public static final String ID = "_id";
    public static final String APP_NAME = "app_name";
    public static final String TYPE_NAME = "type_name";

    @Autowired
    public MongoDBUtils(MongoClient mongoClient) {
        MongoDBUtils.mongoClient = mongoClient;
    }


    public static MongoDatabase getDataBase(final String databaseName) {
        Assert.notNull(databaseName, "database name should not be null");
        return mongoClient.getDatabase(databaseName);

    }

    public static void createDocument(MongoDatabase mongoDatabase, String collectionName, List<? extends Document> document) {
        Assert.hasLength(collectionName, "collection should not be empty or null ");
        Assert.notEmpty(document, "Document should not be null");

        mongoDatabase
                .getCollection(collectionName)
                .insertMany(document);

        log.info("Creating document(s) [{}] successfully", document);
    }

    public static void updateDocument(MongoDatabase mongoDatabase, String collectionName, List<? extends Document> documents) {
        Assert.hasLength(collectionName, "collection should not be empty or null ");
        Assert.notEmpty(documents, "MongoClient should not be null");

        ArrayList<WriteModel<Document>> updates = new ArrayList<>();
        documents.forEach(document -> {
            Document id = new Document(ID, document.get(ID));
            Document data = new Document("$set", document);
            UpdateOneModel<Document> updateOneModel = new UpdateOneModel<>(id, data);
            updates.add(updateOneModel);
        });

        BulkWriteResult res = mongoDatabase
                .getCollection(collectionName)
                .bulkWrite(updates, new BulkWriteOptions().ordered(true));

        log.info("Updates: [{}] document(s) count", res.getModifiedCount());
    }

    public static long deleteDocument(MongoDatabase mongoDatabase, String collectionName, List<String> documentIds) {
        Assert.hasLength(collectionName, "collection should not be empty or null");
        Assert.notEmpty(documentIds, "id should not be empty");

        List<ObjectId> objectIdList = documentIds.stream().map(ObjectId::new).collect(Collectors.toList());

        DeleteResult deleteResult = mongoDatabase.getCollection(collectionName).deleteMany(Filters.in("_id", objectIdList));
        log.info("Deletes: [{}] document(s) count", deleteResult.getDeletedCount());
        return deleteResult.getDeletedCount();
    }

    public static long deleteRelationWithObjectId(MongoDatabase mongoDatabase, String typeName1, String objectId1, String typeName2, String objectId2) {
        Document searchDocument = new Document("_id", new ObjectId(objectId1));
        Document update = new Document(typeName2, new ObjectId(objectId2));

        UpdateResult updateResult = mongoDatabase
                .getCollection(typeName1)
                .updateOne(searchDocument, new Document("$pull", update));


        log.info("Updates: [{}] document(s) count", updateResult.getModifiedCount());
        return updateResult.getModifiedCount();
    }

    public static long deleteRelationWithType(MongoDatabase mongoDatabase, String typeName1, String objectId1, String typeName2) {
        Document searchDocument = new Document("_id", new ObjectId(objectId1));
        Document update = new Document(typeName2, "");

        UpdateResult updateResult = mongoDatabase
                .getCollection(typeName1)
                .updateOne(searchDocument, new Document("$unset", update));


        log.info("Updates: [{}] document(s) count", updateResult.getModifiedCount());
        return updateResult.getModifiedCount();
    }


    public static Optional<Document> findDocumentById(MongoDatabase mongoDatabase, String collectionName, String id) {
        Document document = mongoDatabase.getCollection(collectionName)
                .find(Filters.eq("_id", new ObjectId(id)))
                .first();

        return Optional.ofNullable(document);
    }

    @SuppressWarnings("unchecked")
    public static List<Document> findRelatedDocument(MongoDatabase mongoDatabase, Document parentDocument, String targetType, DenaPager pager) {
        List<ObjectId> otherObjectIds = (ArrayList<ObjectId>) parentDocument.get(targetType);
        Bson searchDocument = Filters.in("_id", otherObjectIds);

        int startIndex = (int) pager.getCount() * pager.getLimit();

        pager.setCount(otherObjectIds.size());
        List<Document> documentList = mongoDatabase.getCollection(targetType)
                .find(searchDocument)
                .skip(startIndex)
                .batchSize(pager.getLimit())
                .limit(pager.getLimit())
                .into(new ArrayList<>());


        return documentList;

    }

}