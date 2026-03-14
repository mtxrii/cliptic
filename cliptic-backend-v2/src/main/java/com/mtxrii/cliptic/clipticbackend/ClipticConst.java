package com.mtxrii.cliptic.clipticbackend;

import java.util.List;

public final class ClipticConst {
    private ClipticConst() { }

    public static final String EMPTY_STRING = "";

    public static final String REDIRECT_BASE_URL = "https://cliptic.to/";

    public static final String MAPPING_URL_CONTROLLER = "/url";
    public static final String REDIRECT_URL_CONTROLLER = "/{alias}";

    public static final String ALIAS_REQUEST_PARAM = "alias";
    public static final String OWNER_REQUEST_PARAM = "owner";
    public static final String PASSCODE_REQUEST_PARAM = "passcode";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_AUTH_HEADER_PREFIX = "Bearer ";
    public static final String NONE_AUTH_HEADER = "None";

    public static final String BEARER_TOKEN_ENV_VAR_KEY = "BEARER_TOKEN";
    public static final String PASSCODE_ENV_VAR_KEY = "PASSCODE";

    public static final int CREATE_RANDOM_ALIAS_MAX_RETRIES = 10;
    public static final int CREATE_RANDOM_ALIAS_LENGTH_PADDING = 4;
    public static final int CUSTOM_ALIAS_MAX_LENGTH = 16;

    public static final List<String> RESERVED_ALIASES = List.of("ABOUT", "INFO", "404", "HELP", "PRIVACY", "TERMS");
    public static final List<String> SENSITIVE_ALIASES = List.of(
            "ADMIN", "ADMINISTRATOR", "SUPPORT", "STAFF", "MOD", "MODERATOR", "OFFICIAL", "SYSTEM", "SECURITY", "ROOT",
            "OWNER", "HELP", "CONTACT", "API", "WWW", "MAIL", "EMAIL", "LOGIN", "SIGNIN", "SIGN-IN", "SIGNUP", "SIGN-UP",
            "CLIPTIC", "CLIPTICTO", "CLIP", "NULL", "UNDEFINED", "TEST", "DEV", "PROD", "STAGE", "HEALTH", "STATUS",
            "ERROR", "ADMIN", "DOCS", "TERMS", "PRIVACY", "ASSETS", "STATIC", "FAVICON", "ROBOTS", "SITEMAP", "LEGAL"
    );

    public static final int RATE_LIMIT_PER_MINUTE = 100;
    public static final int RATE_LIMIT_WINDOW_IN_SECONDS = 60;
}