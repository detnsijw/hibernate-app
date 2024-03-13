import model.Category;
import model.Option;
import model.Product;
import model.Value;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

public class CreateProduct {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.
                createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<Category> categories = entityManager
                .createQuery("select c from Category c", Category.class)
                .getResultList();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите id категории:");
        int categoryId = Integer.parseInt(scanner.nextLine());

        Category category = entityManager.find(Category.class, categoryId);
//      System.out.println(category);
        if(category == null) {
            System.out.println("Категории с таким id не существует");
            return;
        }

        System.out.println("Введите название товара:");
        String productName = scanner.nextLine();

        System.out.println("Введите стоимость товара:");
        double productPrice = Double.parseDouble(scanner.nextLine());

        Product product = new Product();
        product.setName(productName);
        product.setPrice(productPrice);
        product.setCategory(category);
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(product);
            for (Option option : category.getOptionList()) {
                System.out.println(option.getName());
                String valueName = scanner.nextLine();
                Value value = new Value();
                value.setName(valueName);
                value.setProduct(product);
                value.setOption(option);
                entityManager.persist(value);
            }
            entityManager.getTransaction().commit();
            System.out.println("Продукт добавлен");
        } catch(Exception e) {
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
    }
}
