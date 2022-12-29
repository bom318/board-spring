package bom.basicboard.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import bom.basicboard.domain.File;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos+1);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

    public List<File> saveFiles(Long boardNum, List<MultipartFile> multipartFiles) {
        
        if(multipartFiles.isEmpty()) {
            return null;
        } else {
            List<File> files = new ArrayList<>();
            for (MultipartFile multipartFile : multipartFiles) {
                if(multipartFile.isEmpty()) {
                    return null;
                }else {
                    String originalFilename = multipartFile.getOriginalFilename();
                    String storeFileName = createStoreFileName(originalFilename);

                    try {
                        log.info("getFullPath={}",getFullPath(storeFileName));
                        multipartFile.transferTo(new java.io.File(getFullPath(storeFileName)));
                    }catch (IllegalStateException e) {
                        log.error("storeFile error={}", e);
                    } catch (IOException e) {
                        log.error("storeFile error={}", e);
                    } finally {
                        File savedFile = new File(boardNum, originalFilename, storeFileName);
                        files.add(savedFile);
                    }
                }

            }
            return files;
        }
    }
    
}
