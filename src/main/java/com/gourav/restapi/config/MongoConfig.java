package com.gourav.restapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * MongoDB configuration — enables @CreatedDate / @LastModifiedDate auditing on documents.
 * Kept in a separate @Configuration class so that @WebMvcTest slices (which don't load
 * the MongoDB data layer) can run without this bean causing a context failure.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
}
