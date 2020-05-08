package edu.uci.ics.luisae.service.gateway.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;



@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginAndSessionResponse extends ResponseModel {
    @JsonProperty(value="session_id")
    private String session_id;
    public LoginAndSessionResponse(){}
    @JsonCreator public LoginAndSessionResponse(@JsonProperty(value="session_id")String session_id,
                                                @JsonProperty(value = "message") String message,
                                                @JsonProperty(value = "resultCode") int resultCode){
        //test that we dont need this super();
        super();
        this.session_id = session_id;
        super.setResult(returnResult(resultCode));
    }

    @JsonIgnore
    private Result returnResult(int givenResultCode){
        for(Result r : Result.values()) {
            if (r.getResultCode() == givenResultCode)
                return r;
        }
        return Result.COULD_NOT_FIND_RESULTCODE;
    }

    @JsonProperty(value = "session_id")
    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
