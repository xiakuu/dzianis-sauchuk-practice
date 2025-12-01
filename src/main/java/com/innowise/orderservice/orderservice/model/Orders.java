package com.innowise.orderservice.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private String status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItems> orderItems;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @PrePersist
    public void setStartData() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "new";
        }
    }

}
