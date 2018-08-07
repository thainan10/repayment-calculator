package br.com.lendico.repaymentcalculator.service;

import br.com.lendico.repaymentcalculator.domain.RepaymentPlanInput;
import br.com.lendico.repaymentcalculator.domain.RepaymentPlanResult;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class RepaymentPlanService {

    private final int DAYS_PER_MONTH = 30;

    private final int DAYS_PER_YEAR = 360;

    private Double fromCentsToEuros(Double cents) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        return Double.valueOf(decimalFormat.format(cents / 100));
    }

    private Double calculateAnnuity(RepaymentPlanInput repaymentPlanInput) {
        Integer periods = repaymentPlanInput.getDuration();
        Double payment = repaymentPlanInput.getLoanAmount();
        Double ratePerPeriod = repaymentPlanInput.getNominalRate() / periods;

        Double auxSquare = Math.pow(1 + ratePerPeriod, periods * -1);
        return (payment * (1 - auxSquare)) / ratePerPeriod;
    }

    private Double calculateInterest(RepaymentPlanInput repaymentPlanInput) {
        Double nominalRate = repaymentPlanInput.getNominalRate();
        Double loanAmount = repaymentPlanInput.getLoanAmount();

        Double interest = (nominalRate * DAYS_PER_MONTH * loanAmount) / DAYS_PER_YEAR;
        return fromCentsToEuros(interest);
    }

    private Double calculatePrincipal(Double annuity, Double interest) {
        return annuity - interest;
    }

    private Double calculateRemainingOutstanding(Double initialOutstanding, Double principal) {
        return initialOutstanding - principal;
    }

    private Date addMonthToDate(Date currentDate, Integer mouths) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, mouths);
        return calendar.getTime();
    }

    private RepaymentPlanResult buildRepaymentPlan(
            RepaymentPlanInput repaymentPlanInput,
            Date date,
            Double initialOutstanding) {
        Double annuity = calculateAnnuity(repaymentPlanInput);
        Double interest = calculateInterest(repaymentPlanInput);
        Double principal = calculatePrincipal(annuity, interest);

        Double remainingOutstanding = calculateRemainingOutstanding(initialOutstanding, principal);

        return new RepaymentPlanResult(date, annuity, principal, interest, initialOutstanding, remainingOutstanding);
    }

    public List<RepaymentPlanResult> createRepaymentPlan(RepaymentPlanInput repaymentPlanInput) {
        List<RepaymentPlanResult> result = new ArrayList<>();

        Date date = repaymentPlanInput.getStartDate();
        Double initialOutstanding = repaymentPlanInput.getLoanAmount();

        for (int k = 0; k < repaymentPlanInput.getDuration(); k++) {
            date = addMonthToDate(date, k);

            RepaymentPlanResult repaymentPlanResult = buildRepaymentPlan(repaymentPlanInput, date, initialOutstanding);
            result.add(repaymentPlanResult);

            initialOutstanding = repaymentPlanResult.getRemainingOutstandingPrincipal();
        }

        return result;
    }
}