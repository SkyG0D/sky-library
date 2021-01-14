package sky.skygod.skylibrary.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.UUID;

@Component
public class ResourceCreatedListener implements ApplicationListener<ResourceCreatedEvent> {

    @Override
    public void onApplicationEvent(ResourceCreatedEvent event) {
        HttpServletResponse response = event.getResponse();
        UUID uuid = event.getUuid();
        addLocationHeader(response, uuid);
    }

    private void addLocationHeader(HttpServletResponse response, UUID uuid) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        response.setHeader("Location", uri.toASCIIString());
    }

}
