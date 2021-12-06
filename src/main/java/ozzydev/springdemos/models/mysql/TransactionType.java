package ozzydev.springdemos.models.mysql;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmEnumeration;

@EdmEnumeration()
public enum TransactionType
{
    SALE, PURCHASE, OTHER
}


