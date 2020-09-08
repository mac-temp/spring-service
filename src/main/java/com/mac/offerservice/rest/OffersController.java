package com.mac.offerservice.rest;

import com.mac.offerservice.domain.offer.CreateOfferCommand;
import com.mac.offerservice.domain.offer.Offer;
import com.mac.offerservice.domain.offer.OfferService;
import com.mac.offerservice.domain.offer.UpdateOfferCommand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/offers")
public class OffersController {

    private final OfferService offerService;

    public OffersController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Offer createOffer(@Valid @RequestBody CreateOfferCommand command) {
        return offerService.createOffer(command);
    }

    @GetMapping("/{id}")
    public Offer getOfferById(@PathVariable UUID id) {
        return offerService.getOfferById(id).orElseThrow(OfferNotFoundException::new);
    }

    @PutMapping("/{id}")
    public Offer updateOffer(@Valid @RequestBody UpdateOfferCommand command) {
        return offerService.updateOffer(command);
    }

    @PutMapping("/{id}/disable")
    public Offer expireOffer(@PathVariable UUID id) {
        return offerService.getOfferById(id)
                .map(offerService::disable)
                .orElseThrow(OfferNotFoundException::new);
    }

    @GetMapping
    public List<Offer> getAllOffers() {
        return offerService.getOffers();
    }
}
