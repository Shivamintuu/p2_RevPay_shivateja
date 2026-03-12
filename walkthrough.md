# Redis Cache Integration Walkthrough

## Overview
The application has been upgraded to use **Redis** as a distributed caching mechanism instead of the default in-memory concurrent map cache. This is a crucial step towards scaling the application across multiple instances, ensuring that cached data (like User details, Wallets, and Business Analytics) remains consistent and centralized.

## Changes Made
1. **Dependency Added**: Added `spring-boot-starter-data-redis` to `pom.xml`. The application still relies on `spring-boot-starter-cache` for the abstraction annotations (`@Cacheable`, `@CacheEvict`).
2. **Configuration Settings**: Configured `application.properties` to connect to a Redis server.
   - `spring.cache.type=redis`
   - `spring.data.redis.host=localhost`
   - `spring.data.redis.port=6379`
3. **Redis Configuration Class**: Created `RedisConfig.java` to define a customized `RedisCacheManager`. The customizations include:
   - **JSON Serialization**: Switched from default Java binary serialization to JSON (`GenericJackson2JsonRedisSerializer`). This ensures that cached data inside Redis is human-readable (easy to debug via `redis-cli`) and prevents `Serializable` interface conflicts.
   - **Global TTL (Time-To-Live)**: Set a default expiration time of 60 minutes for all cached entries. This prevents stale data buildup in the Redis server.
   - **No Nulls**: Disabled caching of null values to prevent caching empty query results mistakenly.

## How to Test
1. **Prerequisite**: Ensure a Redis server is running locally on port `6379`. You can use Docker to spin one up quickly: 
   ```bash
   docker run -p 6379:6379 -d redis
   ```
2. Start the Spring Boot application normally.
3. Use the application (e.g., fetch your wallet balance, log in).
4. **Verify Database Logs**: The first time you load the wallet or user details, you should see logging in the console indicating a database query.
5. **Verify Cache Hit**: Refresh the page or perform the action again. The query should *not* appear in the database logs, proving it served the result from Redis.
6. **Inspect Redis (Optional)**: If you open a `redis-cli` and run `keys *`, you should see cached keys like `wallets::1` or `users::1` with their respective JSON payloads.
