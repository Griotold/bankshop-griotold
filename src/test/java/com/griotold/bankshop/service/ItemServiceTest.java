package com.griotold.bankshop.service;

import com.griotold.bankshop.config.dummy.DummyObject;
import com.griotold.bankshop.domain.item.Item;
import com.griotold.bankshop.domain.item.ItemImg;
import com.griotold.bankshop.domain.item.ItemImgRepository;
import com.griotold.bankshop.domain.item.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import static com.griotold.bankshop.dto.item.ItemReqDto.ItemEditReqDto;
import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class ItemServiceTest extends DummyObject {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("editEntity() 테스트")
    void editEntity_test() throws Exception {
        // given
        Item item = newItem("테스트 아이템");
        Item itemPS = itemRepository.save(item);

        ItemImg itemImg = newItemImg("테스트 아이템 이미지 네임", item);
        ItemImg itemImgPS = itemImgRepository.save(itemImg);

        ItemEditReqDto itemEditReqDto = new ItemEditReqDto();
        itemEditReqDto.setItemId(itemPS.getId());
        itemEditReqDto.setPrice(4567);
        itemEditReqDto.setItemDetail("수정후 상세 설명");
        itemEditReqDto.setItemSellStatus("SOLD_OUT");
        itemEditReqDto.setImgName("수정후 이미지네임");

        // when
        itemService.editEntity(itemEditReqDto, itemPS, itemImgPS);
        em.flush();
        em.clear();

        Item findedItem = itemRepository.findById(itemPS.getId())
                .orElseThrow(EntityNotFoundException::new);
        ItemImg findedItemImg = itemImgRepository.findByItem(itemPS);

        // then
        assertThat(findedItem.getPrice()).isEqualTo(4567);
        assertThat(findedItem.getItemDetail()).isEqualTo("수정후 상세 설명");
        assertThat(findedItem.getStockNumber()).isEqualTo(100);
        assertThat(findedItem.getItemSellStatus().toString()).isEqualTo("SOLD_OUT");
        assertThat(findedItemImg.getImgName()).isEqualTo("수정후 이미지네임");
        assertThat(findedItemImg.getOriImgName()).isEqualTo("원본 이미지명");
    }

}