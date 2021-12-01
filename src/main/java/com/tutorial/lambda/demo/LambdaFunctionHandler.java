package com.tutorial.lambda.demo;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<Request, Object> {

	@Override
	public Object handleRequest(Request request, Context context) {
		// TODO Auto-generated method stub
		//context.getLogger().log("Nombre: " + name);
		
		AmazonDynamoDB dynamoDbClient = AmazonDynamoDBClientBuilder.defaultClient();
		DynamoDBMapper dynamoMapper = new DynamoDBMapper(dynamoDbClient);
		Product product = null;
		
		switch (request.getHttpMethod()) {
		case "GET":
			if(request.getId()==0) {
				List<Product> products = new ArrayList<>();
				products = dynamoMapper.scan(Product.class, new DynamoDBScanExpression());
				return products;
			}
			else {
				product = dynamoMapper.load(Product.class, request.getId()); //Retorna el elemento que coincide con la PK = request.getId()
				return product;
			}
		case "POST":
			product = request.getProduct();
			dynamoMapper.save(product);
			return product;
		case "DELETE":
			product = dynamoMapper.load(Product.class, request.getId());
			dynamoMapper.delete(product);
			return product;
		}
		
		return null;
	}

}
