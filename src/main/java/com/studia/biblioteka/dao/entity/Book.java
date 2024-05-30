package com.studia.biblioteka.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Builder
@Setter
@Getter
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String authors;
    private String category;

    public Book(Long id, String title, String authors, String category) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.category = category;
    }

    public Book() {

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
