package br.com.lendico.repaymentcalculator.domain;

import java.time.ZonedDateTime;

public class RepaymentPlanResult {

    private ZonedDateTime date;

    private Double borrowerPaymentAmount;

    private Double principal;

    private Double interest;

    private Double initialOutstandingPrincipal;

    private Double remainingOutstandingPrincipal;

    public RepaymentPlanResult() {
    }

    public RepaymentPlanResult(
            ZonedDateTime date,
            Double borrowerPaymentAmount,
            Double principal,
            Double interest,
            Double initialOutstandingPrincipal,
            Double remainingOutstandingPrincipal) {
        this.date = date;
        this.borrowerPaymentAmount = borrowerPaymentAmount;
        this.principal = principal;
        this.interest = interest;
        this.initialOutstandingPrincipal = initialOutstandingPrincipal;
        this.remainingOutstandingPrincipal = remainingOutstandingPrincipal;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Double getBorrowerPaymentAmount() {
        return borrowerPaymentAmount;
    }

    public void setBorrowerPaymentAmount(Double borrowerPaymentAmount) {
        this.borrowerPaymentAmount = borrowerPaymentAmount;
    }

    public Double getPrincipal() {
        return principal;
    }

    public void setPrincipal(Double principal) {
        this.principal = principal;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Double getInitialOutstandingPrincipal() {
        return initialOutstandingPrincipal;
    }

    public void setInitialOutstandingPrincipal(Double initialOutstandingPrincipal) {
        this.initialOutstandingPrincipal = initialOutstandingPrincipal;
    }

    public Double getRemainingOutstandingPrincipal() {
        return remainingOutstandingPrincipal;
    }

    public void setRemainingOutstandingPrincipal(Double remainingOutstandingPrincipal) {
        this.remainingOutstandingPrincipal = remainingOutstandingPrincipal;
    }
}
