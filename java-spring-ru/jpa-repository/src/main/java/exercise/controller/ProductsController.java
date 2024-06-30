package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping(path = "")
    public List<Product> index(@RequestParam Optional<Integer> min, @RequestParam Optional<Integer> max) {
        if (min.isPresent() && max.isPresent()) {
            return productRepository.findByPriceBetween(min.get(), max.get()).stream()
                    .sorted(Comparator.comparingInt(Product::getPrice))
                    .toList();
        }
        else if (min.isPresent() && !max.isPresent()) {
            return productRepository.findByPriceGreaterThanEqual(min.get()).stream()
                    .sorted(Comparator.comparingInt(Product::getPrice))
                    .toList();
        }
        else if (!min.isPresent() && max.isPresent()) {
            return productRepository.findByPriceLessThanEqual(max.get()).stream()
                    .sorted(Comparator.comparingInt(Product::getPrice))
                    .toList();
        }
        else {
            return productRepository.findAll().stream()
                    .sorted(Comparator.comparingInt(Product::getPrice))
                    .toList();
        }

    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
