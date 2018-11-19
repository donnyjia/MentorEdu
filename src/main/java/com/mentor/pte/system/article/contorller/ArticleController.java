package com.mentor.pte.system.article.contorller;

import com.mentor.pte.system.article.repository.ArticleRepository;
import com.mentor.pte.system.article.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;

    @PostMapping("/save")
    public Article save(Article article) {
        articleRepository.save(article);
        return article;
    }

    @GetMapping("/total")
    public Long getTotal(){
        return articleRepository.count();

    }

    @GetMapping("/page/{pageNo}")
    public Article getOneOfPageNo(@PathVariable  Integer pageNo){
        Pageable pageable = new PageRequest(pageNo-1,1);
        Page<Article> page=articleRepository.findAll(pageable);
        if(page.getContent()==null){
            return null;
        }
        return page.getContent().get(0);
    }

}
