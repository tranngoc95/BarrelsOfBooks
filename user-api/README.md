# Dev10 User API

## Documentation

### Running the application using Docker

Run this command from the root of the project to build out the Docker images and containers for the API:

```
docker-compose up
```

Once the images and containers have been created and started, you can use the supplied HTTP requests in the `http` folder to test your application using the [Visual Studio Code REST Client extension](https://marketplace.visualstudio.com/items?itemName=humao.rest-client).

Press `CTRL+C` to stop your application.

To remove the containers that were created by Docker Compose, run the following command:

```
docker-compose down
```

Include the `-v` option if you'd like to remove the persistent Docker volume that the API's database stores data within:

```
docker-compose down -v
```

### Testing the application

You can use the supplied [HTTP requests in the `http` folder](./http) to test your application using the [Visual Studio Code REST Client extension](https://marketplace.visualstudio.com/items?itemName=humao.rest-client).

### Seed Data

The provided database scripts seed the database with two user accounts:

```json
{
  "id": "983f1224-af4f-11eb-8368-0242ac110002",
  "username": "johnsmith",
  "password": "P@ssw0rd!",
  "first_name": "John",
  "last_name": "Smith",
  "email_address": "john@smith.com",
  "mobile_phone": "555-555-5555",
  "roles": "ADMIN"
}
```

```json
{
  "id": "9e5d9272-af4f-11eb-8368-0242ac110002",
  "username": "sally@jones.com",
  "password": "P@ssw0rd!",
  "first_name": null,
  "last_name": null,
  "email_address": null,
  "mobile_phone": null,
  "roles": "USER"
}
```

You can use these test accounts when testing the API.

### Endpoints

The API provides the following endpoints for creating a new user account, authenticating an existing user, and refreshing a non-expired token.

#### POST http://localhost:5000/create_account

Used to create a new user account.

**Request**

```
POST http://localhost:5000/create_account HTTP/1.1
Content-Type: application/json

{
  "username": "smashdev",
  "password": "Asdff88f67!"
}
```

**Response**

```
HTTP/1.1 201 

{
  "id": "a312e741-f33c-4adb-946c-a6a44cfe26be"
}
```

**Example Error Response**

```
HTTP/1.1 400 

{
  "messages": [
    "`password` is required.",
    "`password` must be between 8 and 50 characters.",
    "`username` is required."
  ]
}
```

##### Extra User Fields

A new user account can be created with extra fields (all are optional).

**Request**

```
POST http://localhost:5000/create_account HTTP/1.1
Content-Type: application/json

{
  "username": "smashdev",
  "password": "Asdff88f67!",
  "first_name": "James",
  "last_name": "Churchill",
  "email_address": "james@churchill.com",
  "mobile_phone": "555-555-5555"
}
```

#### POST http://localhost:5000/authenticate

Used to authenticate an existing user.

**Request**

```
POST http://localhost:5000/authenticate HTTP/1.1
Content-Type: application/json

{
    "username": "johnsmith",
    "password": "P@ssw0rd!"
}
```

**Response**

```
HTTP/1.1 200 

{
  "jwt_token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZXYxMC11c2Vycy1hcGkiLCJzdWIiOiJqb2huc21pdGgiLCJpZCI6Ijk4M2YxMjI0LWFmNGYtMTFlYi04MzY4LTAyNDJhYzExMDAwMiIsImZpcnN0X25hbWUiOiJKb2huIiwibGFzdF9uYW1lIjoiU21pdGgiLCJlbWFpbF9hZGRyZXNzIjoiam9obkBzbWl0aC5jb20iLCJtb2JpbGVfcGhvbmUiOiI1NTUtNTU1LTU1NTUiLCJyb2xlcyI6IkFETUlOIiwiZXhwIjoxNjI5MTU5Nzg3fQ.K7QuemrU4Eh62mXPpHFloUbHHzi1QzOie5ZfdL1iePc"
}
```

**Token Payload**

Tokens returned from the API contain the following payload:

```json
{
  "iss": "dev10-users-api",
  "sub": "johnsmith",
  "id": "983f1224-af4f-11eb-8368-0242ac110002",
  "first_name": "John",
  "last_name": "Smith",
  "email_address": "john@smith.com",
  "mobile_phone": "555-555-5555",
  "roles": "ADMIN",
  "exp": 1628042100
}
```

* `iss` - the issuer of the token (i.e. the name of the API)
* `sub` - the subject (i.e. the user's username)
* `id` - the user's unique ID
* `first_name` - the user's first name (optional)
* `last_name` - the user's last name (optional)
* `email_address` - the user's email address (optional)
* `mobile_phone` - the user's mobile phone (optional)
* `roles` - the user's assigned roles (will either be `USER` or `ADMIN`; new user accounts are assigned the role `USER` by default)
* `exp` - the expiration date of the token (measured in milliseconds from midnight January 1, 1970 UTC)

> For more information about JWTs see [jwt.io](https://jwt.io/).

**Example Error Response**

For security reasons, the API will only return a `403 FORBIDDEN` HTTP status code if the authentication process fails for any reason.

```
HTTP/1.1 403
```

#### POST http://localhost:5000/refresh_token

Used to refresh a non-expired token.

**Request**

```
POST http://localhost:5000/refresh_token HTTP/1.1
Content-Type: application/json

{
  "jwt_token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZXYxMC11c2Vycy1hcGkiLCJzdWIiOiJqb2huc21pdGgiLCJpZCI6Ijk4M2YxMjI0LWFmNGYtMTFlYi04MzY4LTAyNDJhYzExMDAwMiIsImZpcnN0X25hbWUiOiJKb2huIiwibGFzdF9uYW1lIjoiU21pdGgiLCJlbWFpbF9hZGRyZXNzIjoiam9obkBzbWl0aC5jb20iLCJtb2JpbGVfcGhvbmUiOiI1NTUtNTU1LTU1NTUiLCJyb2xlcyI6IkFETUlOIiwiZXhwIjoxNjI5MTU5Nzg3fQ.K7QuemrU4Eh62mXPpHFloUbHHzi1QzOie5ZfdL1iePc"
}
```

**Response**

```
HTTP/1.1 200 

{
  "jwt_token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJkZXYxMC11c2Vycy1hcGkiLCJzdWIiOiJqb2huc21pdGgiLCJpZCI6Ijk4M2YxMjI0LWFmNGYtMTFlYi04MzY4LTAyNDJhYzExMDAwMiIsImZpcnN0X25hbWUiOiJKb2huIiwibGFzdF9uYW1lIjoiU21pdGgiLCJlbWFpbF9hZGRyZXNzIjoiam9obkBzbWl0aC5jb20iLCJyb2xlcyI6IkFETUlOIiwiZXhwIjoxNjI5MTU5ODkxfQ.NGRUpOqB9LDR_HA3QkqHl3TjfbCcuSEXy01OP1XwaM0"
}
```

**Example Error Response**

For security reasons, the API will only return a `403 FORBIDDEN` HTTP status code if the token refresh process fails for any reason.

```
HTTP/1.1 403
```
