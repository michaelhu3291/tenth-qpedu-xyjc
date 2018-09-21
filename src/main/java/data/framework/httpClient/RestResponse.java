package data.framework.httpClient;

public class RestResponse {

	private static final String OK = "ok";  
    private static final String ERROR = "error";  
  
    private Meta meta;  
    private Object data;  
  
    public RestResponse success() {  
        this.meta = new Meta(true, OK);  
        return this;  
    }  
  
    public RestResponse success(Object data) {  
        this.meta = new Meta(true, OK);  
        this.data = data;  
        return this;  
    }  
  
    public RestResponse failure() {  
        this.meta = new Meta(false, ERROR);  
        return this;  
    }  
  
    public RestResponse failure(String message) {  
        this.meta = new Meta(false, message);  
        return this;  
    }  
  
    public Meta getMeta() {  
        return meta;  
    }  
  
    public Object getData() {  
        return data;  
    }
    
    public static String getOk() {
		return OK;
	}

	public static String getError() {
		return ERROR;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public void setData(Object data) {
		this.data = data;
	}



	public class Meta {  
  
        private boolean success;  
        private String message;  
  
        public Meta(boolean success) {  
            this.success = success;  
        }  
  
        public Meta(boolean success, String message) {  
            this.success = success;  
            this.message = message;  
        }  
  
        public boolean isSuccess() {  
            return success;  
        }  
  
        public String getMessage() {  
            return message;  
        }

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public void setMessage(String message) {
			this.message = message;
		} 
    }

	@Override
	public String toString() {
		return "RestResponse [meta=" + meta + ", data=" + data + ", success()="
				+ success() + ", failure()=" + failure() + ", getMeta()="
				+ getMeta() + ", getData()=" + getData() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}  
    
    
    
    
    
}
