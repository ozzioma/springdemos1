package ozzydev.springdemos.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class QueryFilter //<T>
{
    private String field;
    private QueryOperator operator;
    //private String value;
    private List<Object> values = new ArrayList<>();
}


