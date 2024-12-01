## Environment
- Java version: 17
- Maven version: 3.*
- Spring Boot version: 3.2.2

## Data
Example of a Scan data JSON object:
```json
{
    "id":1,
    "domainName":"testdomain.com",
    "numPages":574,
    "numBrokenLinks":45,
    "numMissingImages": 34,
    "deleted": false
}
```

## Requirements
Assume there is database which contains scan data of several websites and you want to create a REST API to access them.


You have to implement `/scan` REST endpoint for following 3 operations.

`GET` request to `/scan/{id}`:
* return the scan with given id and status code 200
* if the requested scan doesn't exist, then status code 404 should be returned

`DELETE` request to `/scan/{id}`:
* delete the scan with give id and return status code 200
* by "deleted" means, the scan is logically deleted - not completely removed from database.
* if the scan doesn't exist in the database it should return status code 404


`GET` request to `/scan/search/{domainName}?orderBy={numPages)`:
* return the scans filtered by product and sorted by given column with status code 200
* when the given order by column doesn't exist, return status code 400
 
`Test writing`

In addition to implementing the REST endpoints, you are supposed to write several(at least 5) unit tests to test your implementation.


## Commands
- run: 
```bash
mvn clean spring-boot:run
```
- install: 
```bash
mvn clean install
```
- test: 
```bash
mvn clean test
```