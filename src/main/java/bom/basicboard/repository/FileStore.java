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
    public List<File> storeFiles(Long boardNum, List<MultipartFile> multipartFiles) {
        List<File> storeFileResult = new ArrayList<>();
        for(MultipartFile multipartFile:multipartFiles) {
            if(!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(boardNum, multipartFile));
            }
        }
        return storeFileResult;
    }

    public File storeFile(Long boardNum, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        try {
            multipartFile.transferTo(new java.io.File(getFullPath(storeFileName)));
        } catch (IllegalStateException e) {
            log.error("storeFile error={}", e);
        } catch (IOException e) {
            log.error("storeFile error={}", e);
        }finally {
            return new File(boardNum, originalFilename, storeFileName);
        }
        
    }

    //uuid 이용해 저장할 파일명 생성
    private String createStoreFileName(String originalFilename) {

        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

    //확장자 추출
    private String extractExt(String originalFilename) {

        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos+1);

    }

}
