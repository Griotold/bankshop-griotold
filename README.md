# bankshop-griotold
## 은행 거래 및 쇼핑몰 서비스 REST API

- 사이트 주소 : https://www.griotold.shop
  - 비용 문제로 서비스 종료  
- 깃허브 : https://github.com/Griotold/bankshop-griotold
- 사용 툴
  - Java 11
  - Spring 2.7.14
  - QueryDSL 5.0.0
  - JPA
  - MySql 8
  - h2
  - JWT 4.2.1
  - Swagger 3.0.0
  - LucidChart : ERD 툴
  - Postman
  - Docker
  - AWS
    - Elastic BeanStalk
    - Route 53
    - AWS Certificate Manager
- 인원 : 1명
- 기간 : 2023.08.01~2023.08.28

# 프로젝트 목적
- 평소에 농협 모바일 앱 NH콕뱅크를 자주 사용하고 있다. NH콕뱅크 앱은 계좌 이체와 같은 은행 관련 기능뿐만 계좌와 연동하여 쇼핑몰 서비스를 제공하고 있다.
- 해당 앱의 백엔드 단계에서는 어떻게 처리되고 있을까를 상상해보며 데이터베이스를 구축해보고 Spring Framework를 통하여 로직을 구현해봤다.

# 프로젝트 수행 과정
### 08.01 ~ 08.08 
- Spring Security JWT 세팅 및 로그인, 회원가입 기능 구현
- Swagger 세팅 
- @ControllerAdvice를 통한 공통 예외 처리
- Spring AOP를 통한 Validation
- 은행 관련 기능
  - 계좌 생성, 삭제, 조회, 상세조회
  - 계좌 입금, 출금, 이체

### 08.09 ~ 08.16
- QueryDSL 복습 : https://www.inflearn.com/course/querydsl-%EC%8B%A4%EC%A0%84
- Spring Data Jpa 복습 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84/dashboard

### 08.17 ~ 08.23
- 쇼핑몰 관련 기능
  - 상품
  - 주문, 주문 아이템
  - 장바구니

### 08.24 ~ 08.28
- AWS를 활용한 HTTPS 배포
- 문서화 작업

# 중요 포인트
## 1. ERD
![bankshop_ERD](https://github.com/Griotold/Portfolio/assets/101307758/dfc430ce-623a-40fd-84ff-5b54670a986a)

## 2. API 
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





