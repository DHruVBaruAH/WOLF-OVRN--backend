# Graph Report - .  (2026-05-13)

## Corpus Check
- Corpus is ~2,174 words - fits in a single context window. You may not need a graph.

## Summary
- 31 nodes · 32 edges · 7 communities (3 shown, 4 thin omitted)
- Extraction: 75% EXTRACTED · 25% INFERRED · 0% AMBIGUOUS · INFERRED: 8 edges (avg confidence: 0.91)
- Token cost: 26,337 input · 6,585 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Spring Boot Configuration|Spring Boot Configuration]]
- [[_COMMUNITY_User & Order Domain|User & Order Domain]]
- [[_COMMUNITY_Product Catalog Schema|Product Catalog Schema]]
- [[_COMMUNITY_Application Entrypoint|Application Entrypoint]]
- [[_COMMUNITY_Security Configuration|Security Configuration]]
- [[_COMMUNITY_Smoke Test|Smoke Test]]
- [[_COMMUNITY_Project README|Project README]]

## God Nodes (most connected - your core abstractions)
1. `WolfOvrnBackendApplication (Spring Boot entrypoint)` - 5 edges
2. `users table (OAuth-based)` - 5 edges
3. `orders table` - 5 edges
4. `categories table (self-referencing tree)` - 4 edges
5. `products table` - 4 edges
6. `update_modified_column() trigger function` - 4 edges
7. `spring.flyway config (baseline-on-migrate, classpath:db/migration)` - 4 edges
8. `SecurityConfig.securityFilterChain bean` - 3 edges
9. `WolfOvrnBackendApplication` - 2 edges
10. `SecurityConfig` - 2 edges

## Surprising Connections (you probably didn't know these)
- `Package rename note (hyphen -> underscore)` --rationale_for--> `WolfOvrnBackendApplication (Spring Boot entrypoint)`  [EXTRACTED]
  HELP.md → src/main/java/com/Ishwarjit/Wolf_OVRN_backend/WolfOvrnBackendApplication.java
- `WolfOvrnBackendApplication (Spring Boot entrypoint)` --references--> `spring.datasource (env-driven Postgres)`  [INFERRED]
  src/main/java/com/Ishwarjit/Wolf_OVRN_backend/WolfOvrnBackendApplication.java → src/main/resources/application.yml
- `Spring Boot 4.0.6 reference docs (Web/JPA/Security/Flyway)` --cites--> `SecurityConfig.securityFilterChain bean`  [EXTRACTED]
  HELP.md → src/main/java/com/Ishwarjit/Wolf_OVRN_backend/config/SecurityConfig.java
- `spring.flyway config (baseline-on-migrate, classpath:db/migration)` --references--> `users table (OAuth-based)`  [INFERRED]
  src/main/resources/application.yml → src/main/resources/db/migration/V1__initial_schema.sql
- `WolfOvrnBackendApplication (Spring Boot entrypoint)` --references--> `spring.flyway config (baseline-on-migrate, classpath:db/migration)`  [INFERRED]
  src/main/java/com/Ishwarjit/Wolf_OVRN_backend/WolfOvrnBackendApplication.java → src/main/resources/application.yml

## Hyperedges (group relationships)
- **E-commerce order flow (user places order with product line items)** — v1initialschema_users_table, v1initialschema_orders_table, v1initialschema_order_items_table, v1initialschema_products_table [INFERRED 0.90]
- **Flyway-managed Postgres schema lifecycle (compose -> datasource -> migration -> JPA validate)** — dockercompose_postgres_service, applicationyml_datasource, applicationyml_flyway, v1initialschema_users_table, applicationyml_jpa_validate_rationale [INFERRED 0.90]
- **updated_at auto-touch trigger pattern across mutable tables** — v1initialschema_update_modified_column_fn, v1initialschema_users_table, v1initialschema_categories_table, v1initialschema_products_table, v1initialschema_orders_table [EXTRACTED 1.00]

## Communities (7 total, 4 thin omitted)

### Community 0 - "Spring Boot Configuration"
Cohesion: 0.22
Nodes (10): spring.datasource (env-driven Postgres), spring.flyway config (baseline-on-migrate, classpath:db/migration), JPA ddl-auto: validate (schema owned by Flyway, JPA only verifies), postgres:17 service (wolf-ovrn-postgres), Package rename note (hyphen -> underscore), Spring Boot 4.0.6 reference docs (Web/JPA/Security/Flyway), Permit-all + CSRF disabled (dev-stage open API), SecurityConfig.securityFilterChain bean (+2 more)

### Community 1 - "User & Order Domain"
Cohesion: 0.33
Nodes (7): Address snapshot via JSONB (denormalized for historical accuracy), OAuth-only user model (no password_hash, provider+provider_id unique), order_items table, order_status ENUM, orders table, user_role ENUM (USER/STAFF/MANAGER/ADMIN), users table (OAuth-based)

### Community 2 - "Product Catalog Schema"
Cohesion: 0.83
Nodes (4): categories table (self-referencing tree), product_images table, products table, update_modified_column() trigger function

## Knowledge Gaps
- **9 isolated node(s):** `Permit-all + CSRF disabled (dev-stage open API)`, `product_images table`, `OAuth-only user model (no password_hash, provider+provider_id unique)`, `Address snapshot via JSONB (denormalized for historical accuracy)`, `contextLoads() smoke test` (+4 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **4 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `users table (OAuth-based)` connect `User & Order Domain` to `Spring Boot Configuration`, `Product Catalog Schema`?**
  _High betweenness centrality (0.263) - this node is a cross-community bridge._
- **Why does `spring.flyway config (baseline-on-migrate, classpath:db/migration)` connect `Spring Boot Configuration` to `User & Order Domain`?**
  _High betweenness centrality (0.252) - this node is a cross-community bridge._
- **Are the 3 inferred relationships involving `WolfOvrnBackendApplication (Spring Boot entrypoint)` (e.g. with `SecurityConfig.securityFilterChain bean` and `spring.datasource (env-driven Postgres)`) actually correct?**
  _`WolfOvrnBackendApplication (Spring Boot entrypoint)` has 3 INFERRED edges - model-reasoned connections that need verification._
- **What connects `Permit-all + CSRF disabled (dev-stage open API)`, `product_images table`, `OAuth-only user model (no password_hash, provider+provider_id unique)` to the rest of the system?**
  _9 weakly-connected nodes found - possible documentation gaps or missing edges._