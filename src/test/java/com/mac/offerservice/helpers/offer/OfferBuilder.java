package com.mac.offerservice.helpers.offer;

import com.mac.offerservice.domain.offer.Offer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;

public class OfferBuilder {
    private String id = UUID.randomUUID().toString();
    private String name = "default name";
    private BigDecimal price = BigDecimal.TEN;
    private String description = "default name";
    private LocalDateTime startDate = now();
    private LocalDateTime endDate = now().plusHours(1);
    private boolean enabled = true;

    public static OfferBuilder anOffer() {
        return new OfferBuilder();
    }

    public OfferBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public OfferBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public OfferBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public OfferBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public OfferBuilder withStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public OfferBuilder withEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public OfferBuilder withEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Offer build() {
        return new Offer(id, name, price, description, startDate, endDate, enabled);
    }
}