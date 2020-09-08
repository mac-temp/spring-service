package com.mac.offerservice.domain.offer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mac.offerservice.helpers.offer.OfferBuilder.anOffer;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OfferRepositoryShould {

    private static final UUID UUID = java.util.UUID.randomUUID();
    private static final Offer OFFER_2_4_1 = anOffer().withName("2_4_1").withId(UUID.toString()).build();
    private static final String UPDATED_DESCRIPTION = "Updated description";
    private static final Offer OFFER_2_4_1_UPDATED = anOffer()
            .withId(OFFER_2_4_1.getId())
            .withName(OFFER_2_4_1.getName())
            .withStartDate(OFFER_2_4_1.getStartDate())
            .withEndDate(OFFER_2_4_1.getEndDate())
            .withDescription(UPDATED_DESCRIPTION)
            .build();
    private static final Offer OFFER_HALF_PRICE = anOffer().withName("half-price").build();

    @Autowired
    private OfferRepository repository;

    @Test
    void add_offer() {
        repository.add(OFFER_2_4_1);

        assertThat(repository.has(OFFER_2_4_1)).isEqualTo(true);
    }

    @Test
    void get_one_offer_by_id() {
        repository.add(OFFER_2_4_1);

        assertThat(repository.getById(UUID)).isEqualTo(Optional.of(OFFER_2_4_1));
    }

    @Test
    void return_a_list_of_offers() {
        repository.add(OFFER_2_4_1);
        repository.add(OFFER_HALF_PRICE);

        List<Offer> result = repository.getAll();
        assertThat(result).contains(OFFER_2_4_1);
        assertThat(result).contains(OFFER_HALF_PRICE);
    }

    @Test
    void update_an_offer() {
        repository.add(OFFER_2_4_1);
        repository.update(OFFER_2_4_1_UPDATED);

        Optional<Offer> updatedOffer = repository.getById(UUID);

        assertThat(updatedOffer).isPresent();
        assertThat(updatedOffer.get().getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }
}
