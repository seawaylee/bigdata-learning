package hadoop.rpc;

/**
 * @author NikoBelic
 * @create 2017/5/2 16:03
 */
public interface LoginServiceInterface
{
    public static final long versionID = 1L;

    public String login(String username, String passowrd);
}
