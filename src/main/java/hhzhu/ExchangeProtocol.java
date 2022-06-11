package hhzhu;

/**
 * Author: Haohong Zhu
 * Student ID: 1305370
 */
public class ExchangeProtocol {
    public int length;
    public byte[] content ;

    public void setContent(String msg, String charset)
    {
        if(msg == null)
        {
            this.length = 0;
            return;
        }

        try {
            this.content = msg.getBytes(charset);
            this.length = this.content.length;

        }catch(Exception e)
        {
            String message = e.getMessage() + ":" + e.getClass().getName();
            this.content = message.getBytes();
            this.length = this.content.length;
        }
    }

    public String toString(String charset)
    {
        if(this.length <=0)
            return "";
        try {
            String str = new String(content, 0, length, charset);
            return str;
        }catch(Exception e)
        {
            return e.getMessage() + " : " + e.getClass().getName() ;
        }
    }

    @Override
    public String toString()
    {
        return toString("UTF-8");
    }
}
