package nl.jdriven.onlineshopapi.models;

public record Product(String name,
                      String description,
                      double price,
                      String imageUrl) {
}
