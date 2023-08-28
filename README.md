# bankshop-griotold
## 은행 거래 및 쇼핑몰 서비스 REST API

- 사이트 주소 : https://www.griotold.shop
  - 비용 문제로 서비스 종료  
- 깃허브 : https://github.com/Griotold/bankshop-griotold
- 사용 툴
  - `Java 11`
  - `Spring 2.7.14`
  - `QueryDSL 5.0.0`
  - `JPA`
  - `Spring Security`
  - `MySql 8`
  - `h2`
  - `JWT 4.2.1`
  - `Swagger 3.0.0`
  - `LucidChart` : ERD 툴
  - `Postman`
  - `Docker`
  - `AWS`
    - `Elastic BeanStalk`
    - `Route 53`
    - `AWS Certificate Manager`
- 인원 : 1명
- 기간 : 2023.08.01~2023.08.28

# 프로젝트 목적
- 평소에 농협 모바일 앱 `NH콕뱅크`를 자주 사용하고 있다.
- `NH콕뱅크` 앱은 계좌 이체와 같은 은행 관련 기능뿐만 계좌와 연동하여 쇼핑몰 서비스를 제공하고 있다.
- `NH콕뱅크` 앱은 백엔드 단계에서 어떻게 처리되고 있을까를 상상해보며 데이터베이스를 구축해보고
- `Spring Framework`를 활용하여 로직을 구현해봤다.

# 프로젝트 수행 과정
### 08.01 ~ 08.08 
- Spring Security JWT 세팅 및 로그인, 회원가입 기능 구현
- Swagger 세팅 
- @RestControllerAdvice를 통한 공통 예외 처리
- Spring AOP를 통한 Validation
- 은행 관련 기능
  - 계좌 생성, 삭제, 조회, 상세조회
  - 계좌 입금, 출금, 이체

### 08.09 ~ 08.16
- QueryDSL 복습 : [실전! Querydsl](https://www.inflearn.com/course/querydsl-%EC%8B%A4%EC%A0%84)
- Spring Data Jpa 복습 : [실전! 스프링 데이터 JPA](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84/dashboard)

### 08.17 ~ 08.23
- 쇼핑몰 관련 기능
  - 상품
  - 주문, 주문 아이템
  - 장바구니
- N + 1문제 해결과 컬렉션 페이징 조회를 위한 복습 : [실전! 스프링 부트와 JPA 활용2 - API 개발과 성능 최적화](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-API%EA%B0%9C%EB%B0%9C-%EC%84%B1%EB%8A%A5%EC%B5%9C%EC%A0%81%ED%99%94/dashboard)

### 08.24 ~ 08.28
- AWS를 활용한 HTTPS 배포
- 문서화 작업

# ERD
![bankshop_ERD](https://github.com/Griotold/Portfolio/assets/101307758/dfc430ce-623a-40fd-84ff-5b54670a986a)

# API 스펙
- [자세한 사항은 여기를 클릭!](https://github.com/Griotold/bankshop-griotold/blob/master/docs/api.md)
### 예시 - 계좌 입금
- https://www.griotold.shop/api/accounts/deposit
- Request Body
```
{
    "number" : "1111",
    "amount" : 100000,
    "transactionType" : "DEPOSIT",
    "tel" : "01012345678"
}
```
- Response Body
```
{
    "code": 1,
    "msg": "계좌 입금 완료",
    "data": {
        "id": 1,
        "number": 1111,
        "transactionDto": {
            "id": 6,
            "transactionType": "입금",
            "sender": "ATM",
            "receiver": "1111",
            "amount": 100000,
            "tel": "01012345678",
            "createdAt": "2023-08-28 11:02:51"
        }
    }
}
```

# 중요 포인트
## 1. @ExceptionHandler와 @RestControllerAdvice를 통한 예외처리
### @ExceptionHandler
- 스프링은 `API` 예외 처리 문제를 해결하기 위해 `@ExceptionHandler` 라는 애노테이션을 사용하는 매우 편리한 예외 처리 기능을 제공하는데,
- 이것이 바로 `ExceptionHandlerExceptionResolver` 이다.
- 스프링은 `ExceptionHandlerExceptionResolver` 를 기본으로 제공하고,
- 기본으로 제공하는 `ExceptionResolver` 중에 우선순위도 가장 높다.
- 실무에서 `API` 예외 처리는 대부분 이 기능을 사용한다.

### @RestControllerAdvice
- `@ExceptionHandler` 를 사용해서 예외를 깔끔하게 처리할 수 있게 되었지만,
- `@ExceptionHandler` 만 사용하면 정상 코드와 예외 처리 코드가 하나의 컨트롤러에 섞여 있게 된다.
- `@ControllerAdvice` 또는 `@RestControllerAdvice` 를 사용하면 둘을 분리할 수 있다.
    - 둘의 차이는 그냥 `@ResponseBody` 차이

### CustomExceptionHandler
- 스프링이 제공하는 두 기능을 활용하여 예외 처리를 적용했다.
- [자세한건 여기를 클릭!](https://github.com/Griotold/bankshop-griotold/blob/master/src/main/java/com/griotold/bankshop/handler/CustomExceptionalHandler.java)

## 2. AOP를 활용하여 Validation
- `Spring Bean Validation`으로 사용자의 입력을 검사할 수 있다.
- 유효성 검사는 하나의 서비스에만 국한된 것이 아니고 `Request Body`가 들어가는 모든 요청에 공통 관심사다.
- `AOP`를 도입하여 `Request Body`의 유효성 검사를 처리하는 방식으로 구현했다.


