package com.griotold.bankshop.web;

import com.griotold.bankshop.dto.ResponseDto;
import com.griotold.bankshop.dto.item.ItemReqDto;
import com.griotold.bankshop.dto.item.ItemRespDto;
import com.griotold.bankshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.griotold.bankshop.dto.item.ItemReqDto.*;
import static com.griotold.bankshop.dto.item.ItemRespDto.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/admin/items")
    public ResponseEntity<?> registerItem(@RequestBody @Valid ItemRegisterReqDto itemRegisterReqDto,
                                          BindingResult bindingResult) {
        ItemRegisterRespDto itemRegisterRespDto = itemService.register(itemRegisterReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "아이템 등록 성공", itemRegisterRespDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/items")
    public ResponseEntity<?> retrieveItemList() {
        ItemListRespDto itemListRespDto = itemService.itemList();
        return new ResponseEntity<>(new ResponseDto<>(1, "상품 리스트", itemListRespDto),
                HttpStatus.OK);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<?> retrieveItem(@PathVariable Long id) {
        ItemIdRespDto itemIdRespDto = itemService.findOne(id);
        return new ResponseEntity<>(new ResponseDto<>(1, id + "번 상품", itemIdRespDto),
                HttpStatus.OK);
    }
    @PutMapping("/admin/items/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id,
                                        @RequestBody @Valid ItemEditReqDto itemEditReqDto,
                                        BindingResult bindingResult) {
        ItemEditRespDto itemEditRespDto = itemService.editItem(itemEditReqDto, id);
        return new ResponseEntity<>(new ResponseDto<>(1, "상품 수정 완료", itemEditRespDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/admin/items/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return new ResponseEntity<>(new ResponseDto<>(1, "상품 삭제 완료", null),
                HttpStatus.OK);
    }
}
