package com.griotold.bankshop.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    ItemImg findByItem(Item item);
}
