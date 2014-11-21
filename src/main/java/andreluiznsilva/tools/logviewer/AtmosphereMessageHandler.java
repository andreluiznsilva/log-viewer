package andreluiznsilva.tools.logviewer;

import java.io.IOException;

import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.AtmosphereResponse;
import org.atmosphere.handler.OnMessage;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.BroadcastOnPostAtmosphereInterceptor;
import org.atmosphere.interceptor.HeartbeatInterceptor;

@AtmosphereHandlerService(
		path = "/log/change/{fileName}",
		broadcasterCache = UUIDBroadcasterCache.class,
		interceptors = {
				AtmosphereResourceLifecycleInterceptor.class,
				BroadcastOnPostAtmosphereInterceptor.class,
				TrackMessageSizeInterceptor.class,
				HeartbeatInterceptor.class
		})
public class AtmosphereMessageHandler extends OnMessage<String> {

	@Override
	public void onMessage(AtmosphereResponse resp, String message) throws IOException {

		String result = "[\"" + message + "\"]";

		resp.getWriter().write(result);
		resp.setContentType("application/json; charset=utf-8");
		resp.setContentLength(result.getBytes().length);

	}

}
