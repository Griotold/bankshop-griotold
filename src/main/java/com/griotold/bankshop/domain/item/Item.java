package com.griotold.bankshop.domain.item;

import com.griotold.bankshop.handler.ex.CustomOutOfStockException;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "item_tb")
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemSellStatus itemSellStatus;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Item(Long id, String itemName, int price, int stockNumber,
                String itemDetail, ItemSellStatus itemSellStatus,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;
        if (restStock < 0) {
            throw new CustomOutOfStockException("상품의 재고가 부족합니다. 현재 재고 수량: " + this.stockNumber);
        }
        this.stockNumber = restStock;
    }
}
