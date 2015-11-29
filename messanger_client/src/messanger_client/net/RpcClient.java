package messanger_client.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import messanger_api.net.DataPackage;
import messanger_api.service.Service;
import messanger_client.service.ClientService;

public class RpcClient {
	
	private static final Logger log = Logger.getLogger( ClientService.class.getName() );
	private String host;
	private int port;
	
	public RpcClient() {
		this.host=Service.SERVICE_HOST;
		this.port=Service.SERVICE_PORT;
		log.info("RpcClient created");
	}

	public DataPackage sendAndReceive(DataPackage data) {
		OutputStream os = null;
        InputStream is = null;
        log.info("Connecting to service");
        try{ 
        	Socket socket = new Socket(host,port);
        	log.info("Connected to service");
            // write request
            os = socket.getOutputStream();
            data.writeTo(os);
            os.flush();
            log.info("Request sent: " + data.toString());
            // read response
            is = socket.getInputStream();
            byte[] bytes = new byte[1024];	//de facut:trebuie sa citesc pana cand nu mai are de citit
            int bytesRec = is.read(bytes);
            // prepare response message
            DataPackage response = new DataPackage();
            response.readFrom(new ByteArrayInputStream(bytes, 0, bytesRec));
//            if (response.what().equalsIgnoreCase(Message.OK)) {
//                LOG.info("Response OK: " + response.toString());
//                return response;
//            } else { // error, so throw an exception
//                LOG.info("Response ERROR: " + response.toString());
//                throw new ServiceException(response.body()); // include details
//            }
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            //throw new Exception(e);
        } finally {
            try {
				(is).close();
				(os).close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return null;
	}

}
