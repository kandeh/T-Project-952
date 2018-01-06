package server;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.xml.bind.DatatypeConverter;

public class TeamSocket extends Thread {

    
    private String teamName = null;
    private Socket socket;
    public String response = null;
    
    private PrintWriter out;
    private Scanner in;
    private InputStream inStream;
    private OutputStream outStream;
    
    private int teamNumber = 0;

    public TeamSocket(Socket socket, int team_number) {
        try {
            this.teamNumber = team_number;
            this.socket = socket;
            inStream = socket.getInputStream();
            outStream = socket.getOutputStream();
            out = new PrintWriter(outStream, true);
            in = new Scanner(inStream);
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getTeamNumber() {
        return teamNumber;
    }
    
    public boolean isOk() {
        if(teamName != null && teamName.length() > 0) {
            return true;
        }
        return false;
    }

    public String getTeamName() {
        return teamName;
    }
    
    public void send(String msg) {
        out.println(stringToHex(msg));
        out.flush();
    }
    
    public void close() {
        try {
            socket.close();
            stop();
        } catch (Exception e) {
        }
    }
    
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    
    public String stringToHex(String str) {
        byte bytes[] = str.getBytes();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public String hexToString(String hex) {
        byte[] bytes = DatatypeConverter.parseHexBinary(hex);
        return new String(bytes);
    }
    
    @Override
    public void run() {
        String msg;
        while(in.hasNextLine()) {
            msg = hexToString(in.nextLine());

            if(msg.startsWith("TEAM: ")) {
                teamName = msg.substring("TEAM: ".length());
            }
            this.response = msg;
        }
    }
}