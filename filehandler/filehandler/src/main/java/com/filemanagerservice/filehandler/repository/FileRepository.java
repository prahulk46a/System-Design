package com.filemanagerservice.filehandler.repository;
import com.filemanagerservice.filehandler.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByUserId(Long userId);
}
