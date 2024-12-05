# REST API Util 라이브러리

이 라이브러리는 스프링 프레임워크를 사용하여 Java 21 버전에서 REST API 개발에 필요한 다양한 유틸리티 기능을 제공하기 위해 설계되었습니다. REST API의 일관된 응답 처리, 예외 처리, 그리고 기타 편의 기능을 포함하고 있습니다.

## 주요 기능

- **Response Formatting**: API 응답을 일관된 형식으로 포맷팅하여 클라이언트에게 전달합니다.
- **Custom Exception Handling**: 다양한 예외 상황에 대해 사용자 정의 예외를 처리할 수 있습니다.
- **확장성**: 프로젝트의 요구에 맞게 쉽게 확장할 수 있습니다.

## 설치 방법

### Gradle (Groovy)

```groovy
// 저장소 추가
repositories {
    maven { url 'https://jitpack.io' }
}

// 의존성 추가
dependencies {
    implementation 'com.github.s12171934:rest-response:{TAG-VERSION}'
}
```

### Gradle (Kotlin)

```kotlin
// 저장소 추가
repositories {
    maven { url 'https://jitpack.io' }
}

// 의존성 추가
dependencies {
    implementation("com.github.s12171934:rest-response:{TAG-VERSION}")
}
```

### Maven

```xml
<!-- jitpack.io 저장소 추가 -->
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<!-- 의존성 추가 -->
<dependency>
    <groupId>com.github.s12171934</groupId>
    <artifactId>rest-response</artifactId>
    <version>{TAG-VERSION}</version>
</dependency>
```

## 사용 방법

### Response 형식

이 라이브러리는 모든 API 응답을 일관된 형식으로 제공합니다:

```json
{
    "success": true,
    "status": {
        "code": 200,
        "message": "정상 처리되었습니다"
    },
    "content": {
        // 실제 응답 데이터
    },
    "metaData": {
        "timeStamp": "2024-03-21T10:30:15.123Z",
        "path": "/api/v1/users",
        "version": "1.0.0",
        "pagination": {
            // 페이지네이션 정보 (있는 경우에만 포함)
        }
    }
}
```

### 응답 생성 예시

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<User>> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return RestResponseFactory.createContentResponseEntity(
            HttpStatus.OK,
            user
        );
    }
}
```

### Custom Exception 정의 및 자동 처리

`BaseException`을 상속받고 `@CustomException` 어노테이션을 사용하여 커스텀 익셉션을 정의하면, 다음과 같은 일관된 형식의 에러 응답이 자동으로 생성됩니다:

```java
@CustomException(codeClass = RestResponseCode.class, codeName = "USER_NOT_FOUND")
public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
```

에러 발생 시 응답:
```json
{
    "success": false,
    "status": {
        "code": 404,
        "message": "사용자를 찾을 수 없습니다"
    },
    "metaData": {
        "timeStamp": "2024-03-21T10:30:15.123Z",
        "path": "/api/v1/users/999",
        "version": "1.0.0"
    }
}
```

별도의 `@ControllerAdvice`나 `@ExceptionHandler` 구현이 필요하지 않으며, 라이브러리의 내장된 글로벌 익셉션 핸들러가 모든 커스텀 익셉션을 자동으로 처리합니다.