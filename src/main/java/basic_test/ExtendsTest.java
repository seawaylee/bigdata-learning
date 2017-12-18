package basic_test;

/**
 * @author NikoBelic
 * @create 2017/12/13 11:02
 */
public class ExtendsTest
{
    public static void main(String[] args)
    {
        IDataImporter bookImporter = new BookImporter();
        bookImporter.importData();
    }
}


interface IDataImporter
{
    void importData();
}
class BaseImporter implements IDataImporter
{
    public void checkData()
    {
        System.out.println("Father checking data...");
    }
    @Override
    public void importData()
    {
        checkData();
        System.out.println("Father importing data...");
    }

}

class BookImporter extends BaseImporter
{
    @Override
    public void importData()
    {
        System.out.println("BookImporter importing data...");
    }

    @Override
    public void checkData()
    {
        System.out.println("BookImporter checking data...");
    }
}
