# API 스펙
- /api/s/** : 일반 회원 로그인후 요청
- /api/admin/** : 관리자만 요청 가능

## 회원가입
- https://www.griotold.shop/api/join
- POST
### Request Body
```
{
    "username" : "smartdog",
    "password" : "libpencil34",
    "email" : "smartdog@nate.com",
    "fullName" : "똑똑한강아지",
    "address" : "행복시 희망구 사랑동 123번지"
}
```
### Response Body
```
{
  "code": 1,
    "msg": "회원가입 성공",
    "data": {
        "id": 5,
        "username": "smartdog",
        "fullName": "똑똑한강아지"
    }
}
```

## 로그인
- https://www.griotold.shop/api/login
- POST
### Request Body
```
{   
    "username" : "smartdog",
    "password" : "libpencil134"
}
```
### Response Body
```
{
    "code": 1,
    "msg": "로그인 성공",
    "data": {
        "id": 5,
        "username": "smartdog",
        "createdAt": "2023-08-28 11:15:04"
    }
}
```

## 계좌 등록
- https://www.griotold.shop/api/s/accounts
- POST
### Request Body - 헤더에 JWT 토큰을 함께 보내기
```
{
    "number": "2211",
    "password": "7890"
}
```
### Response Body
```
{
    "code": 1,
    "msg": "계좌 등록 성공",
    "data": {
        "id": 5,
        "number": 2211,
        "balance": 1000
    }
}
```
## 회원별 계좌 조회
- https://www.griotold.shop/api/s/accounts/login-user
- GET
### Request Body 는 없고, 헤더에 JWT 토큰만 보내기
### Response Body
```
{
    "code": 1,
    "msg": "계좌 목록 보기_유저별 성공",
    "data": {
        "fullName": "똑똑한강아지",
        "accountDtos": [
            {
                "id": 5,
                "number": 2211,
                "balance": 1000
            }
        ]
    }
}
```

## 계좌 삭제
- https://www.griotold.shop/api/s/accounts/{accountNumber}
- DELETE
### Response Body
```
{
    "code": 1,
    "msg": "계좌 삭제 완료",
    "data": null
}
```

## 전체 계좌 조회 - 관리자
- https://www.griotold.shop/api/admin/accounts
- GET
### Response Body
```
{
    "code": 1,
    "msg": "전체 계좌 목록 보기_관리자 성공",
    "data": {
        "accountDtos": [
            {
                "accountId": 1,
                "userId": 2,
                "number": 1111,
                "balance": 100800
            },
            {
                "accountId": 2,
                "userId": 3,
                "number": 2222,
                "balance": 1100
            },
            {
                "accountId": 3,
                "userId": 4,
                "number": 3333,
                "balance": 1100
            },
            {
                "accountId": 4,
                "userId": 2,
                "number": 4444,
                "balance": 1000
            }
        ]
    }
}
```

## 계좌 입금
- https://www.griotold.shop/api/accounts/deposit
- POST
### Request Body
```
{
    "number" : "1111",
    "amount" : 100000,
    "transactionType" : "DEPOSIT",
    "tel" : "01012345678"
}
```
### Response Body
```
{
    "code": 1,
    "msg": "계좌 입금 완료",
    "data": {
        "id": 1,
        "number": 1111,
        "transactionDto": {
            "id": 7,
            "transactionType": "입금",
            "sender": "ATM",
            "receiver": "1111",
            "amount": 100000,
            "tel": "01012345678",
            "createdAt": "2023-08-28 11:32:21"
        }
    }
}
```
## 계좌 출금
- https://www.griotold.shop/api/accounts/withdraw
- POST
### Request Body
```
{
    "number" : "1111",
    "password" : "1234",
    "amount" : 100,
    "transactionType" : "WITHDRAW"
}
```
### Response Body
```
{
    "code": 1,
    "msg": "계좌 출금 완료",
    "data": {
        "id": 1,
        "number": 1111,
        "balance": 200700,
        "transactionDto": {
            "id": 8,
            "transactionType": "출금",
            "sender": "1111",
            "receiver": "ATM",
            "amount": 100,
            "createdAt": "2023-08-28 11:35:52"
        }
    }
}
```

## 계좌 이체
- https://www.griotold.shop/api/accounts/transfer
- POST
### Request Body
```
{
    "withdrawNumber" : "1111",
    "depositNumber" : "3333",
    "withdrawPassword" : "1234",
    "amount" : 100,
    "transactionType" : "TRANSFER"
}
```
### Response Body
```
{
    "code": 1,
    "msg": "계좌 이체 완료",
    "data": {
        "accountId": 1,
        "number": 1111,
        "balance": 200600,
        "transactionDto": {
            "id": 9,
            "transactionType": "이체",
            "sender": "1111",
            "receiver": "3333",
            "amount": 100,
            "createdAt": "2023-08-28 11:38:12"
        }
    }
}
```

## 입출금 목록 보기
- https://www.griotold.shop/api/s/accounts/1111/transactions?transactionType=DEPOSIT&page=0
- GET
- 쿼리 파라미터
  - transactionType
    - DEPOSIT, WITHDRAW, TRANSFER
  - page
  - size
### Response Body
```
{
    "code": 1,
    "msg": "입출금 목록 보기 성공",
    "data": {
        "transactionDtos": {
            "content": [
                {
                    "id": 5,
                    "transactionType": "이체",
                    "amount": 100,
                    "sender": "2222",
                    "receiver": "1111",
                    "tel": "없음",
                    "createdAt": "2023-08-28 11:00:57",
                    "balance": 800
                },
                {
                    "id": 6,
                    "transactionType": "입금",
                    "amount": 100000,
                    "sender": "ATM",
                    "receiver": "1111",
                    "tel": "01012345678",
                    "createdAt": "2023-08-28 11:02:51",
                    "balance": 100800
                },
                {
                    "id": 7,
                    "transactionType": "입금",
                    "amount": 100000,
                    "sender": "ATM",
                    "receiver": "1111",
                    "tel": "01012345678",
                    "createdAt": "2023-08-28 11:32:21",
                    "balance": 200800
                }
            ],
            "pageable": {
                "sort": {
                    "empty": true,
                    "sorted": false,
                    "unsorted": true
                },
                "offset": 0,
                "pageNumber": 0,
                "pageSize": 5,
                "paged": true,
                "unpaged": false
            },
            "last": true,
            "totalPages": 1,
            "totalElements": 3,
            "size": 5,
            "number": 0,
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "first": true,
            "numberOfElements": 3,
            "empty": false
        }
    }
}
```

## 계좌 상세 보기
- https://www.griotold.shop/api/s/accounts/{accountNumber}
- GET
- 쿼리 파라미터
  - page
  - size
### Response Body
```
{
    "code": 1,
    "msg": "계좌 상세보기 성공",
    "data": {
        "accountId": 1,
        "number": 1111,
        "balance": 200600,
        "transactionDtos": {
            "content": [
                {
                    "id": 1,
                    "transactionType": "출금",
                    "amount": 100,
                    "sender": "1111",
                    "receiver": "ATM",
                    "tel": "없음",
                    "createdAt": "2023-08-28 11:00:57",
                    "balance": 900
                },
                {
                    "id": 3,
                    "transactionType": "이체",
                    "amount": 100,
                    "sender": "1111",
                    "receiver": "2222",
                    "tel": "없음",
                    "createdAt": "2023-08-28 11:00:57",
                    "balance": 800
                },
                {
                    "id": 4,
                    "transactionType": "이체",
                    "amount": 100,
                    "sender": "1111",
                    "receiver": "3333",
                    "tel": "없음",
                    "createdAt": "2023-08-28 11:00:57",
                    "balance": 700
                },
                {
                    "id": 5,
                    "transactionType": "이체",
                    "amount": 100,
                    "sender": "2222",
                    "receiver": "1111",
                    "tel": "없음",
                    "createdAt": "2023-08-28 11:00:57",
                    "balance": 800
                },
                {
                    "id": 6,
                    "transactionType": "입금",
                    "amount": 100000,
                    "sender": "ATM",
                    "receiver": "1111",
                    "tel": "01012345678",
                    "createdAt": "2023-08-28 11:02:51",
                    "balance": 100800
                }
            ],
            "pageable": {
                "sort": {
                    "empty": true,
                    "sorted": false,
                    "unsorted": true
                },
                "offset": 0,
                "pageNumber": 0,
                "pageSize": 5,
                "paged": true,
                "unpaged": false
            },
            "last": false,
            "totalPages": 2,
            "totalElements": 8,
            "size": 5,
            "number": 0,
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "first": true,
            "numberOfElements": 5,
            "empty": false
        }
    }
}
```
