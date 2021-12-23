package com.webflux.mongo2.config.routes.project;

public final class RoutesTempl {
  public static final String TEMPL_ROOT = "/project";
  public static final String TEMPL_BYNAME = TEMPL_ROOT + "/templ/ByNameQuery";
  public static final String TEMPL_EST_COST_BET = TEMPL_ROOT + "/templ/ByEstCostBetwQuer";
  public static final String TEMPL_BYNAME_REG = TEMPL_ROOT + "/templ/ByNameRegexQuery";
  public static final String TEMPL_UPSERT_CRIT = TEMPL_ROOT + "/templ/upsertCostWithCriteria";
  public static final String TEMPL_UPD_ARRAY_CRIT = TEMPL_ROOT + "/templ/criteriaArray";
  public static final String TEMPL_UPD_CHILD_CRIT = TEMPL_ROOT + "/templ/criteriaChild";
  public static final String TEMPL_DEL_CHILD_CRIT = TEMPL_ROOT + "/templ/delcriteriaChild";
  public static final String TEMPL_DEL_ARRAY_CRIT = TEMPL_ROOT + "/templ/delcriteriaArray";
  public static final String TEMPL_DEL_CRIT = TEMPL_ROOT + "/templ/deleteWithCriteria";
  public static final String TEMPL_DEL_CRIT_MULT = TEMPL_ROOT + "/templ/deleteWithCriteriaMult";
}