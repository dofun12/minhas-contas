package org.lemanoman.minhascontas.dto;

public class JsonResponse {
   public boolean success;
   public String message;
   public Object data;

   public JsonResponse() {
   }

   public static JsonResponse ok(Object data){
      return new JsonResponse(true,data);
   }

   public JsonResponse(boolean success, String message) {
      this.success = success;
      this.message = message;
   }

   public JsonResponse(boolean success, Object data) {
      this.success = success;
      this.data = data;
   }

   public boolean isSuccess() {
      return success;
   }

   public void setSuccess(boolean success) {
      this.success = success;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public Object getData() {
      return data;
   }

   public void setData(Object data) {
      this.data = data;
   }
}
