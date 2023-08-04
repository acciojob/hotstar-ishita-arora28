package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import com.driver.transformer.SubscriptionTransformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
       
        Integer totalAmountPaid=0;
        
        Subscription subscription=SubscriptionTransformer.convertDtoToEntity(subscriptionEntryDto);

        if(subscription.getSubscriptionType().toString().equals("BASIC")){
            totalAmountPaid=500+200*subscription.getNoOfScreensSubscribed();

        }   
        else if(subscription.getSubscriptionType().toString().equals("PRO")){
            totalAmountPaid=800+250*subscription.getNoOfScreensSubscribed();
        } 
        else if(subscription.getSubscriptionType().toString().equals("ELITE")){
            totalAmountPaid=1000+350*subscription.getNoOfScreensSubscribed();
        } 
        subscription.setTotalAmountPaid(totalAmountPaid);
        subscription.setStartSubscriptionDate(new Date());
        subscriptionRepository.save(subscription);

        User user=userRepository.findById(subscriptionEntryDto.getUserId()).get();
        user.setSubscription(subscription);
        userRepository.save(user);


        return totalAmountPaid;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user=userRepository.findById(userId).get();
        Subscription userSubscription=user.getSubscription();
        if(userSubscription.getSubscriptionType().toString().equals("ELITE")){
            throw new Exception("Already the best Subscription");
        }
        Integer previousFair=userSubscription.getTotalAmountPaid();
        Integer currentFair=0;
        if(userSubscription.getSubscriptionType().toString().equals("BASIC")){
            userSubscription.setSubscriptionType(SubscriptionType.PRO);
            currentFair=previousFair+300+(50*userSubscription.getNoOfScreensSubscribed());
        
        }
        else{
            userSubscription.setSubscriptionType(SubscriptionType.ELITE);
            currentFair=previousFair+200+(100*userSubscription.getNoOfScreensSubscribed());
        }
        userSubscription.setTotalAmountPaid(currentFair);
        user.setSubscription(userSubscription);
        subscriptionRepository.save(userSubscription);

        return currentFair-previousFair;

    
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        Integer totalRevenue=0;
        List<Subscription> listOfSubscription=subscriptionRepository.findAll();
        
        for(Subscription subscription:listOfSubscription){
            totalRevenue+=subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
