package com.webflux.mongo2.config.routes.project;

public final class RoutesRepo {
  public static final String REPO_ROOT = "/project";
  public static final String REPO_BYNAME = REPO_ROOT + "/find/ByName";
  public static final String REPO_BYNAME_NOT = REPO_ROOT + "/find/ByNameNot";
  public static final String REPO_COST_GREATER = REPO_ROOT + "/find/ByEstimCostGreatThan";
  public static final String REPO_COST_BETW = REPO_ROOT + "/find/ByEstimatedCostBetween";
  public static final String REPO_BYNAME_LIKE = REPO_ROOT + "/find/ByNameLike";
  public static final String REPO_BYNAME_REGEX = REPO_ROOT + "/find/ByNameRegex";
  public static final String REPO_QUERY_BYNAME = REPO_ROOT + "/query/ByName";
  public static final String REPO_QUERY_BYNAME_COST = REPO_ROOT + "/query/ByNameAndCostQuery";
  public static final String REPO_QUERY_EST_COST_BET = REPO_ROOT + "/query/ByEstimatedCostBetween";
  public static final String REPO_QUERY_NYNAME_REGEX = REPO_ROOT + "/query/ByNameRegex";


  public static final String REPO_CRID_CHUNK_SAVE = REPO_ROOT + "/grid/chunkAndSave";
  public static final String REPO_CRID_DEL = REPO_ROOT + "/grid/delete";
}