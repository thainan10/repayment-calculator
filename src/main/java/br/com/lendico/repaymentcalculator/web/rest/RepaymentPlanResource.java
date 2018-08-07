package br.com.lendico.repaymentcalculator.web.rest;

import br.com.lendico.repaymentcalculator.domain.RepaymentPlanInput;
import br.com.lendico.repaymentcalculator.domain.RepaymentPlanResult;
import br.com.lendico.repaymentcalculator.service.RepaymentPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RepaymentPlanResource {

    private final RepaymentPlanService repaymentPlanService;

    public RepaymentPlanResource(RepaymentPlanService repaymentPlanService) {
        this.repaymentPlanService = repaymentPlanService;
    }

    @PostMapping("/generate-plan")
    public ResponseEntity<List<RepaymentPlanResult>> generateRepaymentPlan(
            @RequestBody RepaymentPlanInput repaymentPlanInput) {
        return ResponseEntity.ok(repaymentPlanService.createRepaymentPlan(repaymentPlanInput));
    }
}