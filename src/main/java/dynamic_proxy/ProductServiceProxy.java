package dynamic_proxy;

import java.lang.reflect.Proxy;

/**
 * 动态代理测试
 * @author NikoBelic
 * @create 2017/4/10 16:22
 */
public class ProductServiceProxy
{
    /**
     * 获取动态代理实例
     * @Author SeawayLee
     * @Date 2017/04/10 17:06
     */
    public ProductService getProxy(Class clazz,Class[] interfaces,Integer discount) throws ClassNotFoundException
    {
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, (proxyObj, method, args) ->
        {
            Integer returnValue = (Integer) method.invoke(new ProductServiceImpl(), args);
            return returnValue - discount;
        });
        return (ProductService) proxy;
    }

    public static void main(String[] args) throws ClassNotFoundException
    {
        ProductServiceProxy proxy = new ProductServiceProxy();
        // 获取代理类
        ProductService productServiceProxy = proxy.getProxy(ProductServiceImpl.class,new Class[]{ProductService.class},40);
        System.out.println(productServiceProxy.getPrice());
    }
}