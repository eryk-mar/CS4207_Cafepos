package com.cafepos.payment;
import com.cafepos.domain.Order;
public final class WalletPayment implements PaymentStrategy {
    private final String walletId;
   public WalletPayment(String walletId) {

       if(walletId.isEmpty()){
           throw new IllegalArgumentException("Please provide a valid (non empty) wallet ID");
       }else {
           this.walletId = walletId;
       }

   }
    @Override
    public void pay(Order order) {
        System.out.println("[Wallet] Customer paid " +
                order.totalWithTax(10) + " EUR via wallet " + walletId);
    }
}