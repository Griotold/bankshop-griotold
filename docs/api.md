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

## 상품 등록 - 관리자
- https://www.griotold.shop/api/admin/items
- POST
### Request Body
```
{
    "itemName": "연필",
    "price": 7000,
    "stockNumber": 70,
    "itemDetail": "연필 상세 설명",
    "imgName": "연필 이미지.jpg",
    "oriImgName": "연필 원본이미지.jpg",
    "imgUrl": "www.pencil.com"
}
```
### Response Body
```
{
    "code": 1,
    "msg": "아이템 등록 성공",
    "data": {
        "id": 4,
        "itemName": "연필",
        "price": 7000,
        "stockNumber": 70,
        "itemSellStatus": "SELL",
        "itemImgId": 4
    }
}
```

## 상품 목록 조회 - 고객용
- https://www.griotold.shop/api/items
- GET
### Responseo body
```
{
    "code": 1,
    "msg": "상품 리스트",
    "data": {
        "itemDtos": {
            "content": [
                {
                    "id": 1,
                    "itemName": "츄르",
                    "price": 10000,
                    "stockNumber": 100,
                    "itemSellStatus": "SELL",
                    "imgName": "츄르 이미지",
                    "oriImgName": "원본 이미지명",
                    "imgUrl": "www.test.com"
                },
                {
                    "id": 2,
                    "itemName": "안경닦이",
                    "price": 10000,
                    "stockNumber": 100,
                    "itemSellStatus": "SELL",
                    "imgName": "안경닦이 이미지",
                    "oriImgName": "원본 이미지명",
                    "imgUrl": "www.test.com"
                },
                {
                    "id": 3,
                    "itemName": "물티슈",
                    "price": 10000,
                    "stockNumber": 100,
                    "itemSellStatus": "SELL",
                    "imgName": "물티슈 이미지",
                    "oriImgName": "원본 이미지명",
                    "imgUrl": "www.test.com"
                },
                {
                    "id": 4,
                    "itemName": "연필",
                    "price": 7000,
                    "stockNumber": 70,
                    "itemSellStatus": "SELL",
                    "imgName": "연필 이미지.jpg",
                    "oriImgName": "연필 원본이미지.jpg",
                    "imgUrl": "www.pencil.com"
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
            "totalElements": 4,
            "size": 5,
            "number": 0,
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "first": true,
            "numberOfElements": 4,
            "empty": false
        }
    }
}
```

## 상품 목록 조회 - 관리자용
- https://www.griotold.shop/api/admin/items?status=SOLD_OUT
- GET
- 쿼리파라미터
  - status
    - SELL
    - SOLD_OUT
  - page
  - size 
### Response Body
```
{
    "code": 1,
    "msg": "관리자용 상품 목록",
    "data": {
        "itemDtos": {
            "content": [
                {
                    "id": 1,
                    "itemName": "츄르",
                    "price": 10000,
                    "stockNumber": 100,
                    "itemSellStatus": "SOLD_OUT",
                    "imgName": "츄르 이미지",
                    "oriImgName": "원본 이미지명",
                    "imgUrl": "www.test.com"
                },
                {
                    "id": 2,
                    "itemName": "안경닦이",
                    "price": 10000,
                    "stockNumber": 100,
                    "itemSellStatus": "SOLD_OUT",
                    "imgName": "안경닦이 이미지",
                    "oriImgName": "원본 이미지명",
                    "imgUrl": "www.test.com"
                },
                {
                    "id": 3,
                    "itemName": "물티슈",
                    "price": 10000,
                    "stockNumber": 100,
                    "itemSellStatus": "SOLD_OUT",
                    "imgName": "물티슈 이미지",
                    "oriImgName": "원본 이미지명",
                    "imgUrl": "www.test.com"
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
            "numberOfElements": 4,
            "empty": false
        }
    }
}
```

## 상품 상세 - 고객용
- https://www.griotold.shop/api/items/{itemId}
- GET
### Response Body
```
{
    "code": 1,
    "msg": "1번 상품",
    "data": {
        "id": 1,
        "itemName": "츄르",
        "price": 10000,
        "stockNumber": 100,
        "itemSellStatus": "SELL",
        "imgName": "츄르 이미지",
        "oriImgName": "원본 이미지명",
        "imgUrl": "www.test.com"
    }
}
```

## 상품 상세 - 관리자용
- https://www.griotold.shop/api/admin/items/{itemId}
- GET
### Response Body
```
{
    "code": 1,
    "msg": "2번 상품",
    "data": {
        "id": 2,
        "itemName": "안경닦이",
        "price": 10000,
        "stockNumber": 100,
        "itemSellStatus": "SOLD_OUT",
        "imgName": "안경닦이 이미지",
        "oriImgName": "원본 이미지명",
        "imgUrl": "www.test.com"
    }
}
```

## 상품 수정 - 관리자용
- https://www.griotold.shop/api/admin/items/{itemId}
- PUT
### Request Body
```
{
    "itemId" : 3,
    "price": 4446,
    "stockNumber": 4444,
    "itemSellStatus" : "SOLD_OUT",
    "imgName": "수정33 이미지.jpg",
    "oriImgName": "수정33 원본이미지.jpg",
    "imgUrl": "www.editpe333ncil.com"
}
```
### Response Body
```
{
    "code": 1,
    "msg": "상품 수정 완료",
    "data": {
        "id": 3,
        "itemName": "물티슈",
        "price": 4446,
        "stockNumber": 4444,
        "itemDetail": "테스트 아이템 설명",
        "itemSellStatus": "SOLD_OUT",
        "itemImgId": 3,
        "imgName": "수정33 이미지.jpg",
        "oriImgName": "수정33 원본이미지.jpg",
        "imgUrl": "www.editpe333ncil.com"
    }
}
```

## 상품 삭제 - 관리자용
- https://www.griotold.shop/api/admin/items/{itemId}
- DELETE
### Response Body
```
{
    "code": 1,
    "msg": "상품 삭제 완료",
    "data": null
}
```

## 주문 요청
- https://www.griotold.shop/api/s/orders
- POST
### Request Body
```
{
    "accountNumber" : 1111,
    "accountPassword" : 1234,
    "itemId":3,
    "count":2
}
```
### Response Body
```
{
    "code": 1,
    "msg": "주문 완료",
    "data": {
        "accountNumber": 1111,
        "balance": 80800,
        "orderId": 1,
        "orderDate": "2023-08-28 12:30:32",
        "orderStatus": "주문",
        "orderItemDto": {
            "itemId": 3,
            "itemName": "물티슈",
            "count": 2,
            "totalPrice": 20000
        }
    }
}
```

## 주문 취소
- https://www.griotold.shop/api/s/orders/{orderId}
- POST
### Request Body
```
{
    "accountNumber" : 1111,
    "orderId":1
}
```
### Response Body
```
{
    "code": 1,
    "msg": "주문 취소 완료",
    "data": null
}
```

## 주문 이력 조회
- https://www.griotold.shop/api/s/orders/v2/login-user
- GET
- 쿼리 파라미터
  - page
  - size
### Response Body
```
{
    "code": 1,
    "msg": "주문 이력 조회 버젼2",
    "data": {
        "userId": 2,
        "username": "griotold",
        "orderDtoList": {
            "content": [
                {
                    "orderId": 1,
                    "orderTotalPrice": 20000,
                    "orderItemDtoList": [
                        {
                            "itemId": 3,
                            "itemName": "물티슈",
                            "count": 2,
                            "totalPrice": 20000
                        }
                    ]
                },
                {
                    "orderId": 2,
                    "orderTotalPrice": 20000,
                    "orderItemDtoList": [
                        {
                            "itemId": 2,
                            "itemName": "안경닦이",
                            "count": 2,
                            "totalPrice": 20000
                        }
                    ]
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
                "unpaged": false,
                "paged": true
            },
            "last": true,
            "totalElements": 2,
            "totalPages": 1,
            "size": 5,
            "number": 0,
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "first": true,
            "numberOfElements": 2,
            "empty": false
        }
    }
}
```

## 장바구니 담기
- https://www.griotold.shop/api/s/cart/items
- POST
### Request Body
```
{
    "itemId":3,
    "count":2
}
```
### Response Body
```
{
    "code": 1,
    "msg": "장바구니 담기 성공",
    "data": {
        "itemId": 3,
        "itemName": "물티슈",
        "count": 2,
        "orderPrice": 20000
    }
}
```

## 장바구니 목록 조회
- https://www.griotold.shop/api/s/cart/items
- GET
- 쿼리 파라미터
  - page
  - size
### Response Body
```
{
    "code": 1,
    "msg": "장바구니 목록 보기",
    "data": {
        "cartItems": {
            "content": [
                {
                    "cartItemId": 1,
                    "itemName": "물티슈",
                    "price": 10000,
                    "count": 2,
                    "orderPrice": 20000
                },
                {
                    "cartItemId": 2,
                    "itemName": "안경닦이",
                    "price": 10000,
                    "count": 2,
                    "orderPrice": 20000
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
                "unpaged": false,
                "paged": true
            },
            "last": true,
            "totalElements": 2,
            "totalPages": 1,
            "size": 5,
            "number": 0,
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "first": true,
            "numberOfElements": 2,
            "empty": false
        }
    }
}
```

## 장바구니 상품 수량 수정
- https://www.griotold.shop/api/s/cart/items/{itemId}
- PUT
### Request Body
```
{
    "cartItemId" : 2,
    "count": 5
}
```
### Response Body
```
{
    "code": 1,
    "msg": "장바구니 상품 수량 업데이트 성공",
    "data": {
        "cartItemId": 2,
        "itemName": "안경닦이",
        "count": 5,
        "orderPrice": 50000
    }
}
```

## 장바구니 상품 삭제
- https://www.griotold.shop/api/s/cart/items/{itemId}
- DELETE
### Response Body
```
{
    "code": 1,
    "msg": "장바구니 상품 삭제 성공",
    "data": null
}
```

## 장바구니 상품 주문
- https://www.griotold.shop/api/s/cart/orders
- POST
### Request Body
```
{
    "accountNumber":1111,
    "accountPassword":1234
}
```
### Response Body
```
{
    "code": 1,
    "msg": "장바구니 주문 성공",
    "data": {
        "orderId": 3,
        "accountNumber": 1111,
        "balance": 80800,
        "totalPrice": 100000
    }
}
```
