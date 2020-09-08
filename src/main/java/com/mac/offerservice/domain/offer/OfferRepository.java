package com.mac.offerservice.domain.offer;

import java.util.*;

public class OfferRepository {
    private final Map<String, Offer> offers = new HashMap<>();

    public Offer add(Offer offer) {
        offers.put(offer.getId(), offer);
        return offer;
    }

    public boolean has(Offer offer) {
        return offers.containsKey(offer.getId());
    }

    public List<Offer> getAll() {
        return new ArrayList<>(offers.values());
    }

    public Optional<Offer> getById(UUID uuid) {
        return Optional.ofNullable(offers.get(uuid.toString()));
    }

    public Offer update(Offer offer) {
        offers.replace(offer.getId(), offer);
        return offer;
    }
}
