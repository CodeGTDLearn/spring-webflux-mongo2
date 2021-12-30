package com.webflux.mongo2.project.service.template;


import com.webflux.mongo2.project.dto.ResultByStartDateAndCost;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceTemplAggreg {
    public Mono<Long> findNoOfProjectsCostGreaterThan(Long cost);

    public Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan
    (Long cost);
}