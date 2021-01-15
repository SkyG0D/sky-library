package sky.skygod.skylibrary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sky.skygod.skylibrary.dto.file.Response;
import sky.skygod.skylibrary.service.FileStorageService;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @GetMapping(value = "/images/{fileName:.+}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] getImage(@PathVariable String fileName) throws IOException {
        InputStream in = fileStorageService.loadFileAsResource(fileName).getInputStream();
        return IOUtils.toByteArray(in);
    }

    @PostMapping("/admin/images")
    public ResponseEntity<Response> uploadImage(@RequestParam MultipartFile file) {
        String fileName = fileStorageService.storageImage(file);

        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files/images/")
                .path(fileName)
                .toUriString();

        Response response = Response.builder()
                .fileName(fileName)
                .fileDownloadUri(fileDownloadUri)
                .fileType(file.getContentType())
                .size(file.getSize())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
