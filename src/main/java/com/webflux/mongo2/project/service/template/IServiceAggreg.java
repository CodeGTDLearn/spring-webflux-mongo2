package com.webflux.mongo2.project.service.template;


import com.webflux.mongo2.project.dto.ResultByStartDateAndCost;
import com.webflux.mongo2.project.dto.ResultCount;
import reactor.core.publisher.Flux;

public interface IServiceAggreg {
    Flux<ResultCount> findNoOfProjectsCostGreaterThan(Long cost);

    Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan
    (Long cost);
}