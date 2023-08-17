package com.griotold.bankshop.service;

import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemImg;
import com.griotold.bankshop.domain.item.ItemImgRepository;
import com.griotold.bankshop.domain.item.ItemRepository;
import com.griotold.bankshop.domain.user.UserRepository;
import com.griotold.bankshop.dto.item.ItemReqDto;
import com.griotold.bankshop.dto.item.ItemRespDto;
import com.griotold.bankshop.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.griotold.bankshop.dto.item.ItemReqDto.*;
import static com.griotold.bankshop.dto.item.ItemReqDto.ItemRegisterReqDto.*;
import static com.griotold.bankshop.dto.item.ItemRespDto.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;

    @Transactional
    public ItemRegisterRespDto register(ItemRegisterReqDto itemRegisterReqDto) {
        Optional<Item> itemOP = itemRepository.findByItemName(itemRegisterReqDto.getItemName());
        if (itemOP.isPresent()) {
            throw new CustomApiException("해당 상품은 이미 존재합니다.");
        }

        Item item = itemRegisterReqDto.toEntity();
        Item itemPS = itemRepository.save(item);

        ItemImg itemImg = ItemImg.builder()
                .imgName(itemRegisterReqDto.getImgName())
                .oriImgName(itemRegisterReqDto.getOriImgName())
                .imgUrl(itemRegisterReqDto.getImgUrl())
                .item(itemPS)
                .build();

        ItemImg itemImgPS = itemImgRepository.save(itemImg);
        return new ItemRegisterRespDto(itemPS, itemImgPS);
    }

    public ItemListRespDto itemList() {
        List<Item> itemListPS = itemRepository.findAll();
        return new ItemListRespDto(itemListPS);
    }

    public ItemIdRespDto findOne(Long itemId){
        Item itemPS = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomApiException("상품을 찾을 수 없습니다."));
        return new ItemIdRespDto(itemPS);
    }



    @Transactional
    public void deleteItem(Long itemId) {
        Item itemPS = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomApiException("상품을 찾을 수 없습니다."));
        itemRepository.deleteById(itemId);
    }
}
