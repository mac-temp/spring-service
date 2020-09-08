package com.mac.offerservice.domain.offer;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

@StartShouldBeBeforeEnd(startDate = "startDate", endDate = "endDate")
public class CreateOfferCommand {
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

    private final boolean enabled;

    public CreateOfferCommand(String name,
                              BigDecimal price,
                              String description,
                              LocalDateTime startDate,
                              LocalDateTime endDate,
                              boolean enabled) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.enabled = enabled;
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

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    public Offer execute() {
        return new Offer(UUID.randomUUID().toString(), name, price, description, startDate, endDate, enabled);
    }
}
