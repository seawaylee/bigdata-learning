package hadoop.rpc;

/**
 * Created by NikoBelic on 2017/5/2.
 */
public interface ClientNameNodeProtocol
{
    public static final long versionID = 100L;

    public String getMetaData(String path);
}
