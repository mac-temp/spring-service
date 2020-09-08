package com.mac.offerservice.integration;

import com.mac.offerservice.domain.offer.CreateOfferCommand;
import com.mac.offerservice.domain.offer.Offer;
import com.mac.offerservice.domain.offer.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.mac.offerservice.helpers.offer.OfferBuilder.anOffer;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OffersControllerTest {

    private static final Offer OFFER = anOffer().build();
    private static final Offer EXPIRED_OFFER = anOffer()
            .withStartDate(now().minusHours(2))
            .withEndDate(now().minusHours(1))
            .build();
    private static final String EXPECTED_NAME = "new-offer";
    private static final String EXPECTED_OFFER_DESCRIPTION = "great new offer";
    private static final BigDecimal EXPECTED_PRICE = BigDecimal.valueOf(3.99);
    private static final LocalDateTime EXPECTED_START_DATE = now().plusMinutes(5);
    private static final LocalDateTime EXPECTED_END_DATE = now().plusHours(1);
    private static final UUID RANDOM_UUID = UUID.randomUUID();
    private static final String API_OFFERS = "/api/offers/";
    private static final String API_DISABLE_URL_TEMPLATE = API_OFFERS + "%s/disable";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OfferRepository repository;


    @BeforeEach
    void setUp() {
        repository.add(EXPIRED_OFFER);
        repository.add(OFFER);
    }

    @Test
    public void can_create_an_offer() {

        ResponseEntity<Offer> response = restTemplate.postForEntity(API_OFFERS,
                new CreateOfferCommand(EXPECTED_NAME,
                        EXPECTED_PRICE,
                        EXPECTED_OFFER_DESCRIPTION,
                        EXPECTED_START_DATE,
                        EXPECTED_END_DATE,
                        true),
                Offer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        Offer actual = response.getBody();
        assertThat(actual.getName()).isEqualTo(EXPECTED_NAME);
        assertThat(actual.getPrice()).isEqualTo(EXPECTED_PRICE);
        assertThat(actual.getDescription()).isEqualTo(EXPECTED_OFFER_DESCRIPTION);
        assertThat(actual.getStartDate()).isEqualTo(EXPECTED_START_DATE);
        assertThat(actual.getEndDate()).isEqualTo(EXPECTED_END_DATE);
        assertThat(actual.isExpired()).isEqualTo(false);
    }

    @Test
    public void can_retrieve_by_id_when_exists() {
        ResponseEntity<Offer> response = restTemplate.getForEntity("/api/offers/" + OFFER.getId(), Offer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(OFFER);
    }

    @Test
    public void retrieved_offer_from_the_past_is_expired() {

        ResponseEntity<Offer> response = restTemplate
                .getForEntity("/api/offers/" + EXPIRED_OFFER.getId(), Offer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(EXPIRED_OFFER);
        assertThat(response.getBody().isExpired()).isEqualTo(true);
    }

    @Test
    public void fails_to_create_offer_with_start_date_in_past() {

        ResponseEntity<Offer> response = restTemplate.postForEntity(API_OFFERS,
                new CreateOfferCommand(EXPECTED_NAME,
                        EXPECTED_PRICE,
                        EXPECTED_OFFER_DESCRIPTION,
                        now().minusDays(1),
                        EXPECTED_END_DATE,
                        true),
                Offer.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void fails_to_create_offer_with_start_date_after_end_date() {

        ResponseEntity<Offer> response = restTemplate.postForEntity(API_OFFERS,
                new CreateOfferCommand(EXPECTED_NAME,
                        EXPECTED_PRICE,
                        EXPECTED_OFFER_DESCRIPTION,
                        now().plusDays(3),
                        now().plusDays(2),
                        true),
                Offer.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void disables_an_offer() {
        repository.add(OFFER);

        restTemplate.put(String.format(API_DISABLE_URL_TEMPLATE, OFFER.getId()), null);

        assertThat(repository.has(OFFER)).isTrue();
        Optional<Offer> byId = repository.getById(UUID.fromString(OFFER.getId()));
        assertThat(byId).isPresent();
        assertThat(byId.get().isEnabled()).isFalse();
    }

    @Test
    public void fails_to_disable_an_offer_that_does_not_exist() {
        ResponseEntity<Offer> response = restTemplate.exchange(String.format(API_DISABLE_URL_TEMPLATE, RANDOM_UUID),
                PUT,
                null,
                Offer.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }
}
