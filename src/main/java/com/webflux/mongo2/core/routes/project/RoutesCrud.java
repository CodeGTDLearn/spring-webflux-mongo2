package com.webflux.mongo2.core.routes.project;

public final class RoutesCrud {
  public static final String PROJ_ROOT = "/project";
  public static final String CRUD_CREATE = PROJ_ROOT + "/create";
  public static final String CRUD_UPD = PROJ_ROOT + "/update";
  public static final String CRUD_ID = PROJ_ROOT + "/{id}";
  public static final String CRUD_BYNAME = PROJ_ROOT + "/find/ByName";
}