package com.mac.offerservice.domain.offer;

import com.mac.offerservice.rest.OfferAlreadyExistsException;
import com.mac.offerservice.rest.OfferNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mac.offerservice.helpers.offer.OfferBuilder.anOffer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
class OfferServiceShould {

    private static final UUID OFFER_ID = java.util.UUID.randomUUID();
    private static final Offer OFFER = anOffer()
            .withId(OFFER_ID.toString())
            .build();
    private static final Offer EXPIRED_OFFER = anOffer()
            .withId(OFFER.getId())
            .withStartDate(OFFER.getStartDate())
            .withEndDate(OFFER.getEndDate())
            .withEnabled(false)
            .build();
    private static final List<Offer> LIST_OF_OFFERS = List.of(OFFER);

    @MockBean
    OfferRepository repository;
    @MockBean
    CreateOfferCommand createCommand;
    @MockBean
    UpdateOfferCommand updateCommand;

    @Autowired
    private OfferService offerService;


    @Test
    void create_offer() throws OfferAlreadyExistsException {
        given(createCommand.execute()).willReturn(OFFER);
        given(repository.add(OFFER)).willReturn(OFFER);

        Offer result = offerService.createOffer(createCommand);
        assertThat(result).isEqualTo(OFFER);
    }

    @Test
    void not_create_one_if_one_already_exists() {
        given(createCommand.execute()).willReturn(OFFER);
        given(repository.has(OFFER)).willReturn(true);

        assertThrows(
                OfferAlreadyExistsException.class,
                () -> offerService.createOffer(createCommand)
        );
    }

    @Test
    void return_a_specific_offer_that_exists() {
        given(repository.getById(OFFER_ID)).willReturn(Optional.of(OFFER));

        Optional<Offer> result = offerService.getOfferById(OFFER_ID);
        assertThat(result).isEqualTo(Optional.of(OFFER));
    }

    @Test
    void return_empty_if_queried_offer_does_not_exists() {
        given(repository.getById(OFFER_ID)).willReturn(Optional.empty());

        Optional<Offer> result = offerService.getOfferById(OFFER_ID);
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    void return_a_list_of_offers() {
        given(repository.getAll()).willReturn(LIST_OF_OFFERS);

        List<Offer> result = offerService.getOffers();
        assertThat(result).isEqualTo(LIST_OF_OFFERS);
    }

    @Test
    void update_offer_if_exists() {
        given(updateCommand.getId()).willReturn(OFFER_ID.toString());
        given(updateCommand.execute(OFFER)).willReturn(OFFER);
        given(repository.getById(OFFER_ID)).willReturn(Optional.of(OFFER));
        given(repository.update(OFFER)).willReturn(OFFER);

        offerService.updateOffer(updateCommand);
        verify(repository).update(OFFER);
    }

    @Test
    void return_exception_if_updated_offer_does_not_exist() {
        given(updateCommand.getId()).willReturn(OFFER_ID.toString());
        given(repository.getById(OFFER_ID)).willReturn(Optional.empty());

        assertThrows(
                OfferNotFoundException.class,
                () -> offerService.updateOffer(updateCommand)
        );
        verify(repository).getById(OFFER_ID);
    }

    @Test
    void expire_offer_if_exists() {
        given(repository.getById(OFFER_ID)).willReturn(Optional.of(OFFER));
        given(repository.update(any())).willReturn(EXPIRED_OFFER);

        offerService.disable(OFFER);

        verify(repository).getById(OFFER_ID);
        verify(repository).update(EXPIRED_OFFER);
    }

    @Test
    void return_exception_if_expired_offer_does_not_exist() {
        given(repository.getById(OFFER_ID)).willReturn(Optional.empty());

        assertThrows(
                OfferNotFoundException.class,
                () -> offerService.disable(OFFER)
        );
        verify(repository).getById(OFFER_ID);
    }
}
