package com.mtxrii.cliptic.clipticbackend;

public final class ClipticConst {
    private ClipticConst() { }

    public static final String EMPTY_STRING = "";

    public static final String REDIRECT_BASE_URL = "https://sample.com/";

    public static final String MAPPING_URL_CONTROLLER = "/url";
    public static final String REDIRECT_URL_CONTROLLER = "/{alias}";

    public static final String ALIAS_REQUEST_PARAM = "alias";
    public static final String OWNER_REQUEST_PARAM = "owner";

    public static final int CREATE_RANDOM_ALIAS_MAX_RETRIES = 10;
    public static final int CREATE_RANDOM_ALIAS_LENGTH_PADDING = 4;
}