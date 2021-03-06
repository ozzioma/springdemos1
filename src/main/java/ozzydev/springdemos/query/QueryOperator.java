package ozzydev.springdemos.query;

public enum QueryOperator
{
    EQ, EQUAL, NE, NOT_EQUAL,
    IS_NULL, NOT_NULL,
    IS_TRUE, IS_FALSE,
    GT, GREATER_THAN, NGT, NOT_GREATER_THAN,
    GTE, GREATER_THAN_OR_EQUAL, NGTE, NOT_GREATER_THAN_OR_EQUAL,
    LT, LESS_THAN, NLT, NOT_LESS_THAN,
    LTE, LESS_THAN_OR_EQUAL, NLTE, NOT_LESS_THAN_OR_EQUAL,
    LIKE, CONTAINS, NOT_LIKE,
    SW, STARTS_WITH, EW, ENDS_WITH,
    IN, NOT_IN,
    BW, BETWEEN, NBW, NOT_BETWEEN,
    IS_MEMBER, NOT_MEMBER
}


