package com.example.demo;

import com.example.demo.model.Card;
import com.example.demo.model.TransferTransaction;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * It's the main component that provide us with managing transferable amount of each card
 * we use priority queue here because we want to minimize the number of card
 * for each transfer. we want to use greedy algorithm so the best choice for us here are the cards
 * with maximum transferable amount. this class has a method {@code getTransferTransactions(totalAmount)} witch provide us with
 * the ordered list of card witch are the best choice for fulfilling the total transaction.
 * this class also has another method {@code addAmountToCard} which allow us to add a specific amount
 * to transferable amount of specific card(for transaction that were failed, for example)
 */
@Service
public class CardManager {


    /**
     *  A priority Queue that is used to store cardTemp object
     *  prioritize by their temp object because we want to use greedy algorithm here
     *  every time we want to have the card with the minimum amount of temp(which is the minimum amount
     *  that has already transferred by this card up to now in current day).
     */
    private PriorityQueue<Card> cardHeap;


    /**
     * sum transferable amount for all cards in the cardHeap for short circuiting in situation that
     * sum amount of all card is less than the totalAmount
     */
    private long sumTransferableAmount;


    /**
     * because we can't access cardTemp objects just by their cardId from cardHeap
     * I saved all cardTemp object in a HashMap so that I don't have to
     * regenerate these objects.
     */
    private Map<Long, Card> maps = new HashMap<>();


    public synchronized void initialize(List<Card> cards){
        sumTransferableAmount = 0;
        cardHeap = new PriorityQueue<>(10, new CardComparator());
        cardHeap = new PriorityQueue<>(cards.size(), new CardComparator());
        cards.forEach(card -> {
            cardHeap.add(card);
            maps.put(card.getCardId(), card);
            sumTransferableAmount += card.getTransferableAmount();
        });


    }

    public  List<TransferTransaction> getTransferTransactions(long totalAmount){
        Card card;
        Long sum = 0L;
        TransferTransaction transaction;
        List<TransferTransaction> transferTransactions = new ArrayList<>(10);
        if(sumTransferableAmount < totalAmount)
            return transferTransactions;
        synchronized (this){
            do{
                if(cardHeap.isEmpty())
                    break;
                card = cardHeap.remove();

                if(totalAmount - sum <= card.getTransferableAmount()){
                    card.setTransferableAmount(card.getTransferableAmount() - (totalAmount-sum));
                    transaction = new TransferTransaction(card,
                            totalAmount - sum , "pending" );
                    sum = totalAmount;

                    if(card.getTransferableAmount() >= 1000)
                        cardHeap.add(card);
                }

                else {
                    sum = card.getTransferableAmount() + sum;
                    transaction = new TransferTransaction(card,
                            card.getTransferableAmount() , "pending" );
                    card.setTransferableAmount(0L);
                }
                transferTransactions.add(transaction);
                sumTransferableAmount -= transaction.getAmount();

            }while(sum != totalAmount);
            }
        return transferTransactions;
    }

    public synchronized void  addAmountToCard(long cardId, long amount){
        Card card = maps.get(cardId);
        cardHeap.remove(card);
        card.setTransferableAmount(card.getTransferableAmount() + amount);
        sumTransferableAmount += amount;
        cardHeap.add(card);
    }

    static class CardComparator implements Comparator<Card> {
        @Override
        public int compare(Card o1, Card o2) {

            if(o1.getTransferableAmount() > o2.getTransferableAmount())
                return -1;
            else if(o1.getTransferableAmount() < o2.getTransferableAmount())
                return 1;
            else
                return 0;
        }
    };
}
