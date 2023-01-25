package org.nicvaltel.playground.stm;

import org.multiverse.api.StmUtils;
import org.multiverse.api.references.TxnInteger;
import org.multiverse.api.references.TxnLong;

public class Account {
    private TxnLong lastUpdate;
    private TxnInteger balance;

    public Account(int balance){
        this.lastUpdate = StmUtils.newTxnLong(System.currentTimeMillis());
        this.balance = StmUtils.newTxnInteger(balance);
    }

    public void adjustBy(int amount){
        adjustBy(amount, System.currentTimeMillis());
    }

    public void adjustBy(int amount, long date){
        StmUtils.atomic(() -> {
                balance.increment(amount);
                lastUpdate.set(date);

                if( balance.get() < 0){
                    throw new IllegalArgumentException("Not enough money");
                }

        });
    }

    public Integer getBalance(){
        return balance.atomicGet();
    }

    public void transferTo(Account other, int amount){
        StmUtils.atomic(() -> {
            long date = System.currentTimeMillis();
            adjustBy(-amount,date);
            other.adjustBy(amount, date);
        });
    }


}
