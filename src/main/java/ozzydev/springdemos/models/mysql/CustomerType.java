package ozzydev.springdemos.models.mysql;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmEnumeration;

@EdmEnumeration()
public enum CustomerType
{
    GOLD, SILVER, PLATINUM
}
