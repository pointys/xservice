package xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.MyException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import xuecheng.manage_cms_client.dao.CmsPageRepository;
import xuecheng.manage_cms_client.dao.CmsSiteRepository;

import java.io.*;
import java.util.Optional;

/**
 * @Author: 韩老魔
 * SpingDataJPA之ExampleMatcher实例查询
 * @Date: 2019/8/22 0022 0:34
 */
@Service
@Slf4j
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;


    //保存html页面到服务器物理页面
    public void savePageToServerPath(String pagId) {
        CmsPage cmsPage = this.findCmsPageByPageId(pagId);

        //获取站点的物理路径
        String siteId = cmsPage.getSiteId();
        CmsSite cmsSite = this.findCmsSiteByPageId(siteId);
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        //获取页面的物理路径
        String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        log.info(cmsPage.getPageName()+"页面下载的绝对路径为："+pagePath);
        //获取htmlFileId获取输入流
        String htmlFileId = cmsPage.getHtmlFileId();
        InputStream inputStream = this.getFileByFileId(htmlFileId);
        if (inputStream == null) {
            log.error("根据html的fileId：" + htmlFileId + "未查到文件");
            return;
        }

        //将html文件保存到服务器物理路径
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(pagePath));
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //后开先关
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param pageId
     * @return cmspage
     */
    public CmsPage findCmsPageByPageId(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);

        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * @param siteId
     * @return cmspage
     */
    public CmsSite findCmsSiteByPageId(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);

        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    //根据文件id从GridFSFile查询文件内容
    public InputStream getFileByFileId(String fileId) {
        //获取文件对象
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
