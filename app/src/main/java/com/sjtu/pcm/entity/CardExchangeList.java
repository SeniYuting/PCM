package com.sjtu.pcm.entity;

import java.util.List;

/**
 * Created by Victor_Zhou on 2016-12-15.
 */

public class CardExchangeList {
    private List<CardExchangeEntity> Card_exchange;

    public List<CardExchangeEntity> getCard_exchange(){
        return Card_exchange;
    }

    public void setCard_exchange(List<CardExchangeEntity> Card_exchange){
        this.Card_exchange = Card_exchange;
    }
}
