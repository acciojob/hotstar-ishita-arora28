package com.driver.transformer;

import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;

public class SubscriptionTransformer {
    public static Subscription convertDtoToEntity(SubscriptionEntryDto subscriptionEntryDto){
        Subscription subscription=new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        return subscription;
    }
}
