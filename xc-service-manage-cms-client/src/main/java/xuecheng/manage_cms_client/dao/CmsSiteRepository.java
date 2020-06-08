package xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author 韩浩辰
 * @date 2019/11/28 19:37
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {
}
