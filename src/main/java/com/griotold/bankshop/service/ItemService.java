package com.griotold.bankshop.service;

import com.griotold.bankshop.domain.item.*;
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
import java.util.concurrent.CancellationException;

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

    @Transactional
    public ItemEditRespDto editItem(ItemEditReqDto itemEditReqDto, Long itemId) {
        if (itemEditReqDto.getItemId().longValue() != itemId.longValue()) {
            throw new CustomApiException("URL 주소와 상품 id가 다릅니다.");
        }

        Item itemPS = itemRepository.findById(itemEditReqDto.getItemId())
                .orElseThrow(() -> new CustomApiException("상품을 찾을 수 없습니다."));

        ItemImg itemImgPS = itemImgRepository.findByItem(itemPS);

        editEntity(itemEditReqDto, itemPS, itemImgPS);

        return new ItemEditRespDto(itemPS, itemImgPS);
    }

    public void editEntity(ItemEditReqDto itemEditReqDto, Item item, ItemImg itemImg) {
        Optional.ofNullable(itemEditReqDto.getPrice()).ifPresent(item::setPrice);
        Optional.ofNullable(itemEditReqDto.getStockNumber()).ifPresent(item::setStockNumber);
        Optional.ofNullable(itemEditReqDto.getItemDetail()).ifPresent(item::setItemDetail);
        Optional.ofNullable(itemEditReqDto.getItemSellStatus())
                .ifPresent(status -> item.setItemSellStatus(ItemSellStatus.valueOf(status)));
        Optional.ofNullable(itemEditReqDto.getImgName()).ifPresent(itemImg::setImgName);
        Optional.ofNullable(itemEditReqDto.getOriImgName()).ifPresent(itemImg::setOriImgName);
        Optional.ofNullable(itemEditReqDto.getImgUrl()).ifPresent(itemImg::setImgUrl);
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

        ItemImg itemImg = itemImgRepository.findByItem(itemPS);
        if (itemImg != null) {
            itemImgRepository.delete(itemImg);
        }
        itemRepository.deleteById(itemId);
    }
}
