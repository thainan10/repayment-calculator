package br.com.lendico.repaymentcalculator.service.util;

import br.com.lendico.repaymentcalculator.domain.RepaymentPlanInput;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;

@Service
public class RepaymentPlanUtil {

    private static final int DAYS_IN_MONTH = 30;

    private static final int DAYS_IN_YEAR = 360;

    private static final int QTD_MONTHS_TO_INCREASE = 1;

    private static final int PERIOD_IN_MONTHS = 12;

    private static Double fromPercentageToDecimal(Double percentage) {
        return percentage / 100;
    }

    private static Double roundValue(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        return Double.valueOf(decimalFormat.format(value));
    }

    private static Double calculateRatePerPeriod(Double percentRate) {
        Double decimalRate = fromPercentageToDecimal(percentRate);
        return decimalRate / PERIOD_IN_MONTHS;
    }

    public static ZonedDateTime increaseMonth(ZonedDateTime dateTime) {
        return dateTime.plusMonths(QTD_MONTHS_TO_INCREASE);
    }

    public static Double calculateAnnuity(RepaymentPlanInput repaymentPlanInput) {
        Double presentValue = repaymentPlanInput.getLoanAmount();
        Integer periods = repaymentPlanInput.getDuration();
        Double ratePerPeriod = calculateRatePerPeriod(repaymentPlanInput.getNominalRate());

        Double dividend = ratePerPeriod * presentValue;
        Double divisor = 1 - Math.pow(1 + ratePerPeriod, periods * -1);
        return roundValue(dividend / divisor);
    }

    public static Double calculateInterest(RepaymentPlanInput repaymentPlanInput, Double initialOutstandingPrincipal) {
        Double nominalRate = fromPercentageToDecimal(repaymentPlanInput.getNominalRate());

        Double interest = (nominalRate * DAYS_IN_MONTH * initialOutstandingPrincipal) / DAYS_IN_YEAR;
        return roundValue(interest);
    }

    public static Double calculatePrincipal(Double annuity, Double interest) {
        return roundValue(annuity - interest);
    }

    public static Double calculateRemainingOutstanding(Double initialOutstanding, Double principal) {
        return  roundValue(initialOutstanding - principal);
    }
}
