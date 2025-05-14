package com.example.merchant.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.merchant.entity.Merchant;
import com.example.merchant.service.MerchantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class MerchantRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public Merchant save(Merchant merchant) {
        dynamoDBMapper.save(merchant);
        return merchant;
    }

    public Merchant findById(String id) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":id", new AttributeValue().withS(id));

        DynamoDBQueryExpression<Merchant> queryExpression = new DynamoDBQueryExpression<Merchant>()
                .withIndexName("IdIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("id = :id")
                .withExpressionAttributeValues(eav);

        List<Merchant> merchants = dynamoDBMapper.query(Merchant.class, queryExpression);

        return merchants.isEmpty() ? null : merchants.get(0);
    }

    public List<Merchant> findByName(String name) {

        // Convertimos el nombre a minúsculas para la búsqueda
        String lowerCaseName = name.toLowerCase();

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":pk", new AttributeValue().withS("merchantEntity"));
        expressionAttributeValues.put(":name", new AttributeValue().withS(lowerCaseName));

        Map<String, String> expressionAttributeNames = new HashMap<>();
        expressionAttributeNames.put("#nameLowerCase", "nameLowerCase");

        // Crear la expresión de la consulta
        DynamoDBQueryExpression<Merchant> queryExpression = new DynamoDBQueryExpression<Merchant>()
                .withKeyConditionExpression("PK = :pk")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withExpressionAttributeNames(expressionAttributeNames)
                .withFilterExpression("contains(#nameLowerCase, :name)")
                .withConsistentRead(false);

        // Realizamos la consulta
        return dynamoDBMapper.query(Merchant.class, queryExpression);
    }


    public void delete(Merchant m) {
        dynamoDBMapper.delete(m);
    }

    public List<Merchant> findAllMerchants() {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":pk", new AttributeValue().withS("merchantEntity"));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("PK = :pk")
                .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(Merchant.class, scanExpression);
    }

}