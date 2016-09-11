

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class studentServer {
    private ServerSocket myServer;
    
    public studentServer(int portNumber) throws IOException{
    	this.myServer = new ServerSocket(portNumber);
    }
    
    public void close() throws IOException{
    	myServer.close();
    }
    public void service(){
      try{
    	  while(true){
      		Socket socket = myServer.accept();
      		try{
      			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
      			BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      			String line = "";
      			do{
      				line = in.readLine();
      				System.out.println(line);
      				if(line.contains("SET")){
      					out.println("HTTP/1.0 400 Bad Request");
      				} else if(line.contains("GET")){
      					out.println("HTTP/1.0 200 OK");
      					out.println("Content-Length: 0");
      					out.println("\r\n.\r\n");
      				}
      			}while( line != null && !line.equals(".") && !line.equals(""));
      			
      		} catch(IOException e){
      			throw e;
      		} finally{
      			socket.close();
      		}
    	  }
      } catch(IOException e){
    	  System.out.println(e);
      }
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        if(args.length != 1 || args[0].matches("[^0-9]+")){
        	// Wrong Augment
        	System.out.println("Wrong Usage. Example: java studentServer <PortNumber>");
        } else{
        	try{
        		studentServer server = new studentServer(Integer.valueOf(args[0]).intValue());
        		server.service();
        		server.close();
        	}catch(IOException ioe){
        		ioe.printStackTrace();
        	}
        }
	}

}
