package com.webflux.mongo2.config;

public final class Routes {
  public static final String PROJ_ROOT = "/project";
  public static final String PROJ_CREATE = PROJ_ROOT + "/create";
  public static final String TASK_CREATE = PROJ_ROOT + "/createTask";
  public static final String PROJ_ID = PROJ_ROOT + "/{id}";
  public static final String PROJ_BYNAME = PROJ_ROOT + "/find/ByName";
  public static final String PROJ_BYNAME_NOT = PROJ_ROOT + "/find/ByNameNot";
  public static final String PROJ_COST_GREATER_THAN = PROJ_ROOT + "/find/ByEstimCostGreatThan";
  public static final String PROJ_COST_BETW = PROJ_ROOT + "/find/ByEstimatedCostBetween";
  public static final String PROJ_BYNAME_LIKE = PROJ_ROOT + "/find/ByNameLike";
  public static final String PROJ_BYNAME_REGEX = PROJ_ROOT + "/find/ByNameRegex";
  public static final String PROJ_QUERY_BYNAME = PROJ_ROOT + "/query/ByName";
  public static final String PROJ_QUERY_BYNAME_COST = PROJ_ROOT + "/query/ByNameAndCostQuery";
  public static final String PROJ_QUERY_EST_COST_BET = PROJ_ROOT + "/query/ByEstimatedCostBetween";
  public static final String PROJ_QUERY_NYNAME_REGEX = PROJ_ROOT + "/query/ByNameRegex";
  public static final String PROJ_TEMPL_BYNAME_QUERY = PROJ_ROOT + "/templ/ByNameQuery";
  public static final String PROJ_TEMPL_EST_COST_BET_QUERY = PROJ_ROOT + "/templ/ByEstCostBetwQuer";
  public static final String PROJ_TEMPL_BYNAME_REG_QUERY = PROJ_ROOT + "/templ/ByNameRegexQuery";
  public static final String PROJ_TEMPL_UPCOST_CRIT = PROJ_ROOT + "/templ/upsertCostWithCriteria";
  public static final String PROJ_TEMPL_DEL_CRIT = PROJ_ROOT + "/templ/deleteWithCriteria";
  public static final String PROJ_CRID_CHUNK_SAVE = PROJ_ROOT + "/grid/chunkAndSave";
  public static final String PROJ_CRID_DEL = PROJ_ROOT + "/grid/delete";
}