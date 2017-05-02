package hadoop.rpc.impl;

import hadoop.rpc.LoginServiceInterface;

/**
 * @author NikoBelic
 * @create 2017/5/2 16:07
 */
public class LoginServiceImpl implements LoginServiceInterface
{
    @Override
    public String login(String username, String passowrd)
    {
        System.out.println(username + ", 你好啊!");

        return username + " Successfully login....";
    }
}
