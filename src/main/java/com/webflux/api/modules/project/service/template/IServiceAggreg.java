package com.webflux.api.modules.project.service.template;


import com.webflux.api.modules.project.core.dto.ResultByStartDateAndCost;
import com.webflux.api.modules.project.core.dto.ResultCount;
import reactor.core.publisher.Flux;

public interface IServiceAggreg {
    Flux<ResultCount> findNoOfProjectsCostGreaterThan(Long projectCost);

    Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan
    (Long cost);
}