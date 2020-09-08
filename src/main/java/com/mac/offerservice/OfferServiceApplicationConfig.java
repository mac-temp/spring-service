package com.mac.offerservice;

import com.mac.offerservice.domain.offer.OfferRepository;
import com.mac.offerservice.domain.offer.OfferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfferServiceApplicationConfig {

    @Bean
    public OfferService offerService(OfferRepository repository) {
        return new OfferService(repository);
    }

    @Bean
    public OfferRepository offerRepository() {
        return new OfferRepository();
    }
}
