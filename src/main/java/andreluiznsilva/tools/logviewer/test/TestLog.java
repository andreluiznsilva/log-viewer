package andreluiznsilva.tools.logviewer.test;

import java.io.File;
import java.util.Collections;

import org.apache.commons.io.FileUtils;

public class TestLog {

	public static void main(String[] args) throws Exception {

		File[] files = TestWatcher.DIRECTORY_TO_WATCH.toFile().listFiles();

		int i = 0;
		while (true) {
			for (File file : files) {
				String line = "[" + file.getName() + "] teste " + i++;
				FileUtils.writeLines(file, Collections.singletonList(line), true);
			}
			Thread.sleep(1000);
		}

	}

}
