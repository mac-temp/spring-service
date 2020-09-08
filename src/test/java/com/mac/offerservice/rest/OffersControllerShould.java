package com.mac.offerservice.rest;

import com.mac.offerservice.domain.offer.CreateOfferCommand;
import com.mac.offerservice.domain.offer.Offer;
import com.mac.offerservice.domain.offer.OfferService;
import com.mac.offerservice.domain.offer.UpdateOfferCommand;
import com.mac.offerservice.helpers.offer.OfferBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(OffersController.class)
class OffersControllerShould {
    private static final String API_OFFERS = "/api/offers";
    private static final String API_OFFER_BY_ID_URI_TEMPLATE = API_OFFERS + "/%s";
    private static final String API_DISABLE_URI_TEMPLATE = API_OFFERS + "/%s/disable";
    private static final String NAME = "2-4-1";
    private static final BigDecimal PRICE = BigDecimal.valueOf(4.99);
    private static final String DESCRIPTION = "2-4-1 description";
    private static final UUID UUID = java.util.UUID.randomUUID();
    private static final String ID = UUID.toString();
    private static final Offer OFFER = OfferBuilder.anOffer().build();
    private static final LocalDateTime NOW = now();
    private static final CreateOfferCommand CREATE_OFFER_DATA
            = new CreateOfferCommand(NAME, PRICE, DESCRIPTION, NOW.plusMinutes(1), NOW.plusHours(1), true);
    private static final UpdateOfferCommand UPDATE_OFFER_DATA
            = new UpdateOfferCommand(UUID.toString(), NAME, PRICE, DESCRIPTION, NOW.plusMinutes(1), NOW.plusHours(1));


    @Autowired
    MockMvc mvc;

    @MockBean
    OfferService offerService;

    @Autowired
    JacksonTester<CreateOfferCommand> jsonCreateOfferData;
    @Autowired
    JacksonTester<UpdateOfferCommand> jsonUpdateOfferData;
    @Autowired
    JacksonTester<Offer> jsonOffer;
    @Autowired
    JacksonTester<List<Offer>> jsonOfferList;


    @Test
    void create_a_new_offer() throws Exception {
        given(offerService.createOffer(CREATE_OFFER_DATA))
                .willReturn(OFFER);

        MockHttpServletResponse response = mvc.perform(
                post(API_OFFERS).contentType(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonCreateOfferData.write(CREATE_OFFER_DATA).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonOffer.write(OFFER).getJson()
        );

        verify(offerService).createOffer(CREATE_OFFER_DATA);
    }

    @Test
    void respond_with_conflict_if_already_exists() throws Exception {
        given(offerService.createOffer(CREATE_OFFER_DATA))
                .willThrow(OfferAlreadyExistsException.class);

        MockHttpServletResponse response = mvc.perform(
                post(API_OFFERS).contentType(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonCreateOfferData.write(CREATE_OFFER_DATA).getJson())
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        verify(offerService).createOffer(CREATE_OFFER_DATA);
    }

    @Test
    void get_existing_offer() throws Exception {
        given(offerService.getOfferById(UUID))
                .willReturn(Optional.of(OFFER));

        MockHttpServletResponse response = mvc.perform(
                get(String.format(API_OFFER_BY_ID_URI_TEMPLATE, ID)).accept(APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(jsonOffer.write(OFFER).getJson());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(offerService).getOfferById(UUID);
    }

    @Test
    void get_all_offers() throws Exception {
        List<Offer> offers = List.of(OFFER);
        given(offerService.getOffers()).willReturn(offers);

        MockHttpServletResponse response = mvc.perform(
                get(API_OFFERS).accept(APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(jsonOfferList.write(offers).getJson());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(offerService).getOffers();
    }

    @Test
    void respond_with_not_found_code_if_offer_doesnt_exist() throws Exception {
        given(offerService.getOfferById(UUID))
                .willReturn(Optional.empty());

        MockHttpServletResponse response = mvc.perform(
                get(String.format(API_OFFER_BY_ID_URI_TEMPLATE, ID)).accept(APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(offerService).getOfferById(UUID);
    }

    @Test
    void update_an_offer() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                put(String.format(API_OFFER_BY_ID_URI_TEMPLATE, UUID)).contentType(APPLICATION_JSON)
                        .content(jsonUpdateOfferData.write(UPDATE_OFFER_DATA).getJson())
                        .characterEncoding("UTF-8")
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(offerService).updateOffer(UPDATE_OFFER_DATA);
    }

    @Test
    void disable_an_offer() throws Exception {
        given(offerService.getOfferById(UUID)).willReturn(Optional.of(OFFER));
        given(offerService.disable(OFFER)).willReturn(OFFER);

        MockHttpServletResponse response = mvc.perform(
                put(String.format(API_DISABLE_URI_TEMPLATE, UUID)).contentType(APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(offerService).getOfferById(UUID);
        verify(offerService).disable(OFFER);
    }

    @Test
    void fail_to_disable_a_not_existing_offer() throws Exception {
        given(offerService.getOfferById(UUID)).willReturn(Optional.empty());

        MockHttpServletResponse response = mvc.perform(
                put(String.format(API_DISABLE_URI_TEMPLATE, UUID)).contentType(APPLICATION_JSON)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        verify(offerService).getOfferById(UUID);
        verify(offerService, never()).disable(OFFER);
    }
}
