public class DynamicList {
	private Request LSRequest;
	private ComposedService LSComposed;
	public static long time;

	public Request getLSRequest() {
		return this.LSRequest;
	}

	public void setLSRequest(Request lSRequest) {
		this.LSRequest = lSRequest;
	}

	public ComposedService getLSComposed() {
		return this.LSComposed;
	}

	public void setLSComposed(ComposedService lSComposed) {
		this.LSComposed = lSComposed;
	}

	public DynamicList() {
		this.LSComposed = new ComposedService();
		this.LSRequest = new Request();
	}

	public DynamicList(Request request, ComposedService composedService) {
		this.LSRequest = request;
		this.LSComposed = composedService;
	}
}