package com.k.mongodb;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/5/28 13:38
 **/
@RestController
@RequestMapping("mongo")
public class MongoDbController {
    @Autowired
    private MongoDbService mongoDbService;

    @PostMapping("save")
    public String saveObj(Book book) {
        return mongoDbService.saveObj(book);
    }

    @GetMapping("findAll")
    public List<Book> findAll() {
        return mongoDbService.findAll();
    }

    @GetMapping("findOne")
    public Book findOne(String id) {
        return mongoDbService.getBookById(id);
    }

    @GetMapping("findOneByName")
    public Book findOneByName(String name) {
        return mongoDbService.getBookByName(name);
    }

    @PostMapping("update")
    public String update(Book book) {
        return mongoDbService.updateBook(book);
    }

    @PostMapping("delOne")
    public String delOne(Book book) {
        return mongoDbService.deleteBook(book);
    }

    @GetMapping("delById")
    public String delById(String id) {
        return mongoDbService.deleteBookById(id);
    }

    @GetMapping("findlikes")
    public List<Book> findByLikes(String search) {
        return mongoDbService.findByLikes(search);
    }
}
