## Creating New Project
- Reference
  - https://quarkus.io/guides/cli-tooling
  - https://quarkus.io/blog/quarkus-cli/

```text
% quarkus create app com.example:fruitage
```

## Yaml Configuration
- Reference: https://quarkus.io/guides/config-yaml

```text
% quarkus ext add quarkus-config-yaml
```

## Start Development Mode
```text
% quarkus dev
```
## RestEasy Reactive
```text
% quarkus ext add quarkus-resteasy-reactive quarkus-resteasy-reactive-jackson
```

## Hibernate Reactive
- Reference: https://quarkus.io/guides/hibernate-reactive-panache

```text
% quarkus ext add quarkus-hibernate-reactive-panache quarkus-reactive-pg-client
```

## Swagger UI
```text
% quarkus ext add quarkus-smallrye-openapi 
http://localhost:9000/q/swagger-ui/
```