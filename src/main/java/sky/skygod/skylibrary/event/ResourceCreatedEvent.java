package sky.skygod.skylibrary.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Getter
public class ResourceCreatedEvent extends ApplicationEvent {

    private final HttpServletResponse response;
    private final UUID uuid;

    public ResourceCreatedEvent(Object source, HttpServletResponse response, UUID uuid) {
        super(source);
        this.response = response;
        this.uuid = uuid;
    }

}
