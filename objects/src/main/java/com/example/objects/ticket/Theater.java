package com.example.objects.ticket;

/**
 * 관람객을 맞이함
 */
public class Theater {
    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience) {
       ticketSeller.sellTo(audience);
    }
}
