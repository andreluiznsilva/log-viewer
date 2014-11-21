package andreluiznsilva.tools.logviewer.test;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.input.TailerListenerAdapter;

import andreluiznsilva.tools.logviewer.DirectoryWatcher;

public class TestWatcher extends TailerListenerAdapter {

	public static final Path DIRECTORY_TO_WATCH = Paths.get("logs/");

	public static void main(String[] args) throws Exception {
		new DirectoryWatcher(DIRECTORY_TO_WATCH, TestWatcher.class).start();
	}

	@Override
	public void handle(String line) {
		System.out.println(line);
	}

}
