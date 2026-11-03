/*
Предположим, у вас есть сервис, который собирает информацию о товаре из трёх независимых источников:
цена, описание и рейтинг. Каждый источник работает с задержкой и может иногда выдавать ошибку.

Нужно реализовать асинхронный сбор данных с помощью CompletableFuture,
чтобы не блокировать основной поток, и предусмотреть fallback-значения на случай сбоя любого сервиса.

Требования: Создайте класс ProductInfo с полями:

    String name
    double price
    String description
    double rating

Создайте класс DataAggregator с методом:
Копировать

  ProductInfo aggregateProductInfo(String productName)

Метод должен параллельно запустить три задачи через CompletableFuture.supplyAsync():

    получить цену,
    получить описание,
    получить рейтинг.

    Добавьте имитацию сервисов (например, fetchPrice, fetchDescription, fetchRating).
    Каждый метод делает Thread.sleep() на 1-3 секунды и с вероятностью 20% выбрасывает RuntimeException.
    В методе aggregateProductInfo используйте exceptionally для подстановки значений по умолчанию
    (например, цена 0.0, описание "Нет данных", рейтинг 0.0).
    Объедините результаты трёх задач с помощью thenCombine или allOf и верните собранный объект ProductInfo.
    В main() продемонстрируйте работу на примере товара "Ноутбук".

Пример вывода при сбое описания:

ProductInfo{name='Ноутбук', price=899.99, description='Нет данных', rating=4.7}

 */

public class ProductInfo {
    private final String name;
    private final double price;
    private final String description;
    private final double rating;

    public ProductInfo(
            String name,
            double price,
            String description,
            double rating
    ) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                '}';
    }
}
