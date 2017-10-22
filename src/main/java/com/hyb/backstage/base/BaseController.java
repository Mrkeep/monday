package com.hyb.backstage.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    //test commit git cai
    protected final static String ERROR_MESSAGE_KEY = "errorMessage";
    protected final static String SUCCESS_MESSAGE_KEY = "successMessage";
    protected final static String FORWARD_URL_KEY = "forwardUrl";
    protected final static String BINDING_RESULT_KEY = "bindingResult";
    protected final static String PAGE_KEY = "page";
    protected final static String USER_MAP_KEY = "user_map";
    protected final static String CURRENT_TIMESTAMP = "currentTimestamp";

    protected final static String COMMAND = "command";
    protected final static String SPEC = "spec";

    protected final static String LIST = "/list";
    protected final static String CREATE = "/create";
    protected final static String READ = "/read";
    protected final static String UPDATE = "/update";
    protected final static String DELETE = "/delete";
    protected final static String READ_ID = "/read/{id}";
    protected final static String UPDATE_ID = "/update/{id}";
    protected final static String DELETE_ID = "/delete/{id}";
    protected final static String DOWN_ID = "/down/{id}";
}
