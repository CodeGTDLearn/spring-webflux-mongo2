package com.webflux.api.modules.project.service.template;


import com.webflux.api.modules.project.dto.ResultByStartDateAndCost;
import com.webflux.api.modules.project.dto.ResultCount;
import reactor.core.publisher.Flux;

public interface IServiceAggreg {
    Flux<ResultCount> findNoOfProjectsCostGreaterThan(Long cost);

    Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan
    (Long cost);
}