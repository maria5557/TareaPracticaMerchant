package com.example.merchant.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.merchant.entity.Merchant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<Merchant> allMerchants = dynamoDBMapper.scan(Merchant.class, new DynamoDBScanExpression());
        return allMerchants.stream()
                .filter(merchant -> merchant.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}