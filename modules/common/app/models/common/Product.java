package models.common;

import play.db.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;


@Entity
public class Product extends Model {

    @Id
    public Long id;
    public Long ean;
    public String name;
    public String description;

    public String toString() {
        return String.format("%s - %s", ean, name);
    }


    public static List<Product> findAll() {
       return  Product.find.all();

    }

    public static List<Product> search(String filter,int page,int pageSize) {
        List<Product> products = Product.find.where()
                .ilike("description","%"+filter+"%")
                .orderBy("id desc")
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page)
                .getList();

        return  products;

    }


    public static void remove(Product product) {
        product.delete();
    }


    public static Product findByEan(Long ean) {
        List<Product>  products = findAll();
        for (Product candidate : products) {
            if (candidate.ean.equals(ean)) {
                return candidate;
            }
        }
        return null;
    }


    public static Finder<Long, Product> find = new Finder<>(Long.class, Product.class);




}
