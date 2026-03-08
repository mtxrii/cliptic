SELECT
    COUNT(*) FILTER (WHERE is_custom_alias = TRUE)  AS true_count,
    COUNT(*) FILTER (WHERE is_custom_alias = FALSE) AS false_count
FROM link;