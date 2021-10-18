package ozzydev.springdemos.common;

public class Tuple2<T1, T2> implements Tuple
{
    public final T1 item1;
    public final T2 item2;

    public Tuple2(final T1 _item1, final T2 _item2)
    {
        item1 = _item1;
        item2 = _item2;
    }

    @Override
    public int size()
    {
        return 2;
    }
}
