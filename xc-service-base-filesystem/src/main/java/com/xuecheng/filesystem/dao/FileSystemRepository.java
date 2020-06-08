package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author 韩浩辰
 * @date 2020/5/15 18:56
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
