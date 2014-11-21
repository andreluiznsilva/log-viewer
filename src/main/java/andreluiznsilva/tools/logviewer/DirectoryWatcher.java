package andreluiznsilva.tools.logviewer;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

public class DirectoryWatcher {

	private final Path directory;
	private final Class<? extends TailerListener> listenerClass;
	private final ExecutorService executor;

	public DirectoryWatcher(Path directory, Class<? extends TailerListener> listenerClass) {
		this.directory = directory;
		this.listenerClass = listenerClass;
		this.executor = Executors.newCachedThreadPool();
	}

	public List<File> listFiles() {
		List<File> results = new ArrayList<File>();
		for (File file : directory.toFile().listFiles()) {
			if (file.isFile() && !file.isDirectory() && !file.isHidden()) {
				results.add(file);
			}
		}
		return results;
	}

	private TailerListener newListener() {
		try {
			return listenerClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public void start() {
		for (File file : listFiles()) {
			TailerListener listener = newListener();
			Tailer tailer = new Tailer(file, listener, 1, true);
			executor.submit(tailer);
		}
	}

	public void stop() {
		executor.shutdown();
	}

}