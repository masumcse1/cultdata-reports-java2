package com.reports.CultDataReports.util;

public class AppUtils {

	public static final long DEFAULT_LOCK_TIMEOUT = 2; // seconds

	public static final String RESPONSE_CODE_SUCCESS_MSG = "Successful operation";
	public static final String RESPONSE_CODE_NO_DATA_MSG = "No data found";

	public static final String FAIL = "fail";
	public static final String SUCCESS = "success";

	public static final String BEARER = "Bearer";

	/*
	 * Entity names
	 */
	public static final String LANGUAGE = "Language";

	public static final String MEAL_PLAN = "Meal Plan";

	public static final String DESCRIPTION = "Description";

	public static final String PROPERTY = "Property";
	public static final String BOOKING_STATUS_HISTORY = "Booking Status History";
	public static final String BRAND_OF_BOOKING_ENGINE = "Brand of Booking Engine";
	public static final String EVENT = "Event";

	public static final String ONLINE_PRESENCE = "Online Presence";
	public static final String DISTRIBUTOR = "Distributor";
	public static final String PROMOTION = "Promotion";
	public static final String PROPERTY_FACILITY_META = "Property Facility Meta";
	public static final String PROPERTY_FACILITY_FILTER = "Property Facility Filter";
	public static final String ROOM_FACILITY_META = "Room Facility Meta";
	public static final String CONTRACTS = "Contracts";

	public static final String MESSAGE = "Message";
	public static final String MESSAGE_FEEDBACK = "Message Feedback";
	public static final String MESSAGE_LOG = "Message Log";
	public static final String MEDIA_CATEGORY = "Media Category";

	public static final String SETTING = "Setting";

	public static final String RATINGS = "Rating";

	public static final String STATIC_DATA = "Static data";

	public static final String PROPERTY_ATTRIBUTE = "Property attribute";

	public static final String PROPERTY_INFO = "Property info";

	public static final String PROPERTY_TYPE = "Property type";

	public static final String IDENTIFIER_SOURCE = "Identifier source";

	public static final String PROPERTY_IDENTIFIER = "Property Identifier";

	public static final String PRODUCT_IDENTIFIER = "Product Identifier";

	public static final String SELLABLE_UNIT_IDENTIFIER = "Sellable Unit Identifier";

	public static final String MEDIA = "Media";

	public static final String PRODUCT = "Product";

	public static final String BASKET = "Basket";

	public static final String SELLABLE_UNIT = "Sellable Unit";

	public static final String BRAND = "Brand";

	public static final String POLICY_INFO = "Policy Info";

	public static final String PAYMENT_POLICY = "Payment Policy";

	public static final String CANCELLATION_POLICY = "Cancellation Policy";
	
	public static final String CANCELLATION_RULE = "Cancellation Rule";

	public static final String BOOKING_POLICY = "Booking Policy";

	public static final String PET_POLICY = "Pet Policy";

	public static final String RATE_PLAN = "Rate Plan";

	public static final String BOOKING_GUARANTEE = "Booking guarantee";

	public static final String RATE_PLAN_BASKET = "RatePlanBasket-";

	public static final String RATE_PLAN_PRODUCT = "RatePlanProduct-";

	public static final String SELLABLE_UNIT_TYPE = "Sellable Unit Type";

	public static final String PHONE_TYPE = "Phone type";

	public static final String CREDIT_CARD = "Credit card";

	public static final String EMAIL_TYPE = "Email type";

	public static final String ROOM_TYPE = "Room type";

	public static final String PRICING_MODEL = "Pricing model";

	public static final String PRICING_MODEL_TYPE = "Pricing model type";

	public static final String PRICING_MODEL_STANDARD = "Standard pricing model";

	public static final String PRICING_MODEL_DERIVED = "Derived pricing model";

	public static final String PRICING_MODEL_OCCUPANCY_BASED = "Occupancy based pricing model";

	public static final String PRICING_MODEL_LENGTH_OF_STAY = "Length of stay pricing model";

	public static final String STATE = "State";

	public static final String CONTINENT = "Continent";

	public static final String COUNTRY = "Country";

	public static final String COUNTRY_TRANSLATION = "Country Translation";

	public static final String POINT_OF_INTEREST = "Point Of Interest";

	public static final String CATEGORY_CODE = "Category Code";

	public static final String PREDEFINED_TAG = "Predefined Tag";

	public static final String MEDIA_TYPE = "Media Type";

	public static final String MEDIA_ATTRIBUTE_TYPE = "Media Attribute Type";

	public static final String DESCRIPTION_TYPE = "Description Type";

	public static final String NAME_TYPE = "Name Type";

	public static final String BOOKING_MODE_TYPE = "Booking mode type";

	public static final String LICENSE_TYPE = "License Type";

	public static final String CURRENCY = "Currency";

	public static final String SUPPLIER = "Supplier";

	public static final String WEBHOOK = "Webhook";
	public static final String TABLE = "Table";
	public static final String TABLE_NAME = "table name";

	/*
	 * Field names
	 */

	public static final String ALL = "ALL";

	public static final String ID = "id";

	public static final String EWS_ID = "ewsId";

	public static final String PROPERTY_ID = "propertyId";

	public static final String CULTSWITCH_ID = "CultSwitchId";

	public static final String SUPPLIER_UNIT_ID = "SupplierUnitId";
	public static final String SUPPLIER_UNIT_ID_AND_PROPERTY_ID = "SupplierUnitId And PropertyId";
	public static final String SUPPLIER_UNIT_ID_AND_PARTNER_DISTRIBUTOR_ID = "SupplierUnitId And PartnerDistributorId";

	public static final String CODE = "code";

	public static final String ABBREVIATION = "abbreviation";

	public static final String NAME = "name";

	public static final String SOURCE_ID = "sourceId";

	public static final String SOURCE_ABBREVIATION = "sourceAbbreviation";

	/*
	 * Defaults
	 */
	public static final String DEFAULT_LANGUAGE_CODE = "en";

	public static final String DEFAULT_LOGO_MEDIA_TYPE_CODE = "img";

	public static final String DEFAULT_MEDIA_DESCRIPTION_TYPE_CODE = "med";

	public static final String DEFAULT_POI_DESCRIPTION_TYPE_CODE = "poi";

	public static final String DEFAULT_SELLABLE_UNIT_DESCRIPTION_TYPE_CODE = "su";

	public static final String DEFAULT_RATE_PLAN_DESCRIPTION_TYPE_CODE = "rate-plan";

	public static final String DEFAULT_PROPERTY_DESCRIPTION_TYPE_CODE = "prpt";

	public static final String DEFAULT_BASKET_DESCRIPTION_TYPE_CODE = "bsk";

	public static final String DEFAULT_PET_POLICY_DESCRIPTION_TYPE_CODE = "pet-plc";

	public static final String DEFAULT_CANCELLATION_POLICY_DESCRIPTION_TYPE_CODE = "cancel-plc";

	public static final String DEFAULT_BOOKING_POLICY_DESCRIPTION_TYPE_CODE = "book-plc";

	public static final String LONG_PAYMENT_POLICY_DESCRIPTION_TYPE_CODE = "pay-plc";
	public static final String DEFAULT_POLICY_INFO_DESCRIPTION_TYPE_CODE = "info-plc";

	public static final String DEFAULT_SELLABLE_UNIT_NAME_TYPE_CODE = "su";

	public static final String DEFAULT_RATE_PLAN_TYPE_CODE = "rp";

	public static final String DEFAULT_CANCELLATION_POLICY_TYPE_CODE = "cp";

	public static final String DEFAULT_PAYMENT_POLICY_TYPE_CODE = "pp";

	public static final String ROOM_DB_IDENTIFIER_SOURCE_ABBR = "rdb";

	public static final String CULT_SWITCH_IDENTIFIER_SOURCE_ABBR = "cs";

	public static final String GOOGLE_BUSINESS_PLACES_ABBR = "gbp";

	public static final String EHOTEL_IDENTIFIER_SOURCE_ABBR = "ehtl";

	public static final String PRICING_MODEL_CODE_STD = "std"; // Standard pricing model

	public static final String PRICING_MODEL_CODE_DRV = "drv"; // Derived pricing model

	public static final String PRICING_MODEL_CODE_OCC = "occ"; // Occupancy based pricing model

	public static final String PRICING_MODEL_CODE_LEN = "len"; // Length of stay pricing model

	public static final String DEFAULT_PROPERTY_NAME_TYPE_CODE = "property";

	public static final Integer ERROR_CODE_OBTAIN_LOCK_SU = 101;
	public static final Integer ERROR_CODE_FAIL_LOCK_SU = 102;
//    public static final Integer ERROR_CODE = 103;

}
