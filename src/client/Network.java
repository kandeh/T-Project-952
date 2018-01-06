package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.xml.bind.DatatypeConverter;

public class Network extends Thread {
    
    private Socket socket;
    private NetListener netListener;
    private PrintWriter out;
    private Scanner in;
    private InputStream inStream;
    private OutputStream outStream;
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    
    public static String StringToHex(String str) {
        byte bytes[] = str.getBytes();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public static String HexToString(String hex) {
        byte[] bytes = DatatypeConverter.parseHexBinary(hex);
        return new String(bytes);
    }
    
    protected interface NetListener {
        public void onReceive(String msg);
    } 
    
    protected void setNetListener(NetListener nl) {
        netListener = nl;
    }
    
    protected void ConnectTo(String host, int port) throws Exception {
        socket = new Socket(host, port);
        inStream = socket.getInputStream();
        outStream = socket.getOutputStream();
        out = new PrintWriter(outStream, true);
        in = new Scanner(inStream);
        start();
    }
    
    protected void send(String msg) {
        out.println(StringToHex(msg));
        out.flush();
    }
    
    protected void Close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        String msg;
        try {
            while(in.hasNextLine()) {
                msg = HexToString(in.nextLine());
                netListener.onReceive(msg);
            }
            Close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}