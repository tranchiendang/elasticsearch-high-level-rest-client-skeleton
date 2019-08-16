package io.dtc.essearch.helper;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;

/**
 * ResponseHelper use to unify response format body, always return http with status code 200.
 */
public class ResponseHelper {
    
    /**
     * This method is used for successed calling
     * @param data returned object
     * @return
     */
    public static ResponseEntity<?> successResponse(Object data) {
        Map<String, Object> responseData = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;
            {
            put("code", 0);
            put("message", "success");
            put("data", data);
        }};
        return ResponseEntity.ok().body(responseData);
        
    }

    /**
     * This method is used for failed calling
     * @param message error message
     * @param data returned object
     * @return
     */
    public static ResponseEntity<?> failResponse(String message, Object data) {
        Map<String, Object> responseData = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;
            {
            put("code", 0);
            put("message", message);
            put("data", data);
        }};
        return ResponseEntity.ok().body(responseData);
        
    }
}