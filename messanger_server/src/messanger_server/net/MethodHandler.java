package messanger_server.net;

import messanger_api.net.DataPackage;

public interface MethodHandler {
	DataPackage execute(DataPackage data);
}
