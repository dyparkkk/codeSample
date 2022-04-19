package com.example.objects.ticket;

/**
 * 판매원을 초대장을 티켓으로 교환해주거나 티켓을 판매하는 역할을 수행
 */
public class TicketSeller {
    private TicketOffice ticketOffice;

    public TicketSeller(TicketOffice ticketOffice) {
        this.ticketOffice = ticketOffice;
    }

    public void sellTo(Audience audience) {
        ticketOffice.sellTicketTo(audience);
    }
}
