package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.exception.MyException;
import com.xuecheng.framework.exception.MyException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: 韩老魔
 * SpingDataJPA之ExampleMatcher实例查询
 * 设计CmsPage、CmsConfig两个集合
 * @Date: 2019/8/22 0022 0:34
 */
@Service
@Slf4j
public class PageService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    //==========CURD==============

    /**
     * 页面查询
     * 起始页码的dao层0==前端1
     *
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        //自定义查询
        //条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());

        //设置条件对象
        CmsPage cmsPage = new CmsPage();
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getSiteAliase());
        }
        Example<CmsPage> example = Example.of(cmsPage, matcher);

        if (page <= 0) {
            page = 1;
        }
        page = page - 1;

        if (size <= 0) {
            size = 10;
        }
        PageRequest pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);//实现自定义条件查询和分页

        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        queryResult.setList(all.getContent());//数据列表
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 新增页面
     *
     * @param cP
     * @return
     */
    public CmsPageResult add(CmsPage cP) {
        //根据pageName、siteId、pageWebPath三个字段查询页面
        CmsPage cmsPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cP.getPageName(), cP.getSiteId(), cP.getPageWebPath());
        if (cmsPage != null) {
            //页面已存在无法新增页面抛出异常
            MyException.throwException(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cP.setPageId(null);//mongodb主键自增
        cmsPageRepository.save(cP);
        //添加成功返回
        return new CmsPageResult(CommonCode.SUCCESS, cP);
    }

    /**
     * 通过id查询cmsPage
     *
     * @param id
     * @return
     */
    public CmsPage getCmspageByPageId(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        //返回空
        return null;
    }

    /**
     * 修改页面
     *
     * @param id
     * @param cmsPage
     * @return
     */
    public CmsPageResult update(String id, CmsPage cmsPage) {
        //根据id查询页面信息
        CmsPage updateCmsPage = this.getCmspageByPageId(id);
        if (updateCmsPage != null) {
            //更新模板id
            updateCmsPage.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            updateCmsPage.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            updateCmsPage.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            updateCmsPage.setPageName(cmsPage.getPageName());
            //更新访问路径
            updateCmsPage.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            updateCmsPage.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更新数据url
            updateCmsPage.setDataUrl(cmsPage.getDataUrl());
            //更新类型
            updateCmsPage.setPageType(cmsPage.getPageType());
            //执行更新
            CmsPage save = cmsPageRepository.save(updateCmsPage);
            if (save != null) {
                //返回成功
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, save);
                log.info("--------------"+cmsPageResult.toString());
                return cmsPageResult;
            }
        }
        //返回失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
     * 通过ID删除页面
     *
     * @param id
     * @return
     */
    public ResponseResult delete(String id) {
        CmsPage cmsPage = this.getCmspageByPageId(id);
        if (cmsPage != null) {
            //删除页面
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 保存页面，如果已存在则更新页面
     *
     * @param cmsPage
     * @return
     */
    public CmsPageResult save(CmsPage cmsPage) {
        //校验页面是否存在，根据页面名称、站点Id、页面webpath查询
        CmsPage cms_Page =
                cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),
                        cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cms_Page != null) {
            //更新
            return this.update(cms_Page.getPageId(), cmsPage);
        } else {
            //添加
            return this.add(cmsPage);
        }
    }

    //==========页面静态化==============

    /**
     * 根据id查询cmsConfig(模板的数据内容)
     *
     * @param id
     * @return
     */
    public CmsConfig getCmsConfigById(String id) {
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * 总的页面静态化方法
     */
    public String getPageHtmlByPageId(String pageId) {
        //1.获取页面模板ftl
        String template = this.getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(template)) {
            MyException.throwException(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //2.获取ftl文件所需数据
        Map dataModel = this.getModelByCmsPageId(pageId);
        if (dataModel == null) {
            MyException.throwException(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        //执行静态化
        String content = generateHtml(template, dataModel);
        return content;
    }

    /**
     * 通过主键获取cmsPage对象中的dataUrl,
     * ，再通过dataUrl远程调用获取页面模板ftl文件所需数据（dataUrl后面对应cms_config表中的主键）
     *
     * @param id
     * @return
     */
    public Map getModelByCmsPageId(String id) {
        CmsPage cmsPage = this.getCmspageByPageId(id);
        if (cmsPage == null) {
            MyException.throwException(CmsCode.CMS_CMAPAGE_ISNULL);
        }
        //http://localhost:31001/cms/config/getmodel/+【cms_config(轮播图)表的主键或者mysql中course_base(课程)主键】
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            MyException.throwException(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        // 通过resttemplate请求提供数据模型的查询接口
        //CmsConfigControllerl类下的getModel(@PathVariable("id") String id)
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);

        Map body = forEntity.getBody();
        System.out.println("模板中填充的数据：" + body);
        return body;
    }

    /**
     * 获取页面模板信息
     * 由getPageHtmlByPageId调用
     */
    public String getTemplateByPageId(String pageId) {
        CmsPage cmsPage = this.getCmspageByPageId(pageId);
        if (cmsPage == null) {
            MyException.throwException(CmsCode.CMS_CMAPAGE_ISNULL);
        }
        //获取页面模板id
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            MyException.throwException(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optional = this.cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件Id
            String templateFileId = cmsTemplate.getTemplateFileId();
            if (templateFileId == null) {
                MyException.throwException(CmsCode.CMS_templateFileIdISNULL);
            }
            try {
                //从GridFS中获取模板文件内容
                //获取文件对象
                //获取文件对象
                GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
                //打开下载流
                GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
                //定义gridFsResource对象
                GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);

                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                System.out.println("html模板：\n" + content);
                return content;
            } catch (IOException e) {
                e.printStackTrace();
                MyException.throwException(CmsCode.CMS_DOWNLOAD_HTML_EXCEPTION);
            }

        }
        return null;
    }


    /**
     * 由getPageHtmlByPageId调用
     * 执行静态化
     *
     * @param templateContent:模板数据
     * @param model：模板中要塞入的数据
     * @return：静态页面
     */
    public String generateHtml(String templateContent, Map model) {
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        //在内存中创建名字为template的模板(如果有写好的页面就不需要模板加载器了)
        stringTemplateLoader.putTemplate("template", templateContent);
        //向configuration中配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板
        try {
            //根据模板名称获取模板
            Template template = configuration.getTemplate("template");//根据名称获取模板
            //根据模板与数据生成静态模板
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //==========发布页面==============

    /**
     * 页面发布
     *
     * @param pageId
     */
    public ResponseResult post(String pageId) {
        //执行页面静态化
        String pageHtml = this.getPageHtmlByPageId(pageId);
        log.info("静态化文件内容:" + pageHtml);
        //将页面静态化文件保存到GridFs
        CmsPage cmsPage = this.saveHtml(pageId, pageHtml);
        //向mq发消息
        this.sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CmsPage saveHtml(String pageId, String content) {
        //先得到页面信息
        CmsPage cmsPage = this.getCmspageByPageId(pageId);
        if (cmsPage == null) {
            MyException.throwException(CmsCode.CMS_CMAPAGE_ISNULL);
        }
        try {
            InputStream in = IOUtils.toInputStream(content, "utf-8");
            ObjectId objectId = gridFsTemplate.store(in, cmsPage.getPageName());
            cmsPage.setHtmlFileId(objectId.toHexString());
            cmsPageRepository.save(cmsPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cmsPage;
    }

    /**
     * 向mq发送pageId
     *
     * @param pageId
     */
    public void sendPostPage(String pageId) {
        CmsPage cmsPage = this.getCmspageByPageId(pageId);
        if (cmsPage == null) {
            MyException.throwException(CmsCode.CMS_CMAPAGE_ISNULL);
        }
        //站点id(路由key)
        String siteId = cmsPage.getSiteId();
        //创建消息对象
        Map<String, String> map = new HashMap<>();
        map.put("pageId", pageId);
        //消息转成json格式
        String msg = JSON.toJSONString(map);
        //发送
        log.info("生产方根据pageId:" + pageId + "开始向siteId:" + siteId + "发送消息:" + msg);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE, siteId, msg);
    }

    /**
     * 保存或更新cms_page
     * 先页面静态化再将静态化文件保存到Gridfs
     * rabbitmq发送
     * @param pageId
     * @return
     */
    public CmsPostPageResult postPageQuick(CmsPage upCmsPage){
        //1.保存or更新
        CmsPageResult cmsPageResult = this.save(upCmsPage);
        if(!cmsPageResult.isSuccess()){
            MyException.throwException(CommonCode.FAIL);
        }
        //更新后的信息
        CmsPage cmsPage = cmsPageResult.getCmsPage();
        String pageId =cmsPage.getPageId();
        ResponseResult responseResult = this.post(pageId);
        if(!responseResult.isSuccess()){
            MyException.throwException(CommonCode.FAIL);
        }

        //2.拼接url
        String siteId = cmsPage.getSiteId();

    }

public CmsSite
}
