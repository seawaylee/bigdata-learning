package hadoop.mr.flowcount;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author NikoBelic
 * @create 2017/5/7 22:50
 */
public class FlowBean implements WritableComparable<FlowBean>
{
    private Long upFlow;
    private Long downFlow;
    private Long sumFlow;

    public FlowBean()
    {
    }

    public FlowBean(Long upFlow, Long downFlow)
    {
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow = this.upFlow + this.downFlow;
    }

    public Long getUpFlow()
    {
        return upFlow;
    }

    public void setUpFlow(Long upFlow)
    {
        this.upFlow = upFlow;
    }

    public Long getDownFlow()
    {
        return downFlow;
    }

    public void setDownFlow(Long downFlow)
    {
        this.downFlow = downFlow;
    }

    public Long getSumFlow()
    {
        return sumFlow;
    }

    public void setSumFlow(Long sumFlow)
    {
        this.sumFlow = sumFlow;
    }

    public void set(Long upFlow, Long downFlow,Long sumFlow)
    {
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlow = sumFlow;
    }
    @Override
    public String toString()
    {
        return upFlow + " " + downFlow + " " + sumFlow;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException
    {
        dataOutput.writeLong(upFlow);
        dataOutput.writeLong(downFlow);
        dataOutput.writeLong(sumFlow);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException
    {
        this.upFlow = dataInput.readLong();
        this.downFlow  = dataInput.readLong();
        this.sumFlow = dataInput.readLong();
    }

    @Override
    public int compareTo(FlowBean o)
    {
        return this.sumFlow > o.getSumFlow() ? -1 : 1;
    }
}
