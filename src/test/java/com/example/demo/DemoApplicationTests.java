package com.example.demo;

import com.example.demo.model.Card;
import com.example.demo.model.TransferTransaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableJpaRepositories("com.example.demo.repository.transaction")
public class DemoApplicationTests {


    private List<Card> getCardList1(){
        return Arrays.asList(new Card(1L,500000L),
                new Card(2L,1000000L),
                new Card(3L,1000000L),
                new Card(4L,2000000L),
                new Card(5L,500000L));


    }

    private List<Card> getCardList2(){
        return Arrays.asList(new Card(1L,1000000L),
                new Card(2L,2000000L),
                new Card(3L,3000000L));
    }

    @Test
    public void when_cardList1_amountIs_2500000(){
        CardManager cardManager = new CardManager();
        cardManager.initialize(getCardList1());
        List<TransferTransaction> transactions2 = cardManager.getTransferTransactions(2500000L);
        assertThat(transactions2.size(), is(2));
        assertThat(transactions2.get(0).getCard().getCardId(), is(4L));
        assertThat(transactions2.get(1).getCard().getCardId(), is(2L));

    }

    @Test
    public void when_cardList1_amountIs_1m_Then2m(){
        CardManager cardManager = new CardManager();
        cardManager.initialize(getCardList2());
        List<TransferTransaction> transactions2 = cardManager.getTransferTransactions(2000000L);
        assertThat(transactions2.size(), is(1));
        assertThat(transactions2.get(0).getCard().getCardId(), is(3L));

    }

    @Test
    public void when_cardList1_amountIs_6m_Then1m(){

        CardManager cardManager = new CardManager();
        cardManager.initialize(getCardList2());
        List<TransferTransaction> transactions = cardManager.getTransferTransactions(6000000L);
        assertThat(transactions.size(), is(3));
        assertThat(transactions.get(0).getCard().getCardId(), is(3L));
        assertThat(transactions.get(1).getCard().getCardId(), is(2L));
        assertThat(transactions.get(2).getCard().getCardId(), is(1L));

        transactions = cardManager.getTransferTransactions(1000000L);
        assertThat(transactions.size(), is(0));
    }

    @Test
    public void when_cardList1_amountIs_4m_And_Then_2m(){

        CardManager cardManager = new CardManager();
        cardManager.initialize(getCardList2());
        List<TransferTransaction> transactions = cardManager.getTransferTransactions(4000000L);
        assertThat(transactions.size(), is(2));
        assertThat(transactions.get(0).getCard().getCardId(), is(3L));
        assertThat(transactions.get(1).getCard().getCardId(), is(2L));


        transactions = cardManager.getTransferTransactions(2000000L);
        assertThat(transactions.size(), is(2));
        assertThat(transactions.get(0).getCard().getCardId(), is(1L));
        assertThat(transactions.get(1).getCard().getCardId(), is(2L));
    }

    @Test
    public void when_cardList1_amountIs_2m_And_Then_2m(){

        CardManager cardManager = new CardManager();
        cardManager.initialize(getCardList2());
        List<TransferTransaction> transactions = cardManager.getTransferTransactions(2000000L);
        assertThat(transactions.size(), is(1));
        assertThat(transactions.get(0).getCard().getCardId(), is(3L));


        transactions = cardManager.getTransferTransactions(2000000L);
        assertThat(transactions.size(), is(1));
        assertThat(transactions.get(0).getCard().getCardId(), is(2L));

    }

    @Test
    public void when_cardList1_amountIs_3m_And_Then_5m(){

        CardManager cardManager = new CardManager();
        cardManager.initialize(getCardList2());
        List<TransferTransaction> transactions = cardManager.getTransferTransactions(3000000L);
        assertThat(transactions.size(), is(1));
        assertThat(transactions.get(0).getCard().getCardId(), is(3L));


        transactions = cardManager.getTransferTransactions(5000000L);
        assertThat(transactions.size(), is(0));

    }


}

