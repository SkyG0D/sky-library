package sky.skygod.skylibrary.dto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Response {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

}
