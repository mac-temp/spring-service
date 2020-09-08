package com.mac.offerservice.domain.offer;

import com.mac.offerservice.rest.OfferAlreadyExistsException;
import com.mac.offerservice.rest.OfferNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.fromString;

public class OfferService {

    private final OfferRepository repository;

    public OfferService(OfferRepository repository) {
        this.repository = repository;
    }

    public Offer createOffer(CreateOfferCommand createOfferCommand) throws OfferAlreadyExistsException {
        Offer offer = createOfferCommand.execute();
        if (repository.has(offer)) {
            throw new OfferAlreadyExistsException();
        }
        return repository.add(offer);
    }

    public Optional<Offer> getOfferById(UUID id) {
        return repository.getById(id);
    }

    public List<Offer> getOffers() {
        return repository.getAll();
    }

    public Offer updateOffer(UpdateOfferCommand command) {
        return repository.getById(fromString(command.getId()))
                .map(command::execute)
                .map(repository::update)
                .orElseThrow(OfferNotFoundException::new);
    }

    public Offer disable(Offer offer) {
        return repository.getById(fromString(offer.getId()))
                .map(Offer::disable)
                .map(repository::update)
                .orElseThrow(OfferNotFoundException::new);
    }
}
