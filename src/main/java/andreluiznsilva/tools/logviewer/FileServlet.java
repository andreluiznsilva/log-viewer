package andreluiznsilva.tools.logviewer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileServlet extends HttpServlet {

	private DirectoryWatcher watcher;

	@Override
	public void init(ServletConfig config) throws ServletException {

		String dir = config.getServletContext().getInitParameter("directory");

		Path directory = Paths.get(dir);
		if (directory == null) {
			throw new UnsupportedOperationException("Directory '" + dir + "' not found");
		}

		watcher = new DirectoryWatcher(directory, AtmosphereTailerListener.class);
		watcher.start();

	}

	@Override
	public void destroy() {
		watcher.stop();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (File file : watcher.listFiles()) {
			builder.append("\"" + file.getName() + "\",");
		}
		if (builder.length() > 1) {
			builder.deleteCharAt(builder.length() - 1);
		}
		builder.append("]");

		String json = builder.toString();

		resp.getWriter().write(json);
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		resp.setContentLength(json.length());

	}

}
