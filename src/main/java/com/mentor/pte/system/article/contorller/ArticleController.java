package com.mentor.pte.system.article.contorller;

import com.mentor.pte.config.TablePage;
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
    public Long getTotal() {
        return articleRepository.count();

    }

    @GetMapping("/page/{pageNo}")
    public Article getOneOfPageNo(@PathVariable Integer pageNo) {
        Pageable pageable = new PageRequest(pageNo - 1, 1);
        Page<Article> page = articleRepository.findAll(pageable);
        if (page.getContent() == null) {
            return null;
        }
        return page.getContent().get(0);
    }

    @GetMapping("/pages")
    public TablePage getPage(@RequestParam(defaultValue = "0", required = true) Integer pageNo, @RequestParam(defaultValue = "10", required = true) Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Article> page = articleRepository.findAll(pageable);
        return TablePage.of(page.getTotalElements(), page.getContent());
    }


    @GetMapping("/{uuid}")
    public Article getPage(@PathVariable Long uuid) {
        return articleRepository.findById(uuid).get();
    }


    @GetMapping("/del/{uuid}")
    public void del(@PathVariable Long uuid) {
        articleRepository.deleteById(uuid);
    }



}
