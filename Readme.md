# Customer Wishlist management API
## Overview
- This is a monolithic RESTful API service and allows users to register and persist as a user of the application
- Once registered a user can request a JWT token to make authorized requests to the server. Each token is valid for 24hrs.
- A valid user has a *Wishlist* associated with their account. A set number of items are available on the server to add to this wishlist.
- A user can view the available items. The items are associated with a name, price and current stock availability.
- Admins have the privelage to manipulate the available items on the store but a regular user many only view them and add/remove them from their personal wishlist.

## Setup and use
- Currently, there is no programmatic way to assign **admin** role to a user. This must be done externally. However, a separate admin login, registration pathway can be setup later (akin to the existing endpoints available to *Users*).
- After cloning the repository, check the `application.properties` file and ensure a `MySql` database can be setup at localhost:3306/xindus-db-prod. The application is flexible and is indifferent to the relational database configuration in `application.properties`.
- Hibernate is configured to generate DDL schema commands for the specified dialect. (By default, MySQL).

### Setting up an admin account
- Run the SQL script located: `src/main/resources/sql-scripts/start-up.sql`
- This will setup an admin account which can now be used to interact with the application.

### Registering a new user.
- Each unique email can be linked to an account in this system.
- All testing in this document is done using a localhost:8080 server port.
- Registering a new user does not require any authentication or privilege. A POST request to `localhost:8080/api/customers/register` endpoint expects a json object as follows:
```json
{
  "firstName": "Tarun",
  "lastName": "Valecha",
  "email": "tarun@gmail.com",
  "password": "India",
  "addressLine1": "110-Demo-address",
  "addressLine2": "New Delhi, Delhi, India",
  "pinCode": 110027,
  "dateOfBirth": "1990-10-21"
}
```
- This endpoint shall register this email if and only if it isn't already registered and respond with a token.

### Login process
- Firstly, we login with the account we set up using the `start-up.sql` script and use the admin privelages to populate our database with store items which users could add to their wishlist.
  - **POST** http://localhost:8080/api/customers/authenticate expects a json as follows:
```json
        {
            "email": "admin@example.com",
            "password": "admin"
        }
```
- The above endpoint will respond with a JWT token in the message body. This token can be used to perform actions which require admin privileges.
### Adding a new item.
- A **POST** request to `http://localhost:8080/api/items` with a valid admin token will add items to the database.
- The expected json is illustrated below:
```json
[
    {
        "itemName": "Medium T-shirt",
        "price": 49.99,
        "inStock": true
    },
    {
        "itemName": "Small T-shirt",
        "price": 49.99,
        "inStock": true
    },
    {
        "itemName": "Large T-shirt",
        "price": 54.99,
        "inStock": true
    }
]
```
- This adds 3 items to our system which may now be part of any customer's wishlist.

#### Admin feature: Item updation
- A **PUT** request at `http://localhost:8080/api/items/{itemId}` will update the specified store item with details expected in a json as follows:
```json
{
    "itemName": "Medium T-shirt",
    "price": 51.99,
    "inStock": false
}
```
- This will change fields of item with id 1. Appropriate json responses are received if invalid parameters are passed.

#### Admin feature: Item deletion
- A **DELETE** request from an admin on `http://localhost:8080/api/items/{itemId}` wil delete an item from the database.
- Note that this will also destroy any customer's wishlist entry that may hold this item.

### Viewing available items.
- All items that persist in our system can be loaded as a json response.
- Viewing items only requires a valid token from a customer and no special privileges.
- A **GET** request on `http://localhost:8080/api/items` will retrieve all items whereas on `http://localhost:8080/api/items/{itemID}` will retrieve a specific item by id.
- 17-02-2024 -> TODO: Add pagination for performance and readability here.

## CRUD operations on customer wishlist.
- The primary purpose of this application continues to be the access of a wishlist for its users.
- A customer with a valid token is allowed to:
1. Add item to wishlist by id: A **POST** request on `http://localhost:8080/api/customers/wishlist-items/{itemId}` will add #itemId item to the customer's wishlist.
2. Delete item from wishlist by id: A **DELETE** request on `http://localhost:8080/api/customers/wishlist-items/{itemId}` will remove the item from wishlist.
3. View current wishlist: A **GET** request on `http://localhost:8080/api/customers/wishlist-items` will retrieve a customer's current wishlist.
- All the above endpoints have been tested for data integrity and are safeguarded against failures. Any incomplete transaction will result in a detailed error response wrapped in a json body.

## Frameworks and languages used
- Source code: Java 21
- Base framework: SpringBoot using embedded TomCat server
- Dependencies: spring-boot-starter-web, Spring data JPA, Spring-boot-dev-tools, MySQL-jdbc-driver, Project Lombok, Spring Security, Mockito, Hibernate Validator.

#### Domain model
- The following defines the relational database model. All the necessary persistent data is stored here with robust data integrity.
![domain-model-diagram](/images/domainmodel.png)