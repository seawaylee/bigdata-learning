package dynamic_proxy;

/**
 * 产品业务逻辑实现
 * @author NikoBelic
 * @create 2017/4/10 16:20
 */
public class ProductServiceImpl implements ProductService
{
    @Override
    public Integer getPrice()
    {
        // 从数据库中读取产品价格
        return 100;
    }
}
