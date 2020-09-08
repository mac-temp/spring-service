package com.mac.offerservice.domain.offer;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

@StartShouldBeBeforeEnd(startDate = "startDate", endDate = "endDate")
public class UpdateOfferCommand {
    @NotBlank
    private final String id;
    @NotBlank
    private final String name;
    @Positive
    private final BigDecimal price;
    @NotBlank
    private final String description;
    @Future
    private final LocalDateTime startDate;
    @Future
    private final LocalDateTime endDate;

    public UpdateOfferCommand(String id,
                              String name,
                              BigDecimal price,
                              String description,
                              LocalDateTime startDate,
                              LocalDateTime endDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    public Offer execute(Offer offer) {
        offer.setName(this.name);
        offer.setPrice(this.price);
        offer.setDescription(this.description);
        offer.setStartDate(this.startDate);
        offer.setEndDate(this.endDate);
        return offer;
    }
}
