package com.example.demo.repository;

import com.example.demo.model.Card;
import com.example.demo.service.CardManagerService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 *  this service provide us with last cards and current transferable amount
 *  of  that card, we should  trust this service with last transferable amount of each card.
 *  the method getAllCards of this service  only called when the system restart or when the
 *  restart constraint job execute at 00:00. if we don't have such service  we could be left
 *  behind unreliable situation because some transfer request have sent to the bank and we don't know
 *  what is their status(for example because of unwanted restart of service or because we have sent some
 *  request to the bank before the {@link CardManagerService#scheduleForResetConstraints()} execution and we receive the response
 *  after execution of that job
 */
@Service
public class CardManagerRepo {


    public List<Card> getAllCards(){
        return  Arrays.asList(new Card(1L,1000000L),
                              new Card(2L,2000000L),
                              new Card(3L,3000000L));

    }
}
