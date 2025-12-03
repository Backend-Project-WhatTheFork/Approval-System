package com.whatthefork.resourcereservation.resource.domain;

import com.whatthefork.resourcereservation.resource.enums.ResourceCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int maxCapacity;

    @Column(nullable = false)
    private boolean isBooked = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceCategory category;

    @Builder
    public Resources(String name, int maxCapacity, ResourceCategory category) {
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.category = category;
    }
}
