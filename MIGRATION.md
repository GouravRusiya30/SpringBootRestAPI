# Migration Guide

## Migrating from Spring Boot 2 to Spring Boot 3.3.5

This guide helps you upgrade from the old version to the modernized version.

### Prerequisites

- **Java 17** or higher (previously Java 8)
- **MongoDB** (unchanged)
- **Gradle 8.10** or higher (previously Gradle 2.14)

### Breaking Changes

#### 1. **Java 8 → Java 17**

- Update your local JDK to Java 17 or higher
- Update Docker base images if containerizing:
  ```dockerfile
  FROM eclipse-temurin:17-jre-alpine
  ```

#### 2. **ObjectId → String ID for Pets**

**Before**: Pet IDs were MongoDB `ObjectId` values (e.g., `507f1f77bcf86cd799439011`)  
**After**: Pet IDs are now String values

**If you have existing pet data, you must migrate:**

**Option A: Using MongoDB Shell (Recommended for production)**

```javascript
// Connect to MongoDB and switch to your database
use rest_tutorial;

// Migrate pets collection - convert ObjectId to String
db.pets.updateMany({}, [
  {
    $set: {
      _id: { $toString: "$_id" }
    }
  }
]);

// Verify migration
db.pets.find().pretty();
```

**Option B: Clear and Re-seed (For development)**

If the data is not critical, you can simply drop the collection and let the DbSeeder recreate it:

```javascript
use rest_tutorial;
db.pets.deleteMany({});
```

Then restart the application with the `local` profile:

```bash
export SPRING_PROFILES_ACTIVE=local
./gradlew bootRun
```

#### 3. **Environment Variables Configuration**

**Before**: Configuration was stored in `application.properties`

```properties
spring.data.mongodb.host=127.0.0.1
spring.data.mongodb.port=27017
spring.data.mongodb.username=
spring.data.mongodb.password=
spring.data.mongodb.database=rest_tutorial
server.port=8081
springboot.app.jwtSecret=restTutorialSecretKey
springboot.app.jwtExpirationMs=86400000
```

**After**: Configuration is externalized via environment variables

```bash
export SERVER_PORT=8081
export MONGODB_URI=mongodb://localhost:27017/rest_tutorial
export JWT_SECRET=your-secure-secret-at-least-32-characters
export JWT_EXPIRATION_MS=86400000
```

**Setup Instructions:**

1. Copy `.env.example` to `.env`:
   ```bash
   cp .env.example .env
   ```

2. Update `.env` with your values:
   ```env
   SERVER_PORT=8081
   MONGODB_URI=mongodb://localhost:27017/rest_tutorial
   JWT_SECRET=your-super-secure-secret-must-be-32-chars-minimum
   JWT_EXPIRATION_MS=86400000
   ```

3. Source the file before running (or use a tool like `direnv`):
   ```bash
   source .env
   ./gradlew bootRun
   ```

**⚠️ IMPORTANT: JWT Secret Security**

The JWT secret is critical for security:
- **Minimum length**: 32 characters (required for HS512 algorithm)
- **Production**: Use a strong, randomly generated secret
- **Never commit**: Add `.env` to `.gitignore`
- **Validation**: The app will fail to start if the secret is too weak or missing in production

Generate a secure secret:

```bash
# On macOS/Linux
openssl rand -base64 32

# Or use Python
python3 -c "import secrets; print(secrets.token_urlsafe(32))"
```

#### 4. **Spring Security Changes**

- `@EnableGlobalMethodSecurity` → `@EnableMethodSecurity`
- `antMatchers()` → `requestMatchers()` in security rules
- Deprecated `WebSecurityConfigurerAdapter` is no longer extended

#### 5. **Servlet API Changes**

- `javax.servlet.*` → `jakarta.servlet.*`
- `javax.validation.*` → `jakarta.validation.*`

All import statements have been updated automatically.

### Deployment

#### Docker

Ensure your `Dockerfile` uses Java 17:

```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=0 /app/build/libs/*.jar app.jar
EXPOSE 8081
ENV JAVA_OPTS="-Xmx512m"
CMD ["java", "$JAVA_OPTS", "-jar", "app.jar"]
```

#### Environment Variables

Set these environment variables in your production deployment:

```bash
SERVER_PORT=8081
MONGODB_URI=mongodb+srv://user:password@cluster.mongodb.net/rest_tutorial?retryWrites=true&w=majority
JWT_SECRET=<generate-a-strong-secret>
JWT_EXPIRATION_MS=86400000
SPRING_PROFILES_ACTIVE=prod
```

### Troubleshooting

**Q: Application fails to start with "JWT_SECRET is set to the default value"**

A: Set a strong JWT_SECRET environment variable before deployment.

```bash
export JWT_SECRET="your-secure-random-string-with-at-least-32-characters"
```

**Q: "Cannot find pet with ID X" after migration**

A: Run the MongoDB migration script above to convert ObjectIds to Strings.

**Q: Old clients still expecting ObjectId format**

A: The API now returns String IDs. Update client code:

```javascript
// Before
const petId = pet._id; // ObjectId

// After
const petId = pet.id; // String
```

### Support

For questions, refer to the main [README.md](./README.md) or check existing issues.
