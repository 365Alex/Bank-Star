package com.bank.star.star.controller;

import com.bank.star.star.DTO.RuleStatsResponse;
import com.bank.star.star.entity.RuleStatistic;
import com.bank.star.star.repository.RuleStatisticRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class RuleStatsController {

    private final RuleStatisticRepository ruleStatisticRepository;

    public RuleStatsController(RuleStatisticRepository ruleStatisticRepository) {
        this.ruleStatisticRepository = ruleStatisticRepository;
    }

    @GetMapping("/stats")
    public RuleStatsResponse getRuleStats() {
        List<RuleStatistic> allStats = ruleStatisticRepository.findAll();

        List<RuleStatsResponse.RuleStat> stats = allStats.stream()
                .map(stat -> new RuleStatsResponse.RuleStat(
                        stat.getRuleId(),
                        stat.getExecutionCount()
                ))
                .collect(Collectors.toList());

        return new RuleStatsResponse(stats);
    }
}