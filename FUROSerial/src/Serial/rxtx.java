package Serial;

import Control.ControlManual;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class rxtx
{

    ControlManual CM;
    public rxtx()
    {
        super();
    }

    public void connect ( String portName , ControlManual cm) throws Exception
    {
        CM=cm;
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Puerto Ocupado");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(4800,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();

                (new Thread(new SerialReader(in,CM))).start();
                SerialWriter sw=new SerialWriter(out);
                CM.setOutput(sw);
                (new Thread(sw)).start();

            }
            else
            {
                System.out.println("Error: Porfavor usar solamente un puerto serial.");
            }
        }


    }

    
    /** */
    public static class SerialReader implements Runnable
    {
        InputStream in;
        ControlManual CM;

        public SerialReader ( InputStream in,ControlManual cm )
        {
            this.in = in;
            this.CM=cm;
        }

        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                    //System.out.print(new String(buffer,0,len));
                    CM.dataReceived(new String(buffer,0,len));
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

    /** */
    public static class SerialWriter implements Runnable
    {
        OutputStream out;
        String msg;

        public SerialWriter ( OutputStream out )
        {
            this.out = out;
            msg=new String("");
        }
        public void send(byte msg){
        
            this.msg+=String.valueOf(msg);
            //System.out.println("gotMsg");
        }
        public void send(String msg){
            this.msg+=msg;
            //System.out.println("gotMsg");
            
        }
        public void run ()
        {
            try
            {
                /*
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                    
                }*/
                while(true){
                //System.out.println(msg.length());
                while(this.msg.length()>0){
                    System.out.println("sending "+ msg+" XX "+msg.getBytes());
                    this.out.write(msg.getBytes());
                    //msg="";
                    msg=msg.substring(1);
                }
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(rxtx.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }
    }

   
}