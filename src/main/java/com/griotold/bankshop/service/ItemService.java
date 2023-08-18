package com.griotold.bankshop.service;

import com.griotold.bankshop.domain.item.*;
import com.griotold.bankshop.domain.user.UserRepository;
import com.griotold.bankshop.dto.item.ItemReqDto;
import com.griotold.bankshop.dto.item.ItemRespDto;
import com.griotold.bankshop.handler.ex.CustomApiException;
import com.griotold.bankshop.handler.ex.CustomForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ItemImgQueryRepository itemImgQueryRepository;

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

    public ItemList4AdminDto itemList4Admin(String itemSellStatus, Pageable pageable) {
        Page<ItemImg> itemImgPage = itemImgQueryRepository.findAllPage(itemSellStatus, pageable);
        return new ItemList4AdminDto(itemImgPage);
    }

    public ItemListRespDto itemList4Customer(Pageable pageable) {
        Page<ItemImg> itemImgPage = itemImgQueryRepository.findAllPage("SELL", pageable);
        return new ItemListRespDto(itemImgPage);
    }


    public ItemIdRespDto findOne(Long itemId){
        Item itemPS = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomApiException("상품을 찾을 수 없습니다."));

        if (itemPS.getItemSellStatus() == ItemSellStatus.SOLD_OUT) {
            throw new CustomForbiddenException("권한이 없습니다.");
        }
        ItemImg itemImgPS = itemImgRepository.findByItem(itemPS);
        return new ItemIdRespDto(itemImgPS);
    }

    public ItemIdRespDto findOne4Admin(Long itemId) {

        Item itemPS = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomApiException("상품을 찾을 수 없습니다."));

        ItemImg itemImgPS = itemImgRepository.findByItem(itemPS);
        return new ItemIdRespDto(itemImgPS);

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
