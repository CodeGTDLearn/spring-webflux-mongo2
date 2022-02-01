package com.webflux.api.modules.project.core.routes;

public final class RoutesRepo {
  public static final String REPO_ROOT = "/project";
  public static final String REPO_BYNAME_NOT = "/find/ByNameNot";
  public static final String REPO_COST_GREATER = "/find/ByEstimCostGreatThan";
  public static final String REPO_COST_BETW = "/find/ByEstimatedCostBetween";
  public static final String REPO_BYNAME_LIKE = "/find/ByNameLike";
  public static final String REPO_BYNAME_REGEX = "/find/ByNameRegex";
  public static final String REPO_QUERY_BYNAME = "/query/ByName";
  public static final String REPO_QUERY_BYNAME_COST = "/query/ByNameAndCostQuery";
  public static final String REPO_QUERY_EST_COST_BET = "/query/ByEstimatedCostBetween";
  public static final String REPO_QUERY_NYNAME_REGEX = "/query/ByNameRegex";


  public static final String REPO_CRID_CHUNK_SAVE = "/grid/chunkAndSave";
  public static final String REPO_CRID_DEL = "/grid/delete";
}