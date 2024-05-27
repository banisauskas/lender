## Run

#### IntelliJ

Open `build.gradle.kts` in IntelliJ as Gradle project and Run/Debug.

#### Gradle

```shell
./gradlew bootRun
```

#### Manually

```shell
./gradlew build
cd build/libs
java -jar lender-1.0.jar
```

## Database

http://localhost:8080/h2-console

* JDBC URL: jdbc:h2:mem:lenderdb
* User Name: username
* Password: password

## Examples

Create loan:
```
POST http://localhost:8080/loans
{
	"name": "John",
	"term": "2024-10-01",
	"amount": 999.99
}
```

Get all loans:
```
GET http://localhost:8080/loans
```

Get loan `id=1`:
```
GET http://localhost:8080/loans/1
```

Create extension for loan `id=1` with specified term:
```
POST http://localhost:8080/loans/1/extensions
{
	"term": "2024-10-08"
}
```

Create extension for loan `id=1` with automatically calculated term:
```
POST http://localhost:8080/loans/1/extensions
{
}
```

Get all extensions for loan `id=1`:
```
GET http://localhost:8080/loans/1/extensions
```

Get extension `id=1` for loan `id=1`:
```
GET http://localhost:8080/loans/1/extensions/1
```