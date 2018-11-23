package com.mentor.pte.system.article.service.impl;

import com.mentor.pte.system.article.entity.Article;
import com.mentor.pte.system.article.mapper.ArticleMapper;
import com.mentor.pte.system.article.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author John
 * @since 2018-11-23
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
