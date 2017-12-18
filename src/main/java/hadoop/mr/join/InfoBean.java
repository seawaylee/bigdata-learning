package hadoop.mr.join;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author NikoBelic
 * @create 2017/5/22 21:22
 */
public class InfoBean implements Writable
{

    String orderId;
    String timestamp;
    String amount;
    String productId;
    String productName;
    String categoryId;
    String price;


    public InfoBean()
    {
    }


    public void setAll(String orderId, String timestamp, String productId, String amount, String productName, String categoryId, String price)
    {
        this.orderId = orderId;
        this.timestamp = timestamp;
        this.productId = productId;
        this.amount = amount;
        this.productName = productName;
        this.categoryId = categoryId;
        this.price = price;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException
    {
        dataOutput.writeUTF(orderId);
        dataOutput.writeUTF(timestamp);
        dataOutput.writeUTF(productId);
        dataOutput.writeUTF(amount);
        dataOutput.writeUTF(productName);
        dataOutput.writeUTF(categoryId);
        dataOutput.writeUTF(price);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException
    {
        this.orderId = dataInput.readUTF();
        this.timestamp = dataInput.readUTF();
        this.productId = dataInput.readUTF();
        this.amount = dataInput.readUTF();
        this.productName = dataInput.readUTF();
        this.categoryId = dataInput.readUTF();
        this.price = dataInput.readUTF();
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    @Override
    public String toString()
    {
        return "InfoBean{" + "orderId='" + orderId + '\'' + ", timestamp='" + timestamp + '\'' + ", amount='" + amount + '\'' + ", productId='" + productId + '\'' + ", productName='" + productName + '\'' + ", categoryId='" + categoryId + '\'' + ", price='" + price + '\'' + '}';
    }
}
