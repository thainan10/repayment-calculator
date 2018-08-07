package br.com.lendico.repaymentcalculator.domain;

import java.util.Date;

public class RepaymentPlanInput {

    private Integer duration;

    private Double nominalRate;

    private Double loanAmount;

    private Date startDate;

    public RepaymentPlanInput() {
    }

    public RepaymentPlanInput(Integer duration, Double nominalRate, Double loanAmount, Date startDate) {
        this.duration = duration;
        this.nominalRate = nominalRate;
        this.loanAmount = loanAmount;
        this.startDate = startDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getNominalRate() {
        return nominalRate;
    }

    public void setNominalRate(Double nominalRate) {
        this.nominalRate = nominalRate;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
