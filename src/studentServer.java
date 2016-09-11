


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class studentServer {
    private ServerSocket myServer;
    private int count;
    
    public studentServer(int portNumber) throws IOException{
    	this.myServer = new ServerSocket(portNumber);
    	this.count = 0;
    }
    public List<String> processRequest(List<String> request){
    	// Only Handle GET Request
    	List<String> response  = new LinkedList<String>();
    	if(request.size() < 1){
    		response.add("HTTP/1.0 400 Bad Request");
    		response.add("\r\n");
    	} else{
    		String line_1  = request.get(0);
    		if(line_1.contains("GET")){
    			String[] words = line_1.split(" ");
    			String fileName = words[1].replaceAll("/", "");
    			Charset charset = Charset.forName("UTF-8");
    			try {
    				File file = new File(fileName);
    				if(file.exists()){
    					FileReader filereader = new FileReader(file);
    					BufferedReader reader = new BufferedReader(filereader);
    					response.add("HTTP/1.0 200 OK");
					    response.add("Content-Length: "+file.length());
					    response.add("");
					    String line = null;
					    while((line = reader.readLine())!=null){
						    response.add(line);
						    }
					    response.add("\r\n");
					    reader.close();
					    filereader.close();
    				} else{
    					// File Not found
    					response.add("HTTP/1.0 404 Not Found");	
    					response.add("\r\n");	
    				}
				} catch (IOException e) {
					// Other IOException
					response.add("HTTP/1.0 404 Not Found");	
					response.add("\r\n");
				}
    		}else{
    			response.add("HTTP/1.0 400 Bad Request");	
    			response.add("\r\n");
    		}
    	}
    	return response;
    }
    public void close() throws IOException{
    	myServer.close();
    }
    public void service(){
      try{
    	  while(true){
    		System.out.println(String.valueOf(count)+" connections served.Accepting new client...");
      		Socket socket = myServer.accept();
      		try{
      			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
      			BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      			String line = "";
      			// Read Header
      			List<String> header = new LinkedList<String>();
      			do{
      				line = in.readLine();
      				header.add(line);
      				System.out.println(line);
      			}while( line != null && !line.equals(""));
      			// Process Header and return response
      			List<String> response = this.processRequest(header);
      			// Return response to client
      			System.out.println("My response");
      			for(int i=0;i<response.size();i++){
      				System.out.println(response.get(i));
      				out.println(response.get(i));
      			}	
      		} catch(IOException e){
      			throw e;
      		} finally{
      			socket.close();
      		}
      		count++;
    	  }
      } catch(IOException e){
    	  System.out.println(e);
      }
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        if(args.length != 1 || args[0].matches("[^0-9]+")){
        	// Wrong Augment
        	System.out.println("Wrong Usage. Usage: java studentServer <PortNumber>");
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
