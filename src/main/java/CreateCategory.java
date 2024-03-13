import model.Category;
import model.Option;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

public class CreateCategory {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название категорий:");
        String categoryName = scanner.nextLine();

        List<Category> categoryList = entityManager
                .createQuery("select c from Category c where c.name = ?1", Category.class)
                .setParameter(1, categoryName)
                .getResultList();

        if(categoryList.size() > 0){
            System.out.println("Категория с таким названием уже существует:(");
            return;
        }

        Category category = new Category();
        category.setName(categoryName);

        System.out.println("Введите характеристики через запятую");
        String text = scanner.nextLine();
        String[] optionNames = text.split(", ");

        try{
            entityManager.getTransaction().begin();
            entityManager.persist(category);
            for (String optionName : optionNames) {
                Option option = new Option();
                option.setName(optionName);
                option.setCategory(category);
                entityManager.persist(option);
            }
            entityManager.getTransaction().commit();
            System.out.println("Категория добавлена");
        } catch(Exception e){
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
        }
    }
}
