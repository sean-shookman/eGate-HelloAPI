package com.demo.api.service;

import com.demo.api.products.Product;
import com.mongodb.*;
import com.sun.jersey.spi.resource.Singleton;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.UnknownHostException;

/**
 * @author Sean Shookman
 */
@Named
@Singleton
@Path("/products")
public class ProductService {

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getAllProducts() {

        MongoClient mongoClient;

        try {

            mongoClient = new MongoClient("localhost" , 27017 );
        } catch (UnknownHostException e) {

            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        DB db = mongoClient.getDB("eGate");
        boolean auth = db.authenticate("admin", "admin".toCharArray());

        DBCollection products = db.getCollection("products");

        DBCursor cursor = products.find();

        Product product = null;

        while (cursor.hasNext()) {
            DBObject current = cursor.next();
            product = new Product();
            product.setBrand(current.get("brand").toString());
            product.setTitle(current.get("Title").toString());
            product.setPrice(new Double(current.get("Price").toString()));
        }

        return Response.ok(product).build();
    }
}