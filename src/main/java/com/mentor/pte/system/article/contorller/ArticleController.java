package com.mentor.pte.system.article.contorller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mentor.pte.config.TablePage;
import com.mentor.pte.system.article.entity.Article;
import com.mentor.pte.system.article.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/article")
public class ArticleController {


    @Autowired
    private IArticleService articleService;

    @PostMapping("/save")
    public Article save(Article article) {
        articleService.saveOrUpdate(article);
        return article;
    }

    @GetMapping("/total")
    public Integer getTotal() {
        return articleService.count();
    }

    @GetMapping("/page/{pageNo}")
    public Article getOneOfPageNo(@PathVariable Integer pageNo) {
        Page<Article> page = new Page<>(pageNo, 1);
        page = (Page<Article>) articleService.page(page);
        if (page.getRecords().size() == 0) {
            return null;
        }
        return page.getRecords().get(0);
    }

    @GetMapping("/pages")
    public TablePage getPage(@RequestParam(defaultValue = "0", required = true) Integer pageNo, @RequestParam(defaultValue = "10", required = true) Integer pageSize) {
        Page<Article> page = new Page<>(pageNo, pageSize);
        page = (Page<Article>) articleService.page(page);
        return TablePage.of(page.getTotal(), page.getRecords());

    }


    @GetMapping("/{uuid}")
    public Article getPage(@PathVariable Long uuid) {
        return articleService.getById(uuid);
    }


    @GetMapping("/del/{uuid}")
    public void del(@PathVariable Long uuid) {
        articleService.removeById(uuid);
    }


}
