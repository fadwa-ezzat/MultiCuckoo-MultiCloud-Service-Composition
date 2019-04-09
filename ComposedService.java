
import java.util.HashMap;

public class ComposedService {
	private HashMap<String, Cloud> composedService;

	public ComposedService() {
		this.composedService = new HashMap<String, Cloud>();
	}

	public Cloud getComposed(String serviceName) {
		if (this.composedService.size() > 0) {
			if (this.composedService.get(serviceName) != null) {
				return this.composedService.get(serviceName);
			}
		}
		return new Cloud();
	}

	public void addToComposedService(String serviceName, Cloud cloud) {
		this.composedService.put(serviceName, cloud);
	}

	public int composedSize() {
		return this.composedService.size();
	}

	public HashMap<String, Cloud> getHashMap() {
		return this.composedService;
	}
}
