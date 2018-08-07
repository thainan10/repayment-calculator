package br.com.lendico.repaymentcalculator.service;

import br.com.lendico.repaymentcalculator.domain.RepaymentPlanInput;
import br.com.lendico.repaymentcalculator.domain.RepaymentPlanResult;
import br.com.lendico.repaymentcalculator.service.util.RepaymentPlanUtil;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepaymentPlanService {

    private RepaymentPlanResult buildRepaymentPlan(
            RepaymentPlanInput repaymentPlanInput,
            ZonedDateTime date,
            Double initialOutstanding) {
        Double annuity = RepaymentPlanUtil.calculateAnnuity(repaymentPlanInput);
        Double interest = RepaymentPlanUtil.calculateInterest(repaymentPlanInput, initialOutstanding);
        Double principal = RepaymentPlanUtil.calculatePrincipal(annuity, interest);

        Double remainingOutstanding = RepaymentPlanUtil.calculateRemainingOutstanding(initialOutstanding, principal);

        return new RepaymentPlanResult(date, annuity, principal, interest, initialOutstanding, remainingOutstanding);
    }

    public List<RepaymentPlanResult> createRepaymentPlan(RepaymentPlanInput repaymentPlanInput) {
        List<RepaymentPlanResult> result = new ArrayList<>();

        ZonedDateTime date = repaymentPlanInput.getStartDate();
        Double initialOutstanding = repaymentPlanInput.getLoanAmount();

        for (int k = 0; k <= repaymentPlanInput.getDuration(); k++) {
            RepaymentPlanResult repaymentPlanResult = buildRepaymentPlan(repaymentPlanInput, date, initialOutstanding);
            result.add(repaymentPlanResult);

            date = RepaymentPlanUtil.increaseMonth(date);
            initialOutstanding = repaymentPlanResult.getRemainingOutstandingPrincipal();
        }

        return result;
    }
}