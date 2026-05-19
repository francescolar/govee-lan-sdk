# Commands

## Build & test

Build and run tests:
```
docker run --rm -v "$HOME/.m2:/root/.m2" -v "$(pwd):/app" -w /app maven:3.9-eclipse-temurin-17 mvn package
```

Skip tests:
```
docker run --rm -v "$HOME/.m2:/root/.m2" -v "$(pwd):/app" -w /app maven:3.9-eclipse-temurin-17 mvn package -DskipTests
```

Single test class:
```
docker run --rm -v "$HOME/.m2:/root/.m2" -v "$(pwd):/app" -w /app maven:3.9-eclipse-temurin-17 mvn test -Dtest=CommandFactoryTest
```

Install into local Maven cache (needed by the plugin while SDK is not yet on Central):
```
docker run --rm -v "$HOME/.m2:/root/.m2" -v "$(pwd):/app" -w /app maven:3.9-eclipse-temurin-17 mvn install -DskipTests
```

## Publish to Maven Central

Requirements before running this:
- `~/.m2/settings.xml` has the `central` server entry with your Sonatype token (see below)
- GPG key for laruffafrancesco1@gmail.com is in `~/.gnupg`

```
docker run --rm \
  -v "$HOME/.m2:/root/.m2" \
  -v "$HOME/.gnupg:/root/.gnupg" \
  -v "$(pwd):/app" \
  -w /app \
  maven:3.9-eclipse-temurin-17 \
  mvn clean deploy -Prelease
```

After the command completes, go to https://central.sonatype.com/publishing/deployments, find the deployment and click **Publish**. It will appear on Maven Central within ~30 minutes.

## settings.xml reference

`~/.m2/settings.xml` must contain:
```xml
<settings>
  <localRepository>/usr/share/maven/ref/repository</localRepository>
  <servers>
    <server>
      <id>central</id>
      <username>YOUR_SONATYPE_TOKEN_USERNAME</username>
      <password>YOUR_SONATYPE_TOKEN_PASSWORD</password>
    </server>
  </servers>
</settings>
```

## Release checklist

1. Update `<version>` in `pom.xml` (remove `-SNAPSHOT` if present)
2. Run the publish command above
3. Approve the deployment on central.sonatype.com
4. Tag the commit: `git tag v1.0.0 && git push origin v1.0.0`
5. Create a GitHub release from the tag
