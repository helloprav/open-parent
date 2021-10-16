package org.openframework.commons.domain.exceptions;

/**
 * Created by francesco on 2/11/14.
 */
public class ErrorCode {
	
    public static final String SUCCESS = "SUCCESS";

    public static final String LOGIN_ONE_TIME = "LOGIN_ONE_TIME";
    public static final String ACCESS_DENIED = "ACCESS_DENIED";

    public static final String SERVICE_NOT_FOUND = "SRV_001";

    public static final String ENTITY_NOT_FOUND = "ENTITY_NOT_FOUND";

    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    public static final String GENERIC_ERROR = "GEN_001";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String AUTHENTICATION_ERROR = "AUTHENTICATION_ERROR";

    public static final String PROFILE_NOT_FOUND = "PRO_001";
    public static final String PROFILE_ERROR = "PRO_002";
    public static final String PROFILE_EMAIL_NOT_FOUND = "PROFILE_EMAIL_NOT_FOUND";
    public static final String PROFILE_EMAIL_REQUIRED = "PROFILE_EMAIL_REQUIRED";
    public static final String PROFILE_VALIDATION_ERROR = "PROFILE_VALIDATION_ERROR";
    public static final String PROFILE_DISTRIBUTION_LIST_NOT_EMPTY = "PROFILE_DISTRIBUTION_LIST_NOT_EMPTY";

    public static final String ORGANIZATION_ERROR = "ORGANIZATION_ERROR";

    public static final String GROUP_NOT_FOUND = "GROUP_NOT_FOUND";

    public static final String CATALOG_ERROR = "CAT_001";
    public static final String CATALOG_PRODUCT_NOT_FOUND = "CATALOG_PRODUCT_NOT_FOUND";
    public static final String CATALOG_ECOSYSTEM_NOT_FOUND = "CATALOG_ECOSYSTEM_NOT_FOUND";

    public static final String CATALOG_MANAGEMENT_ERROR = "CAT_MGMT_001";
    public static final String CATALOG_MANAGEMENT_NOT_EMPTY_CATALOG = "CATALOG_MANAGEMENT_NOT_EMPTY_CATALOG";
    public static final String CATALOG_MANAGEMENT_CURRENCY_NOT_EDITABLE = "CATALOG_MANAGEMENT_CURRENCY_NOT_EDITABLE";
    public static final String PRODUCT_MANAGEMENT_CLONE_PRICE_WITH_NO_THRESHOLD = "PRODUCT_MANAGEMENT_CLONE_PRICE_WITH_NO_THRESHOLD";
    public static final String PRODUCT_MANAGEMENT_PRODUCT_WITHOUT_PRICE = "PRODUCT_MANAGEMENT_PRODUCT_WITHOUT_PRICE";
    public static final String PRODUCT_MANAGEMENT_PRODUCT_NAME_REQUIRED = "PRODUCT_MANAGEMENT_PRODUCT_NAME_REQUIRED";
    public static final String ECOSYSTEM_MANAGEMENT_ECOSYSTEM_NAME_REQUIRED = "ECOSYSTEM_MANAGEMENT_ECOSYSTEM_NAME_REQUIRED";
    public static final String ECOSYSTEM_MANAGEMENT_ECOSYSTEM_NOT_FOUND = "ECOSYSTEM_MANAGEMENT_ECOSYSTEM_NOT_FOUND";
    public static final String ECOSYSTEM_MANAGEMENT_CATALOG_NOT_FOUND = "ECOSYSTEM_MANAGEMENT_CATALOG_NOT_FOUND";
    public static final String ECOSYSTEM_MANAGEMENT_CATALOG_IDS_REQUIRED = "ECOSYSTEM_MANAGEMENT_CATALOG_IDS_REQUIRED";
    public static final String PRODUCT_MANAGEMENT_CATALOG_IDS_REQUIRED = "PRODUCT_MANAGEMENT_CATALOG_IDS_REQUIRED";
    public static final String PRODUCT_MANAGEMENT_PRODUCT_NOT_FOUND = "PRODUCT_MANAGEMENT_PRODUCT_NOT_FOUND";
    public static final String IMAGE_MANAGEMENT_LOGICALIMAGE_NOT_FOUND = "IMAGE_MANAGEMENT_LOGICALIMAGE_NOT_FOUND";
    public static final String CATALOG_MANAGEMENT_DUPLICATE = "DUPLICATE_NAME";
    
    
    public static final String SOFTWARE_MANAGEMENT_ERROR = "SOFTWARE_MANAGEMENT_ERROR";

    public static final String ECOSYSTEM_ERROR = "ECOSYSTEM_ERROR";
    public static final String ECOSYSTEM_NOT_EMPTY = "ECOSYSTEM_NOT_EMPTY";
    public static final String ECOSYSTEM_NOT_FOUND = "ECOSYSTEM_NOT_FOUND";
    public static final String ENVIRONMENT_ERROR = "ENVIRONMENT_ERROR";

    public static final String IMPORT_ERROR = "IMPORT_ERROR";

    public static final String TAG_DUPLICATED_NAME = "TAG_DUPLICATED_NAME";
    public static final String TAG_GENERIC_ERROR = "TAG_GENERIC_ERROR";
    public static final String TAG_INVALID_ID = "TAG_INVALID_ID";

    public static final String INVENTORY_ERROR = "INVENTORY_ERROR";
    public static final String INVENTORY_ASSET_NOT_FOUND = "INVENTORY_ASSET_NOT_FOUND";
    public static final String INVENTORY_VALIDATION_ERROR = "INVENTORY_VALIDATION_ERROR";
    public static final String INVENTORY_ENVIRONMENT_NOT_FOUND = "INVENTORY_ENVIRONMENT_NOT_FOUND";
    public static final String INVENTORY_ENVIRONMENT_NOT_AUTHORIZED = "INVENTORY_ENVIRONMENT_NOT_AUTHORIZED";

    public static final String LOCK_ERROR = "LOCK_ERROR";
    public static final String ITEM_ALREADY_LOCKED = "ITEM_ALREADY_LOCKED";
    public static final String ITEM_ALREADY_UNLOCKED = "ITEM_ALREADY_UNLOCKED";


    public static final String ASSESSMENT_ERROR = "ASSESSMENT_ERROR";
    public static final String ASSESSMENT_VALIDATION_ERROR = "ASSESSMENT_VALIDATION_ERROR";
    public static final String ASSESSMENT_OK = "ASSESSMENT_OK";
    public static final String ASSESSMENT_OK_ONLY_NAME_CHANGED= "ASSESSMENT_OK_ONLY_NAME_CHANGED";
    public static final String ASSESSMENT_NOTHING_TO_DO= "ASSESSMENT_NOTHING_TO_DO";
    public static final String ASSESSMENT_SERVER_IN_WAITING= "ASSESSMENT_SERVER_IN_WAITING";

}
