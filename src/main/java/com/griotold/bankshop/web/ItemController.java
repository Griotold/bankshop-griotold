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

    @PostMapping("/admin/item")
    public ResponseEntity<?> registerItem(@RequestBody @Valid ItemRegisterReqDto itemRegisterReqDto,
                                          BindingResult bindingResult) {
        ItemRegisterRespDto itemRegisterRespDto = itemService.register(itemRegisterReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "아이템 등록 성공", itemRegisterRespDto),
                HttpStatus.CREATED);
    }
}
