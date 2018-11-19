package com.mentor.pte.system.article.repository;

import com.mentor.pte.system.article.model.Article;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ArticleRepository  extends PagingAndSortingRepository<Article, Long> {


}
