package messanger_client.service;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import messanger_api.models.Email;
import messanger_api.models.Page;
import messanger_api.models.Status;
import messanger_api.net.DataPackage;
import messanger_api.service.Service;
import messanger_client.net.RpcClient;
import messanger_client.ui.ConsoleUI;
import utils.AsyncCallback;
import utils.ConvertStringToMap;

public class ClientService implements Service {

	private static final Logger log = Logger.getLogger( ClientService.class.getName() );
	protected RpcClient rpcClient;
	private ExecutorService executorService;
	
	public ClientService() {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		log.info("ClientService created");
	}
	
	@Override
	public void add_email(String title, String content) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Email> get_all() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Email> searchByTitle(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean edit_email(int id, String title, String content) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean send(int id, String receiver) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Email> filterByStatus(Status status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean authenticate(String username, String password) {
		Future<Boolean> result=executorService.submit(()->{
			DataPackage data=new DataPackage();
			Map<String,String> header=new HashMap<>();
			Map<String,String> body=new HashMap<>();
			header.put(Service.METHOD, Service.AUTHENTICATE);
			body.put(Service.USERNAME, username);
			body.put(Service.PASSWORD, password);
			data.setHeader(header);
			data.addData(body);
			DataPackage dataReceived=rpcClient.sendAndReceive(data);
			return dataReceived.getData().get(0).get("result").equals("true");		
			});
		try {
			return result.get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Page<Email> getPage(int pageNr) {
		Future<Page<Email>> result=executorService.submit(()->{
			DataPackage data=new DataPackage();
			Map<String,String> header=new HashMap<>();
			Map<String,String> body=new HashMap<>();
			header.put(Service.METHOD, Service.GET_PAGE);
			body.put(Service.PAGE_NUMBER, String.valueOf(pageNr));
			data.setHeader(header);
			data.addData(body);
			DataPackage dataReceived=rpcClient.sendAndReceive(data);
			List<Map<String,String>> mapData=dataReceived.getData();
			Page<Email> page=new Page<>();
			for(Map<String,String> pageData:mapData){
				page.addElement(new Email(pageData));
			}
			return page;
			});
		try {
			return result.get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public void asyncGetAll(AsyncCallback<List<Email>> asyncCallback) {
		executorService.submit(() -> {
			
			DataPackage data=new DataPackage();
			
			Map<String,String> header=new HashMap<>();
			header.put(Service.METHOD, Service.GET_ALL);
			
			data.setHeader(header);
			DataPackage dataReceived=rpcClient.sendAndReceive(data);
			
			
			List<Map<String,String>> mapData=dataReceived.getData();
			
			
        	List<Email> res =new ArrayList<>();
        	for(Map<String,String> emailMap:mapData){
        		res.add(new Email(emailMap));
        	}
            asyncCallback.onSuccess(res);
});
		
	}

}
