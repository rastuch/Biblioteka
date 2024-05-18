package com.studia.biblioteka.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.Set;

@Getter
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @ManyToMany
    private Set<Author> authors;

    @ManyToOne
    private Category category;

    public Book(Long id, String title, Set<Author> authors, Category category) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.category = category;
    }

    public Book() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
// getters and setters
}


//@RestController
//@RequestMapping("/api/category")
//public class CategoryApi {
//    private CategoryManager categories;
//
//    @Autowired
//    public CategoryApi(CategoryManager categories) {
//        this.categories = categories;
//    }
//
//    @GetMapping
//    public Optional<Category> getById(@RequestParam long id){
//        return categories.findById(id);
//    }
//
//    @GetMapping("/all") Iterable<Category> getAll(){
//        return categories.findAll();
//    }
//
//    @PostMapping
//    public Category addUser(@RequestBody Category category){
//        return categories.save(category);
//    }
//
//    @PutMapping
//    public Category updateUser(@RequestBody Category category){
//        return categories.save(category);
//    }
//
//    @DeleteMapping
//    public void deleteUser(@RequestParam long id){
//        categories.delete(id);
//    }
//}
