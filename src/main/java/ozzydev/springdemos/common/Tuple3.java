package ozzydev.springdemos.common;

public class Tuple3<T1, T2, T3> implements Tuple
{
    public final T1 item1;
    public final T2 item2;
    public final T3 item3;

    public Tuple3(final T1 _item1, final T2 _item2, final T3 _item3)
    {
        item1 = _item1;
        item2 = _item2;
        item3 = _item3;
    }

    @Override
    public int size()
    {
        return 3;
    }
}
