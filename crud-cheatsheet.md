# crud-cheatsheet

## postgres docker-compose.yml

```
version: "3"

services:
  postgres:
    image: postgres
    container_name: postgres-crud-checkpoint
    ports:
      - 8432:5432
    volumes:
      - ./postgres/:/var/lib/postgresql/data
    environment:
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
      - PUID=1000
      - PGID=1000
```

## dependencies

```
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-web'
runtimeOnly 'org.postgresql:postgresql'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
```

## application.properties

```
spring.datasource.url=jdbc:postgresql://localhost:8432/crud_checkpoint?serverTimezone=UTC
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=create-drop
```

## jackson

- `@JsonInclude(value = Include.NON_NULL)`
- `@JsonProperty(access = Access.WRITE_ONLY)`
- `@JsonFormat(pattern = "yyyy-MM-dd")`

## DB annotations

```
@Entity
@Table(name = "foo")
//
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

## RestController

```
@RestController
@RequestMapping("/users")
```

### controller repo

```
private final FooRepo fooRepo;

public FooController(FooRepo fooRepo) {
  this.fooRepo = fooRepo;
}
```

### Mapping Status

- `@ResponseStatus(code = HttpStatus.CREATED)`
- `@ResponseStatus(HttpStatus.NO_CONTENT)`

### Exception Handler

```
@ExceptionHandler(NoSuchElementException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public String fooNotFound() {
  return "";
}
```

### Set fields

```
BeanWrapper accessor = PropertyAccessorFactory.forBeanPropertyAccess(foo);
Field[] fooFields = foo.getClass().getDeclaredFields();
for (Field field : fooFields) {
  String fieldName = field.getName();
  if (body.containsKey(fieldName)) accessor.setPropertyValue(fieldName, body.get(fieldName));
}
```

## Repo

```
public interface FooRepo extends JpaRepository<Foo, Integer> {
  Foo findByEmail(String email); // custom methods
}
```

## Tests

```
@SpringBootTest
@AutoConfigureMockMvc
//
@Autowired
private MockMvc mvc;

@Autowired
FooRepo fooRepo;
//
@Test
@Transactional
@Rollback
```

### Imports

```
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
```

### Get response JSON

```
String response = this.mvc.perform(request)
  .andExpect(status().isCreated())
  .andReturn().getResponse().getContentAsString();
//
ObjectMapper mapper = new ObjectMapper();
int fooId = mapper.readTree(response).findValue("id").asInt();
```

### getJSON

```
private String getJSON(String path) throws Exception {
  URL url = this.getClass().getResource(path);
  return new String(Files.readAllBytes(Paths.get(url.getFile())));
}
```

## Date Stuff

- `LocalDate.of(2022, 1, 1)`
- `LocalDate.parse("2022-01-01")`
