package messanger_server.net;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import messanger_api.models.Email;
import messanger_api.models.Page;
import messanger_api.net.DataPackage;
import messanger_api.service.Service;
import messanger_server.service.MessangerService;
import messanger_server.service.utils.Collections;

public class RpcServer {

	protected MessangerService service;
	private static final Logger log = Logger.getLogger( RpcServer.class.getName() );
	private ExecutorService executorService =Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	protected Map<String,MethodHandler> methods=new HashMap<>();
	
	public void run(){
		try {
			log.info("Creating server socket");
            ServerSocket ss = new ServerSocket(Service.SERVICE_PORT);
            log.info("Server socket created");
            while (!Thread.currentThread().isInterrupted()) {
            	log.info("Listening new clients");
                Socket s = ss.accept();
                log.info("Client acepted");
                
                executorService.submit(new RpcHandler(s));
            }
        } catch (IOException e) {
            e.printStackTrace();
            //throw new ServiceException(e);
        }
		
	}
	
	public void addMethod(String methodName,MethodHandler method){
		methods.put(methodName, method);
	}
	
	private class RpcHandler implements Runnable {
        private Socket socket;

        public RpcHandler(Socket s) {
            this.socket = s;
        }

        @Override
        public void run() {
            
            InputStream is = null;OutputStream os = null;
            try {
                log.info("Handling client request");
                // read request
                is = socket.getInputStream();
                byte[] bytes = new byte[1024];
                int bytesRec = is.read(bytes);
                DataPackage dataReceived = new DataPackage();
                dataReceived.readFrom(new ByteArrayInputStream(bytes, 0, bytesRec));
                System.out.println(dataReceived.toString());
                //sa verific metoda printr-un switch momentan
                Map<String,String> header=dataReceived.getHeader();
                List<Map<String,String>> dataRecevied=dataReceived.getData();
				
				
                DataPackage response=new DataPackage();
                List<Map<String,String>> data=dataReceived.getData();
                switch(header.get(Service.METHOD)){	//inlocuiesc cu un method handler daca am timp
	                case Service.AUTHENTICATE:
	                	
		                try{
		                	boolean rez=service.authenticate(dataRecevied.get(0).get("username"), dataRecevied.get(0).get("password"));
		                	Map<String, String> returnData=new HashMap<String,String>();
		                	returnData.put("result", String.valueOf(rez));
		                	response.addData(returnData);
		                }catch(Exception e){
		                	Map<String,String> exceptionMap=new HashMap<String,String>();
		                	exceptionMap.put("exception", e.getMessage());
		                	data.add(exceptionMap);
		                }
		                break;
	                case Service.GET_ALL:
	                	
	                	try{
		                	
		                	List<Email> emails =service.get_all();
		                	for(Email email:emails){
		                		response.addData(email.toMap());
		                	}
		                }catch(Exception e){
		                	Map<String,String> exceptionMap=new HashMap<String,String>();
		                	exceptionMap.put("exception", e.getMessage());
		                	data.add(exceptionMap);
		                }
	                	
	                	
	                	break;
	                case Service.GET_PAGE:
	                	
	                	try{
	                		Integer pageNr = Integer.valueOf(data.get(0).get(Service.PAGE_NUMBER));
		                	Page<Email> page=service.getPage(pageNr);
		                	List<Email> emails =Collections.makeList(page.getElements());
		                	for(Email email:emails){
		                		response.addData(email.toMap());
		                	}
		                }catch(Exception e){
		                	Map<String,String> exceptionMap=new HashMap<String,String>();
		                	exceptionMap.put("exception", e.getMessage());
		                	data.add(exceptionMap);
		                }
	                	
	                	
	                	break;
                
                }
                
                
                
                
                
                
                // write response
                os = socket.getOutputStream();
                response.writeTo(os);
                os.flush();
                
            } catch (Exception e) {
                log.info("Exception during handling call");
                e.printStackTrace();
            } finally {
                try {
					(is).close();
					(os).close();
					(socket).close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
            }
        }
	}
}
