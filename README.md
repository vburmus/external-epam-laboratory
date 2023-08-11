# Authentication & Spring Security
### Task

This module is an extension of the REST API Advanced module and covers the following topics:

1. Spring Security framework
2. Oauth2 and OpenId Connect
3. JWT token

Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications. OAuth 2.0 is a security standard where you give one application permission to access your data in another application. The steps to grant permission, or consent, are often referred to as authorization or even delegated authorization. You authorize one application to access your data or use features in another application on your behalf, without giving them your password. OpenID Connect (OIDC) is a thin layer that sits on top of OAuth 2.0 that adds login and profile information about the person who is logged in. JSON Web Tokens are JSON objects used to send information between parties in a compact and secure manner.

#### Application requirements

1. Spring Security should be used as a security framework.
2. Application should support only stateless user authentication and verify the integrity of the JWT token.
3. Users should be stored in a database with some basic information and a password.

User Permissions:

     - Guest:
        * Read operations for the main entity.
        * Signup.
        * Login.
     - User:
        * Make an order on the main entity.
        * All read operations.
     - Administrator (can be added only via database call):
        * All operations, including addition and modification of entities.

4. Get acquainted with the concepts of Oauth2 and OpenId Connect
5. (Optional task) Use Oauth2 as an authorization protocol.
    a. OAuth2 scopes should be used to restrict data.
    b. Implicit grant and Resource owner credentials grant should be implemented.
6. (Optional task) It's allowed to use Spring Data. Requirement for this task - all repositories (and existing ones) should be migrated to Spring Data.

## How to run?
### Add env variables
1. Host your db and add SQL_USERNAME, SQL_PASSWORD, SQL_URL.
2. Path to tokens:
   - ACCESS_TOKEN_PRIVATE_KEY
   - ACCESS_TOKEN_PUBLIC_KEY
   - REFRESH_TOKEN_PRIVATE_KEY
   - REFRESH_TOKEN_PUBLIC_KEY
3. GOOGLE_CLIENT_ID,GOOGLE_CLIENT_SECRET
4. JWT_SECRET(generated RSA )
5. SPRING_PROFILES_ACTIVE(default or hateaos)
    - If you provide HATEOAS, keys will be generated under the path to tokens.
   
