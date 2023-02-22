Spring boot를 이용한 RESTful API

REST란
Http 메서드를 사용하여 client가 요청한 결과를 반환해주는 api
http 메서드의 POST,GET,DELETE,PUT 메서드를 사용한다.

컨트롤러에는 @RestController 사용
이프로젝트에서는 사용자에대해서 CRUD기능을 RESTAPI로 구현했다. **(DB는 아직 존재하지 않음)
Client의 요청에 따라 정보를 전달해야 하기 떄문에 적절한 Response가 필요하다
정상적인 요청으로 인한 정상적인 Response일때 --> JSON데이터 전달

비정상적인 요청 또는 서버 내부적인 오류일때 오류코드, 메세지 및 설명등을 적절히 전달해야 한다.

예외처리
예외처리 클래스 생성 후 예상되는 예외상항에서 throw한다

aop를 이용한 Exception handling
ExceptionResponse 객체를 만든다 해당 객체는 에러 발생시간,메세지,상세정보 데이터를 가지고 있다.

CustomizedResponseEntityExceptionHandler 클래스를 생성하고 해당 클래스는 ResponseEntityExceptionHandler를 상속한다.
--> @ControllerAdvice (모든 컨트롤러가 실행될때 해당 어노테이션을 가진 빈이 사전에 실행된다.

Validation API 사용
해당 프로젝트 User도메인 클래스
@Size , @Past 등으로 validation annotation 설정
컨트롤러에서 @Valid로 해당 도메인클래스를 받는곳에 validation 지정
CustomizedResponseEntityExceptionHandler에서 해당 validation 오류로인한 exceptionHandler 생성
ResponseEntityExceptionHandler에서 ~~~argumentValidation? 메소드를 찾아 오버라이드 한다.
해서 ResponseEntity를 생성해서 반환

다국어처리
스프링 부트 메인클래스에서
LocaleResolver를 반환하는 빈등록 하고 기본 설정 값을 한국으로 설정
    @Bean
    public SessionLocaleResolver localResolver(){
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREA);
        return localeResolver;
    }

설정파일에 메세지 설정 추가
spring:
  messages:
    basename: messages(메세지 설정파일의 시작은 messages라고 시작된다.)

다국어 설정은 request header에다 설정한다.

받는곳에서는 Locale을 받는다.

전달받은 Locale에 따라 사용할 메세지 properties사용을 위해
Message 개체를 주입한다.
    @Autowired
    private MessageSource messageSource;

사실 어떻게 message_설정언어.properties를 가져오는지 이해가 되지 않음

/**************************xml 형태로 반환하기**************************/
xml 형태로 반환하기 위해선 먼저 클라이언트가 header에서 Accept 값을 application/xml형식으로 요청한다.
서버측에선 xml형태로 데이터를 반환하기위해 maven에서 다음 의존성을 추가한다.
        <dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-xml</artifactId>
            <version>2.10.2</version>
        </dependency>

/**************************Response 데이터 제어**************************/
@JsonIgnore
@JsonIgnoreProperties(value = {"password","ssn"})

User도메인에 @JsonFilter("UserInfo")//이름은 임의지정

SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","name","joinDate","ssn");
FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);
MappingJacksonValue mapping = new MappingJacksonValue(user);
mapping.setFilters(filters);

/*****************************URI를 통한 rest api version관리**************************/
자마깐 상속받을때 부모클래스의 기본 생성자가 없으면 오류가 발생?

그냥 URI에다 v1 또는 v2로 다른 버전 URI를 등록후 다른 버전의 객체를 던저준다.

/********************Request Parameter와 Header를 이용한 API 버전관리
Request Parameter
@GetMapping(value = "/users/{id}",params = "version=1")
@GetMapping(value = "/users/{id}",params = "version=2")
이런 식으로 파라미터로 받는 버전을 명시하고 각기 다른 버전의 객체를 반환한다.

Header값 이용
@GetMapping(value = "/users/{id}",headers = "X-API-VERSION=1")
@GetMapping(value = "/users/{id}",headers = "X-API-VERSION=2")
와 같이 헤더값을 통해 다른 버전을 관리할 수도 있다.

Header값의 MIME TYPE 이용
@GetMapping(value = "/v1/users/{id}",produces = "application/vnd.company.appv2+json")

요청시 header에 accept에 application/vnd.company.appv2+json 값으로 요청

버전관리에 주의해야 할점
URI가 너무 복잡해지지 않게 한다.
헤더값이 잘못들어가게 하지 않는다.
브라우저 캐쉬때문에 지정한 값이 제대로 반영되지 않을 수 있다. 캐쉬삭제 후 제대로 된 값 확인 필요
브라우저에서 실행될수 있는 버전도 존재해야 한다. URI실행 또는 request param
API DOCUMENT가 제공되어야 한다.

/**************HATEOAS적용***********************/
HATEOAS는 무엇인가?
잘 설계된 REST API를 구현하기 위한 단계가 존재하는데 , 그 마지막 단계가

Hypermedia Controls (하이퍼미디어 컨트롤) - HATEOAS라는 개념을 통해, 자원에 호출 가능한 API 정보를 자원의 상태를 반영하여 표현하는것이다.

2.1.8
Resource
ControllerLinkBuilder

2.2
Resource -> EntityModel
ControllerLinkBuilder -> WebMvcLinkBuilder

EntityModel<Usr> model = new EntityModel<>(user);
WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retieveAllUsers());
model.add(linkTo.withRel("all-Users");
return model;

학습이 필요한 객체들
1. ResponseEntity
httpentity를 상속받는, 결과 데이터와 HTTP 상태 코드를 직접 제어할 수 있는 클래스이다.
ResponseEntity에는 사용자의  HttpRequest에 대한 응답 데이터가 포함된다.

ResponseEntity는 HttpEntity를 상속받고 사용자의 응답 데이터가 포함된 클래스이기 때문에 HttpStatus, HttpHeaders, HttpBody 를 포함한다.
즉 responseEntity는 응답에 필요한 헤더,바디,상태코드를 설정하여 넘겨줄수 있도록 하는 객체이다.
@RestController
@RequiredArgsConstructor
public class ResponseEntityController {

    private final ResponseEntityService service;

    @GetMapping("/user/{id}")
    public ResponseEntity<MyDto> findByid(@PathVariable Long id) {
        User user = service.getUser();
        MyDto dto = new MyDto();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        dto.setStatus(StatusEnum.OK);
        dto.setData(user);
        dto.setMessage("메세지메세지!");

        return new ResponseEntity<>(dto, header, HttpStatus.OK);
    }
}
Constructor보다는 Builder
이렇게 ResponseEntity 를 사용할 때, Constructor 를 사용하기보다는 Builder 를 활용하는 것을 권장하고 있습니다. 그 이유는 숫자로 된 상태 코드를 넣을 때, 잘못된 숫자를 넣을 수 있는 실수 때문입니다. 따라서, Builder Pattern 를 활용하면 다음과 같이 코드를 변경할 수가 있습니다.

  return new ResponseEntity<MoveResponseDto>(moveResponseDto, headers, HttpStatus.valueOf(200));

  return ResponseEntity.ok()
        .headers(headers)
        .body(moveResponseDto);
이렇게 Builder Pattern 을 활용하면 각 상태에 매칭되는 숫자 코드를 외울 필요 없이 Builder 메소드를 선택하면 됩니다.
2. URI  특정 리소스를 식별하는 통합 자원 식별자이다. 자바로는 개체로 생성할 수 있다.
3. ServletUriComponentsBuilder URI를 생성하기위해 보다 편리하도록 만들어진 builder이다.
7. @ResponseStatus responst 응답코드설정
8. WebRequest
9. @ExceptionHandler(Exception.class)
10. LocaleResolver
11. SessionLocaleResolver
12. MessageSource