package andreluiznsilva.tools.logviewer;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;

public class AtmosphereTailerListener extends TailerListenerAdapter {

	private String fileName;

	@Override
	public void init(Tailer tailer) {
		fileName = tailer.getFile().getName();
	}

	@Override
	public void handle(String line) {
		BroadcasterFactory factory = BroadcasterFactory.getDefault();
		if (factory != null) {
			Broadcaster broadcaster = factory.lookup("/log/change/" + fileName, true);
			if (broadcaster != null) {
				broadcaster.broadcast(line);
			}
		}
	}

	@Override
	public void handle(Exception ex) {
		ex.printStackTrace();
	}

}
