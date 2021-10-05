package ozzydev.springdemos.query;

public enum QueryOperator
{
    EQ, NE,
    GT, GTE, NGT,
    LT, LTE, NLT,
    LIKE, NOT_LIKE,
    IN, NOT_IN,
    STARTSWITH, ENDSWITH, CONTAINS,
    IS_NULL, NOT_NULL,
    IS_TRUE,IS_FALSE,
    BETWEEN
}
