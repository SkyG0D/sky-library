package sky.skygod.skylibrary.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenPayload {

    private String token;
    private String type;
    private int expires;
    private String scope;
    private String name;
    private String jti;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TokenPayload(@JsonProperty("access_token") String token,
                        @JsonProperty("token_type")  String type,
                        @JsonProperty("expires_in") int expires,
                        @JsonProperty("scope") String scope,
                        @JsonProperty("name") String name,
                        @JsonProperty("jti") String jti) {

        this.token = token;
        this.type = type;
        this.expires = expires;
        this.scope = scope;
        this.name = name;
        this.jti = jti;
    }

}
