import messanger_server.net.RpcServer;
import utils.AppContext;

public class Server {

	public static void main(String[] args) {
		AppContext context=new AppContext("settings.xml");
		RpcServer rpc=context.getBean(RpcServer.class);
		rpc.run();

	}

}
