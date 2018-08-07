package br.com.lendico.repaymentcalculator.service;

import br.com.lendico.repaymentcalculator.domain.RepaymentPlanInput;
import br.com.lendico.repaymentcalculator.domain.RepaymentPlanResult;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepaymentPlanService {

    private final int DAYS_PER_MONTH = 30;

    private final int DAYS_PER_YEAR = 360;

    private final int QTD_MONTHS_TO_INCREASE = 1;

    private final int PERIOD_IN_MONTHS = 12;

    private Double fromCentsToEuros(Double cents) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        return Double.valueOf(decimalFormat.format(cents / 100));
    }

    private ZonedDateTime increaseMonth(ZonedDateTime dateTime) {
        return dateTime.plusMonths(QTD_MONTHS_TO_INCREASE);
    }

    private Double roundValue(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        decimalFormat.setRoundingMode(RoundingMode.UP);
        return Double.valueOf(decimalFormat.format(value));
    }

    private Double calculateAnnuity(RepaymentPlanInput repaymentPlanInput) {
        Double presentValue = repaymentPlanInput.getLoanAmount();
        Integer periods = repaymentPlanInput.getDuration();
        Double ratePerPeriod = repaymentPlanInput.getNominalRate() / PERIOD_IN_MONTHS / 100;

        Double dividend = ratePerPeriod * presentValue;
        Double divisor = 1 - Math.pow(1 + ratePerPeriod, periods * -1);
        return roundValue(dividend / divisor);
    }

    private Double calculateInterest(RepaymentPlanInput repaymentPlanInput) {
        Double nominalRate = repaymentPlanInput.getNominalRate();
        Double loanAmount = repaymentPlanInput.getLoanAmount();

        Double interest = (nominalRate * DAYS_PER_MONTH * loanAmount) / DAYS_PER_YEAR;
        return fromCentsToEuros(interest);
    }

    private Double calculatePrincipal(Double annuity, Double interest) {
        return roundValue(annuity - interest);
    }

    private Double calculateRemainingOutstanding(Double initialOutstanding, Double principal) {
        return initialOutstanding - principal;
    }

    private RepaymentPlanResult buildRepaymentPlan(
            RepaymentPlanInput repaymentPlanInput,
            ZonedDateTime date,
            Double initialOutstanding) {
        Double annuity = calculateAnnuity(repaymentPlanInput);
        Double interest = calculateInterest(repaymentPlanInput);
        Double principal = calculatePrincipal(annuity, interest);

        Double remainingOutstanding = calculateRemainingOutstanding(initialOutstanding, principal);

        return new RepaymentPlanResult(date, annuity, principal, interest, initialOutstanding, remainingOutstanding);
    }

    public List<RepaymentPlanResult> createRepaymentPlan(RepaymentPlanInput repaymentPlanInput) {
        List<RepaymentPlanResult> result = new ArrayList<>();

        ZonedDateTime date = repaymentPlanInput.getStartDate();
        Double initialOutstanding = repaymentPlanInput.getLoanAmount();

        for (int k = 0; k <= repaymentPlanInput.getDuration(); k++) {
            RepaymentPlanResult repaymentPlanResult = buildRepaymentPlan(repaymentPlanInput, date, initialOutstanding);
            result.add(repaymentPlanResult);

            date = increaseMonth(date);
            initialOutstanding = repaymentPlanResult.getRemainingOutstandingPrincipal();
        }

        return result;
    }
}