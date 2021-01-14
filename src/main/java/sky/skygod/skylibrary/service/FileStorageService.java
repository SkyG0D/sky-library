package sky.skygod.skylibrary.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sky.skygod.skylibrary.exception.FileStorageException;
import sky.skygod.skylibrary.exception.MyFileNotFoundException;
import sky.skygod.skylibrary.property.FileStorageProperties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class FileStorageService {

    private final Path fileStoragePath;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStoragePath = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStoragePath);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory: " + this.fileStoragePath, ex);
        }

    }

    public String storageImage(MultipartFile file) {
        List<String> contentTypes = Arrays.asList(
                MediaType.IMAGE_JPEG_VALUE,
                MediaType.IMAGE_PNG_VALUE
        );

        if (!contentTypes.contains(file.getContentType())) {
            throw new FileStorageException("File is not an image");
        }

        return storeFile(file);
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {

            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStoragePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStoragePath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }

        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }

    }

}
